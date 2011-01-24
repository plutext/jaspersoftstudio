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
package com.jaspersoft.studio.model;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.combo.RWComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.hyperlink.parameter.ParameterPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.hyperlink.parameter.dialog.ParameterDTO;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.utils.ModelUtils;

public class MHyperLink extends APropertyNode {

	public MHyperLink(JRHyperlink hyperLink) {
		super();
		setValue(hyperLink);
	}

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		JRExpressionPropertyDescriptor anchorExpressionD = new JRExpressionPropertyDescriptor(
				JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION, Messages.MHyperLink_hyperlink_anchor_expression);
		anchorExpressionD.setDescription(Messages.MHyperLink_hyperlink_anchor_expression_description);
		desc.add(anchorExpressionD);

		JRExpressionPropertyDescriptor pageExpressionD = new JRExpressionPropertyDescriptor(
				JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION, Messages.MHyperLink_hyperlink_page_expression);
		pageExpressionD.setDescription(Messages.MHyperLink_hyperlink_page_expression_description);
		desc.add(pageExpressionD);

		JRExpressionPropertyDescriptor referenceExpressionD = new JRExpressionPropertyDescriptor(
				JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION, Messages.MHyperLink_hyperlink_reference_expression);
		referenceExpressionD.setDescription(Messages.MHyperLink_hyperlink_reference_expression_description);
		desc.add(referenceExpressionD);

		JRExpressionPropertyDescriptor toolTipExpressionD = new JRExpressionPropertyDescriptor(
				JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION, Messages.MHyperLink_hyperlink_tooltip_expression);
		toolTipExpressionD.setDescription(Messages.MHyperLink_hyperlink_tooltip_expression_description);
		desc.add(toolTipExpressionD);

		NTextPropertyDescriptor linkTargetD = new NTextPropertyDescriptor(JRDesignHyperlink.PROPERTY_LINK_TARGET,
				Messages.MHyperLink_link_target);
		linkTargetD.setDescription(Messages.MHyperLink_link_target_description);
		desc.add(linkTargetD);

		RWComboBoxPropertyDescriptor linkTypeD = new RWComboBoxPropertyDescriptor(JRDesignHyperlink.PROPERTY_LINK_TYPE,
				Messages.MHyperLink_link_type, ModelUtils.getHyperLinkType(), NullEnum.NULL);
		linkTypeD.setDescription(Messages.MHyperLink_link_type_description);
		desc.add(linkTypeD);

		ParameterPropertyDescriptor propertiesD = new ParameterPropertyDescriptor(
				JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS, Messages.common_parameters);
		propertiesD.setDescription(Messages.MHyperLink_parameters_description);
		desc.add(propertiesD);

		propertiesD.setCategory(Messages.MHyperLink_hyperlink_category);
		anchorExpressionD.setCategory(Messages.MHyperLink_hyperlink_category);
		pageExpressionD.setCategory(Messages.MHyperLink_hyperlink_category);
		referenceExpressionD.setCategory(Messages.MHyperLink_hyperlink_category);
		toolTipExpressionD.setCategory(Messages.MHyperLink_hyperlink_category);

		linkTargetD.setCategory(Messages.MHyperLink_hyperlink_category);
		linkTypeD.setCategory(Messages.MHyperLink_hyperlink_category);
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

	private MExpression mAnchorExpression;
	private MExpression mPageExpression;
	private MExpression mReferenceExpression;
	private MExpression mToolTipExpression;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		// pen
		JRHyperlink jrElement = (JRHyperlink) getValue();
		if (jrElement != null) {
			if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TARGET))
				return jrElement.getLinkTarget();
			if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TYPE))
				return jrElement.getLinkType();
			if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION)) {
				mAnchorExpression = ExprUtil.getExpression(this, mAnchorExpression, jrElement.getHyperlinkAnchorExpression());
				return mAnchorExpression;
			}
			if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION)) {
				mPageExpression = ExprUtil.getExpression(this, mPageExpression, jrElement.getHyperlinkPageExpression());
				return mPageExpression;
			}
			if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION)) {
				mReferenceExpression = ExprUtil.getExpression(this, mReferenceExpression,
						jrElement.getHyperlinkReferenceExpression());
				return mReferenceExpression;
			}
			if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION)) {
				mToolTipExpression = ExprUtil
						.getExpression(this, mToolTipExpression, jrElement.getHyperlinkTooltipExpression());
				return mToolTipExpression;
			}
			if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS)) {
				if (propertyDTO == null) {
					propertyDTO = new ParameterDTO();
					propertyDTO.setJasperDesign(getJasperDesign());
					propertyDTO.setValue(jrElement.getHyperlinkParameters());
				}
				return propertyDTO;
			}
		}
		return null;
	}

	private ParameterDTO propertyDTO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRDesignHyperlink jrElement = (JRDesignHyperlink) getValue();
		if (jrElement != null) {
			if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TARGET))
				jrElement.setLinkTarget((String) value);
			else if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TYPE))
				jrElement.setLinkType((String) value);
			else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION))
				jrElement.setHyperlinkAnchorExpression(ExprUtil.setValues(jrElement.getHyperlinkAnchorExpression(), value));
			else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION))
				jrElement.setHyperlinkPageExpression(ExprUtil.setValues(jrElement.getHyperlinkPageExpression(), value));
			else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION))
				jrElement
						.setHyperlinkReferenceExpression(ExprUtil.setValues(jrElement.getHyperlinkReferenceExpression(), value));
			else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION))
				jrElement.setHyperlinkTooltipExpression(ExprUtil.setValues(jrElement.getHyperlinkTooltipExpression(), value));
			else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS)) {
				if (value instanceof ParameterDTO) {
					ParameterDTO v = (ParameterDTO) value;

					JRHyperlinkParameter[] hyperlinkParameters = jrElement.getHyperlinkParameters();
					if (hyperlinkParameters != null)
						for (JRHyperlinkParameter prm : hyperlinkParameters)
							jrElement.removeHyperlinkParameter(prm);

					for (JRHyperlinkParameter param : v.getValue())
						jrElement.addHyperlinkParameter(param);

					propertyDTO = v;
				}
			}
		}
	}

	public String getDisplayText() {
		return null;
	}

	public ImageDescriptor getImagePath() {
		return null;
	}

}
