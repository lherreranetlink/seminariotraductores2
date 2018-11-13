package rules;

import parser.RuleType;

public class BlockStm extends SyntaxTreeNode {
	public SyntaxTreeNode statement;
	
	public BlockStm() {}
	
	public String getType() {
		this.statement.symbolTableReference = this.symbolTableReference;
		this.statement.errorLog = this.errorLog;
		this.statement.scope = this.scope;
		
		this.statement.semanticType = getStatementType();
		this.semanticType = this.statement.semanticType;
		
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
