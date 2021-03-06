package rules;

import parser.RuleType;
import semantic.SemanticType;

public class Statement extends SyntaxTreeNode{
	public SyntaxTreeNode identifier;
	public SyntaxTreeNode equalsSign;
	public SyntaxTreeNode expression;
	
	public Statement() {}
	
	public String getType() {
		String assignment_identifier = ((SimpleToken) identifier).token.value;

		if (!this.symbolTableReference.existsSymbol(assignment_identifier, this.scope)) {
			this.errorLog.append_content("Semantic error: Identifier has not been declared: " + assignment_identifier + " in scope: " + this.scope + "\n");
			this.identifier.semanticType = SemanticType.ERROR_TYPE;
		} else {
			this.identifier.semanticType = this.symbolTableReference.getType(assignment_identifier, this.scope);
		}
		
		this.expression.symbolTableReference = this.symbolTableReference;
		this.expression.errorLog = this.errorLog;
		this.expression.scope = this.scope;
		this.expression.semanticType = this.getExpressionType();
		
		if (this.identifier.semanticType == SemanticType.ERROR_TYPE || this.expression.semanticType == SemanticType.ERROR_TYPE) {
			
			this.errorLog.append_content("Semantic error: identifier: " + assignment_identifier + " and expression must have the same data type\n");
			this.semanticType = SemanticType.ERROR_TYPE ;
			
		} else if (this.identifier.semanticType == SemanticType.INTEGER_TYPE && this.expression.semanticType == SemanticType.FLOAT_TYPE) {
			
			this.errorLog.append_content("Semantic error: identifier: " + assignment_identifier + " and expression must have the same data type\n");
			this.semanticType = SemanticType.ERROR_TYPE;
			
		} else if (this.identifier.semanticType == SemanticType.FLOAT_TYPE && this.expression.semanticType == SemanticType.INTEGER_TYPE) {
			
			this.errorLog.append_content("Semantic error: identifier: " + assignment_identifier + " and expression must have the same data type\n");
			this.semanticType = SemanticType.ERROR_TYPE;
			
		} else {
			this.semanticType = SemanticType.VOID_TYPE;
		}
		
		return this.semanticType;
	}
	
	protected String getExpressionType() {
		switch(this.expression.ruleType) {
			case RuleType.EXPRESSION:
				return ((Expression) this.expression).getType();
			case RuleType.EXPRESSION_1:
				return ((Expression_1) this.expression).getType();
			case RuleType.EXPRESSION_2:
				return ((Expression_2) this.expression).getType();
			case RuleType.EXPRESSION_3:
				return ((Expression_3) this.expression).getType();
			case RuleType.EXPRESSION_4:
				return ((Expression_4) this.expression).getType();
			case RuleType.EXPRESSION_5:
				return ((Expression_5) this.expression).getType();
			case RuleType.EXPRESSION_6:
				return ((Expression_6) this.expression).getType();
			case RuleType.EXPRESSION_7:
				return ((Expression_7) this.expression).getType();
			case RuleType.EXPRESSION_8:
				return ((Expression_8) this.expression).getType();
			case RuleType.EXPRESSION_9:
				return ((Expression_9)this.expression).getType();
		}
		
		return null;
	}
}
