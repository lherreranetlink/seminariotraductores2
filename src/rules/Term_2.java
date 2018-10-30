package rules;

import lex.Constants;
import semantic.SemanticType;

public class Term_2 extends SyntaxTreeNode {
	public SyntaxTreeNode constant;
	
	public Term_2() {}
	
	public String getType() {
		switch (((SimpleToken) this.constant).token.extraAttr) {
			case Constants.INTEGER_NUMBER:
				return SemanticType.INTEGER_TYPE;
			case Constants.REAL_NUMBER:
				return SemanticType.FLOAT_TYPE;
			default:
				return SemanticType.ERROR_TYPE;
		}
	}
}
