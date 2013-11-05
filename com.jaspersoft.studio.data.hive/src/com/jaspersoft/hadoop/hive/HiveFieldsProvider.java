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
package com.jaspersoft.hadoop.hive;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignField;

import com.jaspersoft.hadoop.hive.connection.HiveConnection;
import com.jaspersoft.hadoop.hive.query.HiveParameter;
import com.jaspersoft.hadoop.hive.query.HiveQueryExecuter;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class HiveFieldsProvider {

	private static HiveFieldsProvider instance;

	private static final Lock lock = new ReentrantLock();

	private HiveFieldsProvider() {
	}

	public static HiveFieldsProvider getInstance() {
		lock.lock();
		try {
			if (instance == null) {
				instance = new HiveFieldsProvider();
			}
			return instance;
		} finally {
			lock.unlock();
		}
	}

	public JRField[] getFields(JasperReportsContext jasperReportsContext, HiveConnection connection, JRDataset reportDataset, Map<String, Object> parameters) throws JRException,
			UnsupportedOperationException {
		HiveQueryExecuter queryExecuter = null;
		HiveDataSource datasource = null;
		try {
			Map<String, JRValueParameter> newValueParameters = new HashMap<String, JRValueParameter>();
			for (String parameterName : parameters.keySet()) {
				Object parameterValue = parameters.get(parameterName);
				if (parameterValue == null && parameterName.equals(JRParameter.REPORT_PARAMETERS_MAP)) {
					parameterValue = new HashMap<String, Object>();
				}
				HiveParameter newParameter = new HiveParameter(parameterName, parameterValue);
				newValueParameters.put(parameterName, newParameter);
			}
			parameters.put(JRParameter.REPORT_CONNECTION, connection);
			if (!newValueParameters.containsKey(JRParameter.REPORT_PARAMETERS_MAP)) {
				newValueParameters.put(JRParameter.REPORT_PARAMETERS_MAP, new HiveParameter(JRParameter.REPORT_PARAMETERS_MAP, parameters));
			}
			newValueParameters.put(JRParameter.REPORT_CONNECTION, new HiveParameter(JRParameter.REPORT_CONNECTION, connection));
			if (!newValueParameters.containsKey(JRParameter.REPORT_MAX_COUNT)) {
				newValueParameters.put(JRParameter.REPORT_MAX_COUNT, new HiveParameter(JRParameter.REPORT_MAX_COUNT, null));
			}
			queryExecuter = new HiveQueryExecuter(jasperReportsContext, reportDataset, newValueParameters);
			datasource = (HiveDataSource) queryExecuter.createDatasource();
			ResultSetMetaData resultSetMetaData = datasource.getResultSet().getMetaData();
			List<JRDesignField> columns = new ArrayList<JRDesignField>();
			for (int index = 1; index <= resultSetMetaData.getColumnCount(); ++index) {
				JRDesignField field = new JRDesignField();
				field.setName(resultSetMetaData.getColumnLabel(index));
				field.setValueClassName(HiveConnection.getJdbcTypeClass(resultSetMetaData, index));
				field.setDescription(null);
				columns.add(field);
			}

			JRField[] finalFields = new JRField[columns.size()];
			for (int index = 0; index < finalFields.length; ++index) {
				finalFields[index] = (JRField) columns.get(index);
			}
			return finalFields;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JRException(e.getMessage());
		} finally {
			if (queryExecuter != null) {
				queryExecuter.close();
			}
		}
	}
}
