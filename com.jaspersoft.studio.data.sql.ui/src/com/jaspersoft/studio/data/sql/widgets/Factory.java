package com.jaspersoft.studio.data.sql.widgets;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.jaspersoft.studio.data.sql.model.query.MExpression;
import com.jaspersoft.studio.data.sql.model.query.operand.AOperand;
import com.jaspersoft.studio.data.sql.model.query.operand.FieldOperand;
import com.jaspersoft.studio.data.sql.model.query.operand.ParameterOperand;
import com.jaspersoft.studio.data.sql.model.query.operand.ScalarOperand;
import com.jaspersoft.studio.data.sql.widgets.scalar.DateWidget;
import com.jaspersoft.studio.data.sql.widgets.scalar.NumberWidget;
import com.jaspersoft.studio.data.sql.widgets.scalar.StringWidget;
import com.jaspersoft.studio.data.sql.widgets.scalar.TimeWidget;
import com.jaspersoft.studio.data.sql.widgets.scalar.TimestampWidget;

public class Factory {
	public static final String OPERANDWIDGET = "operandwidget";
	public static final String OPERAND = "operand";
	public static final String OPERANDS = "OPERANDS";
	public static final String OPERANDS_INDEX = "OPERANDS_INDEX";

	public static Control createWidget(Composite parent, List<AOperand> operands, int index, MExpression mexpr) {
		return createWidget(parent, operands, index, mexpr, false);
	}

	public static Control createWidget(Composite parent, List<AOperand> operands, int index, MExpression mexpr, boolean exludeField) {
		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setLayout(new FillLayout());

		AOperand op = null;
		if (index >= 0 && index < operands.size())
			op = operands.get(index);
		else
			op = new ScalarOperand<String>(mexpr, "Venice");

		AOperandWidget<?> w = createWidget(cmp, op);
		w.setExludeField(exludeField);
		createWidgetMenu(w, operands, index, mexpr);
		return cmp;
	}

	protected static void createWidgetMenu(AOperandWidget<?> w, List<AOperand> operands, int index, MExpression mexpr) {
		for (Control c : w.getChildren()) {
			Menu popupMenu = new Menu(c);
			buildMenu(popupMenu, w, operands, index, mexpr);
			c.setMenu(popupMenu);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T extends AOperand> AOperandWidget<?> createWidget(Composite parent, T operand) {
		AOperandWidget<T> w = null;
		if (operand instanceof FieldOperand)
			return new FieldWidget(parent, (FieldOperand) operand);
		if (operand instanceof ParameterOperand)
			return new ParameterWidget(parent, (ParameterOperand) operand);
		if (operand instanceof ScalarOperand) {
			Object opval = ((ScalarOperand<?>) operand).getValue();
			if (opval instanceof String)
				return new StringWidget(parent, (ScalarOperand<String>) operand);
			if (opval instanceof Number)
				return new NumberWidget(parent, (ScalarOperand<Number>) operand);
			if (opval instanceof Time)
				return new TimeWidget(parent, (ScalarOperand<Time>) operand);
			if (opval instanceof Timestamp)
				return new TimestampWidget(parent, (ScalarOperand<Timestamp>) operand);
			if (opval instanceof Date)
				return new DateWidget(parent, (ScalarOperand<Date>) operand);
		}
		return w;
	}

	public static void buildMenu(Menu pMenu, AOperandWidget<?> w, List<AOperand> operands, int index, MExpression mexpr) {
		Map<String, AOperand> opMap = buildMap(w, mexpr);
		Menu newMenu = null;
		for (String key : opMap.keySet()) {
			MenuItem mi1 = null;
			AOperand aOperand = opMap.get(key);
			if (aOperand instanceof FieldOperand && w.isExludeField())
				continue;
			if (aOperand instanceof ScalarOperand) {
				if (newMenu == null) {
					MenuItem cmi = new MenuItem(pMenu, SWT.CASCADE);
					cmi.setText("Scalar Value");

					newMenu = new Menu(pMenu);
					cmi.setMenu(newMenu);
				}
				mi1 = new MenuItem(newMenu, SWT.CHECK);
			} else
				mi1 = new MenuItem(pMenu, SWT.CHECK);

			mi1.setText(key);
			mi1.setData(OPERAND, aOperand);
			setupMenuItem(mi1, w, operands, index);
			if (w.getValue() == aOperand)
				mi1.setSelection(true);
		}
	}

	private static void setupMenuItem(MenuItem mi1, AOperandWidget<?> w, List<AOperand> operands, int index) {
		mi1.setData(OPERANDWIDGET, w);
		mi1.setData(OPERANDS, operands);
		mi1.setData(OPERANDS_INDEX, index);
		mi1.addSelectionListener(slistener);
	}

	private static SelectionAdapter slistener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			MenuItem mi = (MenuItem) e.getSource();
			AOperandWidget<?> w = (AOperandWidget<?>) mi.getData(OPERANDWIDGET);
			AOperand op = (AOperand) mi.getData(OPERAND);
			List<AOperand> operands = (List<AOperand>) mi.getData(OPERANDS);
			int index = (Integer) mi.getData(OPERANDS_INDEX);
			Composite parent = w.getParent();
			for (Control c : parent.getChildren())
				c.dispose();
			if (index < operands.size())
				operands.set(index, op);
			else
				operands.add(op);

			AOperandWidget<?> neww = createWidget(parent, operands.get(index));
			neww.setOperandMap(w.getOperandMap());
			createWidgetMenu(neww, operands, index, op.getExpression());
			parent.layout(true);
		}
	};

	public static Map<String, AOperand> buildMap(AOperandWidget<?> w, MExpression mexpr) {
		Map<String, AOperand> opMap = w.getOperandMap();
		if (opMap == null) {
			opMap = new LinkedHashMap<String, AOperand>();
			opMap.put("Parameter", getOperand(w, new ParameterOperand(mexpr)));
			opMap.put("Database Field", getOperand(w, new FieldOperand(null, mexpr)));
			opMap.put("String", getOperand(w, getDefaultOperand(mexpr)));
			opMap.put("Number", getOperand(w, new ScalarOperand<BigDecimal>(mexpr, BigDecimal.ZERO)));
			opMap.put("Date", getOperand(w, new ScalarOperand<Date>(mexpr, new Date())));
			opMap.put("Time", getOperand(w, new ScalarOperand<Time>(mexpr, new Time(new Date().getTime()))));
			opMap.put("Timestamp", getOperand(w, new ScalarOperand<Timestamp>(mexpr, new Timestamp(new Date().getTime()))));
			w.setOperandMap(opMap);
		}
		return opMap;
	}

	private static AOperand getOperand(AOperandWidget<?> w, AOperand operand) {
		if (w.getValue().getClass().isInstance(operand)) {
			if (operand instanceof ScalarOperand<?>) {
				ScalarOperand<?> op = (ScalarOperand<?>) operand;
				ScalarOperand<?> wop = (ScalarOperand<?>) w.getValue();
				if (op.getValue().getClass().isAssignableFrom(wop.getValue().getClass()))
					return w.getValue();
			} else
				return w.getValue();
		}
		return operand;
	}

	public static final AOperand getDefaultOperand(MExpression mexpr) {
		return new ScalarOperand<String>(mexpr, "Write your text here");
	}
}
