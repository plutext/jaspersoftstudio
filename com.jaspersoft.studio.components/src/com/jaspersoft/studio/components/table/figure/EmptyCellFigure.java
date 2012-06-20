/*
 * JasperReports - Free Java Reporting Library. Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.studio.components.table.figure;

import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.engine.export.draw.DrawVisitor;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;

import com.jaspersoft.studio.editor.gef.figures.FrameFigure;

public class EmptyCellFigure extends FrameFigure {
	private StandardBaseColumn column;

	public EmptyCellFigure() {
		super();
		setOpaque(true);
		setBackgroundColor(ColorConstants.lightGray);
		setBorder(new LineBorder(1));
	}

	public void setJRElement(StandardBaseColumn column,
			DrawVisitor drawVisitor, int height) {
		this.column = column;
		this.height = height;
		super.setJRElement(null, drawVisitor);
		setSize(getElementWidth() + 3, getElementHeight() + 3);
	}

	private int height;

	@Override
	protected int getElementHeight() {
		return height;
	}

	@Override
	protected int getElementWidth() {
		return column.getWidth();
	}

	@Override
	public void paint(Graphics graphics) {
		Rectangle b = (this instanceof HandleBounds) ? ((HandleBounds) this)
				.getHandleBounds() : this.getBounds();

		graphics.setBackgroundColor(getBackgroundColor());
		graphics.fillRectangle(b.x, b.y, b.width, b.height);

		paintBorder(graphics);
	}
}
