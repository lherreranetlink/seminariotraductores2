package parser;

import java.util.Stack;

import rules.SyntaxTreeNode;

public class TokenStack {
	private Stack<SyntaxTreeNode> stack;
	
	public TokenStack() {}
	
	public void push(SyntaxTreeNode data) {
		stack.push(data);
	}
	
	public SyntaxTreeNode gettop() { 
		return (stack.size() > 0) ? stack.elementAt(0) : null;
	}
	
	public SyntaxTreeNode pop() {
		return stack.pop();
	}
}
