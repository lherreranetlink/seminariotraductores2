package rules;

import parser.RuleType;
import semantic.SemanticType;

public class FuncCall extends SyntaxTreeNode {
	public SyntaxTreeNode identifier;
	public SyntaxTreeNode args;
	
	public FuncCall() {}
	
	public String getType() {
		String func_identifier = ((SimpleToken) identifier).token.value;
		if (this.symbolTableReference.existsSymbol(func_identifier, this.scope)) {
			this.identifier.semanticType = this.symbolTableReference.getType(func_identifier, this.scope);
		} else {
			this.identifier.semanticType = SemanticType.ERROR_TYPE;
			String errors = this.errorLog.getText();
			this.errorLog.setText(errors + " Semantic error: function does not exist: " 
			                             + func_identifier + 
			                             " in scope: " + this.scope + "\n");
		}
		
		this.paramsPattern = "";
		
		if (args.ruleType != RuleType.EPSILON_RULE) {
			this.args.symbolTableReference = this.symbolTableReference;
			this.args.errorLog = this.errorLog;
			this.args.scope = this.scope;
			this.args.semanticType = ((Args) this.args).getType();
			this.paramsPattern += this.args.paramsPattern;
		} else {
			this.args.semanticType = SemanticType.VOID_TYPE;
		}
		
		if (!this.identifier.semanticType.equals(SemanticType.ERROR_TYPE)) {
			String paramsPatternDefinition = this.symbolTableReference.getScopeParamsPattern(this.scope);
			if (!this.paramsPattern.equals(paramsPatternDefinition)) {
				String errors = this.errorLog.getText();
				this.errorLog.setText(errors + " Semantic error: Arguments passed in: " 
				                             + func_identifier + 
				                             " does not match with original definition in scope : " + this.scope + "\n");
			}
		}
		
		this.semanticType = (!this.identifier.semanticType.equals(SemanticType.ERROR_TYPE)
				             && !this.args.semanticType.equals(SemanticType.ERROR_TYPE))
				          ? this.identifier.semanticType
				          : SemanticType.ERROR_TYPE;
		
		return this.semanticType;
	}
}
