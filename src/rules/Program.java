package rules;


import parser.RuleType;
import semantic.SemanticType;

public class Program extends SyntaxTreeNode { 
	public SyntaxTreeNode definitions;
	
	public String getType() {
		if (definitions.ruleType != RuleType.EPSILON_RULE) {
			definitions.errorLog = this.errorLog;
			definitions.symbolTableReference = this.symbolTableReference;
			definitions.scope = this.scope;
			this.definitions.semanticType = ((Definitions) definitions).getType(); 
		} else {
			this.definitions.semanticType = SemanticType.VOID_TYPE;
		}
		
		this.semanticType = this.definitions.semanticType;
		
		return this.semanticType;
	}
	
	public String generateAsm() {
		if (this.definitions.ruleType != RuleType.EPSILON_RULE) {
			return ((Definitions) this.definitions).generateAsm();
		} else {
			return "";
		}
	}
}
