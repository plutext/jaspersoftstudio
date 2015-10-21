/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.components.table.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.WhenNoDataTypeTableEnum;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.section.name.NameSection;
import com.jaspersoft.studio.components.table.TableComponentFactory;
import com.jaspersoft.studio.components.table.TableDatasetRunProperyDescriptor;
import com.jaspersoft.studio.components.table.TableManager;
import com.jaspersoft.studio.components.table.TableNodeIconDescriptor;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.editor.layout.FreeLayout;
import com.jaspersoft.studio.editor.layout.ILayout;
import com.jaspersoft.studio.editor.layout.LayoutManager;
import com.jaspersoft.studio.editor.layout.VerticalRowLayout;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IContainer;
import com.jaspersoft.studio.model.IContainerEditPart;
import com.jaspersoft.studio.model.IContainerLayout;
import com.jaspersoft.studio.model.IDatasetContainer;
import com.jaspersoft.studio.model.IGraphicalPropertiesHandler;
import com.jaspersoft.studio.model.IGroupElement;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MPage;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.NamedEnumPropertyDescriptor;
import com.jaspersoft.studio.utils.Misc;

public class MTable extends MGraphicElement implements IContainer,
		IContainerEditPart, IGroupElement, IContainerLayout, IDatasetContainer {

	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	/**
	 * The property used to know if the resize of the column
	 * take the space from the following one is it is a standard resize
	 */
	public static final String PROPERTY_COLUMNS_AUTORESIZE_NEXT = "com.jaspersoft.studio.components.autoresize.next"; //$NON-NLS-1$

	/**
	 * The property used to know if the columns of the table are automatically resize if necessary to fit the table area, the width is distributed
	 * proportionally to the columns initial size
	 */
	public static final String PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL = "com.jaspersoft.studio.components.autoresize.proportional"; //$NON-NLS-1$
	
	private static IIconDescriptor iconDescriptor;

	private TableManager ctManager;

	/**
	 * The dataset where the group listener was placed last
	 */
	private JRDesignDataset datasetWithListener = null;

	/**
	 * Listener put on the current dataset to refresh the group node on the
	 * tables when a group is added on removed on his dataset
	 */
	private PropertyChangeListener datasetGroupListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(JRDesignDataset.PROPERTY_GROUPS)) {
				// this need to be done only inside the table editor
				if (evt.getNewValue() != null && evt.getOldValue() == null
						&& getChildren().size() > 0) {
					MTableDetail detailNode = null;
					for (INode child : getChildren()) {
						if (detailNode == null && child instanceof MTableDetail) {
							detailNode = (MTableDetail) child;
							break;
						}
					}
					int detailIndex = getChildren().indexOf(detailNode);
					JRDesignGroup jrGroup = (JRDesignGroup) evt.getNewValue();
					MTableGroupHeader newHeader = new MTableGroupHeader(
							MTable.this, (JRDesignComponentElement) getValue(),
							jrGroup, ""); //$NON-NLS-1$
					addChild(newHeader, detailIndex);
					detailIndex += 2;
					MTableGroupFooter newFooter = new MTableGroupFooter(
							MTable.this, (JRDesignComponentElement) getValue(),
							jrGroup, ""); //$NON-NLS-1$
					addChild(newFooter, detailIndex);
					List<BaseColumn> columns = getStandardTable().getColumns();
					for (int i = 0; i < columns.size(); i++) {
						BaseColumn bc = columns.get(i);
						TableComponentFactory.createCellGroupHeader(newHeader,
								bc, i + 1, jrGroup.getName(), i);
						TableComponentFactory.createCellGroupFooter(newFooter,
								bc, i + 1, jrGroup.getName(), i);
					}
				} else if (evt.getNewValue() == null
						&& evt.getOldValue() != null) {
					JRDesignGroup jrGroup = (JRDesignGroup) evt.getOldValue();
					deleteGroup(jrGroup.getName());
				}
				// Run an event on the table to force a grapghical refresh of
				// the columnss
				setChangedProperty(true);
				MTable.this.propertyChange(new PropertyChangeEvent(getValue(),
						StandardTable.PROPERTY_COLUMNS, null, null));
			}
		}
	};

	/**
	 * Instantiates a new m chart.
	 */
	public MTable() {
		super();
	}

	public MTable(ANode parent, int newIndex, TableManager ctManager) {
		super(parent, newIndex);
		this.ctManager = ctManager;
	}

	public TableManager getTableManager() {
		return ctManager;
	}

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new TableNodeIconDescriptor("table"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * 
	 * @param parent
	 *            the parent
	 * @param jrTable
	 *            the jr chart
	 * @param newIndex
	 *            the new index
	 */
	public MTable(ANode parent, JRDesignComponentElement jrTable, int newIndex,
			TableManager ctManager) {
		super(parent, newIndex);
		setValue(jrTable);
		this.ctManager = ctManager;
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
	public void setDescriptors(IPropertyDescriptor[] descriptors1,
			Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *            the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc,
			Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		TableDatasetRunProperyDescriptor datasetRunD = new TableDatasetRunProperyDescriptor(StandardTable.PROPERTY_DATASET_RUN,Messages.MTable_dataset_run, false);
		datasetRunD.setDescription(Messages.MTable_dataset_run_description);
		datasetRunD.setCategory(Messages.MTable_table_properties_category);
		desc.add(datasetRunD);

		whennodataD = new NamedEnumPropertyDescriptor<WhenNoDataTypeTableEnum>(
				StandardTable.PROPERTY_WHEN_NO_DATA_TYPE,
				Messages.MTable_whennodatalabel, WhenNoDataTypeTableEnum.BLANK,
				NullEnum.NULL);
		whennodataD.setDescription(Messages.MTable_whennodatadescription);
		desc.add(whennodataD);
		whennodataD.setCategory(Messages.MTable_table_properties_category);
		
		
		CheckBoxPropertyDescriptor columnsIncreaseDescriptor = new CheckBoxPropertyDescriptor(PROPERTY_COLUMNS_AUTORESIZE_NEXT, Messages.MTable_autoresizeNext);
		columnsIncreaseDescriptor.setDescription(Messages.MTable_autoresizeNextDescription);
		desc.add(columnsIncreaseDescriptor);
		columnsIncreaseDescriptor.setCategory(Messages.MTable_table_properties_category);
		
		CheckBoxPropertyDescriptor columnsFillDescriptor = new CheckBoxPropertyDescriptor(PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL, Messages.MTable_propertyForceFill);
		columnsFillDescriptor.setDescription(Messages.MTable_propertyForceFillDescription);
		desc.add(columnsFillDescriptor);
		columnsFillDescriptor.setCategory(Messages.MTable_table_properties_category);


		defaultsMap.put(StandardTable.PROPERTY_WHEN_NO_DATA_TYPE,whennodataD.getEnumValue(WhenNoDataTypeTableEnum.BLANK));
		defaultsMap.put(PROPERTY_COLUMNS_AUTORESIZE_NEXT, Boolean.FALSE);
		defaultsMap.put(PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL, Boolean.FALSE);
		
		setHelpPrefix(desc,
				"net.sf.jasperreports.doc/docs/components.schema.reference.html#table"); //$NON-NLS-1$
	}

	private MDatasetRun mDatasetRun;
	private static NamedEnumPropertyDescriptor<WhenNoDataTypeTableEnum> whennodataD;

	@Override
	public void setGroupItems(String[] items) {
		super.setGroupItems(items);
	}

	@Override
	public Object getPropertyValue(Object id) {
		StandardTable jrTable = getStandardTable();

		if (id.equals(StandardTable.PROPERTY_DATASET_RUN)) {
			JRDatasetRun j = jrTable.getDatasetRun();
			if (j == null)
				j = new JRDesignDatasetRun();
			if (mDatasetRun != null)
				mDatasetRun.setValue(j);
			else {
				mDatasetRun = new MDatasetRun(j, getJasperDesign());
				mDatasetRun.setJasperConfiguration(getJasperConfiguration());
				setChildListener(mDatasetRun);
			}
			return mDatasetRun;
		}
		if (id.equals(StandardTable.PROPERTY_WHEN_NO_DATA_TYPE)){
			return whennodataD.getIntValue(jrTable.getWhenNoDataType());
		}
		if (id.equals(PROPERTY_COLUMNS_AUTORESIZE_NEXT)){
			return hasColumnsAutoresizeNext();
		}
		if (id.equals(PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL)){
			return hasColumnsAutoresizeProportional();
		}

		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		StandardTable jrTable = getStandardTable();

		if (id.equals(StandardTable.PROPERTY_WHEN_NO_DATA_TYPE))
			jrTable.setWhenNoDataType((WhenNoDataTypeTableEnum) whennodataD
					.getEnumValue(value));
		else if (id.equals(StandardTable.PROPERTY_DATASET_RUN)) {
			MDatasetRun mdr = (MDatasetRun) value;
			JRDesignDatasetRun dr = (JRDesignDatasetRun) mdr.getValue();
			if (dr.getDatasetName() != null)
				jrTable.setDatasetRun(dr);
			else
				jrTable.setDatasetRun(null);
		} else if (id.equals(PROPERTY_COLUMNS_AUTORESIZE_NEXT)){
			if (value == null || !Boolean.parseBoolean(value.toString())){
				getValue().getPropertiesMap().removeProperty(PROPERTY_COLUMNS_AUTORESIZE_NEXT);
			} else {
				getValue().getPropertiesMap().setProperty(PROPERTY_COLUMNS_AUTORESIZE_NEXT, value.toString());
			}
		} else if (id.equals(PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL)){
			Object oldValue = getValue().getPropertiesMap().getProperty(PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL);
			Object newValue = null;
			if (value == null || !Boolean.parseBoolean(value.toString())){
				getValue().getPropertiesMap().removeProperty(PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL);
			} else {
				getValue().getPropertiesMap().setProperty(PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL, value.toString());
				newValue = value;
			}
			propertyChange(new PropertyChangeEvent(this, PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL, oldValue, newValue));
		} else super.setPropertyValue(id, value);
	}

	/**
	 * Check if in the current table is set the flag to autoresize the columns taking the 
	 * space from the next one when it is drag and dropped
	 * 
	 * @return true if the resize of a column should take the space from the next one, false otherwise
	 */
	public boolean hasColumnsAutoresizeNext(){
		if (getValue() != null){
			JRPropertiesMap map = getValue().getPropertiesMap();
			Object value = map.getProperty(PROPERTY_COLUMNS_AUTORESIZE_NEXT);
			if (value != null){
				return Boolean.parseBoolean(value.toString());
			}
		}
		return false;
	}
	
	/**
	 * Check if in the current table is set the flag to autoresize the columns taking the 
	 * space from the next one when it is drag and dropped
	 * 
	 * @return true if the resize of a column should take the space from the next one, false otherwise
	 */
	public boolean hasColumnsAutoresizeProportional(){
		if (getValue() != null){
			JRPropertiesMap map = getValue().getPropertiesMap();
			Object value = map.getProperty(PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL);
			if (value != null){
				return Boolean.parseBoolean(value.toString());
			}
		}
		return false;
	}
	
	public StandardTable getStandardTable() {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		StandardTable jrTable = (StandardTable) jrElement.getComponent();
		return jrTable;
	}

	@Override
	public int getDefaultHeight() {
		Object defaultValue = DefaultManager.INSTANCE
				.getDefaultPropertiesValue(this.getClass(),
						JRDesignElement.PROPERTY_HEIGHT);
		return defaultValue != null ? (Integer) defaultValue : 200;
	}

	@Override
	public int getDefaultWidth() {
		Object defaultValue = DefaultManager.INSTANCE
				.getDefaultPropertiesValue(this.getClass(),
						JRDesignElement.PROPERTY_WIDTH);
		return defaultValue != null ? (Integer) defaultValue : 200;
	}

	@Override
	public JRDesignElement createJRElement(JasperDesign jasperDesign) {
		JRDesignComponentElement jrElement = new JRDesignComponentElement();
		StandardTable component = new StandardTable();

		((JRDesignComponentElement) jrElement).setComponent(component);
		((JRDesignComponentElement) jrElement)
				.setComponentKey(new ComponentKey(
						"http://jasperreports.sourceforge.net/jasperreports/components", "jr", "table")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		JRDesignDatasetRun datasetRun = new JRDesignDatasetRun();
		component.setDatasetRun(datasetRun);

		DefaultManager.INSTANCE.applyDefault(this.getClass(), jrElement);
		jrElement.getPropertiesMap().setProperty(ILayout.KEY,
				VerticalRowLayout.class.getName());

		return jrElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		String name = getPropertiesMap().getProperty(
				NameSection.getNamePropertyId(this));
		return getIconDescriptor().getTitle() + " " + Misc.nvl(name); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getImagePath()
	 */
	@Override
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return getIconDescriptor().getToolTip();
	}

	public JRElementGroup getJRElementGroup() {
		return null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getPropertyName().equals(StandardTable.PROPERTY_DATASET_RUN)) {
			addDatasetGroupListener();
		}

		if (evt.getPropertyName().equals(
				MGraphicElement.FORCE_GRAPHICAL_REFRESH)) {
			ANode parent = getParent();
			IGraphicalPropertiesHandler upperGrahpicHandler = null;
			while (parent != null) {
				if (parent instanceof IGraphicalPropertiesHandler) {
					upperGrahpicHandler = (IGraphicalPropertiesHandler) parent;
				}
				parent = parent.getParent();
			}
			if (upperGrahpicHandler != null) {
				((MGraphicElement) upperGrahpicHandler)
						.getValue()
						.getEventSupport()
						.firePropertyChange(
								MGraphicElement.FORCE_GRAPHICAL_REFRESH, null,
								null);
			}
		}

		if (getTableManager() != null){
			if (hasColumnsAutoresizeProportional() && isColumnsResizeEvent(evt)){
				String oldValue = getValue().getPropertiesMap().getProperty(PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL);
				getValue().getPropertiesMap().removeProperty(PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL);
				getTableManager().fillSpace(getValue().getWidth(), true);
				getValue().getPropertiesMap().setProperty(PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL, oldValue);
			}
			getTableManager().update();
		}
	
		if (!(evt.getPropertyName().equals(StandardColumn.PROPERTY_TABLE_FOOTER) ||
				evt.getPropertyName().equals(StandardColumn.PROPERTY_TABLE_HEADER) ||
				evt.getPropertyName().equals(StandardColumn.PROPERTY_COLUMN_HEADER) ||
				evt.getPropertyName().equals(StandardColumn.PROPERTY_COLUMN_FOOTER) ||
				evt.getPropertyName().equals(StandardColumn.PROPERTY_GROUP_HEADERS) ||
				evt.getPropertyName().equals(StandardColumn.PROPERTY_GROUP_FOOTERS))){
			super.propertyChange(evt);
		} else {
			if (hasChangedProperty()){
				HashSet<String> graphicalProperties = getGraphicalProperties();
				if (graphicalProperties.contains(evt.getPropertyName())) {
					setChangedProperty(true);
				}
			}
		}
	}
	
	private boolean isColumnsResizeEvent(PropertyChangeEvent evt){
		if (evt.getPropertyName().equals(JRDesignElement.PROPERTY_WIDTH)){
			return true;
		}
		if (evt.getPropertyName().equals(StandardTable.PROPERTY_COLUMNS)){
			return true;
		}
		if (evt.getPropertyName().equals(PROPERTY_COLUMNS_AUTORESIZE_PROPORTIONAL)){
			return true;
		}
		return false;
	}

	@Override
	public JRPropertiesHolder[] getPropertyHolder() {
		return new JRPropertiesHolder[] { getValue() };
	}

	@Override
	public HashSet<String> generateGraphicalProperties() {
		HashSet<String> properties = super.generateGraphicalProperties();
		properties.add(DesignCell.PROPERTY_DEFAULT_STYLE_PROVIDER);
		properties.add(DesignCell.PROPERTY_STYLE);
		properties.add(DesignCell.PROPERTY_STYLE_NAME_REFERENCE);
		properties.add(DesignCell.PROPERTY_ROW_SPAN);
		properties.add(DesignCell.PROPERTY_HEIGHT);
		properties.add(JRDesignElement.PROPERTY_ELEMENT_GROUP);
		properties.add(MColumn.PROPERTY_NAME);
		properties.add(StandardBaseColumn.PROPERTY_WIDTH);
		properties.add(StandardBaseColumn.PROPERTY_TABLE_HEADER);
		properties.add(StandardBaseColumn.PROPERTY_TABLE_FOOTER);
		properties.add(StandardBaseColumn.PROPERTY_COLUMN_HEADER);
		properties.add(StandardBaseColumn.PROPERTY_COLUMN_FOOTER);
		properties.add(StandardBaseColumn.PROPERTY_GROUP_HEADERS);
		properties.add(StandardBaseColumn.PROPERTY_GROUP_FOOTERS);
		properties.add(StandardTable.PROPERTY_COLUMNS);
		return properties;
	}

	@Override
	public List<MDatasetRun> getDatasetRunList() {
		List<MDatasetRun> datasetList = new ArrayList<MDatasetRun>();
		datasetList
				.add((MDatasetRun) getPropertyValue(StandardTable.PROPERTY_DATASET_RUN));
		return datasetList;
	}

	private void fillUsedStyles(List<INode> children, HashMap<String, List<ANode>> map) {
		for (INode node : children) {
			if (node instanceof ANode) {
				mergeElementStyle(map, ((ANode) node).getUsedStyles());
			}
		}
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
		addDatasetGroupListener();
	}

	/**
	 * Delete a group node from the table, both header and footer if present
	 * 
	 * @param groupName
	 *            the name of the group
	 */
	private void deleteGroup(String groupName) {
		MTableGroupFooter footer = null;
		MTableGroupHeader header = null;
		for (INode child : getChildren()) {
			if (child instanceof MTableGroupHeader) {
				MTableGroupHeader groupHeader = (MTableGroupHeader) child;
				if (groupHeader.getJrDesignGroup().getName().equals(groupName)) {
					header = groupHeader;
				}
			} else if (child instanceof MTableGroupFooter) {
				MTableGroupFooter groupFooter = (MTableGroupFooter) child;
				if (groupFooter.getJrDesignGroup().getName().equals(groupName)) {
					footer = groupFooter;
				}
			}
			if (footer != null && header != null)
				break;
		}
		if (footer != null)
			removeChild(footer);
		if (header != null)
			removeChild(header);
	}

	/**
	 * Add the dataset group listener to the current table dataset, but before
	 * remove the old one if present
	 */
	private void addDatasetGroupListener() {
		if (datasetWithListener != null) {
			datasetWithListener.getEventSupport().removePropertyChangeListener(
					datasetGroupListener);
		}
		JRDatasetRun datasetRun = getStandardTable().getDatasetRun();
		JasperDesign design = getJasperDesign();
		if (design != null) {
			JRDesignDataset dataset = (JRDesignDataset) design.getDatasetMap()
					.get(datasetRun.getDatasetName());
			datasetWithListener = dataset;
			if (dataset != null) {
				dataset.getEventSupport().addPropertyChangeListener(
						datasetGroupListener);
			}
		}
	}

	@Override
	public HashMap<String, List<ANode>> getUsedStyles() {
		HashMap<String, List<ANode>> result = super.getUsedStyles();
		fillUsedStyles(getChildren(), result);
		return result;
	}
	
	@Override
	public boolean showChildren() {
		return getParent() instanceof MPage;
	}

	@Override
	public void createSubeditor() {
		TableComponentFactory.createSubeditor(this);
	}
	
	@Override
	public ILayout getDefaultLayout() {
		return LayoutManager.getLayout(FreeLayout.class.getName());
	}
}