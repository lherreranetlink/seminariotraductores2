package rules;

import javax.swing.JTextArea;

import symbol_table.SymbolTable;

public class SyntaxTreeNode {
	public int stateToSee;
	public int ruleType;
	
	public String semanticType;
	public String scope;
	public String paramsPattern;
	
	public SymbolTable symbolTableReference;
	public JTextArea errorLog;
	
	public SyntaxTreeNode() {
		this.semanticType = "";
		this.scope = "";
		this.paramsPattern = "";
	}
	
	public void validateTypes() {
	}
	
	public String getType() {
		return null;
	}
}
