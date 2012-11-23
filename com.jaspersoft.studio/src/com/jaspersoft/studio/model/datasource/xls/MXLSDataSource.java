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
package com.jaspersoft.studio.model.datasource.xls;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.datasource.AMFileDataSource;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;

public class MXLSDataSource extends AMFileDataSource {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new NodeIconDescriptor("datasourceXLS"); //$NON-NLS-1$
		return iconDescriptor;
	}

	public MXLSDataSource() {
		super(null, -1);
	}

	public MXLSDataSource(ANode parent, int index) {
		super(parent, index);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getImagePath()
	 */
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return getIconDescriptor().getToolTip();
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;

	@Override
	public Map<String, Object> getDefaultsMap() {
		return defaultsMap;
	}

	@Override
	public IPropertyDescriptor[] getDescriptors() {
		return descriptors;
	}

	@Override
	public void setDescriptors(IPropertyDescriptor[] descriptors1, Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		NTextPropertyDescriptor columnNames = new NTextPropertyDescriptor(PROPERTY_COLUMNNAMES,
				Messages.common_column_names);
		desc.add(columnNames);

		CheckBoxPropertyDescriptor firstRowHeaderD = new CheckBoxPropertyDescriptor(PROPERTY_FIRSTROWASHEADER,
				Messages.MFileDataSource_first_row_as_header);
		firstRowHeaderD.setDescription(Messages.MFileDataSource_first_row_as_header_description);
		desc.add(firstRowHeaderD);

		defaultsMap.put(PROPERTY_FIRSTROWASHEADER, false);

	}

	public static final String PROPERTY_FIRSTROWASHEADER = "PROPERTY_FIRSTROWASHEADER"; //$NON-NLS-1$
	protected boolean firstRowHeader;

	public static final String PROPERTY_COLUMNNAMES = "PROPERTY_COLUMNNAMES"; //$NON-NLS-1$
	protected String columnnames;

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(PROPERTY_COLUMNNAMES))
			return columnnames;
		if (id.equals(PROPERTY_FIRSTROWASHEADER))
			return firstRowHeader;
		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (id.equals(PROPERTY_COLUMNNAMES)) {
			columnnames = (String) value;
		} else if (id.equals(PROPERTY_FIRSTROWASHEADER)) {
			if (value instanceof String)
				firstRowHeader = new Boolean((String) value);
			else
				firstRowHeader = (Boolean) value;
		}
		super.setPropertyValue(id, value);
	}
}
