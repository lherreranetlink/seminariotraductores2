package parser;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import fileutils.FileManager;
import lex.Constants;
import lex.Lex;
import lex.Token;
import rules.Args;
import rules.Block;
import rules.DefFunc;
import rules.DefVar;
import rules.Definition;
import rules.Definition_1;
import rules.Definitions;
import rules.ElsePart;
import rules.FuncBlock;
import rules.LocalVarDef;
import rules.LocalVarDef_1;
import rules.LocalVarsDef;
import rules.ParamList;
import rules.Params;
import rules.Program;
import rules.ReturnValue;
import rules.SimpleToken;
import rules.Statement;
import rules.Statement_1;
import rules.Statement_2;
import rules.Statement_3;
import rules.Statement_4;
import rules.Statements;
import rules.SyntaxTreeNode;
import rules.VarList;

public class Parser {
	
	private Lex lexAnalyxer;
	private FileManager grammar_file_manager;
	private Token currentToken;
	private ParserTableCell[][] parserTable;
	private RulesList rulesList;
	private TokenStack tokenStack;
	
	public static final int SHIFT = 0;
	public static final int REDUCTION = 1;
	public static final int GO_TO_STATE = 2;
	public static final int ERROR = 3;
	
	public Parser() {
		try {
			this.grammar_file_manager = new FileManager("grammar");
			this.lexAnalyxer = new Lex();
			this.rulesList = new RulesList();
			this.tokenStack = new TokenStack();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Parser(String input_filename) throws FileNotFoundException {
		this.lexAnalyxer = new Lex(input_filename);
		this.grammar_file_manager = new FileManager("grammar");
		this.rulesList = new RulesList();
		this.tokenStack = new TokenStack();
	}
	
	public void parse() {
		this.buildGrammar();
		this.buildTransitionTable();
		this.rulesList.insertInitialTransition();
		
		boolean finishParse = true;
		this.tokenStack.initializeStack();
		ParserTableCell currentState;
		
		while (finishParse) {
			this.currentToken = this.lexAnalyxer.getNextToken();
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
	}
	
	private void buildAndPushSyntaxTreeNode(int rule) {
		SyntaxTreeNode newNode;
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
		        statement_3.returnValue = this.tokenStack.pop();
		        this.tokenStack.pop();
		        
		        statement_3.ruleType = RuleType.STATEMENT_3;
		        newNode = statement_3;
				break;
			case RuleType.STATEMENT_4:
				Statement_4 statement_4 = new Statement_4();
				this.tokenStack.pop();
		        statement_4.funcCall = this.tokenStack.pop();

		        statement_4.ruleType = RuleType.STATEMENT_4;
		        newNode = statement_4;
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
			case RuleType.RETURN_VALUE_1:
				ReturnValue returnValue = new ReturnValue();
		        returnValue.expression = this.tokenStack.pop();

		        returnValue.ruleType = RuleType.RETURN_VALUE_1;
		        newNode = returnValue;
				break;
			case RuleType.ARGS_1:
				Args args = new Args();
		        args.argList = this.tokenStack.pop();
		        args.expression = this.tokenStack.pop();

		        args.ruleType = RuleType.ARGS_1;
		        newNode = args;
				break;
		}
		/*
    switch(rule)
    {
    case ARG_LIST_1:
    {
        ArgList* argList = malloc(sizeof(ArgList));
        argList->argList = pop(&stackTop);
        argList->expression = pop(&stackTop);
        pop(&stackTop);

        node->ruleType = ARG_LIST_1;
        node->attr.argList = argList;
    }
    break;
    case TERM:
    {
        Term* term = malloc(sizeof (Term));
        term->funcCall = pop(&stackTop);

        node->ruleType = TERM;
        node->attr.term = term;
    }
    break;
    case TERM_1:
    {
        Term_1* term_1 = malloc(sizeof(Term_1));
        term_1->identifier = pop(&stackTop);

        node->ruleType = TERM_1;
        node->attr.term_1 = term_1;
    }
    break;
    case TERM_2:
    {
        Term_2* term_2 = malloc(sizeof(Term_2));
        term_2->integer = pop(&stackTop);

        node->ruleType = TERM_2;
        node->attr.term_2 = term_2;
    }
    break;
    case TERM_3:
    {
        Term_3* term_3 = malloc(sizeof(Term_3));
        term_3->realNumber = pop(&stackTop);

        node->ruleType = TERM_3;
        node->attr.term_3 = term_3;
    }
    break;
    case TERM_4:
    {
        Term_4* term_4 = malloc(sizeof(Term_4));
        term_4->constChar = pop(&stackTop);

        node->ruleType = TERM_4;
        node->attr.term_4 = term_4;
    }
    break;
    case FUNC_CALL:
    {
        FuncCall* funcCall = malloc(sizeof(FuncCall));
        pop(&stackTop);
        funcCall->args = pop(&stackTop);
        pop(&stackTop);
        funcCall->identifier = pop(&stackTop);

        node->ruleType = FUNC_CALL;
        node->attr.funcCall = funcCall;
    }
    break;
    case BLOCK_STM:
    {
        BlockStm* blockStm = malloc(sizeof(BlockStm));
        blockStm->statement = pop(&stackTop);

        node->ruleType = BLOCK_STM;
        node->attr.blockStm = blockStm;
    }
    break;
    case BLOCK_STM_1:
    {
        BlockStm_1* blockStm_1 = malloc(sizeof(BlockStm_1));
        blockStm_1->block = pop(&stackTop);

        node->ruleType = BLOCK_STM_1;
        node->attr.blockStm_1 = blockStm_1;
    }
    break;
    case EXPRESSION:
    {
        Expression* expression = malloc(sizeof(Expression));
        pop(&stackTop);
        expression->expression = pop(&stackTop);
        pop(&stackTop);

        node->ruleType = EXPRESSION;
        node->attr.expression = expression;
    }
    break;
    case EXPRESSION_1:
    {
        Expression_1* expression_1 = malloc(sizeof(Expression_1));
        expression_1->expression = pop(&stackTop);
        expression_1->additionOperator = pop(&stackTop);

        node->ruleType = EXPRESSION_1;
        node->attr.expression_1 = expression_1;
    }
    break;
    case EXPRESSION_2:
    {
        Expression_2* expression_2 = malloc(sizeof(Expression_2));
        expression_2->expression = pop(&stackTop);
        expression_2->notOperator = pop(&stackTop);

        node->ruleType = EXPRESSION_2;
        node->attr.expression_2 = expression_2;
    }
    break;
    case EXPRESSION_3:
    {
        Expression_3* expression_3 = malloc(sizeof(Expression_3));
        expression_3->expressionRight = pop(&stackTop);
        expression_3->multOperator = pop(&stackTop);
        expression_3->expressionLeft = pop(&stackTop);

        node->ruleType = EXPRESSION_3;
        node->attr.expression_3 = expression_3;
    }
    break;
    case EXPRESSION_4:
    {
        Expression_4* expression_4 = malloc(sizeof(Expression_4));
        expression_4->expressionRight = pop(&stackTop);
        expression_4->additionOperator = pop(&stackTop);
        expression_4->expressionLeft = pop(&stackTop);

        node->ruleType = EXPRESSION_4;
        node->attr.expression_4 = expression_4;
    }
    break;
    case EXPRESSION_5:
    {
        Expression_5* expression_5 = malloc(sizeof(Expression_5));
        expression_5->expressionRight = pop(&stackTop);
        expression_5->relationalOperator = pop(&stackTop);
        expression_5->expressionLeft = pop(&stackTop);

        node->ruleType = EXPRESSION_5;
        node->attr.expression_5 = expression_5;
    }
    break;
    case EXPRESSION_6:
    {
        Expression_6* expression_6 = malloc(sizeof(Expression_6));
        expression_6->expressionRight = pop(&stackTop);
        expression_6->equalsComparisonOperator = pop(&stackTop);
        expression_6->expressionLeft = pop(&stackTop);

        node->ruleType = EXPRESSION_6;
        node->attr.expression_6 = expression_6;
    }
    break;
    case EXPRESSION_7:
    {
        Expression_7* expression_7 = malloc(sizeof(Expression_7));
        expression_7->expressionRight = pop(&stackTop);
        expression_7->andOperator = pop(&stackTop);
        expression_7->expressionLeft = pop(&stackTop);

        node->ruleType = EXPRESSION_7;
        node->attr.expression_7 = expression_7;
    }
    break;
    case EXPRESSION_8:
    {
        Expression_8* expression_8 = malloc(sizeof(Expression_8));
        expression_8->expressionRight = pop(&stackTop);
        expression_8->orOperator = pop(&stackTop);
        expression_8->expressionLeft = pop(&stackTop);

        node->ruleType = EXPRESSION_8;
        node->attr.expression_8 = expression_8;
    }
    break;
    case EXPRESSION_9:
    {
        Expression_9* expression_9 = malloc(sizeof(Expression_9));
        expression_9->term = pop(&stackTop);

        node->ruleType = EXPRESSION_9;
        node->attr.expression_9 = expression_9;
    }
    break;
    case VAR_LIST:
    case DEFINITIONS:
    case PARAMETERS:
    case PARAMS_LIST:
    case LOCAL_VARS_DEF:
    case STATEMENTS:
    case ELSE_PART:
    case RETURN_VALUE:
    case ARGS:
    case ARG_LIST:
        node->ruleType = EPSILON_RULE;
    }

    ParserTableCell goToState;
    Rule* ruleToReduce = getRuleByPosition(&rulesListHeader, rule);
    goToState = parserTable[gettop(&stackTop)->stateToSee][ruleToReduce->index];

    if (goToState.transitionType == ERROR)
        syntaxError();

    node->stateToSee = goToState.goTo;
    push(&stackTop, node);*/
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	
	private void SyntaxError() {
		JOptionPane.showMessageDialog(null, "Syntax Error: Unexpected token: " + this.currentToken.value, "Syntax Error", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

}
