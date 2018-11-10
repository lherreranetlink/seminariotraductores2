package rules;

import parser.RuleType;
import semantic.SemanticType;
import symbol_table.SymbolTable;

public class FuncCall extends SyntaxTreeNode {
	public SyntaxTreeNode identifier;
	public SyntaxTreeNode args;
	
	public FuncCall() {}
	
	public String getType() {
		String func_identifier = ((SimpleToken) identifier).token.value;
		if (this.symbolTableReference.existsSymbol(func_identifier, SymbolTable.GLOBAL_SCOPE)) {
			this.identifier.semanticType = this.symbolTableReference.getType(func_identifier, SymbolTable.GLOBAL_SCOPE);
		} else if (func_identifier.equals("print")) {
			this.identifier.semanticType = SemanticType.VOID_TYPE;
		} else {
			this.identifier.semanticType = SemanticType.ERROR_TYPE;
			this.errorLog.append_content("Semantic error: function does not exist: " + func_identifier + 
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
		
		if (!this.identifier.semanticType.equals(SemanticType.ERROR_TYPE) && !func_identifier.equals("print")) {
			String paramsPatternDefinition = this.symbolTableReference.getScopeParamsPattern(func_identifier);
			if (!this.paramsPattern.equals(paramsPatternDefinition)) {
				this.errorLog.append_content("Semantic error: Arguments passed in: " + func_identifier + 
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
