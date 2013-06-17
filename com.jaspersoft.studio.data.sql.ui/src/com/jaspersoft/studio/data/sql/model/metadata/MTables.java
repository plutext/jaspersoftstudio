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
package com.jaspersoft.studio.data.sql.model.metadata;

import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.studio.data.sql.model.MDBObjects;
import com.jaspersoft.studio.model.ANode;

public class MTables extends MDBObjects {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MTables(ANode parent, String value) {
		super(parent, value, "icons/table.png");
	}

	public String getTableCatalog() {
		return (String) getParent().getValue();
	}

	public String getTableSchema() {
		return ((MSqlSchema) getParent()).getTableCatalog();
	}
}
