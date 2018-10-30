package rules;

import parser.RuleType;

public class BlockStm extends SyntaxTreeNode {
	public SyntaxTreeNode statement;
	
	public BlockStm() {}
	
	public void validateTypes() {
		this.statement.errorLog = this.errorLog;
		this.statement.symbolTableReference = this.symbolTableReference;
		
		if (this.statement.ruleType != RuleType.EPSILON_RULE) {
			this.statement.errorLog  =this.errorLog;
			this.statement.symbolTableReference = this.symbolTableReference;
		}
	}
}
