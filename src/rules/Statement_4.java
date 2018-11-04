package rules;

import parser.RuleType;
import semantic.SemanticType;

public class Statement_4 extends SyntaxTreeNode{
	//For(x;x;x) Node
	public SyntaxTreeNode identifier;
	public SyntaxTreeNode initializationExpression;
	public SyntaxTreeNode conditionExpression;
	public SyntaxTreeNode iterationExpression;
	public SyntaxTreeNode blockStatement;
	
	public Statement_4() {}
	
	public String getType() {
		this.setInheritedAttributes();
		
		String iteration_identifier = ((SimpleToken) identifier).token.value;
		
		if (!this.symbolTableReference.existsSymbol(iteration_identifier, this.scope)) {
			String errors = this.errorLog.getText();
			this.errorLog.setText(errors + " Semantic error: Identifier has not been declared: " + iteration_identifier + " in scope: " + this.scope + "\n");
			this.identifier.semanticType = SemanticType.ERROR_TYPE;
		} else {
			this.identifier.semanticType = this.symbolTableReference.getType(iteration_identifier, this.scope);
		}
		
		this.initializationExpression.semanticType = this.getExpressionType(this.initializationExpression);
		this.conditionExpression.semanticType = this.getExpressionType(this.conditionExpression);
		this.iterationExpression.semanticType = this.getExpressionType(this.iterationExpression);
		this.blockStatement.semanticType = this.getBlockStatementType();
		
		if (!this.identifier.semanticType.equals(this.initializationExpression.semanticType) 
		     || !this.identifier.semanticType.equals(this.conditionExpression.semanticType) 
		     || !this.identifier.semanticType.equals(this.iterationExpression.semanticType)) {
			this.semanticType = SemanticType.ERROR_TYPE;
		} else {
			this.semanticType = (!this.identifier.semanticType.equals(SemanticType.ERROR_TYPE) 
								  || !this.initializationExpression.semanticType.equals(SemanticType.ERROR_TYPE)
								  || !this.conditionExpression.semanticType.equals(SemanticType.ERROR_TYPE)
								  || !this.iterationExpression.semanticType.equals(SemanticType.ERROR_TYPE)
								  || !this.blockStatement.semanticType.equals(SemanticType.ERROR_TYPE))
					          ? SemanticType.VOID_TYPE
					          : SemanticType.ERROR_TYPE;
		}
		
		return this.semanticType;
	}
	
	private String getExpressionType(SyntaxTreeNode expression) {
		switch (expression.ruleType) {
			case RuleType.EXPRESSION:
				return ((Expression) expression).getType();
			case RuleType.EXPRESSION_1:
				return ((Expression_1) expression).getType();
			case RuleType.EXPRESSION_2:
				return ((Expression_2) expression).getType();
			case RuleType.EXPRESSION_3:
				return ((Expression_3) expression).getType();
			case RuleType.EXPRESSION_4:
				return ((Expression_4) expression).getType();
			case RuleType.EXPRESSION_5:
				return ((Expression_5) expression).getType();
			case RuleType.EXPRESSION_6:
				return ((Expression_6) expression).getType();
			case RuleType.EXPRESSION_7:
				return ((Expression_7) expression).getType();
			case RuleType.EXPRESSION_8:
				return ((Expression_8) expression).getType();
			case RuleType.EXPRESSION_9:
				return ((Expression_9) expression).getType();
		}
		return null;
	}
	
	private String getBlockStatementType() {
		switch(this.blockStatement.ruleType) {
			case RuleType.BLOCK_STM:
				return ((BlockStm) this.blockStatement).getType();
			case RuleType.BLOCK_STM_1:
				return ((BlockStm_1) this.blockStatement).getType();
		}
		return null;
	}
	
	private void setInheritedAttributes() {
		this.identifier.symbolTableReference = this.symbolTableReference;
		this.identifier.errorLog = this.errorLog;
		this.identifier.scope = this.scope;
		this.initializationExpression.symbolTableReference = this.symbolTableReference;
		this.initializationExpression.errorLog = this.errorLog;
		this.initializationExpression.scope = this.scope;
		this.conditionExpression.symbolTableReference = this.symbolTableReference;
		this.conditionExpression.errorLog = this.errorLog;
		this.conditionExpression.scope = this.scope;
		this.iterationExpression.symbolTableReference = this.symbolTableReference;
		this.iterationExpression.errorLog = this.errorLog;
		this.iterationExpression.scope = this.scope;
		this.blockStatement.symbolTableReference = this.symbolTableReference;
		this.blockStatement.errorLog = this.errorLog;
		this.blockStatement.scope = this.scope;
	}
}
