/*
 * Jaspersoft Open Studio - Eclipse-based JasperReports Designer. Copyright (C) 2005 - 2010 Jaspersoft Corporation. All
 * rights reserved. http://www.jaspersoft.com
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program is part of Jaspersoft Open Studio.
 * 
 * Jaspersoft Open Studio is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Jaspersoft Open Studio is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with Jaspersoft Open Studio. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.studio.preferences.util;

import java.util.List;
import java.util.Properties;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRProperties.PropertySuffix;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class PropertiesHelper {
	public static final String JRCONTEXT_PREFERENCE_HELPER_KEY="preferenceHelper";
	public static final InstanceScope INSTANCE_SCOPE = new InstanceScope();
	private IPreferencesService service;
	private String qualifier;
	private String[] lookupOrders;
	private IScopeContext[] contexts;

	public PropertiesHelper(IProject project) {
		service = Platform.getPreferencesService();
		qualifier = JaspersoftStudioPlugin.getUniqueIdentifier();
		if (project != null) {
			lookupOrders = new String[] { ProjectScope.SCOPE, InstanceScope.SCOPE };
			contexts = new IScopeContext[] { new ProjectScope(project), INSTANCE_SCOPE };
		} else {
			lookupOrders = new String[] { InstanceScope.SCOPE };
			contexts = new IScopeContext[] { INSTANCE_SCOPE };
		}
		service.setDefaultLookupOrder(qualifier, null, lookupOrders);
	}

	public void setString(String key, String value, String scope) {
		service.getRootNode().node(scope).node(qualifier).put(key, value);
	}

	public void setBoolean(String key, boolean value, String scope) {
		service.getRootNode().node(scope).node(qualifier).putBoolean(key, value);
	}

	public void setProperties(JasperDesign jd) {
		JRPropertiesMap map = jd.getPropertiesMap();
		List<PropertySuffix> lst = JRProperties.getProperties("");
		for (PropertySuffix ps : lst) {
			String key = ps.getKey();
			if (map.getProperty(key) == null) {
				String val = getString(key);
				if (key != null && val != null)
					map.setProperty(key, val);
			}
		}
	}

	public Properties getProperties() {
		Properties p = new Properties();
		List<PropertySuffix> lst = JRProperties.getProperties("");
		for (PropertySuffix ps : lst) {

			String key = ps.getKey();
			String val = getString(key);
			if (key != null && val != null)
				JRProperties.setProperty(key, val);
		}
		return p;
	}

	public String getString(String key, String def) {
		String str = getString(key);
		if (str == null)
			return def;
		return str;
	}

	public String getString(String key) {
		return service.getString(qualifier, key, null, contexts);
	}

	public Boolean getBoolean(String key) {
		String val = getString(key);
		if (val == null)
			return null;
		return new Boolean(val);
	}

	public Boolean getBoolean(String key, boolean def) {
		String val = getString(key);
		if (val == null)
			return def;
		return new Boolean(val);
	}

	public Integer getInteger(String key, Integer def) {
		Integer val = getInteger(key);
		if (val == null)
			return def;
		return val;
	}

	public Integer getInteger(String key) {
		String val = getString(key);
		if (val == null)
			return null;
		return new Integer(val);
	}

	public Float getFloat(String key) {
		String val = getString(key);
		if (val == null)
			return null;
		return new Float(val);
	}

	public Float getFloat(String key, Float def) {
		Float val = getFloat(key);
		if (val == null)
			return def;
		return val;
	}

	public Character getCharacter(String key) {
		String val = getString(key);
		if (val == null && val != "")
			return null;
		return new Character(val.charAt(0));
	}
	
	/**
	 * Get a {@link PropertiesHelper} instance from the specified {@link JasperReportsConfiguration}.
	 * If a valid JasperReports context is not found then a default {@link PropertiesHelper} is returned.
	 * 
	 * @param jrContext the context from which to extract a {@link PropertiesHelper} instance
	 * @return the properties helper found, a default one otherwise
	 */
	public static PropertiesHelper getInstance(JasperReportsConfiguration jrContext){
		Assert.isNotNull(jrContext);
		PropertiesHelper propHelper = (PropertiesHelper)jrContext.get(JRCONTEXT_PREFERENCE_HELPER_KEY);
		if(propHelper!=null){
			return propHelper;
		}
		else{
			return new PropertiesHelper(null);
		}
	}
}
