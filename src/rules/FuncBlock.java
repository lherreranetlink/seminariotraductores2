package rules;

import parser.RuleType;
import semantic.SemanticType;

public class FuncBlock extends SyntaxTreeNode{
	public SyntaxTreeNode localVarsDef;
	
	public FuncBlock() {}
	
	public String getType() {
		if (localVarsDef.ruleType != RuleType.EPSILON_RULE) {
			this.localVarsDef.errorLog = this.errorLog;
			this.localVarsDef.symbolTableReference = this.symbolTableReference;
			this.localVarsDef.scope = this.scope;
			this.localVarsDef.semanticType = ((LocalVarsDef)this.localVarsDef).getType();
		} else {
			this.localVarsDef.semanticType = SemanticType.VOID_TYPE;
		}
		this.semanticType = this.localVarsDef.semanticType;
		
		return this.semanticType;
	}
}
