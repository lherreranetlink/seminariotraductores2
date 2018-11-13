package rules;

import parser.RuleType;
import semantic.SemanticType;

public class DefVar extends SyntaxTreeNode{
	public SyntaxTreeNode dataType;
	public SyntaxTreeNode identifier;
	public SyntaxTreeNode varList;
	
	public DefVar() {}
	
	public String getType() {
		String data_type = ((SimpleToken) dataType).token.value;
		String var_identifier = ((SimpleToken) identifier).token.value;
		
		if (!this.symbolTableReference.existsSymbol(var_identifier, this.scope)) {
			this.symbolTableReference.add(data_type, var_identifier, this.scope);
			this.identifier.semanticType = this.symbolTableReference.getType(var_identifier, this.scope);
		} else {
			this.errorLog.append_content("Semantic error: identifier already declared: " + var_identifier + 
                    " in scope: " + (this.scope.equals("") ? "Global" : this.scope) + "\n");
			
			this.identifier.semanticType = SemanticType.ERROR_TYPE;
		}
		
		if (this.varList.ruleType != RuleType.EPSILON_RULE) {
			this.varList.symbolTableReference = this.symbolTableReference;
			this.varList.errorLog = this.errorLog;
			this.varList.scope = this.scope;
			this.varList.semanticType = ((VarList) this.varList).getType(data_type);
			
			this.semanticType = (!this.identifier.semanticType.equals(SemanticType.ERROR_TYPE)  && !this.varList.semanticType.equals(SemanticType.ERROR_TYPE))
					          ? SemanticType.VOID_TYPE
					          : SemanticType.ERROR_TYPE;
		} else {
			this.semanticType = this.identifier.semanticType;
		}
		
		return this.semanticType;
	}
}
