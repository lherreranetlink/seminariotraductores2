package rules;

import lex.Constants;

public class Term_2 extends SyntaxTreeNode {
	public SyntaxTreeNode constant;
	
	public Term_2() {}
	
	public String getType() {
		switch (((SimpleToken) this.constant).token.extraAttr) {
			case Constants.INTEGER_NUMBER:
				return "int";
			case Constants.REAL_NUMBER:
				return "float";
			default:
				return "error";
		}
	}
}
