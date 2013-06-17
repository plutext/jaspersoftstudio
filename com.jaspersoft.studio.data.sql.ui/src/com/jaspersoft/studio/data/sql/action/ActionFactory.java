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
package com.jaspersoft.studio.data.sql.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.action.expression.ChangeOperator;
import com.jaspersoft.studio.data.sql.action.expression.CreateExpression;
import com.jaspersoft.studio.data.sql.action.expression.CreateExpressionGroup;
import com.jaspersoft.studio.data.sql.action.expression.EditExpression;
import com.jaspersoft.studio.data.sql.action.groupby.CreateGroupByColumn;
import com.jaspersoft.studio.data.sql.action.order.CreateOrderByColumn;
import com.jaspersoft.studio.data.sql.action.order.OrderByDesc;
import com.jaspersoft.studio.data.sql.action.select.CreateColumn;
import com.jaspersoft.studio.data.sql.action.select.CreateGroupByFromColumn;
import com.jaspersoft.studio.data.sql.action.select.CreateHavingFromColumn;
import com.jaspersoft.studio.data.sql.action.select.CreateOrderByFromColumn;
import com.jaspersoft.studio.data.sql.action.select.CreateSelectExpression;
import com.jaspersoft.studio.data.sql.action.select.CreateWhereFromColumn;
import com.jaspersoft.studio.data.sql.action.select.DeleteColumn;
import com.jaspersoft.studio.data.sql.action.select.EditColumn;
import com.jaspersoft.studio.data.sql.action.select.SelectDistinct;
import com.jaspersoft.studio.data.sql.action.table.CreateTable;
import com.jaspersoft.studio.data.sql.action.table.DeleteTableJoin;
import com.jaspersoft.studio.data.sql.action.table.EditTable;
import com.jaspersoft.studio.data.sql.action.table.EditTableJoin;
import com.jaspersoft.studio.data.sql.action.table.JoinTable;
import com.jaspersoft.studio.data.sql.model.query.MExpression;
import com.jaspersoft.studio.data.sql.model.query.MExpressionGroup;
import com.jaspersoft.studio.data.sql.model.query.MGroupByColumn;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderByColumn;

public class ActionFactory {
	private List<AAction> actions = new ArrayList<AAction>();

	public ActionFactory(SQLQueryDesigner designer) {

		actions.add(new SelectDistinct(designer));
		actions.add(new CreateWhereFromColumn(designer));
		actions.add(new CreateGroupByFromColumn(designer));
		actions.add(new CreateHavingFromColumn(designer));
		actions.add(new CreateOrderByFromColumn(designer));
		actions.add(null);
		actions.add(new CreateColumn(designer));
		actions.add(new CreateSelectExpression(designer));
		actions.add(null);
		actions.add(new EditColumn(designer));
		actions.add(null);
		actions.add(new DeleteColumn(designer));
		actions.add(null);

		actions.add(new JoinTable(designer));
		actions.add(new CreateTable(designer));
		actions.add(null);
		actions.add(new EditTableJoin(designer));
		actions.add(new EditTable(designer));
		actions.add(null);
		actions.add(new DeleteTableJoin(designer));
		actions.add(new DeleteAction<MFromTable>(designer, "Table", MFromTable.class));
		actions.add(null);

		actions.add(new CreateGroupByColumn(designer));
		actions.add(null);
		actions.add(new DeleteAction<MGroupByColumn>(designer, "Column", MGroupByColumn.class));
		actions.add(null);

		actions.add(new CreateOrderByColumn(designer));
		actions.add(null);
		actions.add(new OrderByDesc(designer));
		actions.add(null);
		actions.add(new DeleteAction<MOrderByColumn>(designer, "Column", MOrderByColumn.class));

		actions.add(null);
		actions.add(new CreateExpressionGroup(designer));
		actions.add(new CreateExpression(designer));
		actions.add(new ChangeOperator(designer));
		actions.add(null);
		actions.add(new EditExpression(designer));
		actions.add(null);
		actions.add(new DeleteAction<MExpression>(designer, "Expression", MExpression.class));
		actions.add(new DeleteAction<MExpressionGroup>(designer, "Expression Group", MExpressionGroup.class));
	}

	@SuppressWarnings("unchecked")
	public <T extends AAction> T getAction(Class<T> aclass) {
		for (AAction act : actions) {
			if (act != null && aclass.isAssignableFrom(act.getClass()))
				return (T) act;
		}
		return null;
	}

	public void fillMenu(Object[] selection, IMenuManager menu) {
		for (AAction act : actions) {
			if (act == null)
				menu.add(new org.eclipse.jface.action.Separator());
			else if (act.calculateEnabled(selection))
				menu.add(act);
		}
	}
}
