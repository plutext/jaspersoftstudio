/*
* generated by Xtext
*/
package com.jaspersoft.studio.data.ui.contentassist.antlr;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.RecognitionException;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.AbstractContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr.FollowElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;

import com.google.inject.Inject;

import com.jaspersoft.studio.data.services.SqlGrammarAccess;

public class SqlParser extends AbstractContentAssistParser {
	
	@Inject
	private SqlGrammarAccess grammarAccess;
	
	private Map<AbstractElement, String> nameMappings;
	
	@Override
	protected com.jaspersoft.studio.data.ui.contentassist.antlr.internal.InternalSqlParser createParser() {
		com.jaspersoft.studio.data.ui.contentassist.antlr.internal.InternalSqlParser result = new com.jaspersoft.studio.data.ui.contentassist.antlr.internal.InternalSqlParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}
	
	@Override
	protected String getRuleName(AbstractElement element) {
		if (nameMappings == null) {
			nameMappings = new HashMap<AbstractElement, String>() {
				private static final long serialVersionUID = 1L;
				{
					put(grammarAccess.getColumnOrAliasAccess().getAlternatives(), "rule__ColumnOrAlias__Alternatives");
					put(grammarAccess.getOrderByColumnFullAccess().getAlternatives_1(), "rule__OrderByColumnFull__Alternatives_1");
					put(grammarAccess.getFullExpressionAccess().getAlternatives_1_1_0(), "rule__FullExpression__Alternatives_1_1_0");
					put(grammarAccess.getExpressionFragmentAccess().getAlternatives(), "rule__ExpressionFragment__Alternatives");
					put(grammarAccess.getExpressionAccess().getAlternatives_1(), "rule__Expression__Alternatives_1");
					put(grammarAccess.getExpressionAccess().getAlternatives_1_0(), "rule__Expression__Alternatives_1_0");
					put(grammarAccess.getComparisonAccess().getAlternatives_0(), "rule__Comparison__Alternatives_0");
					put(grammarAccess.getLikeAccess().getAlternatives_0(), "rule__Like__Alternatives_0");
					put(grammarAccess.getInOperatorAccess().getAlternatives_1(), "rule__InOperator__Alternatives_1");
					put(grammarAccess.getInOperatorAccess().getAlternatives_2(), "rule__InOperator__Alternatives_2");
					put(grammarAccess.getOperandAccess().getAlternatives_1_1_0(), "rule__Operand__Alternatives_1_1_0");
					put(grammarAccess.getOperandFragmentAccess().getAlternatives(), "rule__OperandFragment__Alternatives");
					put(grammarAccess.getXOperandFragmentAccess().getAlternatives(), "rule__XOperandFragment__Alternatives");
					put(grammarAccess.getScalarOperandAccess().getAlternatives(), "rule__ScalarOperand__Alternatives");
					put(grammarAccess.getJoinTypeAccess().getAlternatives(), "rule__JoinType__Alternatives");
					put(grammarAccess.getModelAccess().getGroup(), "rule__Model__Group__0");
					put(grammarAccess.getModelAccess().getGroup_1(), "rule__Model__Group_1__0");
					put(grammarAccess.getModelAccess().getGroup_1_1(), "rule__Model__Group_1_1__0");
					put(grammarAccess.getModelAccess().getGroup_2(), "rule__Model__Group_2__0");
					put(grammarAccess.getSelectAccess().getGroup(), "rule__Select__Group__0");
					put(grammarAccess.getSelectAccess().getGroup_5(), "rule__Select__Group_5__0");
					put(grammarAccess.getSelectAccess().getGroup_6(), "rule__Select__Group_6__0");
					put(grammarAccess.getSelectAccess().getGroup_7(), "rule__Select__Group_7__0");
					put(grammarAccess.getColumnsAccess().getGroup(), "rule__Columns__Group__0");
					put(grammarAccess.getColumnsAccess().getGroup_1(), "rule__Columns__Group_1__0");
					put(grammarAccess.getColumnsAccess().getGroup_1_1(), "rule__Columns__Group_1_1__0");
					put(grammarAccess.getColumnOrAliasAccess().getGroup_0(), "rule__ColumnOrAlias__Group_0__0");
					put(grammarAccess.getColumnFullAccess().getGroup(), "rule__ColumnFull__Group__0");
					put(grammarAccess.getColumnFullAccess().getGroup_1(), "rule__ColumnFull__Group_1__0");
					put(grammarAccess.getColumnFullAccess().getGroup_1_1(), "rule__ColumnFull__Group_1_1__0");
					put(grammarAccess.getTablesAccess().getGroup(), "rule__Tables__Group__0");
					put(grammarAccess.getTablesAccess().getGroup_1(), "rule__Tables__Group_1__0");
					put(grammarAccess.getTablesAccess().getGroup_1_1(), "rule__Tables__Group_1_1__0");
					put(grammarAccess.getFromTableAccess().getGroup(), "rule__FromTable__Group__0");
					put(grammarAccess.getFromTableAccess().getGroup_1(), "rule__FromTable__Group_1__0");
					put(grammarAccess.getTableOrAliasAccess().getGroup(), "rule__TableOrAlias__Group__0");
					put(grammarAccess.getTableFullAccess().getGroup(), "rule__TableFull__Group__0");
					put(grammarAccess.getTableFullAccess().getGroup_1(), "rule__TableFull__Group_1__0");
					put(grammarAccess.getTableFullAccess().getGroup_1_1(), "rule__TableFull__Group_1_1__0");
					put(grammarAccess.getOrderByColumnsAccess().getGroup(), "rule__OrderByColumns__Group__0");
					put(grammarAccess.getOrderByColumnsAccess().getGroup_1(), "rule__OrderByColumns__Group_1__0");
					put(grammarAccess.getOrderByColumnsAccess().getGroup_1_1(), "rule__OrderByColumns__Group_1_1__0");
					put(grammarAccess.getOrderByColumnFullAccess().getGroup(), "rule__OrderByColumnFull__Group__0");
					put(grammarAccess.getGroupByColumnsAccess().getGroup(), "rule__GroupByColumns__Group__0");
					put(grammarAccess.getGroupByColumnsAccess().getGroup_1(), "rule__GroupByColumns__Group_1__0");
					put(grammarAccess.getGroupByColumnsAccess().getGroup_1_1(), "rule__GroupByColumns__Group_1_1__0");
					put(grammarAccess.getFullExpressionAccess().getGroup(), "rule__FullExpression__Group__0");
					put(grammarAccess.getFullExpressionAccess().getGroup_1(), "rule__FullExpression__Group_1__0");
					put(grammarAccess.getFullExpressionAccess().getGroup_1_1(), "rule__FullExpression__Group_1_1__0");
					put(grammarAccess.getExpressionGroupAccess().getGroup(), "rule__ExpressionGroup__Group__0");
					put(grammarAccess.getXExpressionAccess().getGroup(), "rule__XExpression__Group__0");
					put(grammarAccess.getXExpressionAccess().getGroup_3(), "rule__XExpression__Group_3__0");
					put(grammarAccess.getExpressionAccess().getGroup(), "rule__Expression__Group__0");
					put(grammarAccess.getComparisonAccess().getGroup(), "rule__Comparison__Group__0");
					put(grammarAccess.getLikeAccess().getGroup(), "rule__Like__Group__0");
					put(grammarAccess.getBetweenAccess().getGroup(), "rule__Between__Group__0");
					put(grammarAccess.getInOperatorAccess().getGroup(), "rule__InOperator__Group__0");
					put(grammarAccess.getInOperatorAccess().getGroup_2_1(), "rule__InOperator__Group_2_1__0");
					put(grammarAccess.getInOperatorAccess().getGroup_2_1_1(), "rule__InOperator__Group_2_1_1__0");
					put(grammarAccess.getOperandAccess().getGroup(), "rule__Operand__Group__0");
					put(grammarAccess.getOperandAccess().getGroup_1(), "rule__Operand__Group_1__0");
					put(grammarAccess.getOperandAccess().getGroup_1_1(), "rule__Operand__Group_1_1__0");
					put(grammarAccess.getOperandGroupAccess().getGroup(), "rule__OperandGroup__Group__0");
					put(grammarAccess.getParameterOperandAccess().getGroup(), "rule__ParameterOperand__Group__0");
					put(grammarAccess.getExclamationParameterOperandAccess().getGroup(), "rule__ExclamationParameterOperand__Group__0");
					put(grammarAccess.getSubQueryOperandAccess().getGroup(), "rule__SubQueryOperand__Group__0");
					put(grammarAccess.getModelAccess().getEntriesAssignment_1_1_1(), "rule__Model__EntriesAssignment_1_1_1");
					put(grammarAccess.getModelAccess().getOrderByEntryAssignment_2_1(), "rule__Model__OrderByEntryAssignment_2_1");
					put(grammarAccess.getSelectAccess().getSelectAssignment_0(), "rule__Select__SelectAssignment_0");
					put(grammarAccess.getSelectAccess().getColsAssignment_2(), "rule__Select__ColsAssignment_2");
					put(grammarAccess.getSelectAccess().getTblAssignment_4(), "rule__Select__TblAssignment_4");
					put(grammarAccess.getSelectAccess().getWhereExpressionAssignment_5_1(), "rule__Select__WhereExpressionAssignment_5_1");
					put(grammarAccess.getSelectAccess().getGroupByEntryAssignment_6_1(), "rule__Select__GroupByEntryAssignment_6_1");
					put(grammarAccess.getSelectAccess().getHavingEntryAssignment_7_1(), "rule__Select__HavingEntryAssignment_7_1");
					put(grammarAccess.getColumnsAccess().getEntriesAssignment_1_1_1(), "rule__Columns__EntriesAssignment_1_1_1");
					put(grammarAccess.getColumnOrAliasAccess().getColAliasAssignment_0_2(), "rule__ColumnOrAlias__ColAliasAssignment_0_2");
					put(grammarAccess.getColumnOrAliasAccess().getAllColsAssignment_1(), "rule__ColumnOrAlias__AllColsAssignment_1");
					put(grammarAccess.getColumnFullAccess().getEntriesAssignment_1_1_1(), "rule__ColumnFull__EntriesAssignment_1_1_1");
					put(grammarAccess.getTablesAccess().getEntriesAssignment_1_1_1(), "rule__Tables__EntriesAssignment_1_1_1");
					put(grammarAccess.getFromTableAccess().getTableAssignment_0(), "rule__FromTable__TableAssignment_0");
					put(grammarAccess.getFromTableAccess().getJoinAssignment_1_0(), "rule__FromTable__JoinAssignment_1_0");
					put(grammarAccess.getFromTableAccess().getOnTableAssignment_1_1(), "rule__FromTable__OnTableAssignment_1_1");
					put(grammarAccess.getFromTableAccess().getJoinExprAssignment_1_3(), "rule__FromTable__JoinExprAssignment_1_3");
					put(grammarAccess.getTableOrAliasAccess().getTblAliasAssignment_2(), "rule__TableOrAlias__TblAliasAssignment_2");
					put(grammarAccess.getTableFullAccess().getEntriesAssignment_1_1_1(), "rule__TableFull__EntriesAssignment_1_1_1");
					put(grammarAccess.getDbObjectNameAccess().getDbnameAssignment(), "rule__DbObjectName__DbnameAssignment");
					put(grammarAccess.getOrderByColumnsAccess().getEntriesAssignment_1_1_1(), "rule__OrderByColumns__EntriesAssignment_1_1_1");
					put(grammarAccess.getOrderByColumnFullAccess().getColOrderAssignment_0(), "rule__OrderByColumnFull__ColOrderAssignment_0");
					put(grammarAccess.getGroupByColumnsAccess().getEntriesAssignment_1_1_1(), "rule__GroupByColumns__EntriesAssignment_1_1_1");
					put(grammarAccess.getFullExpressionAccess().getEntriesAssignment_1_1_1(), "rule__FullExpression__EntriesAssignment_1_1_1");
					put(grammarAccess.getExpressionGroupAccess().getExprAssignment_2(), "rule__ExpressionGroup__ExprAssignment_2");
					put(grammarAccess.getExpressionAccess().getOp1Assignment_0(), "rule__Expression__Op1Assignment_0");
					put(grammarAccess.getExpressionAccess().getInAssignment_1_1(), "rule__Expression__InAssignment_1_1");
					put(grammarAccess.getExpressionAccess().getBetweenAssignment_1_2(), "rule__Expression__BetweenAssignment_1_2");
					put(grammarAccess.getExpressionAccess().getLikeAssignment_1_3(), "rule__Expression__LikeAssignment_1_3");
					put(grammarAccess.getExpressionAccess().getCompAssignment_1_4(), "rule__Expression__CompAssignment_1_4");
					put(grammarAccess.getComparisonAccess().getOp2Assignment_1(), "rule__Comparison__Op2Assignment_1");
					put(grammarAccess.getBetweenAccess().getOp1Assignment_1(), "rule__Between__Op1Assignment_1");
					put(grammarAccess.getBetweenAccess().getOp2Assignment_3(), "rule__Between__Op2Assignment_3");
					put(grammarAccess.getInOperatorAccess().getSubqueryAssignment_2_0(), "rule__InOperator__SubqueryAssignment_2_0");
					put(grammarAccess.getInOperatorAccess().getEntriesAssignment_2_1_1_1(), "rule__InOperator__EntriesAssignment_2_1_1_1");
					put(grammarAccess.getOperandAccess().getEntriesAssignment_1_1_1(), "rule__Operand__EntriesAssignment_1_1_1");
					put(grammarAccess.getOperandGroupAccess().getOpAssignment_2(), "rule__OperandGroup__OpAssignment_2");
					put(grammarAccess.getXOperandFragmentAccess().getScalarAssignment_2(), "rule__XOperandFragment__ScalarAssignment_2");
					put(grammarAccess.getSubQueryOperandAccess().getSelAssignment_2(), "rule__SubQueryOperand__SelAssignment_2");
				}
			};
		}
		return nameMappings.get(element);
	}
	
	@Override
	protected Collection<FollowElement> getFollowElements(AbstractInternalContentAssistParser parser) {
		try {
			com.jaspersoft.studio.data.ui.contentassist.antlr.internal.InternalSqlParser typedParser = (com.jaspersoft.studio.data.ui.contentassist.antlr.internal.InternalSqlParser) parser;
			typedParser.entryRuleModel();
			return typedParser.getFollowElements();
		} catch(RecognitionException ex) {
			throw new RuntimeException(ex);
		}		
	}
	
	@Override
	protected String[] getInitialHiddenTokens() {
		return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
	}
	
	public SqlGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(SqlGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
