/*
* generated by Xtext
*/
parser grammar InternalSqlParser;

options {
	tokenVocab=InternalSqlLexer;
	superClass=AbstractInternalAntlrParser;
	
}

@header {
package com.jaspersoft.studio.data.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import com.jaspersoft.studio.data.services.SqlGrammarAccess;

}

@members {


	private SqlGrammarAccess grammarAccess;
	 	
	public InternalSqlParser(TokenStream input, SqlGrammarAccess grammarAccess) {
		this(input);
		this.grammarAccess = grammarAccess;
		registerRules(grammarAccess.getGrammar());
	}
	
	@Override
	protected String getFirstRuleName() {
		return "Model";	
	} 
	   	   	
	@Override
	protected SqlGrammarAccess getGrammarAccess() {
		return grammarAccess;
	}
}

@rulecatch { 
	catch (RecognitionException re) { 
	    recover(input,re); 
	    appendSkippedTokens();
	}
}




// Entry rule entryRuleModel
entryRuleModel returns [EObject current=null]
	:
	{ newCompositeNode(grammarAccess.getModelRule()); }
	 iv_ruleModel=ruleModel 
	 { $current=$iv_ruleModel.current; } 
	 EOF 
;

// Rule Model
ruleModel returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
	otherlv_0=KEYWORD_1
    {
    	newLeafNode(otherlv_0, grammarAccess.getModelAccess().getSELECTKeyword_0());
    }
(
(
		lv_colName_1_0=RULE_ID
		{
			newLeafNode(lv_colName_1_0, grammarAccess.getModelAccess().getColNameIDTerminalRuleCall_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getModelRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"colName",
        		lv_colName_1_0, 
        		"ID");
	    }

)
))
;





