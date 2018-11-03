package rules;

import parser.RuleType;
import semantic.SemanticType;

public class Statement_1 extends SyntaxTreeNode{
	public SyntaxTreeNode expression;
	public SyntaxTreeNode ifPart;
	public SyntaxTreeNode elsePart;
	
	public Statement_1() {}
	
	public String getType() {
		this.setInheritedAttributes();
		
		this.expression.semanticType = this.getExpressionType();
		this.ifPart.semanticType = this.getBlockStatementType();
		this.elsePart.semanticType = (this.elsePart.ruleType != RuleType.EPSILON_RULE)
				                   ? ((ElsePart) this.elsePart).getType()
				                   : SemanticType.VOID_TYPE;
		
		this.semanticType = (!this.expression.semanticType.equals(SemanticType.ERROR_TYPE) 
				             && this.ifPart.semanticType.equals(SemanticType.VOID_TYPE)
				             && this.elsePart.semanticType.equals(SemanticType.VOID_TYPE))
				          ? SemanticType.VOID_TYPE
				          : SemanticType.ERROR_TYPE;
		
		return this.semanticType;
	}
	
	private String getExpressionType() {
		switch (this.expression.ruleType) {
		case RuleType.EXPRESSION:
			return ((Expression) this.expression).getType();
		case RuleType.EXPRESSION_1:
			return ((Expression_1) this.expression).getType();
		case RuleType.EXPRESSION_2:
			return ((Expression_2) this.expression).getType();
		case RuleType.EXPRESSION_3:
			return ((Expression_3) this.expression).getType();
		case RuleType.EXPRESSION_4:
			return ((Expression_4) this.expression).getType();
		case RuleType.EXPRESSION_5:
			return ((Expression_5) this.expression).getType();
		case RuleType.EXPRESSION_6:
			return ((Expression_6) this.expression).getType();
		case RuleType.EXPRESSION_7:
			return ((Expression_7) this.expression).getType();
		case RuleType.EXPRESSION_8:
			return ((Expression_8) this.expression).getType();
		case RuleType.EXPRESSION_9:
			return ((Expression_9) this.expression).getType();
		}
		return null;
	}
	
	private String getBlockStatementType() {
		switch(this.ifPart.ruleType) {
			case RuleType.BLOCK_STM:
				return ((BlockStm) ifPart).getType();
			case RuleType.BLOCK_STM_1:
				return ((BlockStm_1) ifPart).getType();
		}
		return null;
	}
	
	private void setInheritedAttributes() {
		this.expression.symbolTableReference = this.symbolTableReference;
		this.expression.errorLog = this.errorLog;
		this.expression.scope = this .scope;
		this.ifPart.symbolTableReference = this.symbolTableReference;
		this.ifPart.errorLog = this.errorLog;
		this.ifPart.scope = this.scope;
		
		if (elsePart.ruleType != RuleType.EPSILON_RULE) {
			this.elsePart.symbolTableReference = this.symbolTableReference;
			this.elsePart.errorLog = this.errorLog;
			this.elsePart.scope = this.scope;
		}
	}
}
