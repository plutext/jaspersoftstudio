/*******************************************************************************
 * Copyright (C) 2010 - 2016. TIBCO Software Inc. 
 * All Rights Reserved. Confidential & Proprietary.
 ******************************************************************************/
package com.jaspersoft.studio.data.cassandra;

import net.sf.jasperreports.engine.JasperReportsContext;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.data.ADataAdapterComposite;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterEditor;

/**
 * 
 * @author gtoffoli
 * 
 */
public class CassandraDataAdapterEditor implements DataAdapterEditor {

    protected CassandraDataAdapterComposite composite = null;

	public ADataAdapterComposite getComposite(Composite parent, int style, WizardPage wizardPage, JasperReportsContext jrContext) {
        if (composite == null)
			composite = new CassandraDataAdapterComposite(parent, style, jrContext);
        return composite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jaspersoft.studio.data.DataAdapterEditor#getDataAdapter()
     */
    public DataAdapterDescriptor getDataAdapter() {
        return composite.getDataAdapter();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jaspersoft.studio.data.DataAdapterEditor#getHelpContextId()
     */
    public String getHelpContextId() {
        return composite.getHelpContextId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.jaspersoft.studio.data.DataAdapterEditor#setDataAdapter(com.jaspersoft
     * .studio.data.DataAdapter)
     */
    public void setDataAdapter(DataAdapterDescriptor dataAdapter) {
        if (dataAdapter instanceof CassandraDataAdapterDescriptor)
            composite.setDataAdapter((CassandraDataAdapterDescriptor) dataAdapter);
    }
}
