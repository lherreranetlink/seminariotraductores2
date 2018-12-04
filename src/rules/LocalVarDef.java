package rules;

public class LocalVarDef extends SyntaxTreeNode{
	public SyntaxTreeNode defVar;
	
	public LocalVarDef() {}
	
	public String getType() {
		this.defVar.symbolTableReference = this.symbolTableReference;
		this.defVar.errorLog = this.errorLog;
		this.defVar.scope = this.scope;
		this.defVar.semanticType = ((DefVar) this.defVar).getType();
		this.semanticType = this.defVar.semanticType;
		
		return this.semanticType;
	}
	
	public String generateAsm() {
		return ((DefVar) this.defVar).generateAsm();
	}
}
