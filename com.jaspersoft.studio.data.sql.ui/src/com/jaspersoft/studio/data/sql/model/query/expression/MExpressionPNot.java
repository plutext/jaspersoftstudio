/*******************************************************************************
 * Copyright (C) 2010 - 2016. TIBCO Software Inc. 
 * All Rights Reserved. Confidential & Proprietary.
 ******************************************************************************/
package com.jaspersoft.studio.data.sql.model.query.expression;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.jface.viewers.StyledString;

import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.subquery.MQueryTable;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.preferences.fonts.utils.FontUtils;

public class MExpressionPNot extends AMExpression<Object> {

	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MExpressionPNot(ANode parent, JRDesignParameter value, int newIndex) {
		super(parent, value, newIndex);
	}

	@Override
	public JRDesignParameter getValue() {
		return (JRDesignParameter) super.getValue();
	}

	@Override
	public String getDisplayText() {
		String dt = "";
		// if (!isFirst()) {
		// if (getParent() instanceof MFromTableJoin && getParent().getValue()
		// instanceof MQueryTable) {
		// MFromTableJoin mftj = (MFromTableJoin) getParent();
		// dt += ") " + mftj.addAlias() + " ON ";
		// } else
		// dt += prevCond + " ";
		// }

		dt += "$P!{" + getValue().getName() + "}";
		return dt + isLastInGroup(getParent(), this);
	}

	@Override
	public StyledString getStyledDisplayText() {
		String dt = getDisplayText();
		StyledString ss = new StyledString(dt);
		if (!isFirst()) {
			if (getParent() instanceof MFromTableJoin && getParent().getValue() instanceof MQueryTable) {
				int ind = dt.indexOf(" AS ");
				if (ind >= 0)
					ss.setStyle(ind, " AS ".length(), FontUtils.KEYWORDS_STYLER);
				ind = (dt).indexOf(" ON ");
				if (ind >= 0)
					ss.setStyle(ind, " ON ".length(), FontUtils.KEYWORDS_STYLER);
			} else
				ss.setStyle(0, (prevCond + " ").length(), FontUtils.KEYWORDS_STYLER);
		}
		ss.setStyle(dt.lastIndexOf("$P!{"), 4, FontUtils.CLASSTYPE_STYLER);
		ss.setStyle(dt.lastIndexOf("}"), 1, FontUtils.CLASSTYPE_STYLER);
		return ss;
	}

}
