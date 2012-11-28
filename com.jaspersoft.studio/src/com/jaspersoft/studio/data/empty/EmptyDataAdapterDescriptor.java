/*******************************************************************************
 * Copyright (C) 2010 - 2012 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.studio.data.empty;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.empty.EmptyDataAdapter;
import net.sf.jasperreports.data.empty.EmptyDataAdapterImpl;

import org.eclipse.swt.graphics.Image;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterEditor;

public class EmptyDataAdapterDescriptor extends DataAdapterDescriptor {
	private EmptyDataAdapter emptyDataAdapter;

	@Override
	public EmptyDataAdapter getDataAdapter() {
		if (emptyDataAdapter == null) {
			emptyDataAdapter = new EmptyDataAdapterImpl();
			emptyDataAdapter.setRecordCount(1);
		}
		return emptyDataAdapter;
	}

	@Override
	public void setDataAdapter(DataAdapter dataAdapter) {
		this.emptyDataAdapter = (EmptyDataAdapter) dataAdapter;
	}

	@Override
	public Image getIcon(int size) {
		return JaspersoftStudioPlugin.getInstance().getImage("icons/battery-empty.png");
	}

	@Override
	public DataAdapterEditor getEditor() {
		return new EmptyDataAdapterEditor();
	}

}
