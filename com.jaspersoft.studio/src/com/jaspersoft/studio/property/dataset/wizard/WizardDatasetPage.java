/*
 * Jaspersoft Open Studio - Eclipse-based JasperReports Designer. Copyright (C) 2005 - 2010 Jaspersoft Corporation. All
 * rights reserved. http://www.jaspersoft.com
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program is part of Jaspersoft Open Studio.
 * 
 * Jaspersoft Open Studio is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Jaspersoft Open Studio is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with Jaspersoft Open Studio. If not,
 * see <http://www.gnu.org/licenses/>.
 */
/*
 * Jaspersoft Open Studio - Eclipse-based JasperReports Designer. Copyright (C) 2005 - 2010 Jaspersoft Corporation. All
 * rights reserved. http://www.jaspersoft.com
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program is part of iReport.
 * 
 * iReport is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * iReport is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with iReport. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.studio.property.dataset.wizard;

import net.sf.jasperreports.engine.design.JRDesignDatasetRun;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.AWizardNode;
import com.jaspersoft.studio.wizards.JSSWizard;
import com.jaspersoft.studio.wizards.JSSWizardSelectionPage;

public class WizardDatasetPage extends JSSWizardSelectionPage {
	private final class DatasetListListener implements Listener {
		public void handleEvent(Event event) {
			if (datasets.getSelectionIndex() == 0 && all)
				datasetrun.setPropertyValue(JRDesignDatasetRun.PROPERTY_DATASET_NAME, null);
			else if (datasets.getSelectionIndex() >= 0 && datasets.getSelectionIndex() < datasets.getItemCount())
				datasetrun.setPropertyValue(JRDesignDatasetRun.PROPERTY_DATASET_NAME,
						datasets.getItem(datasets.getSelectionIndex()));
		}
	}

	private JasperReportsConfiguration jConfig;
	private boolean all = true;
	private MDatasetRun datasetrun;
	private Combo datasets;
	private DatasetListListener dsListener;
	private Button addDataset;
	private Button selDataset;
	private Button noDataset;

	public void setDataSetRun(MDatasetRun datasetrun) {
		this.datasetrun = datasetrun;
		JRDesignDatasetRun ct = (JRDesignDatasetRun) datasetrun.getValue();
		if (ct == null)
			datasetrun.setValue(new JRDesignDatasetRun());
	}

	public MDatasetRun getDataSetRun() {
		// if (noDataset.getSelection())
		// return null;
		// if (addDataset.getSelection())
		// return null;
		return datasetrun;
	}

	private String componentName = "component";

	public WizardDatasetPage(JasperReportsConfiguration jConfig, String componentName) {
		this(jConfig, true, componentName);
	}

	public WizardDatasetPage(JasperReportsConfiguration jConfig, boolean all, String componentName) {
		super("datasetpage"); //$NON-NLS-1$
		setTitle(Messages.common_dataset);
		setImageDescriptor(MDataset.getIconDescriptor().getIcon32());
		setDescription(Messages.WizardDatasetPage_description);
		this.jConfig = jConfig;
		this.all = all;
		this.componentName = componentName;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		setControl(composite);

		addDataset = new Button(composite, SWT.RADIO);
		addDataset.setText("Create a " + componentName + " using a new dataset");
		addDataset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				datasetrun = null;
				handleNewSelected();
			}

		});

		selDataset = new Button(composite, SWT.RADIO);
		selDataset.setText("Create a " + componentName + " using an existing dataset");
		selDataset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelectedNode(null);
				datasets.setEnabled(selDataset.getSelection());
				setPageComplete(datasets.getSelectionIndex() >= 0);
			}

		});

		String[] dsNames = ModelUtils.getDataSets(jConfig.getJasperDesign(), all);

		datasets = new Combo(composite, SWT.BORDER | SWT.SINGLE);
		datasets.setItems(dsNames);
		dsListener = new DatasetListListener();
		datasets.addListener(SWT.Selection, dsListener);
		datasets.select(0);
		String dsname = (String) datasetrun.getPropertyValue(JRDesignDatasetRun.PROPERTY_DATASET_NAME);
		if (dsname != null) {
			String[] items = datasets.getItems();
			for (int i = 0; i < items.length; i++) {
				if (items[i].equals(dsname)) {
					datasets.select(i);
					break;
				}
			}
		}
		dsListener.handleEvent(new Event());
		GridData gd = new GridData();
		gd.horizontalIndent = 30;
		datasets.setLayoutData(gd);

		noDataset = new Button(composite, SWT.RADIO);
		noDataset.setText("Just create the " + componentName + " (further manual configuration may be required)");
		noDataset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelectedNode(null);
				datasetrun = null;
				datasets.setEnabled(selDataset.getSelection());
				setPageComplete(true);
			}
		});

		if (dsNames.length > 0) {
			selDataset.setSelection(true);
			datasets.setEnabled(true);
		} else {
			addDataset.setSelection(true);
			handleNewSelected();
		}
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "Jaspersoft.wizard");//$NON-NLS-1$
	}

	protected void handleNewSelected() {
		datasets.setEnabled(selDataset.getSelection());
		if (addDataset.getSelection()) {
			setSelectedNode(new AWizardNode() {
				public IWizard createWizard() {
					IWizard pwizard = WizardDatasetPage.this.getWizard();
					DatasetWizard w = new DatasetWizard(pwizard, pwizard.getNextPage(WizardDatasetPage.this));
					if (pwizard instanceof JSSWizard)
						w.init(((JSSWizard) pwizard).getConfig());
					return w;
				}
			});
			setPageComplete(true);
		}
	}

}
