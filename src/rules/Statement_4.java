package rules;

public class Statement_4 extends SyntaxTreeNode{
	//For(x;x;x) Node
	public SyntaxTreeNode identifier;
	public SyntaxTreeNode initializationExpression;
	public SyntaxTreeNode conditionExpression;
	public SyntaxTreeNode iterationExpression;
	public SyntaxTreeNode blockStatement;
}
