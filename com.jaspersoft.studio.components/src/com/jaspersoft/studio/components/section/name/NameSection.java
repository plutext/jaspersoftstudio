/*******************************************************************************
 * Copyright (C) 2010 - 2016. TIBCO Software Inc. 
 * All Rights Reserved. Confidential & Proprietary.
 ******************************************************************************/
package com.jaspersoft.studio.components.section.name;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.list.model.MList;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.descriptors.JSSTextPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.SPText;

import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElement;
import net.sf.jasperreports.engine.JRPropertiesMap;

public class NameSection extends AbstractSection {
	
	private JSSTextPropertyDescriptor pd;

	private static HashMap<Class<?>, String> namePropertyMap;
	
	static{
		namePropertyMap = new HashMap<Class<?>, String>();
		namePropertyMap.put(MTable.class, HeaderToolbarElement.PROPERTY_TABLE_NAME);
		namePropertyMap.put(MList.class, HeaderToolbarElement.PROPERTY_TABLE_NAME);
		namePropertyMap.put(MCrosstab.class, HeaderToolbarElement.PROPERTY_TABLE_NAME);
	}
	
	public NameSection() {
		super();
		pd = new JSSTextPropertyDescriptor(MGraphicElement.PROPERTY_MAP, "Name");
		pd.setDescription("Name");
	}

	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(MGraphicElement.PROPERTY_MAP, "Name");
	}
	
	protected String getPropertyId(){
		ANode node = getSelectedElement();
		if (node != null){
			return namePropertyMap.get(node.getClass());
		}
		return null;
	}

	
	public static String getNamePropertyId(ANode node){
		if (node != null){
			return namePropertyMap.get(node.getClass());
		}
		return null;
	}
	
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);

		parent = getWidgetFactory().createComposite(parent);
		parent.setLayout(new GridLayout(2,false));
		parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		getWidgetFactory().createCLabel(parent, "Name", SWT.RIGHT);

		widgets.put(pd.getId(), new SPText<IPropertyDescriptor>(parent, this, pd) {
			@Override
			protected String getCurrentValue() {
				JRPropertiesMap pmap = (JRPropertiesMap) section.getElement().getPropertyValue(pDescriptor.getId());
				String propertyName = getPropertyId();
				return (String) pmap.getProperty(propertyName);
			}

			protected void handleTextChanged(final AbstractSection section, final Object property, String text) {
				String propertyName = getPropertyId();
				if (propertyName != null){
					JRPropertiesMap pmap = (JRPropertiesMap) pnode.getPropertyValue(MGraphicElement.PROPERTY_MAP);
					pmap = (JRPropertiesMap) pmap.clone();
					pmap.setProperty(propertyName, text);
					section.changeProperty(MGraphicElement.PROPERTY_MAP, pmap);
				}
			}

			@Override
			public void setData(APropertyNode pnode, Object b) {
				if (b instanceof JRPropertiesMap) {
					JRPropertiesMap map = (JRPropertiesMap) b;
					String propertyName = getPropertyId();
					String name = map.getProperty(propertyName);
					super.setData(pnode, name);
				}
			}
		});
	
		IPropertyDescriptor columnDescriptor = getElement().getPropertyDescriptor(MTable.PROPERTY_COLUMNS_AUTORESIZE_NEXT);
		if (columnDescriptor != null){
			ASPropertyWidget<?> widget = createWidget4Property(getElement(), parent, MTable.PROPERTY_COLUMNS_AUTORESIZE_NEXT, false);
			GridData controlData = new GridData();
			controlData.horizontalSpan = 2;
			widget.getControl().setLayoutData(controlData);
		}

		columnDescriptor = getElement().getPropertyDescriptor(MTable.PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL);
		if (columnDescriptor != null){
			ASPropertyWidget<?> widget = createWidget4Property(getElement(), parent, MTable.PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL, false);
			GridData controlData = new GridData();
			controlData.horizontalSpan = 2;
			widget.getControl().setLayoutData(controlData);
		}
	}
}
