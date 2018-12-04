package rules;

public class Definition_1 extends SyntaxTreeNode{
	public SyntaxTreeNode defFunc;
	
	public Definition_1() {}
	
	public String getType() {
		this.defFunc.symbolTableReference = this.symbolTableReference;
		this.defFunc.errorLog = this.errorLog;
		this.defFunc.scope = this.scope;
		this.defFunc.semanticType = ((DefFunc)this.defFunc).getType();
		this.semanticType = this.defFunc.semanticType;
		
		return this.semanticType;
	}
	
	public String generateAsm() {
		return ((DefFunc) this.defFunc).generateAsm();
	}
}
