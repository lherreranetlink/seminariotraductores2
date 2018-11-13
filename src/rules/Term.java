package rules;

public class Term extends SyntaxTreeNode{
	public SyntaxTreeNode funcCall;
	
	public Term() {}
	
	public String getType() {
		this.funcCall.symbolTableReference = this.symbolTableReference;
		this.funcCall.errorLog = this.errorLog;
		this.funcCall.scope = this.scope;
		
		this.funcCall.semanticType = ((FuncCall) this.funcCall).getType();
		this.semanticType = this.funcCall.semanticType;
		
		return this.semanticType;
	}
}
