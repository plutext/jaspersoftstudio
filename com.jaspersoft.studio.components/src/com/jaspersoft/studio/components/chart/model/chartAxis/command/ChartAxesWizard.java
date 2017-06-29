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
package com.jaspersoft.studio.components.chart.model.chartAxis.command;

import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.studio.components.chart.messages.Messages;

public class ChartAxesWizard extends Wizard {
	private ChartAxesWizardPage page0;
	private byte chart;

	public ChartAxesWizard() {
		super();
		setWindowTitle(Messages.common_chartaxis_wizard);
	}

	@Override
	public void addPages() {
		page0 = new ChartAxesWizardPage();
		addPage(page0);
	}

	public byte getChartAxis() {
		if (page0 != null)
			return page0.getChartAxis();
		return chart;
	}

	@Override
	public boolean performFinish() {
		return true;
	}
	
	@Override
	public boolean canFinish() {
		byte chartAxis = getChartAxis();
		if(chartAxis<=0){
			return false;
		}
		return super.canFinish();
	}

}
