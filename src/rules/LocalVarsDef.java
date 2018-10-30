package rules;

import parser.RuleType;
import semantic.SemanticType;

public class LocalVarsDef extends SyntaxTreeNode{
	public SyntaxTreeNode localVarDef;
	public SyntaxTreeNode localVarsDef;
	
	public LocalVarsDef() {}
	
	public String getType() {
		this.localVarDef.symbolTableReference = this.symbolTableReference;
		this.localVarDef.errorLog = this.errorLog;
		this.localVarDef.scope = this.scope;
		this.localVarDef.semanticType = this.getLocalDefinitionType();
		
		if (this.localVarsDef.ruleType != RuleType.EPSILON_RULE) {
			this.localVarsDef.symbolTableReference = this.symbolTableReference;
			this.localVarsDef.errorLog = this.errorLog;
			this.localVarsDef.scope = this.scope;
			this.localVarsDef.semanticType = ((LocalVarsDef)this.localVarsDef).getType();
		} else {
			this.localVarsDef.semanticType = SemanticType.VOID_TYPE;
		}
		
		this.semanticType = (!this.localVarDef.semanticType.equals(SemanticType.ERROR_TYPE) && this.localVarsDef.semanticType.equals(SemanticType.ERROR_TYPE))
			              ? SemanticType.VOID_TYPE
			              : SemanticType.ERROR_TYPE;
		
		return this.semanticType;
	}
	
	private String getLocalDefinitionType() {
		switch(this.localVarDef.ruleType) {
			case RuleType.LOCAL_VAR_DEF:
				return ((LocalVarDef) this.localVarDef).getType();
			case RuleType.LOCAL_VAR_DEF_1:
				return ((LocalVarDef_1) this.localVarDef).getType();
		}
		
		return null;
	}
}
