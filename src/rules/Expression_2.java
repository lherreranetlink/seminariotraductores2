package rules;

import parser.RuleType;

public class Expression_2 extends SyntaxTreeNode {
	public SyntaxTreeNode notOperator;
	public SyntaxTreeNode expression;
	
	public Expression_2() {}
	
	public String getType() {
		this.expression.symbolTableReference = this.symbolTableReference;
		this.expression.errorLog  = this.errorLog;
		this.expression.scope = this.scope;
		this.expression.semanticType = this.getExpressionType();
		this.semanticType = this.expression.semanticType;
		
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
				return ((Expression_5) expression).getType();
			case RuleType.EXPRESSION_6:
				return ((Expression_6) expression).getType();
			case RuleType.EXPRESSION_7:
				return ((Expression_7) this.expression).getType();
			case RuleType.EXPRESSION_8:
				return ((Expression_8) expression).getType();
			case RuleType.EXPRESSION_9:
				return ((Expression_9)expression).getType();
		}
		return null;
	}
}
