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
package com.jaspersoft.studio.editor.gef.parts.text;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;

import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.model.text.MTextField;
import com.jaspersoft.studio.model.text.command.EditTextFieldExpressionCommand;

/*
 * The Class FigureEditPart.
 */
public class TextFieldFigureEditPart extends FigureEditPart {

	@Override
	public void performRequest(Request req) {
		if (RequestConstants.REQ_OPEN.equals(req.getType())) {
			Command cmd = null;
			MTextField textfield = (MTextField) getModel();
			cmd = new EditTextFieldExpressionCommand(textfield) {
				@Override
				public void execute() {
					if(this.showDialog()==Window.OK) {
						super.execute();
					}
				}
			};
			getViewer().getEditDomain().getCommandStack().execute(cmd);
		} else
			super.performRequest(req);
	}

}
