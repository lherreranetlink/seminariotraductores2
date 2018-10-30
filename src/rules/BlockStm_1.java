package rules;

import parser.RuleType;

public class BlockStm_1 extends SyntaxTreeNode {
	public SyntaxTreeNode block;
	
	public BlockStm_1() {}
	
	public void validateTypes() {
		if (block.ruleType != RuleType.EPSILON_RULE) {
			this.block.errorLog = this.errorLog;
			this.block.symbolTableReference = this.symbolTableReference;
			block.validateTypes();
		}
	}
}
