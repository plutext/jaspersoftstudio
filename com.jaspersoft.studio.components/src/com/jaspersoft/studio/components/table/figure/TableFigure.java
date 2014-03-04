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
package com.jaspersoft.studio.components.table.figure;

import java.awt.Graphics2D;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElement;

import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.editor.gef.figures.JRComponentFigure;
import com.jaspersoft.studio.editor.java2d.StackGraphics2D;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;

public class TableFigure extends JRComponentFigure {

	private MTable tableModel = null;
	
	private StackGraphics2D cachedGraphics = null;
	
	
	/**
	 * Instantiates a new text field figure.
	 */
	public TableFigure(MTable tableModel) {
		super();
		this.tableModel = tableModel;
	}

	@Override
	protected void draw(JSSDrawVisitor drawVisitor, JRElement jrElement) {
		if (tableModel != null){
			if (cachedGraphics == null || tableModel.hasChangedProperty()){
				Graphics2D oldGraphics = drawVisitor.getGraphics2d();
				cachedGraphics = new StackGraphics2D(oldGraphics);
				drawVisitor.setGraphics2D(cachedGraphics);
				drawVisitor.visitComponentElement((JRComponentElement) jrElement);
				drawVisitor.setGraphics2D(oldGraphics);
				tableModel.setChangedProperty(false);
			}
			cachedGraphics.setRealDrawer(drawVisitor.getGraphics2d());
			cachedGraphics.paintStack();
		}
	}
}
