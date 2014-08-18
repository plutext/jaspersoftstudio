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
package com.jaspersoft.studio.server.wizard.exp;

import net.sf.jasperreports.eclipse.ui.validator.EmptyStringValidator;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationUpdater;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.Misc;

public class ExportMetadataPage extends WizardPage {

	private Text tfile;

	private DataBindingContext bindingContext;
	private ExportOptions value = new ExportOptions();

	private Button bIncRepPerm;

	private Button bIncRepJobs;

	protected ExportMetadataPage() {
		super("exportmetadata"); //$NON-NLS-1$
		setTitle("Export Metadata");
		setDescription("Export Metadata");
		bindingContext = new DataBindingContext();
	}

	@Override
	public void createControl(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setLayout(new GridLayout(2, false));
		setControl(cmp);

		Label lbl = new Label(cmp, SWT.NONE);
		lbl.setText("Export Data File");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		lbl.setLayoutData(gd);

		tfile = new Text(cmp, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		tfile.setLayoutData(gd);

		Button bfile = new Button(cmp, SWT.PUSH);
		bfile.setText(Messages.common_browse);
		bfile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				FileDialog fd = new FileDialog(Display.getDefault().getActiveShell());
				fd.setFileName("export.zip");
				fd.setFilterPath(root.getLocation().toOSString());
				fd.setFilterExtensions(new String[] { "*.zip", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$  
				String selection = fd.open();
				tfile.setText(Misc.nvl(selection));
			}
		});

		bIncRepPerm = new Button(cmp, SWT.CHECK);
		bIncRepPerm.setText("Include Repository Permissions");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		bIncRepPerm.setLayoutData(gd);
		bIncRepPerm.setSelection(true);

		bIncRepJobs = new Button(cmp, SWT.CHECK);
		bIncRepJobs.setText("Include Report Jobs");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		bIncRepJobs.setLayoutData(gd);
		bIncRepJobs.setSelection(true);

		Binding binding = bindingContext.bindValue(SWTObservables.observeText(tfile, SWT.Modify), PojoObservables.observeValue(value, "file"), //$NON-NLS-1$
				new UpdateValueStrategy().setAfterConvertValidator(new EmptyStringValidator()), null);
		ControlDecorationSupport.create(binding, SWT.TOP | SWT.LEFT, null, new ControlDecorationUpdater());
		bindingContext.bindValue(SWTObservables.observeSelection(bIncRepPerm), PojoObservables.observeValue(value, "incRepositoryPermission")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeSelection(bIncRepJobs), PojoObservables.observeValue(value, "incReportJobs")); //$NON-NLS-1$

	}

	public ExportOptions getValue() {
		return value;
	}
}
