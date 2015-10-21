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
package com.jaspersoft.studio.editor.gef.figures;

import java.awt.Graphics2D;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRRectangle;

import com.jaspersoft.studio.jasper.JSSDrawVisitor;
import com.jaspersoft.studio.model.MRectangle;
/*
 * The Class RectangleFigure.
 */
public class RectangleFigure extends LineFigure {
	
	public RectangleFigure(MRectangle rectangle) {
		super(rectangle);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.editor.gef.figures.LineFigure#draw(net.sf.jasperreports.engine.export.draw.DrawVisitor,
	 * net.sf.jasperreports.engine.JRElement)
	 */
	@Override
	protected void draw(JSSDrawVisitor drawVisitor, JRElement jrElement) {
		if (cachedGraphics == null || model.hasChangedProperty()){
			model.setChangedProperty(false);
			Graphics2D oldGraphics = drawVisitor.getGraphics2d();
			cachedGraphics = getCachedGraphics(oldGraphics);
			drawVisitor.setGraphics2D(cachedGraphics);
			drawVisitor.visitRectangle((JRRectangle) jrElement);
			drawVisitor.setGraphics2D(oldGraphics);
		}
		cachedGraphics.setGraphics(drawVisitor.getGraphics2d());
		cachedGraphics.paintCache();
	}

}