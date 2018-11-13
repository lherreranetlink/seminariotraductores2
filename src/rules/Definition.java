package rules;

public class Definition extends SyntaxTreeNode{
	public SyntaxTreeNode defVar;
	
	public Definition() {}
	
	public String getType() {
		this.defVar.errorLog = this.errorLog;
		this.defVar.symbolTableReference = this.symbolTableReference;
		this.defVar.semanticType = this.defVar.getType();
		this.semanticType = this.defVar.semanticType;
		
		return this.semanticType;
	}
}
