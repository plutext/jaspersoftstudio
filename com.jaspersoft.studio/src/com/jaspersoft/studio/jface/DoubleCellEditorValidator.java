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
package com.jaspersoft.studio.jface;

import org.eclipse.jface.viewers.ICellEditorValidator;

import com.jaspersoft.studio.messages.Messages;
/*
 * The Class FloatCellEditorValidator.
 * 
 * @author Chicu Veaceslav
 */
public class DoubleCellEditorValidator implements ICellEditorValidator {

	/** The instance. */
	private static DoubleCellEditorValidator instance;

	/**
	 * Instance.
	 * 
	 * @return the float cell editor validator
	 */
	public static DoubleCellEditorValidator instance() {
		if (instance == null)
			instance = new DoubleCellEditorValidator();
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	public String isValid(Object value) {
		try {
			if (value instanceof Double)
				return null;
			if (value instanceof String)
				new Double((String) value);
			return null;
		} catch (NumberFormatException exc) {
			return Messages.DoubleCellEditorValidator_this_is_not_a_double_number; 
		}
	}

}
