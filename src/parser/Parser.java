package parser;

import java.io.IOException;

import javax.swing.JTextArea;

import fileutils.FileManager;
import lex.Constants;
import lex.Lex;
import lex.Token;
import rules.ArgList;
import rules.Args;
import rules.Block;
import rules.BlockStm;
import rules.BlockStm_1;
import rules.DefFunc;
import rules.DefVar;
import rules.Definition;
import rules.Definition_1;
import rules.Definitions;
import rules.ElsePart;
import rules.Expression;
import rules.Expression_1;
import rules.Expression_2;
import rules.Expression_3;
import rules.Expression_4;
import rules.Expression_5;
import rules.Expression_6;
import rules.Expression_7;
import rules.Expression_8;
import rules.Expression_9;
import rules.FuncBlock;
import rules.FuncCall;
import rules.LocalVarDef;
import rules.LocalVarDef_1;
import rules.LocalVarsDef;
import rules.ParamList;
import rules.Params;
import rules.Program;
import rules.SimpleToken;
import rules.Statement;
import rules.Statement_1;
import rules.Statement_2;
import rules.Statement_3;
import rules.Statement_4;
import rules.Statement_5;
import rules.Statement_6;
import rules.Statements;
import rules.SyntaxTreeNode;
import rules.Term;
import rules.Term_1;
import rules.Term_2;
import rules.VarList;
import semantic.SemanticAnalyzer;

public class Parser {
	
	private Lex lexAnalyxer;
	private SemanticAnalyzer semanticAnalyzer;
	private JTextArea errorLog;
	private FileManager grammar_file_manager;
	private Token currentToken;
	private ParserTableCell[][] parserTable;
	private RulesList rulesList;
	private TokenStack tokenStack;
	public boolean error;
	
	public static final int SHIFT = 0;
	public static final int REDUCTION = 1;
	public static final int GO_TO_STATE = 2;
	public static final int ERROR = 3;
	
