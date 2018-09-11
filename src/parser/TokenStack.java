package parser;

import java.util.Stack;

import lex.Constants;
import lex.Token;
import rules.SimpleToken;
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
	
	public void initializeStack() {
		Token eofToken = new Token(Constants.EOF_SIGN, "$");
		SimpleToken node = new SimpleToken(eofToken);
		node.ruleType = RuleType.SIMPLE_TOKEN;
		node.stateToSee = 0;
		this.push(node);
	}
}
