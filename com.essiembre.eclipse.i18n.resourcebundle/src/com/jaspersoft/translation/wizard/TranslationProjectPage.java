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
package com.jaspersoft.translation.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.essiembre.eclipse.rbe.RBEPlugin;

/**
 * Page to define the name of a new translation project
 * 
 * @author Orlandin Marco
 *
 */
public class TranslationProjectPage  extends WizardPage {
	
	public TranslationProjectPage() {
		super("JRPROJECTPAGE"); //$NON-NLS-1$
		setTitle(RBEPlugin.getString("translation.wiz.title")); //$NON-NLS-1$
		setDescription(RBEPlugin.getString("translation.wiz.description")); //$NON-NLS-1$
	}
	
	/**
	 * The name of the project
	 */
	private String name;

	/**
	 * Return the project name
	 */
	public String getName() {
		return name;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		composite.setLayout(new GridLayout(2, false));

		new Label(composite, SWT.NONE).setText(RBEPlugin.getString("translation.wiz.name")); //$NON-NLS-1$

		final Text tname = new Text(composite, SWT.BORDER);
		tname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tname.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				name = tname.getText();
			}
		});

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd);
	}
}

