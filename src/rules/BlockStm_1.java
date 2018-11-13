package rules;

public class BlockStm_1 extends SyntaxTreeNode {
	public SyntaxTreeNode block;
	
	public BlockStm_1() {}
	
	public String getType() {
	    this.block.symbolTableReference = this.symbolTableReference;
		this.block.errorLog = this.errorLog;
		this.block.scope = this.scope;	
		this.block.semanticType = ((Block) this.block).getType();
		
		this.semanticType = this.block.semanticType;
		
		return this.semanticType;
	}
}
