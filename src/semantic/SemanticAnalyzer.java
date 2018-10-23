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
		
		defFunc.semanticType = dataType;
		defFunc.scope = this.currentScope;
		
		if (!this.symbolTable.existsSymbol(identifier, this.currentScope)) {
			newNode = new TableElement(identifier, dataType, this.currentScope);
			this.symbolTable.add(newNode);
			this.currentScope = identifier;
			
			if (defFunc.params.ruleType != RuleType.EPSILON_RULE) {
				this.analyzeParams(defFunc.params);
			}
			
			newNode.paramsPattern = this.currentParamsPattern;
			
			defFunc.paramsPattern = this.currentParamsPattern;
			this.currentParamsPattern = "";
			this.analyzeBlockFunc(defFunc.blockFunc);
		} else {
			this.printSemanticError("Function: " + identifier + "Already declared in scope: " + this.currentScope);
		}
		
		this.currentScope = "";
	}
	
	private void analyzeVarDefinition(SyntaxTreeNode definition) {
		DefVar defVar = (DefVar) ((Definition) definition).defVar;
		String dataType = ((SimpleToken) defVar.dataType).token.value;
		String identifier = ((SimpleToken) defVar.identifier).token.value;
		
		defVar.semanticType = dataType;
		defVar.scope = this.currentScope;
		
		if (!this.symbolTable.existsSymbol(identifier, this.currentScope)){
			TableElement newNode = new TableElement(identifier, dataType, this.currentScope);
			this.symbolTable.add(newNode);
			
			if (defVar.varList.ruleType != RuleType.EPSILON_RULE){
				this.analyzeVarList(defVar.varList, dataType);
			}
		} else {
			this.printSemanticError("Variable: " + identifier + " Already declared in scope: " + this.currentScope);
		}
	}
	
	private void analyzeParams(SyntaxTreeNode params) {
		Params parameters = (Params) params;
		String dataType = ((SimpleToken) parameters.dataType).token.value;
		String identifier = ((SimpleToken) parameters.identifier).token.value;
		
		parameters.semanticType = dataType;
		parameters.scope = this.currentScope;
		
		this.currentParamsPattern += dataType.charAt(0); 
		if (!this.symbolTable.existsSymbol(identifier, this.currentScope)) {
			TableElement newNode = new TableElement(identifier, dataType, this.currentScope);
			this.symbolTable.add(newNode);
			
			if (parameters.paramList.ruleType != RuleType.EPSILON_RULE) {
				this.analyzeParamList(parameters.paramList);
			}
		} else {
			this.printSemanticError("Variable: " + identifier + " Already declared in scope: " + this.currentScope);
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
		
		variableList.semanticType = dataType;
		variableList.scope = this.currentScope;
		
		if (!this.symbolTable.existsSymbol(identifier, this.currentScope)) {
			TableElement newNode = new TableElement(identifier, dataType, this.currentScope);
			this.symbolTable.add(newNode);
			
			if (variableList.nextVar.ruleType != RuleType.EPSILON_RULE) {
				this.analyzeVarList(variableList.nextVar, dataType);
			}
		} else {
			this.printSemanticError("Variable: " + identifier + " Already declared in scope: " + this.currentScope);
		}
	}
	
	private void analyzeParamList(SyntaxTreeNode paramsList) {
		ParamList parameterList = (ParamList) paramsList;
		String dataType = ((SimpleToken) parameterList.dataType).token.value;
		String identifier = ((SimpleToken) parameterList.identifier).token.value;
		
		parameterList.semanticType = dataType;
		parameterList.scope = this.currentScope;
		
		this.currentParamsPattern += dataType.charAt(0);
		if (!this.symbolTable.existsSymbol(identifier, this.currentScope)) {
			TableElement newNode = new TableElement(identifier, dataType, this.currentScope);
			this.symbolTable.add(newNode);
			
			if (parameterList.paramList.ruleType != RuleType.EPSILON_RULE) {
				this.analyzeParamList(parameterList.paramList);
			}
		} else {
			this.printSemanticError("Variable: " + identifier + " Already declared in scope: " + this.currentScope);
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
		int ruleType = statement.ruleType;
		switch(ruleType) {
			case RuleType.STATEMENT:
				this.analyzeAssignment(statement);
			break;
			case RuleType.STATEMENT_1:
				this.analyzeIfStatement(statement);
			break;
			case RuleType.STATEMENT_2:
				this.analyzeWhileStatement(statement);
				break;
			case RuleType.STATEMENT_3:
				this.analyzeDoWhileStatement(statement);
				break;
			case RuleType.STATEMENT_4:
				this.analyzeForStatement(statement);
				break;
			case RuleType.STATEMENT_5:
				this.analyzeReturnExpression(statement);
				break;
			case RuleType.STATEMENT_6:
				this.analyzeFunctionCallStatement(statement);
		}
	}
	
	private void analyzeAssignment(SyntaxTreeNode assign) {
		//Comprobar si el simbolo existe en la tabla de símbolo
		//Validar tipo de la expresion
		//Comprobar que el tipo de la variable coincida con el tipo de la expresion.
		
		//Si la variable no existe mandar error y continuar evaluando la expresion (Obviamente dara error, porque al no 
		// existir la variable en la tabla de simbolos esta nos tendra que devolver un tipo error)
		//Si la variable existe, continuar evaluando la expresion, una vez teniendo la expresión, tenemos que compararla con
		//el tipo del identificador previamente declarado y calcular el tipo de toda la asignación.
	}
	
	private void analyzeIfStatement(SyntaxTreeNode ifStm) {
		
	}
	
	private void analyzeWhileStatement(SyntaxTreeNode whileStm) {
		
	}
	
	private void analyzeDoWhileStatement(SyntaxTreeNode doWhileStm) {
		
	}
	
	private void analyzeForStatement(SyntaxTreeNode funcCall) {
		
	}
	
	private void analyzeReturnExpression(SyntaxTreeNode retExp) {
		
	}
	
	private void analyzeFunctionCallStatement(SyntaxTreeNode funCall) {
		
	}
	
	private void printSemanticError(String message){
		String errors = this.errorLog.getText();
		this.errorLog.setText(errors + " Semantic Error: " + message + "\n");
	}
}
