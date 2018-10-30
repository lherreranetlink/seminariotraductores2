package rules;

import parser.RuleType;
import semantic.SemanticType;
import symbol_table.TableElement;

public class DefFunc extends SyntaxTreeNode{
	public SyntaxTreeNode dataType;
	public SyntaxTreeNode identifier;
	public SyntaxTreeNode params;
	public SyntaxTreeNode blockFunc;
	
	public String getType() {
		String data_type = ((SimpleToken) dataType).token.value;
		String func_identifier = ((SimpleToken) identifier).token.value;
		TableElement symbolTableElement = new TableElement();
		
		if (!this.symbolTableReference.existsSymbol(func_identifier, this.scope)) {
			symbolTableElement = new TableElement(data_type, func_identifier, this.scope);
			this.symbolTableReference.add(symbolTableElement);
			this.identifier.semanticType = this.symbolTableReference.getType(func_identifier, this.scope);
			this.scope = func_identifier;
		} else {
			this.identifier.semanticType = SemanticType.ERROR_TYPE;
			String errors = this.errorLog.getText();
			this.errorLog.setText(errors + " Semantic error: function already declared: " 
			                             + func_identifier + 
			                             " in scope: " + (this.scope.equals("") ? "Global" : this.scope) + "\n");
			
			this.identifier.semanticType = SemanticType.ERROR_TYPE;
		}
		
		if (params.ruleType != RuleType.EPSILON_RULE) {
			this.params.symbolTableReference = this.symbolTableReference;
			this.params.errorLog = this.errorLog;
			this.params.scope = this.scope;
			this.params.semanticType = ((Params) params).getType();
			this.paramsPattern += this.params.paramsPattern;
			symbolTableElement.paramsPattern = this.paramsPattern;
		} else {
			this.params.semanticType = SemanticType.VOID_TYPE;
		}
		
	    this.blockFunc.symbolTableReference = this.symbolTableReference;
		this.blockFunc.errorLog = this.errorLog;
		this.blockFunc.scope = this.scope;
    	this.blockFunc.semanticType = ((FuncBlock) blockFunc).getType();
    	System.out.println(this.identifier.semanticType);
    	System.out.println(this.params.semanticType);
    	System.out.println(this.blockFunc.semanticType);
    	this.semanticType = (!this.identifier.semanticType.equals(SemanticType.ERROR_TYPE) 
    			              && !this.params.semanticType.equals(SemanticType.ERROR_TYPE)
    			              && !this.blockFunc.semanticType.equals(SemanticType.ERROR_TYPE))
    			          ? SemanticType.VOID_TYPE
    			          : SemanticType.ERROR_TYPE;
    	
    	return this.semanticType;
	}
}
