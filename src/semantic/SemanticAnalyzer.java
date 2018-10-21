package semantic;

import parser.RuleType;
import rules.DefFunc;
import rules.Definition;
import rules.Definition_1;
import rules.Definitions;
import rules.Program;
import rules.SimpleToken;
import rules.SyntaxTreeNode;
import symbol_table.SymbolTable;
import symbol_table.TableElement;

public class SemanticAnalyzer {
	private SyntaxTreeNode treeRoot;
	private String currentScope;
	private SymbolTable symbolTable;
	
	SemanticAnalyzer(){}
	
	SemanticAnalyzer(SyntaxTreeNode treeRoot) {
		this.treeRoot = treeRoot;
		this.currentScope = "";
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
				break;
		}
	}
	
	private void analyzeFunctionDefinition(SyntaxTreeNode definition_1) {
		DefFunc defFunc = (DefFunc) ((Definition_1) definition_1).defFunc;
		String dataType = ((SimpleToken) defFunc.dataType).token.value;
		String identifier = ((SimpleToken) defFunc.identifier).token.value;
		//this.currentScope = ((SimpleToken) defFunc.identifier).token.value;
		if (!this.symbolTable.existsSymbol(identifier, this.currentScope)) {
			TableElement newNode = new TableElement(identifier, dataType, this.currentScope);
			this.currentScope = identifier;
			//Analyze Params
			//Build params pattern
		} else {
			//Error Manager
		}
	}
	
	private void analyzeVarDefinition(SyntaxTreeNode definition) {
		
	}
}
