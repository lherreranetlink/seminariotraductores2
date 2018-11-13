package rules;

public class Statement_6 extends SyntaxTreeNode {
	//Func call with semicolon node
	public SyntaxTreeNode funcCall;
	
	public Statement_6() {}
	
	public String getType() {
		this.funcCall.symbolTableReference = this.symbolTableReference;
		this.funcCall.errorLog = this.errorLog;
		this.funcCall.scope = this.scope;
		this.funcCall.semanticType = ((FuncCall) this.funcCall).getType();
		this.semanticType = this.funcCall.semanticType;
		
		return this.semanticType;
	}
}
