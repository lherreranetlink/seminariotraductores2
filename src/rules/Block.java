package rules;

import parser.RuleType;

public class Block extends SyntaxTreeNode{
	public SyntaxTreeNode statements;
	
	public Block() {}
	
	public void validateTypes() {
		if (this.statements.ruleType != RuleType.EPSILON_RULE) {
			this.statements.errorLog = this.errorLog;
			this.statements.symbolTableReference = this.symbolTableReference;
			this.statements.validateTypes();
		}
	}
}
