package rules;

import semantic.SemanticType;

public class Term_1 extends SyntaxTreeNode {
	
	public SyntaxTreeNode identifier;
	
	public Term_1() {}
	
	public String getType() {
		String type = this.symbolTableReference.getType(((SimpleToken)identifier).token.value, this.scope);
		
		if (type.equals(SemanticType.ERROR_TYPE)) {
			this.errorLog.append_content("Semantic Error: variable: " + ((SimpleToken)identifier).token.value +
					                     " may not be declared");
		}
			
		return type;
	}
}
