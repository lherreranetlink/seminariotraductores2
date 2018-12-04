package rules;

import parser.RuleType;
import semantic.SemanticType;

public class VarList extends SyntaxTreeNode{
	public SyntaxTreeNode identifier;
	public SyntaxTreeNode nextVar;
	
	public VarList() {}
	
	public String getType(String varListType) {
		String var_identifier = ((SimpleToken) identifier).token.value;
		
		if (!this.symbolTableReference.existsSymbol(var_identifier, this.scope)) {
			this.symbolTableReference.add(varListType, var_identifier, this.scope);
			this.identifier.semanticType = this.symbolTableReference.getType(var_identifier, this.scope);
		} else {
			this.errorLog.append_content("Semantic error: variable already declared: " + var_identifier + 
			                             " in scope: " + (this.scope.equals("") ? "Global" : this.scope) + "\n");
			
			this.identifier.semanticType = SemanticType.ERROR_TYPE;
		}
		
		if (this.nextVar.ruleType != RuleType.EPSILON_RULE) {
			this.nextVar.symbolTableReference = this.symbolTableReference;
			this.nextVar.errorLog = this.errorLog;
			this.nextVar.scope  = this.scope;
			this.nextVar.semanticType = ((VarList)this.nextVar).getType(varListType);
			this.semanticType = (!this.identifier.semanticType.equals(SemanticType.ERROR_TYPE) && !this.nextVar.semanticType.equals(SemanticType.ERROR_TYPE))
					          ? SemanticType.VOID_TYPE
					          : SemanticType.ERROR_TYPE;
		} else {
			this.semanticType = this.identifier.semanticType;
		}
		
		return this.semanticType;
	}
	
	public String generateAsm(String varListType) {
		String dataTypeCode = "";
		String varCode = "";
		String code = "";
		String var_identifier = ((SimpleToken) identifier).token.value;
	
		if (varListType.equals("int")) {
			dataTypeCode += (this.scope.equals("")) ? " DWORD 0 " : ": DWORD ";
		} else {
			dataTypeCode += (this.scope.equals("")) ? " real4 0.0 " : ": real4 ";
		}
		
		varCode += var_identifier + dataTypeCode;
		code += this.scope.equals("") ? varCode + "\n" : "local " + varCode + "\n";
		
		if (this.nextVar.ruleType != RuleType.EPSILON_RULE) {
			code += ((VarList) this.nextVar).generateAsm(varListType);
		}
		
		return code;
	}
}
