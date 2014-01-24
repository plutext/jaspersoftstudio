package com.jaspersoft.studio.server.protocol.soap;

import java.io.File;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.design.JRDesignQuery;

import org.apache.axis.AxisProperties;
import org.apache.axis.components.net.DefaultCommonsHTTPClientProperties;
import org.eclipse.core.runtime.IProgressMonitor;

import com.jaspersoft.ireport.jasperserver.ws.FileContent;
import com.jaspersoft.ireport.jasperserver.ws.JServer;
import com.jaspersoft.ireport.jasperserver.ws.WSClient;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Argument;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.jasperserver.dto.serverinfo.ServerInfo;
import com.jaspersoft.studio.server.AFinderUI;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.editor.input.InputControlsManager;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.server.publish.PublishUtil;
import com.jaspersoft.studio.server.wizard.resource.page.selector.SelectorDatasource;
import com.jaspersoft.studio.utils.Misc;

public class SoapConnection implements IConnection {
	protected DateFormat dateFormat = SimpleDateFormat.getDateInstance();
	protected DateFormat timestampFormat = SimpleDateFormat.getTimeInstance();
	protected NumberFormat numberFormat = NumberFormat.getInstance();
	private ServerProfile sp;

	public ServerProfile getServerProfile() {
		return sp;
	}

	public Format getDateFormat() {
		return dateFormat;
	}

	public Format getTimestampFormat() {
		return timestampFormat;
	}

	public Format getNumberFormat() {
		return numberFormat;
	}

	private WSClient client;
	private ServerInfo serverInfo;

	@Override
	public ServerInfo getServerInfo(IProgressMonitor monitor) throws Exception {
		if (serverInfo != null)
			return serverInfo;
		serverInfo = new ServerInfo();
		serverInfo.setVersion("4.5");
		// serverInfo.setVersion(client.getVersion());
		return serverInfo;
	}

	@Override
	public boolean connect(IProgressMonitor monitor, ServerProfile sp) throws Exception {
		JServer server = new JServer();
		this.sp = sp;
		setupJServer(server, sp);

		client = server.getWSClient();
		if (getServerInfo(monitor) == null)
			return false;
		return true;
	}

	private static void setupJServer(JServer server, ServerProfile sp) {
		AxisProperties.setProperty(DefaultCommonsHTTPClientProperties.MAXIMUM_CONNECTIONS_PER_HOST_PROPERTY_KEY, "4");
		server.setName(sp.getName());
		String rurl = sp.getUrl();
		if (rurl.endsWith("services/repository/"))
			rurl = rurl.substring(0, rurl.length() - 1);
		if (!rurl.endsWith("services/repository"))
			rurl += "services/repository";
		server.setUrl(rurl);
		String username = sp.getUser();
		if (sp.getOrganisation() != null && !sp.getOrganisation().trim().isEmpty())
			username += "|" + sp.getOrganisation();
		server.setUsername(username);
		server.setPassword(sp.getPass());
		server.setTimeout(sp.getTimeout());
		server.setChunked(sp.isChunked());
	}

	@Override
	public ResourceDescriptor get(IProgressMonitor monitor, ResourceDescriptor rd, File f) throws Exception {
		return client.get(rd, f);
	}

	@Override
	public List<ResourceDescriptor> list(IProgressMonitor monitor, ResourceDescriptor rd) throws Exception {
		return client.list(rd);
	}

	@Override
	public ResourceDescriptor move(IProgressMonitor monitor, ResourceDescriptor rd, String destFolderURI) throws Exception {
		client.move(rd, destFolderURI);
		return rd;
	}

	@Override
	public ResourceDescriptor copy(IProgressMonitor monitor, ResourceDescriptor rd, String destFolderURI) throws Exception {
		return client.copy(rd, destFolderURI);
	}

	@Override
	public ResourceDescriptor addOrModifyResource(IProgressMonitor monitor, ResourceDescriptor rd, File inputFile) throws Exception {
		List<ResourceDescriptor> children = rd.getChildren();
		rd = client.addOrModifyResource(rd, inputFile);
		List<ResourceDescriptor> oldChildren = list(monitor, rd);
		for (ResourceDescriptor r : oldChildren) {
			for (ResourceDescriptor newr : children) {
				if (r.getWsType().equals(newr.getWsType()) && r.getUriString().equals(newr.getUriString()))
					newr.setIsNew(false);
			}
		}

		if (rd.getWsType().equals(ResourceDescriptor.TYPE_REPORTUNIT)) {
			rd = get(monitor, rd, null);
			for (ResourceDescriptor r : children) {
				if (r.getWsType().equals(ResourceDescriptor.TYPE_JRXML) && r.getName().equals("main_jrxml"))
					r.setMainReport(true);
				if (r.isMainReport())
					continue;
				if (r.getWsType().equals(ResourceDescriptor.TYPE_INPUT_CONTROL) && !r.getIsNew())
					r = client.addOrModifyResource(r, null);
				else
					r = client.modifyReportUnitResource(rd.getUriString(), r, null);
				rd.getChildren().add(r);
			}
		}
		return rd;
	}

