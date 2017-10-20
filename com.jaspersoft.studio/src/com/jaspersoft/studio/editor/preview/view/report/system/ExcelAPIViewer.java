/*******************************************************************************
 * Copyright (C) 2010 - 2016. TIBCO Software Inc. 
 * All Rights Reserved. Confidential & Proprietary.
 ******************************************************************************/
package com.jaspersoft.studio.editor.preview.view.report.system;

import net.sf.jasperreports.eclipse.viewer.ReportViewer;
import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.editor.preview.actions.export.AExportAction;
import com.jaspersoft.studio.editor.preview.actions.export.xls.ExportAsExcelAPIAction;
import com.jaspersoft.studio.preferences.exporter.ExcelExporterPreferencePage;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ExcelAPIViewer extends ASystemViewer {

	public ExcelAPIViewer(Composite parent, JasperReportsConfiguration jContext) {
		super(parent, jContext);
	}

	@Override
	protected AExportAction createExporter(ReportViewer rptv) {
		return new ExportAsExcelAPIAction(rptv, jContext, null);
	}

	@Override
	protected String getExtension(JasperPrint jrprint) {
		return ".xls";
	}

	@Override
	public PreferencePage getPreferencePage() {
		return new ExcelExporterPreferencePage();
	}

}
