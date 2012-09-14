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

package com.jaspersoft.studio.preferences.editor.properties;

import java.io.IOException;
import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.preferences.editor.table.TableFieldEditor;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;
import com.jaspersoft.studio.utils.FileUtils;

public class PropertyListFieldEditor extends TableFieldEditor {

	public static final String NET_SF_JASPERREPORTS_JRPROPERTIES = "net.sf.jasperreports.JRPROPERTIES";

	public PropertyListFieldEditor() {
		super();
	}

	public PropertyListFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, new String[] { "Property", "Value" }, new int[] { 400, 30 }, parent);
	}

	@Override
	protected String createList(String[][] items) {
		return "";
	}

	@Override
	protected String[][] parseString(String string) {
		return new String[0][0];
	}

	@Override
	protected String[] getNewInputObject() {

		return new String[] { "prop", "value" };
	}

	protected void doStore() {
		TableItem[] items = getTable().getItems();
		Properties props = new Properties();
		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			// getPreferenceStore().setValue(item.getText(0), item.getText(1));
			props.setProperty(item.getText(0), item.getText(1));
		}
		getPreferenceStore().setValue(NET_SF_JASPERREPORTS_JRPROPERTIES, FileUtils.getPropertyAsString(props));
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doLoad() {
		if (getTable() != null) {
			List<PropertySuffix> lst = PropertiesHelper.DPROP.getProperties("");
			Collections.sort(lst, new PropertyComparator());
			Properties props = null;
			try {
				props = FileUtils.load(getPreferenceStore().getString(NET_SF_JASPERREPORTS_JRPROPERTIES));
				for (Object key : props.keySet()) {
					String value = props.getProperty((String) key);
					TableItem tableItem = new TableItem(getTable(), SWT.NONE);
					tableItem.setText(new String[] { (String) key, value });
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			if (props != null)
				for (PropertySuffix ps : lst) {
					if (props.getProperty(ps.getKey()) == null) {
						TableItem tableItem = new TableItem(getTable(), SWT.NONE);
						tableItem.setText(new String[] { ps.getKey(), ps.getValue() });
					}
				}

			TableItem[] items = table.getItems();
			Collator collator = Collator.getInstance(Locale.getDefault());
			for (int i = 1; i < items.length; i++) {
				String value1 = items[i].getText(0);
				for (int j = 0; j < i; j++) {
					String value2 = items[j].getText(0);
					if (collator.compare(value1, value2) < 0) {
						String[] values = { items[i].getText(0), items[i].getText(1) };
						items[i].dispose();
						TableItem item = new TableItem(table, SWT.NONE, j);
						item.setText(values);
						items = table.getItems();
						break;
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doLoadDefault() {
		if (getTable() != null) {
			getTable().removeAll();

			List<PropertySuffix> lst = PropertiesHelper.DPROP.getProperties("");
			Collections.sort(lst, new PropertyComparator());
			for (PropertySuffix ps : lst) {

				TableItem tableItem = new TableItem(getTable(), SWT.NONE);
				tableItem.setText(new String[] { ps.getKey(), ps.getValue() });
			}
		}
	}

	@Override
	protected boolean isFieldEditable(int col, int row) {
		if (col == 0) {
			TableItem ti = table.getItem(row);
			return PropertiesHelper.DPROP.getProperty(ti.getText(0)) == null;
		}
		return super.isFieldEditable(col, row);
	}
}
