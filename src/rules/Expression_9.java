package rules;

import parser.RuleType;

public class Expression_9 extends SyntaxTreeNode {
	public SyntaxTreeNode term;
	
	public Expression_9() {}
	
	public String getType() {
		this.term.symbolTableReference = this.symbolTableReference;
		this.term.errorLog = this.errorLog;
		this.term.scope = this.scope;
		this.term.semanticType = this.getTermType();
		this.semanticType = this.term.semanticType;
		
		return this.semanticType;
	}
	
	private String getTermType() {
		switch(term.ruleType) {
			case RuleType.TERM:
				return ((Term) this.term).getType();
			case RuleType.TERM_1:
				return ((Term_1) this.term).getType();
			case RuleType.TERM_2:
				return ((Term_2) this.term).getType();
		}
		return null;
	}
}
