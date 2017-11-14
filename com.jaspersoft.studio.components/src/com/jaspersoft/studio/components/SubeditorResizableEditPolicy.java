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
package com.jaspersoft.studio.components;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.ResizeTracker;

import com.jaspersoft.studio.compatibility.ToolUtilitiesCompatibility;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.ElementResizableEditPolicy;

/**
 * Edit policy used for elements that can be opened inside subeditors. It is really similar to 
 * the standard one but when an element is opened inside its subeditor it uses a special resize
 * tracker that doesn't allow to change the position of the element in another editor when dragging
 * from the North or West border
 * 
 * @author Marco Orlandin
 *
 */
public class SubeditorResizableEditPolicy extends ElementResizableEditPolicy {
	
	/**
	 * When the element is not in the subeditor works as standard, otherwise return a special
	 * resize tracker 
	 */
	@Override
	protected ResizeTracker getResizeTracker(int direction) {
		if(ToolUtilitiesCompatibility.isSubeditorMainElement(getHost())) return new SubEditorResizeTracker((GraphicalEditPart) getHost(), direction);
		else return super.getResizeTracker(direction);
	};
	
}
