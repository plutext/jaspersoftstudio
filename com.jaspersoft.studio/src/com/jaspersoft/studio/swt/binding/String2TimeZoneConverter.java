/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.swt.binding;

import java.util.TimeZone;

import org.eclipse.core.databinding.conversion.Converter;

public class String2TimeZoneConverter extends Converter {
	public String2TimeZoneConverter() {
		super(String.class, TimeZone.class);
	}

	public Object convert(Object source) {
		if (source instanceof String)
			return TimeZone.getTimeZone((String) source);
		return null;
	}

}
