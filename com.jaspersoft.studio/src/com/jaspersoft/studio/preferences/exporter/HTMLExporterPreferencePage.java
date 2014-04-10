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
package com.jaspersoft.studio.preferences.exporter;

import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.StudioPreferencePage;
import com.jaspersoft.studio.preferences.editor.JSSComboFieldEditor;
import com.jaspersoft.studio.preferences.editor.text.TextFieldEditor;
import com.jaspersoft.studio.preferences.util.FieldEditorOverlayPage;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;
import com.jaspersoft.studio.utils.Misc;

/*
 * 
 */
public class HTMLExporterPreferencePage extends FieldEditorOverlayPage {
	public static final String PAGE_ID = "com.jaspersoft.studio.preferences.exporter.HTMLExporterPreferencePage.property";

	public static final String NSF_EXPORT_HTML_ACCESSIBLE = "net.sf.jasperreports.export.html.accessible"; //$NON-NLS-1$

	public static final String NSF_EXPORT_HTML_HEADER = "net.sf.jasperreports.export.html.header"; //$NON-NLS-1$
	public static final String NSF_EXPORT_HTML_FOOTER = "net.sf.jasperreports.export.html.footer"; //$NON-NLS-1$
	public static final String NSF_EXPORT_HTML_BETWEEN_PAGES = "net.sf.jasperreports.export.html.between.pages"; //$NON-NLS-1$

	public static final String NSF_EXPORT_HTML_IS_OUTPUT_IMAGES_TO_DIR = "net.sf.jasperreports.export.html.is.output.images.to.dir"; //$NON-NLS-1$
	public static final String NSF_EXPORT_HTML_IMAGES_DIR_NAME = "net.sf.jasperreports.export.html.images.dir.name"; //$NON-NLS-1$
	public static final String NSF_EXPORT_HTML_IMAGES_URI = "net.sf.jasperreports.export.html.images.uri"; //$NON-NLS-1$

