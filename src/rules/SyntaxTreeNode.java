package rules;

import fileutils.FileManager;
import symbol_table.SymbolTable;

public class SyntaxTreeNode {
	public int stateToSee;
	public int ruleType;
	
	public String semanticType;
	public String scope;
	public String paramsPattern;
	
	public SymbolTable symbolTableReference;
	public FileManager errorLog;
	
	public SyntaxTreeNode() {
		this.semanticType = "";
		this.scope = "";
		this.paramsPattern = "";
	}
	
	public String getType() {
		return null;
	}
	
	public String generateAsm() {
		return null;
	}
}
