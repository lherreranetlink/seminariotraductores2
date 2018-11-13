package rules;

import parser.RuleType;

public class ElsePart extends SyntaxTreeNode{
	public SyntaxTreeNode blockStm;
	
	public ElsePart() {}
	
	public String getType() {
		this.blockStm.symbolTableReference = this.symbolTableReference;
		this.blockStm.errorLog = this.errorLog;
		this.blockStm.scope = this.scope;
		this.blockStm.semanticType = this.getBlockStatementType();
		
		this.semanticType = this.blockStm.semanticType;
		
		return this.semanticType;
	}
	
	private String getBlockStatementType() {
		switch(this.blockStm.ruleType) {
			case RuleType.BLOCK_STM:
				return ((BlockStm) blockStm).getType();
			case RuleType.BLOCK_STM_1:
				return ((BlockStm_1) blockStm).getType();
		}
		return null;
	}
}
