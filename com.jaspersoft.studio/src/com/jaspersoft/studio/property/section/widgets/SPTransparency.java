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
package com.jaspersoft.studio.property.section.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.property.section.AbstractSection;

public class SPTransparency extends SPNumber {

	private Composite composite;
	
	private Scale scale;
	
	private SelectionListener transparencyChangeListener;
	
	public SPTransparency(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	@Override
	public Control getControl() {
		return composite;
	}

	protected void createComponent(Composite parent) {
		composite = section.getWidgetFactory().createComposite(parent);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.wrap = true;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.center = true;
		composite.setLayout(layout);

		scale = new Scale(composite, SWT.HORIZONTAL);
		scale.setMinimum(0);
		scale.setMaximum(100);
		scale.setIncrement(1);
		scale.setPageIncrement(5);
		RowData rd = new RowData();
		rd.width = 100;
		scale.setLayoutData(rd);
		scale.setToolTipText(pDescriptor.getDescription());
		transparencyChangeListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ftext.setValue(scale.getSelection());
				changeValue();
			}
		};
		scale.addSelectionListener(transparencyChangeListener);
		super.createComponent(composite);
		setBounds(0, 100);
	}
	
	@Override
	protected void changeValue(){
		Number newValue = getValue();
		if (newValue != null) newValue = newValue.floatValue() / 100f;
		section.changeProperty(pDescriptor.getId(), newValue);
		setData(section.getElement(), newValue);
	}

	@Override
	public void setDataNumber(Number f, boolean isInherited) {
		//remove the listener to avoid the setSelection trigger it another time
		scale.removeSelectionListener(transparencyChangeListener);
		if (f != null) {
			int alfa = Math.round(100 * f.floatValue());
			super.setDataNumber(100 * f.floatValue(), isInherited);
			scale.setSelection(alfa);
		} else { 
			scale.setSelection(0);
			super.setDataNumber(null, isInherited);
		}
		scale.addSelectionListener(transparencyChangeListener);
	}

}
