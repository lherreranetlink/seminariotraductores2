package rules;

import parser.RuleType;
import semantic.SemanticType;

public class Expression_8 extends SyntaxTreeNode {
	public SyntaxTreeNode expressionLeft;
	public SyntaxTreeNode orOperator;
	public SyntaxTreeNode expressionRight;
	
	public Expression_8() {}
	
	public String getType() {
		this.expressionLeft.symbolTableReference = this.symbolTableReference;
		this.expressionLeft.errorLog = this.errorLog;
		this.expressionLeft.scope = this.scope;
		this.expressionRight.symbolTableReference = this.symbolTableReference;
		this.expressionRight.errorLog = this.errorLog;
		this.expressionRight.scope = this.scope;
		
		this.expressionLeft.semanticType = this.getExpressionType(this.expressionLeft);
		this.expressionRight.semanticType = this.getExpressionType(this.expressionRight);
		System.out.println("Expression left: " + this.expressionLeft.semanticType);
		System.out.println("Expression right: " + this.expressionRight.semanticType);
		
		if (this.expressionLeft.semanticType == SemanticType.ERROR_TYPE || this.expressionRight.semanticType == SemanticType.ERROR_TYPE) {
			this.semanticType = SemanticType.ERROR_TYPE;
		} else if (this.expressionLeft.semanticType == SemanticType.INTEGER_TYPE && this.expressionRight.semanticType == SemanticType.FLOAT_TYPE) {
			this.semanticType = SemanticType.ERROR_TYPE;
		} else if (this.expressionLeft.semanticType == SemanticType.FLOAT_TYPE && this.expressionRight.semanticType == SemanticType.INTEGER_TYPE){
			this.semanticType = SemanticType.ERROR_TYPE;
		} else if (this.expressionLeft.semanticType == SemanticType.INTEGER_TYPE && this.expressionRight.semanticType == SemanticType.INTEGER_TYPE){
			this.semanticType = SemanticType.INTEGER_TYPE;
		} else if (this.expressionLeft.semanticType == SemanticType.FLOAT_TYPE && this.expressionRight.semanticType == SemanticType.FLOAT_TYPE) {
			this.semanticType = SemanticType.FLOAT_TYPE;
		}
		
		return this.semanticType;
	}
	
	private String getExpressionType(SyntaxTreeNode expression) {
		switch (expression.ruleType) {
		case RuleType.EXPRESSION:
			return ((Expression)expression).getType();
		case RuleType.EXPRESSION_1:
			return ((Expression_1) expression).getType();
		case RuleType.EXPRESSION_2:
			return ((Expression_2) expression).getType();
		case RuleType.EXPRESSION_3:
			return ((Expression_3) expression).getType();
		case RuleType.EXPRESSION_4:
			return ((Expression_4)expression).getType();
		case RuleType.EXPRESSION_5:
			return ((Expression_5) expression).getType();
		case RuleType.EXPRESSION_6:
			return ((Expression_6) expression).getType();
		case RuleType.EXPRESSION_7:
			return ((Expression_7) expression).getType();
		case RuleType.EXPRESSION_8:
			return ((Expression_8) expression).getType();
		case RuleType.EXPRESSION_9:
			return ((Expression_9)expression).getType();
		}
		return null;
	}
}
