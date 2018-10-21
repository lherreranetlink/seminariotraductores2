package semantic;

import javax.swing.JTextArea;

import parser.RuleType;
import rules.DefFunc;
import rules.DefVar;
import rules.Definition;
import rules.Definition_1;
import rules.Definitions;
import rules.FuncBlock;
import rules.LocalVarDef;
import rules.LocalVarDef_1;
import rules.LocalVarsDef;
import rules.ParamList;
import rules.Params;
import rules.Program;
import rules.SimpleToken;
import rules.SyntaxTreeNode;
import rules.VarList;
import symbol_table.SymbolTable;
import symbol_table.TableElement;

public class SemanticAnalyzer {
	private SyntaxTreeNode treeRoot;
	private String currentScope;
	private String currentParamsPattern;
	private SymbolTable symbolTable;
	private JTextArea errorLog;
	
	SemanticAnalyzer(JTextArea errorLog){
		this.errorLog = errorLog;
	}
	
	SemanticAnalyzer(SyntaxTreeNode treeRoot, JTextArea errorLog) {
		this.errorLog = errorLog;
		this.treeRoot = treeRoot;
		this.currentScope = "";
		this.currentParamsPattern = "";
	}
	
	public void analyzeInput() {
		this.analyzeProgram(((Program)this.treeRoot).definitions);
	}
	
	private void analyzeProgram(SyntaxTreeNode program) {
		if (((Program) program).definitions.ruleType != RuleType.EPSILON_RULE) {
			this.analyzeDefinitions(((Program) program).definitions);
		}
	}
	
	private void analyzeDefinitions(SyntaxTreeNode definitions) {
		this.analyzeDefinition(((Definitions)definitions).definition);
		if (((Definitions)definitions).moreDefinitions.ruleType != RuleType.EPSILON_RULE) {
			this.analyzeDefinitions(((Definitions) definitions).moreDefinitions);
		}
	}
	
	private void analyzeDefinition(SyntaxTreeNode definition) {
		int defType = definition.ruleType;
		switch (defType) {
			case RuleType.DEFINITION:
				this.analyzeVarDefinition(((Definition) definition).defVar);
				break;
			case RuleType.DEFINITION_1:
				this.analyzeFunctionDefinition(((Definition_1) definition).defFunc);
		}
	}
	
	private void analyzeFunctionDefinition(SyntaxTreeNode definition_1) {
		DefFunc defFunc = (DefFunc) ((Definition_1) definition_1).defFunc;
		String dataType = ((SimpleToken) defFunc.dataType).token.value;
		String identifier = ((SimpleToken) defFunc.identifier).token.value;
		TableElement newNode = null;
		
		if (!this.symbolTable.existsSymbol(identifier, this.currentScope)) {
			newNode = new TableElement(identifier, dataType, this.currentScope);
			this.symbolTable.add(newNode);
			this.currentScope = identifier;
		} else {
			this.printSemanticError("Function: " + identifier + "Already declared in scope: " + this.currentScope);
		}
		
		if (defFunc.params.ruleType != RuleType.EPSILON_RULE) {
			this.analyzeParams(defFunc.params);
		}
		
		if (newNode != null) {
			newNode.paramsPattern = this.currentParamsPattern;
		}
		
		this.currentParamsPattern = "";
		this.currentScope = "";
		this.analyzeBlockFunc(defFunc.blockFunc);
	}
	
	private void analyzeVarDefinition(SyntaxTreeNode definition) {
		DefVar defVar = (DefVar) ((Definition) definition).defVar;
		String dataType = ((SimpleToken) defVar.dataType).token.value;
		String identifier = ((SimpleToken) defVar.identifier).token.value;
		if (!this.symbolTable.existsSymbol(identifier, this.currentScope)){
			TableElement newNode = new TableElement(identifier, dataType, this.currentScope);
			this.symbolTable.add(newNode);
		} else {
			this.printSemanticError("Variable: " + identifier + " Already declared in scope: " + this.currentScope);
		}
		
		if (defVar.varList.ruleType != RuleType.EPSILON_RULE){
			this.analyzeVarList(defVar.varList, dataType);
		}
	}
	
