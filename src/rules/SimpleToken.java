package rules;

import lex.Token;

public class SimpleToken extends SyntaxTreeNode {
	public Token token;
	
	public SimpleToken(Token token) {
		this.token = token;
	}
}