	public Parser(JTextArea errorLog) {
		try {
			this.errorLog = errorLog;
			this.grammar_file_manager = new FileManager("grammar");
			this.lexAnalyxer = new Lex(this.errorLog);
			this.rulesList = new RulesList();
			this.tokenStack = new TokenStack();
			this.error = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Parser(String input_filename, JTextArea errorLog) throws IOException {
		this.errorLog = errorLog;
		this.lexAnalyxer = new Lex(input_filename, this.errorLog);
		this.grammar_file_manager = new FileManager("grammar");
		this.rulesList = new RulesList();
		this.tokenStack = new TokenStack();
		this.error = false;
	}
	
	public void parse() throws IOException {
		this.buildGrammar();
		this.buildTransitionTable();
		this.rulesList.insertInitialTransition();
		
		boolean finishParse = false;
		this.tokenStack.initializeStack();
		ParserTableCell currentState;
		
		while (!finishParse) {
			this.currentToken = this.lexAnalyxer.getNextToken();
			
			if (this.lexAnalyxer.error) {
				finishParse = true;
				break;
			}
			
			currentState = this.parserTable[this.tokenStack.gettop().stateToSee][currentToken.key];
			int transitionType = currentState.transitionType;
			switch (transitionType) {
			case Parser.SHIFT:
				SimpleToken simpleToken = new SimpleToken(this.currentToken);
				simpleToken.ruleType = RuleType.SIMPLE_TOKEN;
				simpleToken.stateToSee = currentState.goTo;
				this.tokenStack.push(simpleToken);
				break;
			case Parser.REDUCTION:
				this.buildAndPushSyntaxTreeNode(currentState.goTo);
				this.lexAnalyxer.ungetToken(currentToken);
				
				if (this.tokenStack.gettop().stateToSee == RuleType.PROGRAM && this.currentToken.key == Constants.EOF_SIGN) 
					finishParse = true;
				
				break;
			case Parser.ERROR:
				this.SyntaxError();
			}
		}
		
		this.lexAnalyxer.close_input_file();
		this.semanticAnalyzer = new SemanticAnalyzer((Program) this.tokenStack.gettop(), this.errorLog);
		this.semanticAnalyzer.analyzeInput();
		
	}
	
	private void buildAndPushSyntaxTreeNode(int rule) {
		SyntaxTreeNode newNode = new SyntaxTreeNode();
		switch(rule) {
			case RuleType.PROGRAM:
				Program program = new Program();
				program.definitions = this.tokenStack.pop();
				program.ruleType = RuleType.PROGRAM;
				newNode = program;
				break;
			case RuleType.DEFINITIONS_1:
				Definitions definitions = new Definitions();
		        definitions.moreDefinitions = this.tokenStack.pop();
		        definitions.definition = this.tokenStack.pop();
		        definitions.ruleType = RuleType.DEFINITIONS_1;
		        newNode = definitions;
				break;
			case RuleType.DEFINITION:
				Definition definition = new Definition();
				definition.defVar = this.tokenStack.pop();
				definition.ruleType = RuleType.DEFINITION;
				newNode = definition;
				break;
			case RuleType.DEFINITION_1:
				Definition_1 definition_1 = new Definition_1();
		        definition_1.defFunc = this.tokenStack.pop();
		        definition_1.ruleType = RuleType.DEFINITION_1;
		        newNode = definition_1;
				break;
			case RuleType.DEF_VAR:
				this.tokenStack.pop();
				DefVar defVar = new DefVar();
				defVar.varList = this.tokenStack.pop();
				defVar.identifier = this.tokenStack.pop();
				defVar.dataType = this.tokenStack.pop();
				defVar.ruleType = RuleType.DEF_VAR;
				newNode = defVar;
				break;
			case RuleType.VAR_LIST_1:
				VarList varList = new VarList();
				varList.nextVar = this.tokenStack.pop();
				varList.identifier = this.tokenStack.pop();
				this.tokenStack.pop();

		        varList.ruleType = RuleType.VAR_LIST_1;
		        newNode = varList;
				break;
			case RuleType.DEF_FUNC:
				DefFunc defFunc = new DefFunc();
				defFunc.blockFunc = this.tokenStack.pop();
				this.tokenStack.pop();
				defFunc.params = this.tokenStack.pop();
				this.tokenStack.pop();
				defFunc.identifier = this.tokenStack.pop();
				defFunc.dataType = this.tokenStack.pop();
				
				defFunc.ruleType = RuleType.DEF_FUNC;
				newNode = defFunc;
				break;
			case RuleType.PARAMETERS_1:
				Params params = new Params();
				params.paramList = this.tokenStack.pop();
				params.identifier = this.tokenStack.pop();
				params.dataType = this.tokenStack.pop();
				
				params.ruleType = RuleType.PARAMETERS_1;
				newNode = params;
				break;
			case RuleType.PARAMS_LIST_1:
				ParamList paramList = new ParamList();
		        paramList.paramList = this.tokenStack.pop();
		        paramList.identifier = this.tokenStack.pop();
		        paramList.dataType = this.tokenStack.pop();
		        this.tokenStack.pop();

		        paramList.ruleType = RuleType.PARAMS_LIST_1;
		        newNode = paramList;
				break;
			case RuleType.FUNC_BLOCK:
				FuncBlock funcBlock = new FuncBlock();
				this.tokenStack.pop();
		        funcBlock.localVarsDef = this.tokenStack.pop();
		        this.tokenStack.pop();

		        funcBlock.ruleType = RuleType.FUNC_BLOCK;
		        newNode = funcBlock;
				break;
			case RuleType.LOCAL_VARS_DEF_1:
				LocalVarsDef localVarsDef = new LocalVarsDef();
		        localVarsDef.localVarsDef = this.tokenStack.pop();
		        localVarsDef.localVarDef = this.tokenStack.pop();

		        localVarsDef.ruleType = RuleType.LOCAL_VARS_DEF_1;
		        newNode = localVarsDef;
				break;
			case RuleType.LOCAL_VAR_DEF:
				LocalVarDef localVarDef = new LocalVarDef();
		        localVarDef.defVar = this.tokenStack.pop();

		        localVarDef.ruleType = RuleType.LOCAL_VAR_DEF;
		        newNode = localVarDef;
				break;
			case RuleType.LOCAL_VAR_DEF_1:
				LocalVarDef_1 localVarDef_1 = new LocalVarDef_1();
		        localVarDef_1.statement = this.tokenStack.pop();
		        
		        localVarDef_1.ruleType = RuleType.LOCAL_VAR_DEF_1;
		        newNode = localVarDef_1;
				break;
			case RuleType.STATEMENTS_1:
				Statements statements = new Statements();
		        statements.statements = this.tokenStack.pop();
		        statements.statement = this.tokenStack.pop();

		        statements.ruleType = RuleType.STATEMENTS_1;
		        newNode = statements; 
				break;
			case RuleType.STATEMENT:
				Statement statement = new Statement();
				this.tokenStack.pop();
		        statement.expression = this.tokenStack.pop();
		        statement.equalsSign = this.tokenStack.pop();
		        statement.identifier = this.tokenStack.pop();

		        statement.ruleType = RuleType.STATEMENT;
		        newNode = statement;
				break;
			case RuleType.STATEMENT_1:
				Statement_1 statement_1 = new Statement_1();
		        statement_1.elsePart = this.tokenStack.pop();
		        statement_1.ifPart = this.tokenStack.pop();
		        this.tokenStack.pop();
		        statement_1.expression = this.tokenStack.pop();
		        this.tokenStack.pop();
		        this.tokenStack.pop();

		        statement_1.ruleType = RuleType.STATEMENT_1;
		        newNode = statement_1;
				break;
			case RuleType.STATEMENT_2:
				Statement_2 statement_2 = new Statement_2();
		        statement_2.block = this.tokenStack.pop();
		        this.tokenStack.pop();
		        statement_2.expression = this.tokenStack.pop();
		        this.tokenStack.pop();
		        this.tokenStack.pop();

		        statement_2.ruleType = RuleType.STATEMENT_2;
		        newNode = statement_2; 
				break;
			case RuleType.STATEMENT_3:
				Statement_3 statement_3 = new Statement_3();
				this.tokenStack.pop();
				this.tokenStack.pop();
				statement_3.expression = this.tokenStack.pop();
				this.tokenStack.pop();
				this.tokenStack.pop();
				statement_3.block = this.tokenStack.pop();
				this.tokenStack.pop();
				
				statement_3.ruleType = RuleType.STATEMENT_3;
				newNode = statement_3;
				break;
			case RuleType.STATEMENT_4:
				Statement_4 statement_4 = new Statement_4();
				statement_4.blockStatement = this.tokenStack.pop();
				statement_4.iterationExpression = this.tokenStack.pop();
				this.tokenStack.pop();
				statement_4.conditionExpression = this.tokenStack.pop();
				this.tokenStack.pop();
				statement_4.initializationExpression = this.tokenStack.pop();
				this.tokenStack.pop();
				statement_4.identifier = this.tokenStack.pop();
				this.tokenStack.pop();
				
				statement_4.ruleType = RuleType.STATEMENT_4;
		        newNode = statement_4;
				break;
			case RuleType.STATEMENT_5:
				this.tokenStack.pop();
				Statement_5 statement_5 = new Statement_5();
				statement_5.expressionToReturn = this.tokenStack.pop();
				this.tokenStack.pop();
				
				statement_5.ruleType = RuleType.STATEMENT_5;
				newNode = statement_5;
				break;
			case RuleType.STATEMENT_6:
				this.tokenStack.pop();
				Statement_6 statement_6 = new Statement_6();
				statement_6.funcCall = this.tokenStack.pop();
				
				statement_6.ruleType = RuleType.STATEMENT_6;
				newNode = statement_6;
				break;
			case RuleType.ELSE_PART_1:
				ElsePart elsePart = new ElsePart();
		        elsePart.blockStm = this.tokenStack.pop();
		        this.tokenStack.pop();

		        elsePart.ruleType = RuleType.ELSE_PART_1;
		        newNode = elsePart;
				break;
			case RuleType.BLOCK:
				Block block = new Block();
				this.tokenStack.pop();
		        block.statements = this.tokenStack.pop();
		        this.tokenStack.pop();

		        block.ruleType = RuleType.BLOCK;
		        newNode = block;
				break;
			case RuleType.ARGS_1:
				Args args = new Args();
		        args.argList = this.tokenStack.pop();
		        args.expression = this.tokenStack.pop();

		        args.ruleType = RuleType.ARGS_1;
		        newNode = args;
				break;
			case RuleType.ARG_LIST_1:
				ArgList argList = new ArgList();
		        argList.argList = this.tokenStack.pop();
		        argList.expression = this.tokenStack.pop();
		        this.tokenStack.pop();
		        
		        argList.ruleType = RuleType.ARG_LIST_1;
		        newNode = argList;
				break;
			case RuleType.TERM:
				Term term = new Term();
		        term.funcCall = this.tokenStack.pop();
		        
		        term.ruleType = RuleType.TERM;
		        newNode = term;
				break;
			case RuleType.TERM_1:
				Term_1 term_1 = new Term_1();
		        term_1.identifier = this.tokenStack.pop();
		        
		        term_1.ruleType = RuleType.TERM_1;
		        newNode = term_1;
				break;
			case RuleType.TERM_2:
				Term_2 term_2 = new Term_2();
		        term_2.constant = this.tokenStack.pop();

		        term_2.ruleType = RuleType.TERM_2;
		        newNode = term_2;
				break;
			case RuleType.FUNC_CALL:
				FuncCall funcCall = new FuncCall();
				this.tokenStack.pop();
		        funcCall.args = this.tokenStack.pop();
		        this.tokenStack.pop();
		        funcCall.identifier = this.tokenStack.pop();
		        
		        funcCall.ruleType = RuleType.FUNC_CALL;
		        newNode = funcCall;
				break;
			case RuleType.BLOCK_STM:
				BlockStm blockStm = new BlockStm();
		        blockStm.statement = this.tokenStack.pop();

		        blockStm.ruleType = RuleType.BLOCK_STM;
		        newNode = blockStm;
				break;
			case RuleType.BLOCK_STM_1:
				BlockStm_1 blockStm_1 = new BlockStm_1();
		        blockStm_1.block = this.tokenStack.pop();
		        
		        blockStm_1.ruleType = RuleType.BLOCK_STM_1;
		        newNode = blockStm_1;
				break;
			case RuleType.EXPRESSION:
				Expression expression = new Expression();
				this.tokenStack.pop();
		        expression.expression = this.tokenStack.pop();
		        this.tokenStack.pop();
		        
		        expression.ruleType = RuleType.EXPRESSION;
		        newNode = expression;
				break;
			case RuleType.EXPRESSION_1:
				Expression_1 expression_1 = new Expression_1();
		        expression_1.expression = this.tokenStack.pop();
		        expression_1.additionOperator = this.tokenStack.pop();

		        expression_1.ruleType = RuleType.EXPRESSION_1;
		        newNode = expression_1;
				break;
			case RuleType.EXPRESSION_2:
				Expression_2 expression_2 = new Expression_2();
		        expression_2.expression = this.tokenStack.pop();
		        expression_2.notOperator = this.tokenStack.pop();

		        expression_2.ruleType = RuleType.EXPRESSION_2;
		        newNode = expression_2;
				break;
			case RuleType.EXPRESSION_3:
				Expression_3 expression_3 = new Expression_3();
		        expression_3.expressionRight = this.tokenStack.pop();
		        expression_3.multOperator = this.tokenStack.pop();
		        expression_3.expressionLeft = this.tokenStack.pop();

		        expression_3.ruleType = RuleType.EXPRESSION_3;
		        newNode = expression_3;
				break;
			case RuleType.EXPRESSION_4:
				Expression_4 expression_4 = new Expression_4();
		        expression_4.expressionRight = this.tokenStack.pop();
		        expression_4.additionOperator = this.tokenStack.pop();
		        expression_4.expressionLeft = this.tokenStack.pop();

		        expression_4.ruleType = RuleType.EXPRESSION_4;
		        newNode = expression_4;
				break;
			case RuleType.EXPRESSION_5:
				Expression_5 expression_5 = new Expression_5();
		        expression_5.expressionRight = this.tokenStack.pop();
		        expression_5.relationalOperator = this.tokenStack.pop();
		        expression_5.expressionLeft = this.tokenStack.pop();
		        
		        expression_5.ruleType = RuleType.EXPRESSION_5;
		        newNode = expression_5;
				break;
			case RuleType.EXPRESSION_6:
				Expression_6 expression_6 = new Expression_6();
		        expression_6.expressionRight = this.tokenStack.pop();
		        expression_6.equalsComparisonOperator = this.tokenStack.pop();
		        expression_6.expressionLeft = this.tokenStack.pop();
		        
		        expression_6.ruleType = RuleType.EXPRESSION_6;
		        newNode = expression_6;
				break;
			case RuleType.EXPRESSION_7:
				Expression_7 expression_7 = new Expression_7();
		        expression_7.expressionRight = this.tokenStack.pop();
		        expression_7.andOperator = this.tokenStack.pop();
		        expression_7.expressionLeft = this.tokenStack.pop();
		        
		        expression_7.ruleType = RuleType.EXPRESSION_7;
		        newNode = expression_7;
				break;
			case RuleType.EXPRESSION_8:
				Expression_8 expression_8 = new Expression_8();
		        expression_8.expressionRight = this.tokenStack.pop();
		        expression_8.orOperator = this.tokenStack.pop();
		        expression_8.expressionLeft = this.tokenStack.pop();
		        
		        expression_8.ruleType = RuleType.EXPRESSION_8;
		        newNode = expression_8;
				break;
			case RuleType.EXPRESSION_9:
				Expression_9 expression_9 = new Expression_9();
				expression_9.term = this.tokenStack.pop();

				expression_9.ruleType = RuleType.EXPRESSION_9;
				newNode = expression_9;
				break;
			case RuleType.VAR_LIST:
		    case RuleType.DEFINITIONS:
		    case RuleType.PARAMETERS:
		    case RuleType.PARAMS_LIST:
		    case RuleType.LOCAL_VARS_DEF:
		    case RuleType.STATEMENTS:
		    case RuleType.ELSE_PART:
		    case RuleType.ARGS:
		    case RuleType.ARG_LIST:
		    	newNode.ruleType = RuleType.EPSILON_RULE;
		}
		
		Rule ruleToReduce = this.rulesList.getRuleByPosition(rule);
		ParserTableCell goToState = this.parserTable[this.tokenStack.gettop().stateToSee][ruleToReduce.index];
		
		if (goToState.transitionType == Parser.ERROR) {
			this.SyntaxError();
		}
		
		newNode.stateToSee = goToState.goTo;
		this.tokenStack.push(newNode);
	}
	
	private void buildGrammar() {
		try {
			Rule newRule;
			String line;
			for (line = this.grammar_file_manager.get_line(); !line.equals("%"); line = this.grammar_file_manager.get_line()) {
				String parts[] = line.split("\t");
				newRule = new Rule(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), parts[2]);
				this.rulesList.insertRule(newRule);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void buildTransitionTable() {
	    try {
	    	int rows = 0, columns = 0;
		    String line;
			for (line = this.grammar_file_manager.get_line(); !line.equals("%"); line = this.grammar_file_manager.get_line()) {
				String parts[] = line.split("\t");
		        rows = Integer.parseInt(parts[0]);
		        columns = Integer.parseInt(parts[1]);
			}
			
			parserTable = new ParserTableCell[rows][columns];
			for (int i = 0; i < rows; i++) {
				String row = this.grammar_file_manager.get_line();
		        String parts[] = row.split("\t");
				for (int j = 0; j < columns; j++) {
					ParserTableCell newComponent;
					if (parts[j].equals("-0")) {
						newComponent = new ParserTableCell(Parser.REDUCTION, 0);
					} else {
						int cellValue = Integer.parseInt(parts[j]);
						if (cellValue > 0) {
							newComponent = new ParserTableCell((j > Constants.EOF_SIGN)
							        ? Parser.GO_TO_STATE
							        : Parser.SHIFT, cellValue);
						} else if (cellValue < 0) 
							newComponent = new ParserTableCell(Parser.REDUCTION, (cellValue * -1));
						  else {
							newComponent = new ParserTableCell(Parser.ERROR, 0);
						}
					}
					parserTable[i][j] = newComponent;
				}
			}
			this.grammar_file_manager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	
	private void SyntaxError() {
		String errors = this.errorLog.getText();
		this.errorLog.setText(errors + " Syntax Error: Unexpected token: " + this.currentToken.value + "\n");
		this.error = true;
		System.exit(1);
	}

}
