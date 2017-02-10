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

import net.sf.jasperreports.charts.design.JRDesignChartAxis;
import net.sf.jasperreports.charts.design.JRDesignMultiAxisPlot;
import net.sf.jasperreports.engine.design.JRDesignChart;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.chart.model.MChart;
import com.jaspersoft.studio.components.chart.model.chartAxis.MChartAxes;
/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteChartAxesCommand extends Command {

	private JRDesignMultiAxisPlot chartPlot;
	private JRDesignChartAxis jrChart;
	private int oldIndex = 0;

	/**
	 * Instantiates a new delete element command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public DeleteChartAxesCommand(MChart destNode, MChartAxes srcNode) {
		super();
		this.jrChart = (JRDesignChartAxis) srcNode.getValue();
		this.chartPlot = (JRDesignMultiAxisPlot) ((JRDesignChart) destNode.getValue()).getPlot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		oldIndex = chartPlot.getAxes().indexOf(jrChart);
		chartPlot.removeAxis(jrChart);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (chartPlot == null || jrChart == null)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (oldIndex >= 0 && oldIndex < chartPlot.getAxes().size())
			chartPlot.addAxis(oldIndex, jrChart);
		else
			chartPlot.addAxis(jrChart);
	}
	
	@Override
	public boolean canExecute() {
		if(chartPlot==null) {
			return false;
		}
		else {
			// cannot have a multi-axes chart with zero children
			return chartPlot.getAxes().size()>1;
		}
	}
}