	public HTMLExporterPreferencePage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.HTMLExporterPreferencePage_14);
	}

	/**
	 *
	 */
	public void createFieldEditors() {

		CTabFolder tabFolder = new CTabFolder(getFieldEditorParent(), SWT.TOP);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		createTabPageHTML(tabFolder);
		createTabPageImages(tabFolder);
		createTabPageHB(tabFolder);
		createTabPageBP(tabFolder);

		tabFolder.setSelection(0);
	}

	private void createTabPageHTML(CTabFolder tabFolder) {
		CTabItem ptab = new CTabItem(tabFolder, SWT.NONE);
		ptab.setText(Messages.HTMLExporterPreferencePage_15);

		Composite sc = new Composite(tabFolder, SWT.NONE);

		JSSComboFieldEditor cfe = new JSSComboFieldEditor(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT,
				Messages.HTMLExporterPreferencePage_16, new String[][] {
						{ Messages.HTMLExporterPreferencePage_17, Messages.HTMLExporterPreferencePage_18 },
						{ Messages.HTMLExporterPreferencePage_19, Messages.HTMLExporterPreferencePage_20 } }, sc);
		addField(cfe);
		HelpSystem.setHelp(cfe.getComboBoxControl(sc), StudioPreferencePage.REFERENCE_PREFIX + cfe.getPreferenceName());

		BooleanFieldEditor bf = new BooleanFieldEditor(NSF_EXPORT_HTML_ACCESSIBLE, Messages.HTMLExporterPreferencePage_21,
				sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(JRHtmlExporterParameter.PROPERTY_FLUSH_OUTPUT, Messages.HTMLExporterPreferencePage_22,
				sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES,
				Messages.HTMLExporterPreferencePage_23, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(JRHtmlExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Messages.HTMLExporterPreferencePage_24, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(JRHtmlExporterParameter.PROPERTY_USING_IMAGES_TO_ALIGN,
				Messages.HTMLExporterPreferencePage_25, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND,
				Messages.HTMLExporterPreferencePage_26, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD,
				Messages.HTMLExporterPreferencePage_27, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		ptab.setControl(sc);
	}

	private void createTabPageHB(CTabFolder tabFolder) {
		CTabItem ptab = new CTabItem(tabFolder, SWT.NONE);
		ptab.setText(Messages.HTMLExporterPreferencePage_28);

		Composite sc = new Composite(tabFolder, SWT.NONE);
		sc.setLayout(new GridLayout());

		TextFieldEditor se = new TextFieldEditor(NSF_EXPORT_HTML_HEADER, Messages.HTMLExporterPreferencePage_29, sc);
		se.getTextControl(sc).setLayoutData(new GridData(GridData.FILL_BOTH));
		addField(se);

		TextFieldEditor scf = new TextFieldEditor(NSF_EXPORT_HTML_FOOTER, Messages.HTMLExporterPreferencePage_30, sc);
		scf.getTextControl(sc).setLayoutData(new GridData(GridData.FILL_BOTH));
		addField(scf);

		ptab.setControl(sc);
	}

	private void createTabPageBP(CTabFolder tabFolder) {
		CTabItem ptab = new CTabItem(tabFolder, SWT.NONE);
		ptab.setText(Messages.HTMLExporterPreferencePage_31);

		Composite sc = new Composite(tabFolder, SWT.NONE);
		sc.setLayout(new GridLayout());

		TextFieldEditor scf = new TextFieldEditor(NSF_EXPORT_HTML_BETWEEN_PAGES, Messages.HTMLExporterPreferencePage_32, sc);
		scf.getTextControl(sc).setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL));
		addField(scf);

		ptab.setControl(sc);
	}

	private void createTabPageImages(CTabFolder tabFolder) {
		CTabItem ptab = new CTabItem(tabFolder, SWT.NONE);
		ptab.setText(Messages.HTMLExporterPreferencePage_33);

		Composite sc = new Composite(tabFolder, SWT.NONE);
		sc.setLayout(new GridLayout(3, false));

		addField(new BooleanFieldEditor(NSF_EXPORT_HTML_IS_OUTPUT_IMAGES_TO_DIR, Messages.HTMLExporterPreferencePage_34, sc));
		addField(new StringFieldEditor(NSF_EXPORT_HTML_IMAGES_URI, Messages.HTMLExporterPreferencePage_35, sc));
		addField(new DirectoryFieldEditor(NSF_EXPORT_HTML_IMAGES_DIR_NAME, Messages.HTMLExporterPreferencePage_36, sc));

		ptab.setControl(sc);
	}

	public static void getDefaults(IPreferenceStore store) {
		store.setDefault(NSF_EXPORT_HTML_ACCESSIBLE,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(NSF_EXPORT_HTML_ACCESSIBLE), "false")); //$NON-NLS-1$
		store.setDefault(JRHtmlExporterParameter.PROPERTY_FLUSH_OUTPUT,
				PropertiesHelper.DPROP.getProperty(JRHtmlExporterParameter.PROPERTY_FLUSH_OUTPUT));
		store.setDefault(JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES,
				PropertiesHelper.DPROP.getProperty(JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES));
		store.setDefault(JRHtmlExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(JRHtmlExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS), "false")); //$NON-NLS-1$
		store.setDefault(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT,
				PropertiesHelper.DPROP.getProperty(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT));
		store.setDefault(JRHtmlExporterParameter.PROPERTY_USING_IMAGES_TO_ALIGN,
				PropertiesHelper.DPROP.getProperty(JRHtmlExporterParameter.PROPERTY_USING_IMAGES_TO_ALIGN));
		store.setDefault(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND,
				PropertiesHelper.DPROP.getProperty(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND));
		store.setDefault(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD,
				PropertiesHelper.DPROP.getProperty(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD));

		store.setDefault(NSF_EXPORT_HTML_HEADER, Misc.nvl(PropertiesHelper.DPROP.getProperty(NSF_EXPORT_HTML_HEADER), "")); //$NON-NLS-1$
		store.setDefault(NSF_EXPORT_HTML_FOOTER, Misc.nvl(PropertiesHelper.DPROP.getProperty(NSF_EXPORT_HTML_FOOTER), "")); //$NON-NLS-1$
		store.setDefault(NSF_EXPORT_HTML_BETWEEN_PAGES,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(NSF_EXPORT_HTML_BETWEEN_PAGES), "")); //$NON-NLS-1$

		store.setDefault(NSF_EXPORT_HTML_IS_OUTPUT_IMAGES_TO_DIR,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(NSF_EXPORT_HTML_IS_OUTPUT_IMAGES_TO_DIR), "")); //$NON-NLS-1$
		store.setDefault(NSF_EXPORT_HTML_IMAGES_DIR_NAME,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(NSF_EXPORT_HTML_IMAGES_DIR_NAME), "")); //$NON-NLS-1$
		store.setDefault(NSF_EXPORT_HTML_IMAGES_URI,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(NSF_EXPORT_HTML_IMAGES_URI), "")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	@Override
	protected String getPageId() {
		return PAGE_ID;
	}

}
