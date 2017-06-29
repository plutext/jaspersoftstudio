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
package com.jaspersoft.studio.model;

import net.sf.jasperreports.engine.JRElementGroup;

/**
 * Interface that represent a container node that can provide a JRElementGroup 
 * where the children are. Note that not all the container are JRElementGroup, the
 * frame for example are container but the don't have an ElemElement
 */
public interface IGroupElement {
	
	/**
	 * Return the element group of the current element
	 * 
	 * @return the element group
	 */
	public JRElementGroup getJRElementGroup();
}
