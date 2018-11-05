package rules;

import parser.RuleType;
import semantic.SemanticType;

public class ArgList extends SyntaxTreeNode{
	public SyntaxTreeNode expression;
	public SyntaxTreeNode argList;
	
	public ArgList() {}
	
	public String getType() {
		this.setInheritedAttributes();
		
		this.expression.semanticType = this.getExpressionType();
		
		this.paramsPattern = "";
		if (this.argList.ruleType != RuleType.EPSILON_RULE) {
			this.argList.semanticType = ((ArgList) this.argList).getType();
			this.argList.paramsPattern += this.argList.paramsPattern;
		} else {
			this.argList.semanticType = SemanticType.VOID_TYPE;
		}
		
		return this.semanticType;
	}
	
	public String getExpressionType() {
		switch(this.expression.ruleType) {
			case RuleType.EXPRESSION:
				return ((Expression)this.expression).getType();
			case RuleType.EXPRESSION_1:
				return ((Expression_1) this.expression).getType();
			case RuleType.EXPRESSION_2:
				return ((Expression_2) this.expression).getType();
			case RuleType.EXPRESSION_3:
				return ((Expression_3) this.expression).getType();
			case RuleType.EXPRESSION_4:
				return ((Expression_4)this.expression).getType();
			case RuleType.EXPRESSION_5:
				return ((Expression_5) this.expression).getType();
			case RuleType.EXPRESSION_6:
				return ((Expression_6) this.expression).getType();
			case RuleType.EXPRESSION_7:
				return ((Expression_7) this.expression).getType();
			case RuleType.EXPRESSION_8:
				return ((Expression_8) this.expression).getType();
			case RuleType.EXPRESSION_9:
				return ((Expression_9) this.expression).getType();
		}
		return null;
	}
	
	private void setInheritedAttributes() {
		this.expression.symbolTableReference = this.symbolTableReference;
		this.expression.errorLog = this.errorLog;
		this.expression.scope = this.scope;
		
		if (this.argList.ruleType != RuleType.EPSILON_RULE) {
			this.argList.symbolTableReference = this.symbolTableReference;
			this.argList.errorLog = this.errorLog;
			this.argList.scope = this.scope;
		}
	}
}
