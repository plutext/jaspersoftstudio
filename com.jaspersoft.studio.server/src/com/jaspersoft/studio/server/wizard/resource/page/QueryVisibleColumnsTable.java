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
package com.jaspersoft.studio.server.wizard.resource.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.preferences.editor.table.TableLabelProvider;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;
import com.jaspersoft.studio.utils.Misc;

public class QueryVisibleColumnsTable {
	private ResourceDescriptor rd;

	public QueryVisibleColumnsTable(Composite composite, ResourceDescriptor rd) {
		this.rd = rd;
		createControls(composite);
	}

	private Table table;
	private TableViewer tableViewer;
	private NewButton bnew;
	private DeleteButton bdel;
	private ListOrderButtons border;

	public void dispose() {
		List<String> lst = (List<String>) tableViewer.getInput();
		rd.setQueryVisibleColumns(lst.toArray(new String[lst.size()]));
	}

	private void createControls(Composite composite) {
		buildTable(composite);

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		bnew = new NewButton();
		bnew.createNewButtons(bGroup, tableViewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				return "<new field>";
			}

		});

		bdel = new DeleteButton();
		bdel.createDeleteButton(bGroup, tableViewer);
		border = new ListOrderButtons();
		border.createOrderButtons(bGroup, tableViewer);

		tableViewer.setInput(new ArrayList<String>(Arrays.asList(rd
				.getQueryVisibleColumns())));
	}

	private void buildTable(Composite composite) {
		table = new Table(composite, SWT.BORDER | SWT.SINGLE
				| SWT.FULL_SELECTION);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 200;
		table.setLayoutData(gd);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer = new TableViewer(table);
		attachContentProvider(tableViewer);
		attachLabelProvider(tableViewer);
		attachCellEditors(tableViewer, table);

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(50));
		table.setLayout(tlayout);

		TableColumn[] column = new TableColumn[1];
		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText("Visible Query Columns");

		for (int i = 0, n = column.length; i < n; i++)
			column[i].pack();

	}

	private void attachLabelProvider(TableViewer viewer) {
		viewer.setLabelProvider(new TableLabelProvider());
	}

	private void attachContentProvider(TableViewer viewer) {
		viewer.setContentProvider(new ListContentProvider());
	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if (property.equals("KEY")) //$NON-NLS-1$
					return true;
				return false;
			}

			public Object getValue(Object element, String property) {
				if (property.equals("KEY"))//$NON-NLS-1$
					return Misc.nvl((String) element);
				return null;
			}

			public void modify(Object element, String property, Object value) {
				int index = table.getSelectionIndex();
				List<String> lst = (List<String>) tableViewer.getInput();
				lst.set(index, (String) value);
				//				if (property.equals("KEY")) //$NON-NLS-1$
				// mi.setLabel((String) value);

				tableViewer.update(element, new String[] { property });
				tableViewer.refresh();
			}
		});

		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent) });
		viewer.setColumnProperties(new String[] { "KEY" }); //$NON-NLS-1$  
	}
}
