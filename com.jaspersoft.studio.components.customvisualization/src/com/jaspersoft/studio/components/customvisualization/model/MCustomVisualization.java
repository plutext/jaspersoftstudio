/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 ******************************************************************************/
package com.jaspersoft.studio.components.customvisualization.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.items.ItemProperty;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.jasperreports.customvisualization.design.CVDesignComponent;
import com.jaspersoft.studio.components.customvisualization.CVNodeIconDescriptor;
import com.jaspersoft.studio.components.customvisualization.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IDatasetContainer;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.classname.NClassTypePropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.NamedEnumPropertyDescriptor;
import com.jaspersoft.studio.utils.EnumHelper;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * Model object representing the Custom Visualization component element.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class MCustomVisualization extends MGraphicElement implements
		IDatasetContainer {

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private static IIconDescriptor iconDescriptor;
	private IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;
	private RComboBoxPropertyDescriptor evaluationGroupNameD;
	private static NamedEnumPropertyDescriptor<EvaluationTimeEnum> evaluationTimeD;
	private static NamedEnumPropertyDescriptor<OnErrorTypeEnum> onErrorTypeD;

	public MCustomVisualization() {
		super();
	}

	public MCustomVisualization(ANode parent,
			JRDesignComponentElement jrObject, int newIndex) {
		super(parent, jrObject, newIndex);
	}

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

	@Override
	public List<MDatasetRun> getDatasetRunList() {
		// TODO Auto-generated method stub
		return null;
	}

	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new CVNodeIconDescriptor("customvisualization"); //$NON-NLS-1$
		return iconDescriptor;
	}

	@Override
	public String getDisplayText() {
		return getIconDescriptor().getTitle();
	}

	@Override
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	@Override
	public String getToolTip() {
		return getIconDescriptor().getToolTip();
	}

	@Override
	public JRDesignComponentElement createJRElement(JasperDesign jasperDesign) {
		JRDesignComponentElement el = new JRDesignComponentElement();
		CVDesignComponent cvComp = new CVDesignComponent();
		el.setComponent(cvComp);
		el.setComponentKey(new ComponentKey(
				"http://www.jaspersoft.com/cvcomponent", "cvc", "customvisualization")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return el;
	}

	@Override
	public void setValue(Object value) {
		if (getValue() != null) {
			Object obj = getComponent();
			if (obj instanceof JRChangeEventsSupport)
				((JRChangeEventsSupport) obj).getEventSupport()
						.removePropertyChangeListener(this);
		}
		if (value != null) {
			Object obj = getComponent(value);
			if (value instanceof JRChangeEventsSupport)
				((JRChangeEventsSupport) obj).getEventSupport()
						.addPropertyChangeListener(this);
		}
		super.setValue(value);
	}

	public CVDesignComponent getComponent() {
		return getComponent(getValue());
	}

	private CVDesignComponent getComponent(Object value) {
		if (value != null) {
			JRDesignComponentElement jrElement = (JRDesignComponentElement) value;
			return (CVDesignComponent) jrElement.getComponent();
		}
		return null;
	}

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc,
			Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		evaluationTimeD = new NamedEnumPropertyDescriptor<EvaluationTimeEnum>(
				CVDesignComponent.PROPERTY_EVALUATION_TIME,
				Messages.MCustomVisualization_EvalTime,
				EvaluationTimeEnum.AUTO, NullEnum.NOTNULL);
		evaluationTimeD
				.setDescription(Messages.MCustomVisualization_EvalTimeDesc);
		desc.add(evaluationTimeD);

		evaluationGroupNameD = new RComboBoxPropertyDescriptor(
				CVDesignComponent.PROPERTY_EVALUATION_GROUP,
				"Evaluation Group", new String[] { "" }); //$NON-NLS-1$ //$NON-NLS-2$
		evaluationGroupNameD
				.setDescription(Messages.MCustomVisualization_EvalGroupDesc);
		desc.add(evaluationGroupNameD);

		NClassTypePropertyDescriptor processingClassD = new NClassTypePropertyDescriptor(
				CVDesignComponent.PROPERTY_PROCESSING_CLASS,
				Messages.MCustomVisualization_ProcessingClass);
		processingClassD
				.setDescription(Messages.MCustomVisualization_ProcessingClassDesc);
		desc.add(processingClassD);

		CVItemPropertiesDescriptor bItemPropsD = new CVItemPropertiesDescriptor(
				CVDesignComponent.PROPERTY_ITEM_PROPERTIES,
				Messages.MCustomVisualization_ItemProperties);
		bItemPropsD
				.setDescription(Messages.MCustomVisualization_ItemPropertiesDesc);
		desc.add(bItemPropsD);

		CVCItemDataPropertyDescriptor bItemDataD = new CVCItemDataPropertyDescriptor(
				CVDesignComponent.PROPERTY_ITEM_DATA,
				Messages.MCustomVisualization_ItemData);
		bItemDataD.setDescription(Messages.MCustomVisualization_ItemDataDesc);
		desc.add(bItemDataD);

		evaluationTimeD
				.setCategory(Messages.MCustomVisualization_CVPropertiesCategory);
		evaluationGroupNameD
				.setCategory(Messages.MCustomVisualization_CVPropertiesCategory);
		processingClassD
				.setCategory(Messages.MCustomVisualization_CVPropertiesCategory);
		bItemPropsD
				.setCategory(Messages.MCustomVisualization_CVPropertiesCategory);
		bItemDataD
				.setCategory(Messages.MCustomVisualization_CVPropertiesCategory);

		defaultsMap.put(CVDesignComponent.PROPERTY_EVALUATION_TIME,
				EvaluationTimeEnum.NOW);

		onErrorTypeD = new NamedEnumPropertyDescriptor<OnErrorTypeEnum>(
				CVDesignComponent.PROPERTY_ON_ERROR_TYPE,
				Messages.MCustomVisualization_OnErrorType,
				OnErrorTypeEnum.BLANK, NullEnum.NULL);
		onErrorTypeD
				.setDescription(Messages.MCustomVisualization_OnErrorTypeDesc);
		desc.add(onErrorTypeD);

		defaultsMap.put(CVDesignComponent.PROPERTY_ON_ERROR_TYPE,
				onErrorTypeD.getIntValue(OnErrorTypeEnum.ERROR));
	}

	@Override
	protected void setGroupItems(String[] items) {
		super.setGroupItems(items);
		if (evaluationGroupNameD != null)
			evaluationGroupNameD.setItems(items);
	}

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		CVDesignComponent cvComp = (CVDesignComponent) jrElement.getComponent();
		if (CVDesignComponent.PROPERTY_EVALUATION_TIME.equals(id)) {
			return cvComp.getEvaluationTime();
		} else if (CVDesignComponent.PROPERTY_EVALUATION_GROUP.equals(id)) {
			return cvComp.getEvaluationGroup();
		} else if (CVDesignComponent.PROPERTY_PROCESSING_CLASS.equals(id)) {
			return cvComp.getProcessingClass();
		} else if (CVDesignComponent.PROPERTY_ITEM_PROPERTIES.equals(id)) {
			return JRCloneUtils.cloneList(cvComp.getItemProperties());
		} else if (CVDesignComponent.PROPERTY_ITEM_DATA.equals(id)) {
			return JRCloneUtils.cloneList(cvComp.getItemData());
		}
		if (id.equals(CVDesignComponent.PROPERTY_ON_ERROR_TYPE)) {
			return onErrorTypeD.getIntValue(cvComp.getOnErrorType());
		} else {
			return super.getPropertyValue(id);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setPropertyValue(Object id, Object value) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		CVDesignComponent cvComp = (CVDesignComponent) jrElement.getComponent();
		if (CVDesignComponent.PROPERTY_EVALUATION_TIME.equals(id)) {
			EvaluationTimeEnum evalTime = EnumHelper.getEnumByObjectValue(
					EvaluationTimeEnum.values(), value);
			cvComp.setEvaluationTime(evalTime);
			if (evalTime != null && !evalTime.equals(EvaluationTimeEnum.GROUP)) {
				cvComp.setEvaluationGroup(null);
			}
		} else if (CVDesignComponent.PROPERTY_EVALUATION_GROUP.equals(id)) {
			cvComp.setEvaluationGroup(ModelUtils.getGroupNameForProperty(value));
		} else if (CVDesignComponent.PROPERTY_PROCESSING_CLASS.equals(id)) {
			if (value instanceof String && ((String) value).trim().isEmpty())
				value = null;
			cvComp.setProcessingClass((String) value);
		} else if (CVDesignComponent.PROPERTY_ITEM_PROPERTIES.equals(id)) {
			cvComp.getItemProperties().clear();
			Set<String> keys = new HashSet<String>();
			for (ItemProperty i : (List<ItemProperty>) value) {
				if (Misc.isNullOrEmpty(i.getValue())
						&& (i.getValueExpression() == null || Misc
								.isNullOrEmpty(i.getValueExpression().getText())))
					continue;
				if (keys.contains(i.getName()))
					continue;
				keys.add(i.getName());
				cvComp.getItemProperties().add(i);
			}
			// let's not refresh ui on every removeItem, addItem
			cvComp.getEventSupport().fireIndexedPropertyChange(
					CVDesignComponent.PROPERTY_ITEM_PROPERTIES, 0, true, false);
		} else if (CVDesignComponent.PROPERTY_ITEM_DATA.equals(id)) {
			ItemData[] toRemove = cvComp.getItemData().toArray(
					new ItemData[] {});
			for (ItemData i : toRemove) {
				cvComp.removeItemData(i);
			}
			for (ItemData i : (List<ItemData>) value) {
				cvComp.addItemData(i);
			}
		} else if (CVDesignComponent.PROPERTY_ON_ERROR_TYPE.equals(id)) {
			cvComp.setOnErrorType(onErrorTypeD.getEnumValue(value));
		} else {
			super.setPropertyValue(id, value);
		}
	}

}