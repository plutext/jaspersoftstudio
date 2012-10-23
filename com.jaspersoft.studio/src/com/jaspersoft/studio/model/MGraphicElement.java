/*
 * JasperReports - Free Java Reporting Library. Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program is part of JasperReports.
 * 
 * JasperReports is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * JasperReports is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with JasperReports. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.studio.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBaseElement;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;
import net.sf.jasperreports.engine.design.JRDesignGraphicElement;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.editor.gef.rulers.ReportRulerGuide;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.color.ColorPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RWComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.propexpr.JPropertyExpressionsDescriptor;
import com.jaspersoft.studio.property.descriptor.propexpr.PropertyExpressionsDTO;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.IntegerPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.OpaqueModePropertyDescriptor;
import com.jaspersoft.studio.utils.Colors;
import com.jaspersoft.studio.utils.Misc;

/*
 * The Class MGeneric.
 */
public class MGraphicElement extends APropertyNode implements IGraphicElement, ICopyable, IGuidebleElement, IDragable {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private ReportRulerGuide verticalGuide, horizontalGuide;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGuidebleElement#getVerticalGuide()
	 */
	public ReportRulerGuide getVerticalGuide() {
		return verticalGuide;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jaspersoft.studio.model.IGuidebleElement#setVerticalGuide(com.jaspersoft.studio.editor.gef.rulers.ReportRulerGuide
	 * )
	 */
	public void setVerticalGuide(ReportRulerGuide verticalGuide) {
		this.verticalGuide = verticalGuide;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGuidebleElement#getHorizontalGuide()
	 */
	public ReportRulerGuide getHorizontalGuide() {
		return horizontalGuide;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGuidebleElement#setHorizontalGuide(com.jaspersoft.studio.editor.gef.rulers.
	 * ReportRulerGuide)
	 */
	public void setHorizontalGuide(ReportRulerGuide horizontalGuide) {
		this.horizontalGuide = horizontalGuide;
	}

	@Override
	public void setParent(ANode parent, int newIndex) {
		if (parent instanceof MGraphicElement) {
			IGuidebleElement p = (IGuidebleElement) parent;
			if (p.getVerticalGuide() != null)
				p.getVerticalGuide().detachPart(p);
			if (p.getHorizontalGuide() != null)
				p.getHorizontalGuide().detachPart(p);
		}
		super.setParent(parent, newIndex);
	}

	public INode getBand() {
		INode node = this;
		while (!(node instanceof MBand) && !(node instanceof MRoot)) {
			if (node == null || node.getParent() == null)
				return this;
			node = node.getParent();
		}
		return node;
	}

	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new NodeIconDescriptor("generic"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m generic.
	 */
	public MGraphicElement() {
		super();
	}

	/**
	 * Instantiates a new m generic.
	 * 
	 * @param parent
	 *          the parent
	 * @param newIndex
	 *          the new index
	 */
	public MGraphicElement(ANode parent, int newIndex) {
		super(parent, newIndex);
	}

	/**
	 * Instantiates a new m generic.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrLine
	 *          the jr line
	 * @param newIndex
	 *          the new index
	 */
	public MGraphicElement(ANode parent, JRDesignElement jrLine, int newIndex) {
		super(parent, newIndex);
		setValue(jrLine);
	}

	@Override
	public JRDesignElement getValue() {
		return (JRDesignElement) super.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.ANode#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		if (getValue() != null && getValue() instanceof JRDesignGraphicElement)
			((JRBasePen) ((JRDesignGraphicElement) getValue()).getLinePen()).getEventSupport().removePropertyChangeListener(
					this);
		else if (value != null && value instanceof JRDesignGraphicElement)
			((JRBasePen) ((JRDesignGraphicElement) value).getLinePen()).getEventSupport().addPropertyChangeListener(this);
		super.setValue(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGraphicElement#getDefaultHeight()
	 */
	public int getDefaultHeight() {
		return 30;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGraphicElement#getDefaultWidth()
	 */
	public int getDefaultWidth() {
		return 100;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGraphicElement#createJRElement(net.sf.jasperreports.engine.design.JasperDesign)
	 */
	public JRDesignElement createJRElement(JasperDesign jasperDesign) {
		JRDesignGenericElement jrDesignGenericElement = new JRDesignGenericElement(jasperDesign);
		return jrDesignGenericElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		return getIconDescriptor().getTitle();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGraphicElement#getBounds()
	 */
	public Rectangle getBounds() {
		JRElement jr = (JRElement) getValue();
		INode node = getParent();
		while (node != null) {
			if (node instanceof MPage) {
				return new Rectangle(0, 0, jr.getWidth(), jr.getHeight());
			} else if (node instanceof IGraphicElement) {
				Rectangle b = ((IGraphicElement) node).getBounds();
				if (node instanceof IGraphicElementContainer) {
					int x = ((IGraphicElementContainer) node).getLeftPadding();
					int y = ((IGraphicElementContainer) node).getTopPadding();

					b.setLocation(b.x + x, b.y + y);
				}
				return new Rectangle(b.x + jr.getX(), b.y + jr.getY(), jr.getWidth(), jr.getHeight());
			}
			node = node.getParent();
		}
		return new Rectangle(0, 0, jr.getWidth(), jr.getHeight());
	}

	private IPropertyDescriptor[] descriptors;
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
	protected void postDescriptors(IPropertyDescriptor[] descriptors) {
		super.postDescriptors(descriptors);
		// initialize style
		JasperDesign jasperDesign = getJasperDesign();
		if (jasperDesign != null) {
			if (styleD != null) {
				JRDesignElement jrElement = (JRDesignElement) getValue();
				JRStyle[] styles = jasperDesign.getStyles();
				String[] items = new String[styles.length + 1];
				items[0] = jrElement.getStyleNameReference() != null ? jrElement.getStyleNameReference() : ""; //$NON-NLS-1$
				for (int j = 0; j < styles.length; j++) {
					items[j + 1] = styles[j].getName();
				}
				styleD.setItems(items);
			}
			// initialize groups
			JRGroup[] groups = jasperDesign.getGroups();
			String[] items = new String[groups.length + 1];
			items[0] = ""; //$NON-NLS-1$
			for (int j = 0; j < groups.length; j++) {
				items[j + 1] = groups[j].getName();
			}
			setGroupItems(items);
		}
	}

	protected void setGroupItems(String[] items) {
		if (groupChangesD != null)
			groupChangesD.setItems(items);
	}
	
	@Override
	public HashMap<String,Object> getStylesDescriptors() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		if (getValue() == null)
			return result;
		JRDesignElement element = (JRDesignElement) getValue();
		result.put(JRDesignStyle.PROPERTY_BACKCOLOR, element.getOwnBackcolor());
		result.put(JRDesignStyle.PROPERTY_FORECOLOR, element.getOwnForecolor());
		result.put(JRDesignStyle.PROPERTY_MODE, element.getOwnModeValue());
		return result;
	}

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *          the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		styleD = new RWComboBoxPropertyDescriptor(JRDesignElement.PROPERTY_PARENT_STYLE, Messages.common_parent_style,
				new String[] { "" }, NullEnum.NULL); //$NON-NLS-1$
		styleD.setDescription(Messages.MGraphicElement_parent_style_description);
		desc.add(styleD);

		groupChangesD = new RComboBoxPropertyDescriptor(JRDesignElement.PROPERTY_PRINT_WHEN_GROUP_CHANGES,
				Messages.MGraphicElement_print_when_group_changes, new String[] { "" }); //$NON-NLS-1$
		groupChangesD.setDescription(Messages.MGraphicElement_print_when_group_changes_description);
		groupChangesD.setCategory(Messages.MGraphicElement_print_when);
		desc.add(groupChangesD);

		NTextPropertyDescriptor keyD = new NTextPropertyDescriptor(JRDesignElement.PROPERTY_KEY, Messages.common_key);
		keyD.setDescription(Messages.MGraphicElement_key_description);
		desc.add(keyD);

		// bounds
		IntegerPropertyDescriptor heightD = new IntegerPropertyDescriptor(JRDesignElement.PROPERTY_HEIGHT,
				Messages.common_height);
		heightD.setCategory(Messages.common_size);
		heightD.setDescription(Messages.MGraphicElement_height_description);
		desc.add(heightD);

		IntegerPropertyDescriptor widthD = new IntegerPropertyDescriptor(JRBaseElement.PROPERTY_WIDTH,
				Messages.MGraphicElement_width);
		widthD.setCategory(Messages.common_size);
		widthD.setDescription(Messages.MGraphicElement_width_description);
		desc.add(widthD);

		IntegerPropertyDescriptor xD = new IntegerPropertyDescriptor(JRBaseElement.PROPERTY_X, Messages.common_left);
		xD.setCategory(Messages.MGraphicElement_location_category);
		xD.setDescription(Messages.MGraphicElement_left_description);
		desc.add(xD);

		IntegerPropertyDescriptor yD = new IntegerPropertyDescriptor(JRDesignElement.PROPERTY_Y, Messages.common_top);
		yD.setCategory(Messages.MGraphicElement_location_category);
		yD.setDescription(Messages.MGraphicElement_top_description);
		desc.add(yD);
		// colors
		ColorPropertyDescriptor backcolorD = new ColorPropertyDescriptor(JRBaseStyle.PROPERTY_BACKCOLOR,
				Messages.common_backcolor, NullEnum.INHERITED);
		backcolorD.setDescription(Messages.MGraphicElement_backcolor_description);
		desc.add(backcolorD);

		ColorPropertyDescriptor forecolorD = new ColorPropertyDescriptor(JRBaseStyle.PROPERTY_FORECOLOR,
				Messages.common_forecolor, NullEnum.INHERITED);
		forecolorD.setDescription(Messages.MGraphicElement_forecolor_description);
		desc.add(forecolorD);

		opaqueD = new OpaqueModePropertyDescriptor(JRBaseStyle.PROPERTY_MODE, Messages.common_opaque, ModeEnum.class,
				NullEnum.INHERITED);
		//opaqueD.setDescription(Messages.MGraphicElement_opaque_description);
		//opaqueD.setCategory(Messages.common_graphic);
		//desc.add(opaqueD);
		
		CheckBoxPropertyDescriptor opaqueDBool = new CheckBoxPropertyDescriptor(
				JRBaseStyle.PROPERTY_MODE, Messages.common_opaque);
		opaqueDBool.setDescription(Messages.MGraphicElement_opaque_description);
		desc.add(opaqueDBool);

		positionTypeD = new JSSEnumPropertyDescriptor(JRDesignElement.PROPERTY_POSITION_TYPE,
				Messages.common_position_type, PositionTypeEnum.class, NullEnum.NOTNULL);
		positionTypeD.setDescription(Messages.MGraphicElement_position_type_description);
		desc.add(positionTypeD);
		positionTypeD.setCategory(Messages.MGraphicElement_location_category);

		stretchTypeD = new JSSEnumPropertyDescriptor(JRDesignElement.PROPERTY_STRETCH_TYPE, Messages.common_stretch_type,
				StretchTypeEnum.class, NullEnum.NOTNULL);
		stretchTypeD.setCategory(Messages.common_size);
		stretchTypeD.setDescription(Messages.MGraphicElement_stretch_type_description);
		desc.add(stretchTypeD);

		CheckBoxPropertyDescriptor printRVAlueD = new CheckBoxPropertyDescriptor(
				JRDesignElement.PROPERTY_PRINT_REPEATED_VALUES, Messages.MGraphicElement_print_repeated_values);
		printRVAlueD.setDescription(Messages.MGraphicElement_print_repeated_values_description);
		desc.add(printRVAlueD);

		CheckBoxPropertyDescriptor rmLineWBlankD = new CheckBoxPropertyDescriptor(
				JRDesignElement.PROPERTY_REMOVE_LINE_WHEN_BLANK, Messages.MGraphicElement_remove_line_when_blank);
		rmLineWBlankD.setDescription(Messages.MGraphicElement_remove_line_when_blank_description);
		desc.add(rmLineWBlankD);

		CheckBoxPropertyDescriptor printInFirstWholeBandD = new CheckBoxPropertyDescriptor(
				JRDesignElement.PROPERTY_PRINT_IN_FIRST_WHOLE_BAND, Messages.MGraphicElement_print_in_first_whole_band);
		printInFirstWholeBandD.setDescription(Messages.MGraphicElement_print_in_first_whole_band_description);
		desc.add(printInFirstWholeBandD);

		CheckBoxPropertyDescriptor printWhenDetailOverflowsD = new CheckBoxPropertyDescriptor(
				JRDesignElement.PROPERTY_PRINT_WHEN_DETAIL_OVERFLOWS, Messages.MGraphicElement_print_when_detail_overflows);
		printWhenDetailOverflowsD.setDescription(Messages.MGraphicElement_print_when_detail_overflows_desription);
		printWhenDetailOverflowsD.setCategory(Messages.MGraphicElement_print_when);
		desc.add(printWhenDetailOverflowsD);

		JRExpressionPropertyDescriptor printWhenExprD = new JRExpressionPropertyDescriptor(
				JRDesignElement.PROPERTY_PRINT_WHEN_EXPRESSION, Messages.common_print_when_expression);
		printWhenExprD.setDescription(Messages.MGraphicElement_print_when_expression_description);
		printWhenExprD.setCategory(Messages.MGraphicElement_print_when);
		desc.add(printWhenExprD);

		JPropertyExpressionsDescriptor propertiesD = new JPropertyExpressionsDescriptor(
				JRDesignElement.PROPERTY_PROPERTY_EXPRESSIONS, Messages.MGraphicElement_property_expressions);
		propertiesD.setDescription(Messages.MGraphicElement_property_expressions_description);
		desc.add(propertiesD);

		// JPropertiesPropertyDescriptor propertiesMapD = new JPropertiesPropertyDescriptor(PROPERTY_MAP,
		// Messages.common_properties);
		// propertiesMapD.setDescription(Messages.common_properties);
		// desc.add(propertiesMapD);

		forecolorD.setCategory(Messages.common_graphic);
		backcolorD.setCategory(Messages.common_graphic);
		styleD.setCategory(Messages.common_graphic);

		defaultsMap.put(JRDesignElement.PROPERTY_PARENT_STYLE, null);
		defaultsMap.put(JRBaseStyle.PROPERTY_FORECOLOR, null);
		defaultsMap.put(JRBaseStyle.PROPERTY_BACKCOLOR, null);

		defaultsMap.put(JRBaseStyle.PROPERTY_MODE, Boolean.FALSE);
		defaultsMap.put(JRDesignElement.PROPERTY_POSITION_TYPE,
				positionTypeD.getEnumValue(PositionTypeEnum.FIX_RELATIVE_TO_TOP));
		defaultsMap.put(JRDesignElement.PROPERTY_STRETCH_TYPE, stretchTypeD.getEnumValue(StretchTypeEnum.NO_STRETCH));
		defaultsMap.put(JRDesignElement.PROPERTY_PRINT_REPEATED_VALUES, Boolean.TRUE);
		defaultsMap.put(JRDesignElement.PROPERTY_REMOVE_LINE_WHEN_BLANK, Boolean.FALSE);
		defaultsMap.put(JRDesignElement.PROPERTY_PRINT_IN_FIRST_WHOLE_BAND, Boolean.FALSE);
		defaultsMap.put(JRDesignElement.PROPERTY_PRINT_WHEN_DETAIL_OVERFLOWS, Boolean.FALSE);
		defaultsMap.put(JRDesignElement.PROPERTY_PRINT_WHEN_EXPRESSION, null);
	}

	public static final String PROPERTY_MAP = "PROPERTY_MAP"; //$NON-NLS-1$
	private RWComboBoxPropertyDescriptor styleD;
	private RComboBoxPropertyDescriptor groupChangesD;
	private static JSSEnumPropertyDescriptor positionTypeD;
	// private static JSSEnumPropertyDescriptor opaqueD;
	private static JSSEnumPropertyDescriptor opaqueD;
	private static JSSEnumPropertyDescriptor stretchTypeD;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		JRDesignElement jrElement = (JRDesignElement) getValue();
		if (id.equals(JRDesignElement.PROPERTY_KEY))
			return jrElement.getKey();
		if (id.equals(JRDesignElement.PROPERTY_PRINT_WHEN_EXPRESSION)) {
			return ExprUtil.getExpression(jrElement.getPrintWhenExpression());
		}
		if (id.equals(JRDesignElement.PROPERTY_PARENT_STYLE)) {
			if (jrElement.getStyleNameReference() != null)
				return jrElement.getStyleNameReference();
			if (jrElement.getStyle() != null)
				return jrElement.getStyle().getName();
			return ""; //$NON-NLS-1$
		}
		if (id.equals(JRDesignElement.PROPERTY_PRINT_WHEN_GROUP_CHANGES)) {
			if (jrElement.getPrintWhenGroupChanges() != null)
				return jrElement.getPrintWhenGroupChanges().getName();
			return ""; //$NON-NLS-1$
		}
		JRPropertiesMap propertiesMap = jrElement.getPropertiesMap();
		if (propertiesMap != null)
			propertiesMap = propertiesMap.cloneProperties();
		if (id.equals(JRDesignElement.PROPERTY_PROPERTY_EXPRESSIONS)) {
			JRPropertyExpression[] propertyExpressions = jrElement.getPropertyExpressions();
			if (propertyExpressions != null)
				propertyExpressions = propertyExpressions.clone();
			return new PropertyExpressionsDTO(propertyExpressions, propertiesMap, this);
		}
		if (id.equals(PROPERTY_MAP))
			return propertiesMap;
		if (id.equals(JRDesignElement.PROPERTY_HEIGHT))
			return new Integer(jrElement.getHeight());
		if (id.equals(JRDesignElement.PROPERTY_WIDTH))
			return new Integer(jrElement.getWidth());
		if (id.equals(JRDesignElement.PROPERTY_X))
			return new Integer(jrElement.getX());
		if (id.equals(JRDesignElement.PROPERTY_Y))
			return new Integer(jrElement.getY());
		// colors
		if (id.equals(JRBaseStyle.PROPERTY_BACKCOLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getOwnBackcolor());
		if (id.equals(JRBaseStyle.PROPERTY_FORECOLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getOwnForecolor());
		// opacity
		if (id.equals(JRBaseStyle.PROPERTY_MODE)){
				ModeEnum modeValue = jrElement.getOwnModeValue();
				return modeValue != null ? modeValue.equals(ModeEnum.TRANSPARENT) : null;
		}
		if (id.equals(JRDesignElement.PROPERTY_POSITION_TYPE))
			return positionTypeD.getEnumValue(jrElement.getPositionTypeValue());
		if (id.equals(JRDesignElement.PROPERTY_STRETCH_TYPE))
			return stretchTypeD.getEnumValue(jrElement.getStretchTypeValue());

		if (id.equals(JRDesignElement.PROPERTY_PRINT_REPEATED_VALUES))
			return new Boolean(jrElement.isPrintRepeatedValues());
		if (id.equals(JRDesignElement.PROPERTY_REMOVE_LINE_WHEN_BLANK))
			return new Boolean(jrElement.isRemoveLineWhenBlank());
		if (id.equals(JRDesignElement.PROPERTY_PRINT_IN_FIRST_WHOLE_BAND))
			return new Boolean(jrElement.isPrintInFirstWholeBand());
		if (id.equals(JRDesignElement.PROPERTY_PRINT_WHEN_DETAIL_OVERFLOWS))
			return new Boolean(jrElement.isPrintWhenDetailOverflows());

		return null;
	}
	
	public Object getPropertyActualValue(Object id) {
		JRDesignElement jrElement = (JRDesignElement) getValue();
		if (id.equals(JRBaseStyle.PROPERTY_BACKCOLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getBackcolor());
		if (id.equals(JRBaseStyle.PROPERTY_FORECOLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getForecolor());
		// opacity
		if (id.equals(JRBaseStyle.PROPERTY_MODE))
			return jrElement.getModeValue().equals(ModeEnum.TRANSPARENT);
		return super.getPropertyActualValue(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRDesignElement jrElement = (JRDesignElement) getValue();
		if (id.equals(JRDesignElement.PROPERTY_KEY))
			jrElement.setKey((String) value);
		else if (id.equals(JRDesignElement.PROPERTY_PARENT_STYLE)) {
			if (value != null) {
				if (!value.equals("")) { //$NON-NLS-1$
					JRStyle style = (JRStyle) getJasperDesign().getStylesMap().get(value);
					if (style != null) {
						jrElement.setStyle(style);
						// jrElement.setStyleNameReference(null);
					} else {
						jrElement.setStyleNameReference((String) value);
						// jrElement.setStyle(null);
					}
				}
			} else {
				jrElement.setStyle(null);
				jrElement.setStyleNameReference(null);
			}
		} else if (id.equals(JRDesignElement.PROPERTY_PRINT_WHEN_EXPRESSION))
			jrElement.setPrintWhenExpression(ExprUtil.setValues(jrElement.getPrintWhenExpression(), value));
		else if (id.equals(JRDesignElement.PROPERTY_PRINT_WHEN_GROUP_CHANGES)) {
			if (!value.equals("")) { //$NON-NLS-1$
				JRGroup group = (JRGroup) getJasperDesign().getGroupsMap().get(value);
				jrElement.setPrintWhenGroupChanges(group);
			}
		} else if (id.equals(JRDesignElement.PROPERTY_PROPERTY_EXPRESSIONS)) {
			if (value instanceof PropertyExpressionsDTO) {
				PropertyExpressionsDTO dto = (PropertyExpressionsDTO) value;
				JRPropertyExpression[] v = dto.getPropExpressions();
				JRPropertyExpression[] expr = jrElement.getPropertyExpressions();
				if (expr != null)
					for (JRPropertyExpression ex : expr)
						jrElement.removePropertyExpression(ex);
				if (v != null)
					for (JRPropertyExpression p : v)
						jrElement.addPropertyExpression(p);
				// now change properties
				JRPropertiesMap vmap = dto.getPropMap();
				String[] names = jrElement.getPropertiesMap().getPropertyNames();
				for (int i = 0; i < names.length; i++) {
					jrElement.getPropertiesMap().removeProperty(names[i]);
				}
				if (vmap != null) {
					names = vmap.getPropertyNames();
					for (int i = 0; i < names.length; i++)
						jrElement.getPropertiesMap().setProperty(names[i], vmap.getProperty(names[i]));
					this.getPropertyChangeSupport().firePropertyChange(PROPERTY_MAP, false, true);
				}
			}
		} else if (id.equals(JRDesignElement.PROPERTY_HEIGHT)) {
			jrElement.setHeight((Integer) Misc.nvl(value, Integer.valueOf(0)));
		} else if (id.equals(JRDesignElement.PROPERTY_WIDTH)) {
			jrElement.setWidth((Integer) Misc.nvl(value, Integer.valueOf(0)));
		} else if (id.equals(JRDesignElement.PROPERTY_X)) {
			jrElement.setX((Integer) Misc.nvl(value, Integer.valueOf(0)));
		} else if (id.equals(JRDesignElement.PROPERTY_Y)) {
			jrElement.setY((Integer) Misc.nvl(value, Integer.valueOf(0)));
		} else
		// colors
		if (id.equals(JRBaseStyle.PROPERTY_FORECOLOR)) {
			jrElement.setForecolor(Colors.getAWT4SWTRGBColor((RGB) value));
		} else if (id.equals(JRBaseStyle.PROPERTY_BACKCOLOR)) {
			jrElement.setBackcolor(Colors.getAWT4SWTRGBColor((RGB) value));
		} else
		// opacity
		if (id.equals(JRBaseStyle.PROPERTY_MODE))
			if (value == null) jrElement.setMode(null);
			else if ((Boolean)value) jrElement.setMode(ModeEnum.TRANSPARENT);
			else jrElement.setMode(ModeEnum.OPAQUE);
		else if (id.equals(JRDesignElement.PROPERTY_POSITION_TYPE))
			jrElement.setPositionType((PositionTypeEnum) positionTypeD.getEnumValue(value));
		else if (id.equals(JRDesignElement.PROPERTY_STRETCH_TYPE))
			jrElement.setStretchType((StretchTypeEnum) stretchTypeD.getEnumValue(value));

		else if (id.equals(JRDesignElement.PROPERTY_PRINT_REPEATED_VALUES))
			jrElement.setPrintRepeatedValues(((Boolean) value).booleanValue());
		else if (id.equals(JRDesignElement.PROPERTY_REMOVE_LINE_WHEN_BLANK))
			jrElement.setRemoveLineWhenBlank(((Boolean) value).booleanValue());
		else if (id.equals(JRDesignElement.PROPERTY_PRINT_IN_FIRST_WHOLE_BAND))
			jrElement.setPrintInFirstWholeBand(((Boolean) value).booleanValue());
		else if (id.equals(JRDesignElement.PROPERTY_PRINT_WHEN_DETAIL_OVERFLOWS))
			jrElement.setPrintWhenDetailOverflows(((Boolean) value).booleanValue());
		else if (id.equals(PROPERTY_MAP)) {
			JRPropertiesMap v = (JRPropertiesMap) value;
			String[] names = jrElement.getPropertiesMap().getPropertyNames();
			for (int i = 0; i < names.length; i++) {
				jrElement.getPropertiesMap().removeProperty(names[i]);
			}
			names = v.getPropertyNames();
			for (int i = 0; i < names.length; i++)
				jrElement.getPropertiesMap().setProperty(names[i], v.getProperty(names[i]));
			this.getPropertyChangeSupport().firePropertyChange(PROPERTY_MAP, false, true);
		}
	}

	public boolean isCopyable2(Object parent) {
		if (parent instanceof MElementGroup || parent instanceof IPastableGraphic)
			return true;
		return false;
	}
}
