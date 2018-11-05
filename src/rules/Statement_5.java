package rules;

import parser.RuleType;
import semantic.SemanticType;

public class Statement_5 extends SyntaxTreeNode{
	//Return value Node
	public SyntaxTreeNode expressionToReturn;
	
	public Statement_5() {}
	
	public String getType() {
		this.expressionToReturn.symbolTableReference = this.symbolTableReference;
		this.expressionToReturn.errorLog = this.errorLog;
		this.expressionToReturn.scope = this.scope;
		
		this.expressionToReturn.semanticType = this.getExpressionType();
		String scopeType = this.symbolTableReference.getScopeType(this.scope);
		
		if (this.expressionToReturn.semanticType.equals(scopeType)) {
			this.semanticType = this.expressionToReturn.semanticType;
		} else {
			this.semanticType = SemanticType.ERROR_TYPE;
			String errors = this.errorLog.getText();
			this.errorLog.setText(errors + " Semantic error: return type does not match with type of function previously declared: " 
			                             + " in scope: " + (this.scope.equals("") ? "Global" : this.scope) + "\n");
		}
		
		
		return this.semanticType;
	}
	
	public String getExpressionType() {
		switch(this.expressionToReturn.ruleType) {
			case RuleType.EXPRESSION:
				return ((Expression)this.expressionToReturn).getType();
			case RuleType.EXPRESSION_1:
				return ((Expression_1) this.expressionToReturn).getType();
			case RuleType.EXPRESSION_2:
				return ((Expression_2) this.expressionToReturn).getType();
			case RuleType.EXPRESSION_3:
				return ((Expression_3) this.expressionToReturn).getType();
			case RuleType.EXPRESSION_4:
				return ((Expression_4)this.expressionToReturn).getType();
			case RuleType.EXPRESSION_5:
				return ((Expression_5) this.expressionToReturn).getType();
			case RuleType.EXPRESSION_6:
				return ((Expression_6) this.expressionToReturn).getType();
			case RuleType.EXPRESSION_7:
				return ((Expression_7) this.expressionToReturn).getType();
			case RuleType.EXPRESSION_8:
				return ((Expression_8) this.expressionToReturn).getType();
			case RuleType.EXPRESSION_9:
				return ((Expression_9) this.expressionToReturn).getType();
		}
		return null;
	}
}