	@Override
	public ResourceDescriptor modifyReportUnitResource(IProgressMonitor monitor, String rUnitUri, ResourceDescriptor rd, File inFile) throws Exception {
		return client.modifyReportUnitResource(rUnitUri, rd, inFile);
	}

	@Override
	public void delete(IProgressMonitor monitor, ResourceDescriptor rd) throws Exception {
		client.delete(rd);
	}

	@Override
	public void delete(IProgressMonitor monitor, ResourceDescriptor rd, ResourceDescriptor runit) throws Exception {
		client.delete(rd, runit.getUriString());
	}

	@Override
	public Map<String, FileContent> runReport(IProgressMonitor monitor, ResourceDescriptor rd, Map<String, Object> prm, List<Argument> args) throws Exception {
		return client.runReport(rd, prm, args);
	}

	@Override
	public List<ResourceDescriptor> listDatasources(IProgressMonitor monitor) throws Exception {
		return client.listDatasources();
	}

	@Override
	public String getWebservicesUri() {
		return client.getWebservicesUri();
	}

	@Override
	public String getUsername() {
		return client.getUsername();
	}

	@Override
	public String getPassword() {
		return client.getPassword();
	}

	@Override
	public void findResources(IProgressMonitor monitor, AFinderUI callback) throws Exception {
		throw new UnsupportedOperationException("Search not implemented for SOAP protocol.");
	}

