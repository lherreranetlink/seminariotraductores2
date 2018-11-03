package rules;

public class Term_1 extends SyntaxTreeNode {
	
	public SyntaxTreeNode identifier;
	
	public Term_1() {}
	
	public String getType() {
		return this.symbolTableReference.getType(((SimpleToken)identifier).token.value, this.scope);
	}
}