	private void analyzeParams(SyntaxTreeNode params) {
		Params parameters = (Params) params;
		String dataType = ((SimpleToken) parameters.dataType).token.value;
		String identifier = ((SimpleToken) parameters.identifier).token.value;
		
		this.currentParamsPattern += dataType.charAt(0); 
		if (!this.symbolTable.existsSymbol(identifier, this.currentScope)) {
			TableElement newNode = new TableElement(identifier, dataType, this.currentScope);
			this.symbolTable.add(newNode);
		} else {
			this.printSemanticError("Variable: " + identifier + " Already declared in scope: " + this.currentScope);
		}
		
		if (parameters.paramList.ruleType != RuleType.EPSILON_RULE) {
			this.analyzeParamList(parameters.paramList);
		}
	}
	
	private void analyzeBlockFunc(SyntaxTreeNode blockFunc) {
		if (((FuncBlock) blockFunc).localVarsDef.ruleType != RuleType.EPSILON_RULE) {
			this.analyzeLocalDefinitions(((FuncBlock) blockFunc).localVarsDef);
		}
	}
	
	private void analyzeVarList(SyntaxTreeNode varList, String dataType) {
		VarList variableList = (VarList) varList;
		String identifier = ((SimpleToken) variableList.identifier).token.value;
		
		if (!this.symbolTable.existsSymbol(identifier, this.currentScope)) {
			TableElement newNode = new TableElement(identifier, dataType, this.currentScope);
			this.symbolTable.add(newNode);
		} else {
			this.printSemanticError("Variable: " + identifier + " Already declared in scope: " + this.currentScope);
		}
		
		if (variableList.nextVar.ruleType != RuleType.EPSILON_RULE) {
			this.analyzeVarList(variableList.nextVar, dataType);
		}
	}
	
	private void analyzeParamList(SyntaxTreeNode paramsList) {
		ParamList parameterList = (ParamList) paramsList;
		String dataType = ((SimpleToken) parameterList.dataType).token.value;
		String identifier = ((SimpleToken) parameterList.identifier).token.value;
		
		this.currentParamsPattern += dataType.charAt(0);
		if (!this.symbolTable.existsSymbol(identifier, this.currentScope)) {
			TableElement newNode = new TableElement(identifier, dataType, this.currentScope);
			this.symbolTable.add(newNode);
		} else {
			this.printSemanticError("Variable: " + identifier + " Already declared in scope: " + this.currentScope);
		}
		
		if (parameterList.paramList.ruleType != RuleType.EPSILON_RULE) {
			this.analyzeParamList(parameterList.paramList);
		}
	}
	
	private void analyzeLocalDefinitions(SyntaxTreeNode localDefs) {
		this.analyzeLocalVarsAnStatements(((LocalVarsDef)localDefs).localVarDef);
		if (((LocalVarsDef)localDefs).localVarsDef.ruleType != RuleType.EPSILON_RULE) {
			this.analyzeLocalVarsAnStatements(((LocalVarsDef)localDefs).localVarsDef);
		}
	}
	
	private void analyzeLocalVarsAnStatements(SyntaxTreeNode localDefinition) {
		int ruleType = localDefinition.ruleType;
		switch(ruleType) {
			case RuleType.LOCAL_VAR_DEF:
					this.analyzeVarDefinition(((LocalVarDef)localDefinition).defVar);
				break;
			case RuleType.LOCAL_VAR_DEF_1:
					this.analyzeSingleStatement(((LocalVarDef_1)localDefinition).statement);
		}
	}
	
	private void analyzeSingleStatement(SyntaxTreeNode statement) {
		
	}
	
	private void printSemanticError(String message){
		String errors = this.errorLog.getText();
		this.errorLog.setText(errors + " Semantic Error: " + message + "\n");
	}
}
