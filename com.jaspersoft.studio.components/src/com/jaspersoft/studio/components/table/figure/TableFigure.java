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
package com.jaspersoft.studio.components.table.figure;

import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.editor.gef.figures.JRComponentFigure;

public class TableFigure extends JRComponentFigure {
	
	/**
	 * Instantiates a new text field figure.
	 */
	public TableFigure(MTable tableModel) {
		super(tableModel);
	}
	
	@Override
	protected boolean allowsFigureDrawCache() {
		return true;
	}
}