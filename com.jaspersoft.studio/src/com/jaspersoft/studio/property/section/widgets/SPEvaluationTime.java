/*******************************************************************************
 * Copyright (C) 2010 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.studio.property.section.widgets;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.utils.EnumHelper;

public class SPEvaluationTime extends ASPropertyWidget {
	private Combo evalTime;
	private IPropertyDescriptor gDescriptor;

	public SPEvaluationTime(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor,
			IPropertyDescriptor gDescriptor) {
		super(parent, section, pDescriptor);
		this.gDescriptor = gDescriptor;
	}

	@Override
	public Control getControl() {
		return evalTime;
	}

	public void createComponent(Composite parent) {
		evalTime = section.getWidgetFactory().createCombo(parent, SWT.READ_ONLY);
		evalTime.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				String group = null;
				Integer et = 1;
				String str = evalTime.getItem(evalTime.getSelectionIndex());
				if (str.startsWith(GROUPPREFIX)) {
					group = str.substring(GROUPPREFIX.length());
					et = EnumHelper.getValue(EvaluationTimeEnum.GROUP, 1, false);
				} else {
					et = EnumHelper.getValue(EvaluationTimeEnum.getByName(str), 1, false);
				}
				//It is important to set first the group because the group changing dosen't trigger an event
				//so otherwise setting the type first trigger the event but the group has not been set to the 
				//setData method dosen't find the group and set always the element 0.
				section.changeProperty(gDescriptor.getId(), group);
				section.changeProperty(pDescriptor.getId(), et);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		evalTime.setToolTipText(pDescriptor.getDescription());
	}

	@Override
	public void setData(APropertyNode pnode, Object value) {
		JasperDesign jasperDesign = pnode.getJasperDesign();
		JRDataset dataset = null;
		MDatasetRun mdataset = (MDatasetRun) pnode.getPropertyValue(JRDesignElementDataset.PROPERTY_DATASET_RUN);
		if (mdataset != null) {
			JRDesignDatasetRun datasetRun = mdataset.getValue();
			if (datasetRun != null) {
				String dsname = datasetRun.getDatasetName();
				dataset = jasperDesign.getDatasetMap().get(dsname);
			}
		}
		if (dataset == null && jasperDesign != null)
			dataset = jasperDesign.getMainDataset();

		setData((Integer) pnode.getPropertyValue(pDescriptor.getId()),
				(String) pnode.getPropertyValue(gDescriptor.getId()), SPEvaluationTime.getItems(dataset));

	}

	public void setData(Integer et, String group, String[] items) {
		evalTime.setItems(items);
		int selection = 0;
		EvaluationTimeEnum sel = (EvaluationTimeEnum) EnumHelper.getSetValue(EvaluationTimeEnum.values(), et, 1, false);

		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(sel.getName())) {
				selection = i;
				break;
			}
			if (items[i].startsWith(GROUPPREFIX) && sel.equals(EvaluationTimeEnum.GROUP)) {
				if (items[i].substring(GROUPPREFIX.length()).equals(group)) {
					selection = i;
					break;
				}
			}
		}
		evalTime.select(selection);
	}

	public static String[] getItems(JRDataset dataset) {
		List<String> lsIncs = new ArrayList<String>();
		for (EvaluationTimeEnum en : EvaluationTimeEnum.values()) {
			if (en.equals(EvaluationTimeEnum.GROUP)) {
				if (dataset != null)
					for (JRGroup gr : dataset.getGroups())
						lsIncs.add(GROUPPREFIX + gr.getName());
			} else {
				lsIncs.add(en.getName());
			}
		}
		return lsIncs.toArray(new String[lsIncs.size()]);
	}

	private static final String GROUPPREFIX = "[Group] ";
}
