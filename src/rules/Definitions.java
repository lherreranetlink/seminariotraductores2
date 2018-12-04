package rules;

import parser.RuleType;
import semantic.SemanticType;

public class Definitions extends SyntaxTreeNode{
	public SyntaxTreeNode definition;
	public SyntaxTreeNode moreDefinitions;
	
	public Definitions() {}
	
	public String getType() {
		
		definition.symbolTableReference = this.symbolTableReference;
		definition.errorLog = this.errorLog;
		definition.scope  = this.scope;
		this.definition.semanticType = this.getDefinitionType();
		
		this.scope = "";
		
		if (moreDefinitions.ruleType != RuleType.EPSILON_RULE) {
			this.moreDefinitions.symbolTableReference = this.symbolTableReference;
			this.moreDefinitions.errorLog = this.errorLog;
			this.moreDefinitions.scope = this.scope;
			this.moreDefinitions.semanticType = ((Definitions) this.moreDefinitions).getType();
			this.semanticType = (!this.definition.semanticType.equals(SemanticType.ERROR_TYPE) 
					             && !this.moreDefinitions.semanticType.equals(SemanticType.ERROR_TYPE))
					          ? SemanticType.VOID_TYPE
					          : SemanticType.ERROR_TYPE;
		} else {
			this.semanticType = this.definition.semanticType;
		}
		
		return this.semanticType;
	}
	
	private String getDefinitionType() {
		switch(this.definition.ruleType){
			case RuleType.DEFINITION:
				return ((Definition) this.definition).getType();
			case RuleType.DEFINITION_1:
				return ((Definition_1) this.definition).getType();
		}
		return null;
	}
	
	public String generateAsm() {
		String code = this.getDefinitionAsm();
		if (this.moreDefinitions.ruleType != RuleType.EPSILON_RULE) {
			code += ((Definitions)this.moreDefinitions).generateAsm();
		}
		return code;
	}
	
	private String getDefinitionAsm() {
		switch(this.definition.ruleType){
			case RuleType.DEFINITION:
				return ((Definition) this.definition).generateAsm();
			case RuleType.DEFINITION_1:
				return ((Definition_1) this.definition).generateAsm();
		}
		return null;
	}
}
