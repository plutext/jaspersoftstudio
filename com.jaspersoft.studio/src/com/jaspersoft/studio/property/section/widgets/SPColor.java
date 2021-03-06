/*******************************************************************************
 * Copyright (C) 2010 - 2016. TIBCO Software Inc. 
 * All Rights Reserved. Confidential & Proprietary.
 ******************************************************************************/
package com.jaspersoft.studio.property.section.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.color.chooser.ColorDialog;
import com.jaspersoft.studio.property.descriptor.color.ColorLabelProvider;
import com.jaspersoft.studio.property.descriptor.color.ColorPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.utils.AlfaRGB;

public class SPColor extends ASPropertyWidget<ColorPropertyDescriptor> {
	
	private ToolItem foreButton;
	
	private ColorLabelProvider colorLabelProvider = new ColorLabelProvider(null);
	
	private APropertyNode parent;
	
	private ToolBar toolBar;

	private boolean isEnabled = true;
	
	public SPColor(Composite parent, AbstractSection section, ColorPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	@Override
	public Control getControl() {
		return toolBar;
	}

	protected void createComponent(Composite parent) {
		toolBar = new ToolBar(parent, SWT.FLAT | SWT.WRAP | SWT.LEFT);
		toolBar.setBackground(parent.getBackground());

		// The listener can not be disposed by its own, so we can dispose it manually
		// when the component that used it don't need it anymore
		toolBar.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				colorLabelProvider.dispose();
			}
		});
		foreButton = new ToolItem(toolBar, SWT.PUSH);
		foreButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (isEnabled){
					ColorDialog cd = new ColorDialog(toolBar.getShell());
					cd.setText(pDescriptor.getDisplayName());
					AlfaRGB rgb = (AlfaRGB) section.getElement().getPropertyActualValue(pDescriptor.getId());
					cd.setRGB(rgb == null ? null : rgb);
					boolean useTransparency = true;
					useTransparency = pDescriptor.supportsTransparency();
					if (useTransparency) {
						AlfaRGB newColor = cd.openAlfaRGB();
						if (newColor != null) {
							changeProperty(section, pDescriptor.getId(), newColor);
						}
					} else {
						RGB newColor = cd.openRGB();
						if (newColor != null) {
							changeProperty(section, pDescriptor.getId(), AlfaRGB.getFullyOpaque(newColor));
						}
					}
				}
			}
		});
		foreButton.setToolTipText(pDescriptor.getDescription());
		toolBar.pack();
	}

	public void setData(APropertyNode parent, AlfaRGB b) {
		this.parent = parent;
		foreButton.setImage(colorLabelProvider.getImage(b));
	}

	public void setData(APropertyNode pnode, Object b) {
		createContextualMenu(pnode);
		isEnabled = pnode.isEditable();
		setData(null, (AlfaRGB) b);
	}

	private void changeProperty(final AbstractSection section, final Object property, AlfaRGB newColor) {
		if (parent == null)
			section.changeProperty(property, newColor);
		else
			section.changePropertyOn(property, newColor, parent);
	}
}
