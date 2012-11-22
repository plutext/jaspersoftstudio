/*******************************************************************************
 * Copyright (C) 2010 - 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, 
 * the following license terms apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Jaspersoft Studio Team - initial API and implementation
 ******************************************************************************/
package com.jaspersoft.studio.server.wizard.resource.page;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.ResourceFactory;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MRQuery;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.properties.dialog.RepositoryDialog;
import com.jaspersoft.studio.server.wizard.resource.ResourceWizard;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.UIUtils;

public class SelectorQuery {
	private Button brRepo;
	private Text jsRefDS;
	private Button brLocal;
	private Text jsLocDS;
	private Button bLoc;
	private Button bRef;
	private MResource res;

	public void createControls(Composite cmp, final ANode parent,
			final MResource res) {
		this.res = res;

		Composite composite = new Composite(cmp, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		composite.setLayoutData(gd);
		composite.setLayout(new GridLayout(2, false));

		brRepo = new Button(composite, SWT.RADIO);
		brRepo.setText(Messages.SelectorQuery_selectfromrepository);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		brRepo.setLayoutData(gd);
		brRepo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setEnabled(0);
			}
		});

		jsRefDS = new Text(composite, SWT.BORDER);
		jsRefDS.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		bRef = new Button(composite, SWT.PUSH);
		bRef.setText(Messages.SelectorQuery_browse);
		bRef.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RepositoryDialog rd = new RepositoryDialog(Display.getDefault()
						.getActiveShell(), ServerManager.getMServerProfileCopy((MServerProfile)parent.getRoot())) {

					@Override
					public boolean isResourceCompatible(MResource r) {
						return r instanceof MRQuery;
					}
				};
				if (rd.open() == Dialog.OK) {
					MResource rs = rd.getResource();
					if (rs != null) {
						ResourceDescriptor runit = res.getValue();
						try {
							ResourceDescriptor ref = rs.getValue();
							ref = WSClientHelper.getResource(parent, ref);
							ref.setIsReference(false);
							ref.setReferenceUri(ref.getUriString());
							ref.setParentFolder(runit.getUriString() + "_files"); //$NON-NLS-1$
							ref.setUriString(ref.getParentFolder() + "/" //$NON-NLS-1$
									+ ref.getName());
							ref.setWsType(ResourceDescriptor.TYPE_REFERENCE);
							replaceQuery(res, ref);

							jsRefDS.setText(ref.getUriString());
						} catch (Exception e1) {
							UIUtils.showError(e1);
						}
					}
				}
			}
		});

		brLocal = new Button(composite, SWT.RADIO);
		brLocal.setText(Messages.SelectorQuery_localresource);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		brLocal.setLayoutData(gd);
		brLocal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setEnabled(1);
			}
		});

		jsLocDS = new Text(composite, SWT.BORDER);
		jsLocDS.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		bLoc = new Button(composite, SWT.PUSH);
		bLoc.setText("..."); //$NON-NLS-1$
		bLoc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResourceDescriptor runit = res.getValue();
				ResourceDescriptor ref = getQuery(runit);
				if (ref != null
						&& ref.getIsReference()
						|| ref.getWsType().equals(
								ResourceDescriptor.TYPE_REFERENCE))
					ref = null;
				boolean newref = false;
				Shell shell = Display.getDefault().getActiveShell();
				if (ref != null)
					ref = cloneResource(ref);
				else {
					ref = MRQuery.createDescriptor(res);
					ref.setIsNew(true);
					ref.setIsReference(false);
					ref.setParentFolder(runit.getUriString() + "_files"); //$NON-NLS-1$

					newref = true;
				}
				MResource r = ResourceFactory.getResource(null, ref, -1);
				ResourceWizard wizard = new ResourceWizard(parent, r);
				WizardDialog dialog = new WizardDialog(shell, wizard);
				dialog.create();
				if (dialog.open() != Dialog.OK)
					return;
				ref.setUriString(ref.getParentFolder() + "/" + ref.getName()); //$NON-NLS-1$
				if (newref) {
					replaceQuery(res, ref);
				} else {
					copyFields(res.getValue(), ref);
				}
				jsLocDS.setText(Misc.nvl(ref.getName()));
			}
		});
		ResourceDescriptor r = getQuery(res.getValue());
		if (r != null) {
			if (r.getIsReference()
					|| r.getWsType().equals(ResourceDescriptor.TYPE_REFERENCE))
				setEnabled(0);
			else
				setEnabled(1);
		}
	}

	public static ResourceDescriptor cloneResource(ResourceDescriptor rd) {
		ResourceDescriptor rnew = new ResourceDescriptor();
		rnew.setIsNew(rd.getIsNew());
		rnew.setIsReference(rd.getIsReference());
		rnew.setName(rd.getName());
		rnew.setLabel(rd.getLabel());
		rnew.setDescription(rd.getDescription());
		rnew.setUriString(rd.getUriString());
		rnew.setParentFolder(rd.getParentFolder());
		rnew.setDataSourceType(rd.getDataSourceType());
		rnew.setWsType(rd.getWsType());

		rnew.setListOfValues(rd.getListOfValues());
		rnew.setParameters(rd.getParameters());
		rnew.setProperties(rd.getProperties());
		rnew.setChildren(rd.getChildren());

		return rnew;
	}

	public static void copyFields(ResourceDescriptor rd, ResourceDescriptor rnew) {
		rnew.setQueryData(rd.getQueryData());
		rnew.setQueryValueColumn(rd.getQueryValueColumn());
		rnew.setQueryVisibleColumns(rd.getQueryVisibleColumns());

		rnew.setName(rd.getName());
		rnew.setLabel(rd.getLabel());
		rnew.setDescription(rd.getDescription());

		rnew.setParameters(rd.getParameters());
		rnew.setProperties(rd.getProperties());
		rnew.setChildren(rd.getChildren());
	}

	protected void replaceQuery(final MResource res, ResourceDescriptor rd) {
		ResourceDescriptor rdel = getQuery(res.getValue());
		if (rdel != null) {
			int index = res.getValue().getChildren().indexOf(rdel);
			if (index >= 0)
				res.getValue().getChildren().remove(index);
		}
		res.getValue().getChildren().add(rd);
	}

	private static ResourceDescriptor getQuery(ResourceDescriptor ru) {
		for (Object obj : ru.getChildren()) {
			ResourceDescriptor r = (ResourceDescriptor) obj;
			if (r.getWsType().equals(ResourceDescriptor.TYPE_QUERY)
					|| r.getWsType().equals(ResourceDescriptor.TYPE_REFERENCE)) {
				return r;
			}
		}
		return null;
	}

	private void setEnabled(int pos) {
		bRef.setEnabled(false);
		jsRefDS.setEnabled(false);

		bLoc.setEnabled(false);
		jsLocDS.setEnabled(false);

		brRepo.setSelection(false);
		brLocal.setSelection(false);

		jsRefDS.setText(""); //$NON-NLS-1$
		jsLocDS.setText(""); //$NON-NLS-1$

		ResourceDescriptor r = getQuery(res.getValue());
		switch (pos) {
		case 0:
			bRef.setEnabled(true);
			brRepo.setSelection(true);
			jsRefDS.setEnabled(true);
			if (r != null
					&& (r.getIsReference() || r.getWsType().equals(
							ResourceDescriptor.TYPE_REFERENCE)))
				jsRefDS.setText(Misc.nvl(r.getReferenceUri()));
			break;
		case 1:
			brLocal.setSelection(true);
			bLoc.setEnabled(true);
			jsLocDS.setEnabled(true);
			if (r != null && !r.getIsReference()
					&& !r.getWsType().equals(ResourceDescriptor.TYPE_REFERENCE))
				jsLocDS.setText(Misc.nvl(r.getName()));
			break;
		}

	}

}