	@Override
	public ResourceDescriptor toResourceDescriptor(ClientResource<?> rest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSupported(Feature f) {
		return false;
	}

	@Override
	public void reorderInputControls(String uri, List<ResourceDescriptor> rds, IProgressMonitor monitor) throws Exception {
		ResourceDescriptor runit = new ResourceDescriptor();
		runit.setUriString(uri);
		runit = get(monitor, runit, null);

		List<ResourceDescriptor> toDel = new ArrayList<ResourceDescriptor>();
		for (ResourceDescriptor r : runit.getChildren()) {
			if (r.getWsType().equals(ResourceDescriptor.TYPE_INPUT_CONTROL)) {
				delete(monitor, r, runit);
				toDel.add(r);
			}
		}
		runit.getChildren().retainAll(toDel);
		for (ResourceDescriptor r : rds) {
			r.setIsNew(true);
			if (!r.getParentFolder().endsWith("_files")) {
				r.setIsReference(true);
				r.setReferenceUri(r.getUriString());
				r.setParentFolder(uri + "_files");
			}
			r.setUriString(uri + "_files/" + r.getName());
			PublishUtil.setChild(runit, r);
		}
		addOrModifyResource(monitor, runit, null);
	}

	@Override
	public ResourceDescriptor initInputControls(String uri, IProgressMonitor monitor) throws Exception {
		ResourceDescriptor rdrepunit = WSClientHelper.getReportUnit(monitor, uri);
		// List<ResourceDescriptor> list = list(monitor, rdrepunit);
		List<ResourceDescriptor> inputcontrols = new ArrayList<ResourceDescriptor>();
		Set<String> icNames = new HashSet<String>();
		String dsUri = null;
		for (ResourceDescriptor sub_rd : rdrepunit.getChildren()) {
			String wsType = sub_rd.getWsType();
			if (wsType.equals(ResourceDescriptor.TYPE_INPUT_CONTROL)) {
				inputcontrols.add(sub_rd);
				icNames.add(sub_rd.getName());
			} else if (wsType.equals(ResourceDescriptor.TYPE_DATASOURCE) && sub_rd.getIsReference())
				dsUri = sub_rd.getReferenceUri();
			else if (SelectorDatasource.isDatasource(sub_rd))
				dsUri = sub_rd.getUriString();
		}

		for (int i = 0; i < inputcontrols.size(); ++i) {
			ResourceDescriptor ic = inputcontrols.get(i);
			if (InputControlsManager.isICQuery(ic)) {
				String dsUriQuery = getDataSourceQueryURI(dsUri, ic);
				ic.setResourceProperty(ResourceDescriptor.PROP_QUERY_DATA, null);
				// Ask to add values to the control....
				List<Argument> args = new ArrayList<Argument>();
				args.add(new Argument(Argument.IC_GET_QUERY_DATA, dsUriQuery));
				args.add(new Argument(Argument.RU_REF_URI, uri));
				ic = client.get(ic, null, args);
				cascadingDependencies(ic, icNames);
			} else if (InputControlsManager.isICListOfValues(ic) && !ic.getChildren().isEmpty()) {
				ResourceDescriptor rd2 = (ResourceDescriptor) ic.getChildren().get(0);
				if (rd2.getWsType().equals(ResourceDescriptor.TYPE_REFERENCE)) {
					ResourceDescriptor tmpRd = new ResourceDescriptor();
					tmpRd.setUriString(rd2.getReferenceUri());
					tmpRd = get(monitor, tmpRd, null);
					ic.setListOfValues(tmpRd.getListOfValues());
				} else
					ic.setListOfValues(rd2.getListOfValues());
			}
			for (int j = 0; j < rdrepunit.getChildren().size(); j++) {
				ResourceDescriptor r = rdrepunit.getChildren().get(j);
				if (r.getName() != null && r.getName().equals(ic.getName()))
					rdrepunit.getChildren().set(j, ic);
			}
		}
		return rdrepunit;
	}

	private void cascadingDependencies(ResourceDescriptor ic, Set<String> icNames) {
		List<ResourceDescriptor> children = ic.getChildren();
		for (ResourceDescriptor sub_ic : children) {
			if (!InputControlsManager.isRDQuery(sub_ic))
				continue;
			String queryString = sub_ic.getSql();
			String lang = sub_ic.getResourceProperty(ResourceDescriptor.PROP_QUERY_LANGUAGE).getValue();
			if (!Misc.isNullOrEmpty(queryString)) {
				List<String> parameters = new ArrayList<String>();
				JRDesignQuery query = new JRDesignQuery();
				query.setText(queryString);
				if (lang != null)
					query.setLanguage(lang);
				for (JRQueryChunk chunk : query.getChunks()) {
					switch (chunk.getType()) {
					case JRQueryChunk.TYPE_TEXT:
						break;
					case JRQueryChunk.TYPE_PARAMETER_CLAUSE:
					case JRQueryChunk.TYPE_PARAMETER:
						String paramName = chunk.getText().trim();
						if (!parameters.contains(paramName) && icNames.contains(paramName))
							parameters.add(paramName);
						break;
					case JRQueryChunk.TYPE_CLAUSE_TOKENS:
						String[] tokens = chunk.getTokens();
						if (tokens.length > 2) {
							for (String t : tokens) {
								t = t.trim();
								if (!parameters.contains(t) && icNames.contains(t))
									parameters.add(t);
							}
						}
						break;
					}
				}
				if (!parameters.isEmpty())
					ic.setMasterInputControls(parameters);
			}
			break;
		}
	}

	@Override
	public List<ResourceDescriptor> cascadeInputControls(ResourceDescriptor runit, List<ResourceDescriptor> ics, IProgressMonitor monitor) throws Exception {
		String dsUri = null;
		for (ResourceDescriptor sub_rd : runit.getChildren()) {
			String wsType = sub_rd.getWsType();
			if (wsType.equals(ResourceDescriptor.TYPE_DATASOURCE) && sub_rd.getIsReference())
				dsUri = sub_rd.getReferenceUri();
			else if (SelectorDatasource.isDatasource(sub_rd))
				dsUri = sub_rd.getUriString();
		}
		String ruri = runit.getUriString();
		List<ResourceDescriptor> res = new ArrayList<ResourceDescriptor>();
		for (ResourceDescriptor rd : ics)
			res.add(updateControl(ruri, dsUri, rd, monitor));
		return res;
	}

	private ResourceDescriptor updateControl(String runit, String dsUri, ResourceDescriptor rd, IProgressMonitor monitor) throws Exception {
		List<Argument> args = new ArrayList<Argument>();

		args.add(new Argument(Argument.IC_GET_QUERY_DATA, getDataSourceQueryURI(dsUri, rd)));
		args.add(new Argument(Argument.RU_REF_URI, runit));

		rd.getParameters().clear();
		rd.setResourceProperty(ResourceDescriptor.PROP_QUERY_DATA, null);
		Map<String, Object> parameters = rd.getIcValues();
		for (String key : parameters.keySet()) {
			Object value = parameters.get(key);
			if (value == null)
				continue;
			if (value instanceof Collection)
				for (String item : ((Collection<String>) value)) {
					ListItem l = new ListItem(key, item);
					l.setIsListItem(true);
					rd.getParameters().add(l);
				}
			else
				rd.getParameters().add(new ListItem(key, value));
		}
		return client.get(rd, null, args);
	}

	private static String getDataSourceQueryURI(String dsUri, ResourceDescriptor ic) {
		String dsUriQuery = null;
		// reset query data...
		// Look if this query has a specific datasource...
		for (int k = 0; dsUriQuery == null && k < ic.getChildren().size(); ++k) {
			ResourceDescriptor sub_ic = (ResourceDescriptor) ic.getChildren().get(k);
			if (InputControlsManager.isRDQuery(sub_ic))
				for (int k2 = 0; k2 < sub_ic.getChildren().size(); ++k2) {
					ResourceDescriptor sub_sub_ic = (ResourceDescriptor) sub_ic.getChildren().get(k2);
					if (SelectorDatasource.isDatasource(sub_sub_ic)) {
						dsUriQuery = sub_sub_ic.getUriString();
						break;
					}
				}
		}
		if (dsUriQuery == null)
			dsUriQuery = dsUri;
		return dsUriQuery;
	}
}
