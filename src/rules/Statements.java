package rules;

import parser.RuleType;
import semantic.SemanticType;

public class Statements extends SyntaxTreeNode{
	public SyntaxTreeNode statement;
	public SyntaxTreeNode statements;
	
	public Statements() {}
	
	public String getType() {
		this.statement.symbolTableReference = this.symbolTableReference;
		this.statement.errorLog = this.errorLog;
		this.statement.scope = this.scope;
		
        this.statement.semanticType = this.getStatementType();
        
        if (this.statements.ruleType != RuleType.EPSILON_RULE) {
        	this.statements.symbolTableReference = this.symbolTableReference;
        	this.statements.errorLog = this.errorLog;
        	this.statements.scope = this.scope;
        	this.statements.semanticType = ((Statements) this.statements).getType();
        } else {
        	this.statements.semanticType = SemanticType.VOID_TYPE;
        }
        
        this.semanticType = (!this.statement.semanticType.equals(SemanticType.ERROR_TYPE) 
        		             && !this.statements.semanticType.equals(SemanticType.ERROR_TYPE))
        		          ? SemanticType.VOID_TYPE
        		          : SemanticType.ERROR_TYPE;
		
		return this.semanticType;
	}
	
	private String getStatementType() {
		switch(this.statement.ruleType) {
			case RuleType.STATEMENT:
				return ((Statement) this.statement).getType();
			case RuleType.STATEMENT_1:
				return ((Statement_1) this.statement).getType();
			case RuleType.STATEMENT_2:
				return ((Statement_2) this.statement).getType();
			case RuleType.STATEMENT_3:
				return ((Statement_3) this.statement).getType();
			case RuleType.STATEMENT_4:
				return ((Statement_4) this.statement).getType();
			case RuleType.STATEMENT_5:
				return ((Statement_5) this.statement).getType();
			case RuleType.STATEMENT_6:
				return ((Statement_6) this.statement).getType();
		}
		return null;
	}
}
