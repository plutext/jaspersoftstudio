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
package com.jaspersoft.studio.components.map.model.marker.dialog;

import java.util.List;

import net.sf.jasperreports.components.map.Marker;
import net.sf.jasperreports.components.map.MarkerProperty;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class TMarkerLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		Marker dto = (Marker) element;
		List<MarkerProperty> prp = dto.getProperties();
		switch (columnIndex) {
		case 1:
			for (MarkerProperty mp : prp)
				if (mp.getName().equals(Marker.PROPERTY_longitude))
					return getValue(mp);
			return "null";
		case 0:
			for (MarkerProperty mp : prp)
				if (mp.getName().equals(Marker.PROPERTY_latitude))
					return getValue(mp);
			return "null";
		}
		return ""; //$NON-NLS-1$
	}

	public static String getValue(MarkerProperty mp) {
		if (mp.getValueExpression() != null)
			return mp.getValueExpression().getText();
		if (mp.getValue() != null)
			return mp.getValue();

		return "null";
	}
}
