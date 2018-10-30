package rules;


import parser.RuleType;
import semantic.SemanticType;

public class Program extends SyntaxTreeNode { 
	public SyntaxTreeNode definitions;
	
	public String getType() {
		this.semanticType = SemanticType.ERROR_TYPE;
		if (definitions.ruleType != RuleType.EPSILON_RULE) {
			definitions.errorLog = this.errorLog;
			definitions.symbolTableReference = this.symbolTableReference;
			definitions.scope = this.scope;
			this.semanticType = ((Definitions) definitions).getType(); 
		}
		
		return this.semanticType;
	}
}
