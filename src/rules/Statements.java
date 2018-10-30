package rules;

import parser.RuleType;

public class Statements extends SyntaxTreeNode{
	public SyntaxTreeNode statement;
	public SyntaxTreeNode statements;
	
	public Statements() {}
	
	public void validateTypes() {
		this.statement.symbolTableReference = this.symbolTableReference;
		this.statement.errorLog = this.errorLog;
		this.statement.scope = this.scope;
		this.validateStatement();
	}
	
	protected void validateStatement() {
		switch(this.statement.ruleType) {
			case RuleType.STATEMENT:
				((Statement) this.statement).validateTypes();
				break;
			case RuleType.STATEMENT_1:
				((Statement_1) this.statement).validateTypes();
				break;
			case RuleType.STATEMENT_2:
				((Statement_2) this.statement).validateTypes();
				break;
			case RuleType.STATEMENT_3:
				((Statement_3) this.statement).validateTypes();
				break;
			case RuleType.STATEMENT_4:
				((Statement_4) this.statement).validateTypes();
				break;
			case RuleType.STATEMENT_5:
				((Statement_5) this.statement).validateTypes();
				break;
			case RuleType.STATEMENT_6:
				((Statement_6) this.statement).validateTypes();
		}
	}
}
