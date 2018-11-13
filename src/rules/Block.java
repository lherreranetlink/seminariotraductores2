package rules;

import parser.RuleType;
import semantic.SemanticType;

public class Block extends SyntaxTreeNode{
	public SyntaxTreeNode statements;
	
	public Block() {}
	
	public String getType() {
		if (this.statements.ruleType != RuleType.EPSILON_RULE) {
			this.statements.symbolTableReference = this.symbolTableReference;
			this.statements.errorLog = this.errorLog;
			this.statements.scope = this.scope;
			
			this.statements.semanticType = ((Statements) this.statements).getType();
		} else {
			this.statements.semanticType = SemanticType.VOID_TYPE;
		}
		
		this.semanticType = this.statements.semanticType;
		
		return this.semanticType;
	}
}
