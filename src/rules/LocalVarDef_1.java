package rules;

import parser.RuleType;

public class LocalVarDef_1 extends SyntaxTreeNode{
	public SyntaxTreeNode statement;
	
	public LocalVarDef_1() {}
	
	public String getType() {
		this.statement.symbolTableReference = this.symbolTableReference;
		this.statement.errorLog = this.errorLog;
		this.statement.scope = this.scope;
		this.statement.semanticType = this.getStatementType();
		this.semanticType = this.statement.semanticType;
		
		return this.semanticType;
	}
	
	private String getStatementType() {
		switch(this.statement.ruleType) {
		case RuleType.STATEMENT:
			return ((Statement) this.statement).getType();
		case RuleType.STATEMENT_1:
		case RuleType.STATEMENT_2:
		case RuleType.STATEMENT_3:
		case RuleType.STATEMENT_4:
		case RuleType.STATEMENT_5:
		case RuleType.STATEMENT_6:
		}
		return null;
	}
}
