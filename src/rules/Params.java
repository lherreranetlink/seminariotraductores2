package rules;

import parser.RuleType;
import semantic.SemanticType;

public class Params extends SyntaxTreeNode{
	public SyntaxTreeNode dataType;
	public SyntaxTreeNode identifier;
	public SyntaxTreeNode paramList;
	
	public Params() {}
	
	public String getType() {
		String data_type = ((SimpleToken) dataType).token.value;
		String param_identifier = ((SimpleToken) identifier).token.value;
		
		this.paramsPattern += data_type.charAt(0);
		
		if (!this.symbolTableReference.existsSymbol(param_identifier, this.scope)) {
			this.symbolTableReference.add(data_type, param_identifier, this.scope);
			this.identifier.semanticType = this.symbolTableReference.getType(param_identifier, this.scope);
		} else {
			String errors = this.errorLog.getText();
			this.errorLog.setText(errors + " Semantic error: parameter already declared: " 
			                             + param_identifier + 
			                             " in scope: " + this.scope + "\n");
			this.identifier.semanticType = SemanticType.ERROR_TYPE;
		}
		
		if (paramList.ruleType != RuleType.EPSILON_RULE) {
			this.paramList.symbolTableReference = this .symbolTableReference;
			this.paramList.errorLog = this.errorLog;
			this.paramList.scope = this.scope;
			this.paramList.semanticType = ((ParamList) this.paramList).getType();
			this.paramsPattern += this.paramList.paramsPattern;
		} else {
			this.paramList.semanticType = SemanticType.VOID_TYPE;
		}
		this.semanticType = (!this.identifier.semanticType.equals(SemanticType.ERROR_TYPE) && !this.paramList.semanticType.equals(SemanticType.ERROR_TYPE))
				          ? SemanticType.VOID_TYPE
				          : SemanticType.ERROR_TYPE;
		
		return this.semanticType;
		
	}
}
