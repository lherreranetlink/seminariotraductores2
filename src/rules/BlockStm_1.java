package rules;

import parser.RuleType;
import semantic.SemanticType;

public class BlockStm_1 extends SyntaxTreeNode {
	public SyntaxTreeNode block;
	
	public BlockStm_1() {}
	
	public String getType() {
		if (this.block.ruleType != RuleType.EPSILON_RULE) {
			this.block.symbolTableReference = this.symbolTableReference;
			this.block.errorLog = this.errorLog;
			this.block.scope = this.scope;
			
			this.block.semanticType = ((Block) this.block).getType();
		} else {
			this.block.semanticType = SemanticType.VOID_TYPE;
		}
		this.semanticType = this.block.semanticType;
		
		return this.semanticType;
	}
}
