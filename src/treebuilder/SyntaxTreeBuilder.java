package treebuilder;

import java.io.IOException;

import fileutils.FileManager;
import parser.RuleType;
import rules.DefVar;
import rules.Definition;
import rules.Definition_1;
import rules.Definitions;
import rules.Program;
import rules.SyntaxTreeNode;

public class SyntaxTreeBuilder {
	
	private Program treeTop;
	private FileManager output_file;
	
	public SyntaxTreeBuilder(Program treeTop) throws IOException {
		this.treeTop = treeTop;
		this.output_file = new FileManager("ouput.xml", true);
	}
	
	public void buildSyntaxTree() throws IOException {
		Program program = this.treeTop;
		this.output_file.append_content("<Program>\n");
		
		if (program.ruleType != RuleType.EPSILON_RULE) {
			this.printDefinitions(program.definitions);
		}
		
		this.output_file.append_content("</Program>\n");
		this.output_file.close();
	}
	
	private void printDefinitions(SyntaxTreeNode definitions) throws IOException {
		this.output_file.append_content("<Definitions>\n");
		
		this.printDefinition(((Definitions) definitions).definition);
		if(((Definitions) definitions).moreDefinitions.ruleType != RuleType.EPSILON_RULE) {
			this.printDefinition(((Definitions) definitions).moreDefinitions);
		}
			
		this.output_file.append_content("</Definitions>\n");
	}
	
	private void printDefinition(SyntaxTreeNode definition) throws IOException {
		this.output_file.append_content("<Definition>\n");
		
		int ruleType = definition.ruleType;
		switch(ruleType) {
			case RuleType.DEFINITION:
				this.printVarDefinition(((Definition) definition).defVar);
				break;
			case RuleType.DEFINITION_1:
				this.printFunctionDefinition( ((Definition_1) definition).defFunc );
				
		}
		this.output_file.append_content("</Definition>\n");
	}
	
	private void printVarDefinition(SyntaxTreeNode defVar) throws IOException {
		this.output_file.append_content("<DefVar>\n");
		this.output_file.append_content("Date type: " + ((DefVar) defVar).dataType + "\n");
		this.output_file.append_content("Identifier: " + ((DefVar) defVar).identifier + "\n");
		
		
		this.output_file.append_content("</DefVar>\n");
	}
	
	private void printFunctionDefinition(SyntaxTreeNode defFunc) throws IOException {
		this.output_file.append_content("<DefFunc>\n");
		
		this.output_file.append_content("</DefFunc>\n");
	}
	
	/*
void printVarDefinition(DefVar* defVar)
{
    fprintf(stdout, "<DefVar>\n");
    fprintf(stdout, "Data Type: %s\n", defVar->dataType->attr.simpleToken->symbol);
    fprintf(stdout, "Identifier: %s\n", defVar->identifier->attr.simpleToken->symbol);

    if (defVar->varList->ruleType != EPSILON_RULE)
        printVarList(defVar->varList->attr.varList);

    fprintf(stdout, "</DefVar>\n");
}

void printFunctionDefinition(DefFunc* defFunc)
{
    fprintf(stdout, "<DefFunc>\n");
    fprintf(stdout, "Data Type: %s\n", defFunc->dataType->attr.simpleToken->symbol);
    fprintf(stdout, "Identifier: %s\n", defFunc->identifier->attr.simpleToken->symbol);

    if (defFunc->params->ruleType != EPSILON_RULE)
        printParams(defFunc->params->attr.params);

    if (defFunc->blockFunc->ruleType != EPSILON_RULE)
        printfFunctionBlok(defFunc->blockFunc->attr.funcBlock);

    fprintf(stdout, "</DefFunc>\n");
}

void printVarList(VarList* varList)
{
    fprintf(stdout, "<VarList>\n");
    fprintf(stdout, "Identifier: %s\n", varList->identifier->attr.simpleToken->symbol);

    if (varList->nextVar->ruleType != EPSILON_RULE)
        printVarList(varList->nextVar->attr.varList);

    fprintf(stdout, "</VarList>\n");
}

void printParams(Params* params)
{
    fprintf(stdout, "<Params>\n");
    fprintf(stdout, "Data Type: %s\n", params->dataType->attr.simpleToken->symbol);
    fprintf(stdout, "Identifier: %s\n", params->identifier->attr.simpleToken->symbol);

    if (params->paramList->ruleType != EPSILON_RULE)
        printParamList(params->paramList->attr.paramList);

    fprintf(stdout, "</Params>\n");
}

void printfFunctionBlok(FuncBlock* funcBlock)
{
    fprintf(stdout, "<BloqFunc>\n");

    if(funcBlock->localVarsDef->ruleType != EPSILON_RULE)
        printLocalDefinitions(funcBlock->localVarsDef->attr.localVarsDef);

    fprintf(stdout, "</BloqFunc>\n");
}

void printParamList(ParamList* paramList)
{
    fprintf(stdout, "<ParamList>\n");
    fprintf(stdout, "Data Type: %s\n", paramList->dataType->attr.simpleToken->symbol);
    fprintf(stdout, "Identifier: %s\n", paramList->identifier->attr.simpleToken->symbol);

    if (paramList->paramList->ruleType != EPSILON_RULE)
        printParamList(paramList->paramList->attr.paramList);

    fprintf(stdout, "</ParamList>\n");
}

void printLocalDefinitions(LocalVarsDef* localVarsDef)
{
    fprintf(stdout, "<DefLocales>\n");

    printLocalVarsAndStatements(localVarsDef->localVarDef);
    if (localVarsDef->localVarsDef->ruleType != EPSILON_RULE)
        printLocalDefinitions(localVarsDef->localVarsDef->attr.localVarsDef);

    fprintf(stdout, "</DefLocales>\n");
}

void printLocalVarsAndStatements(GenericSyntaxTreeNode* localDefinition)
{
    fprintf(stdout, "<DefLocal>\n");

    int ruleType = localDefinition->ruleType;
    switch (ruleType)
    {
    case LOCAL_VAR_DEF:
        printVarDefinition(localDefinition->attr.localVarDef->defVar->attr.defVar);
        break;
    case LOCAL_VAR_DEF_1:
        printSingleStatement(localDefinition->attr.localVarDef_1->statement);
    }

    fprintf(stdout, "</DefLocal>\n");
}

void printSingleStatement(GenericSyntaxTreeNode* singleStm)
{
    fprintf(stdout, "<Sentencia>\n");

    int ruleType = singleStm->ruleType;
    switch(ruleType)
    {
    case STATEMENT:
        printAssignmentStatement(singleStm->attr.statement);
        break;
    case STATEMENT_1:
        printIfStatement(singleStm->attr.statement_1);
        break;
    case STATEMENT_2:
        printWhileStatement(singleStm->attr.statement_2);
        break;
    case STATEMENT_3:
        printReturnStatement(singleStm->attr.statement_3);
        break;
    case STATEMENT_4:
        printFunctionCallStatement(singleStm->attr.statement_4);
    }

    fprintf(stdout, "</Sentencia>\n");
}

void printAssignmentStatement(Statement* assignmentStm)
{
    fprintf(stdout, "<Asignacion>\n");

    fprintf(stdout, "Identifier: %s\n", assignmentStm->identifier->attr.simpleToken->symbol);
    fprintf(stdout, "Operator: %s\n", assignmentStm->equalsSign->attr.simpleToken->symbol);

    printExpression(assignmentStm->expression);

    fprintf(stdout, "</Asignacion>\n");
}

void printIfStatement(Statement_1* ifStm)
{
    fprintf(stdout, "<SentenciaIF>\n");

    printExpression(ifStm->expression);
    printBlockStatement(ifStm->partIf);

    if(ifStm->partElse->ruleType != EPSILON_RULE)
        printElsePart(ifStm->partElse->attr.elsePart);

    fprintf(stdout, "</SentenciaIF>\n");
}

void printWhileStatement(Statement_2* whileStm)
{
    fprintf(stdout, "<SentenciaWhile>\n");

    printExpression(whileStm->expression);
    printBlock(whileStm->block->attr.block);

    fprintf(stdout, "</SentenciaWhile>\n");
}

void printReturnStatement(Statement_3* returnStm)
{
    fprintf(stdout, "<SentenciaReturn>\n");
    fprintf(stdout, "<ValorRegresa>\n");

    if (returnStm->returnValue->ruleType != EPSILON_RULE)
        printExpression(returnStm->returnValue->attr.returnValue->expression);

    fprintf(stdout, "</ValorRegresa>\n");
    fprintf(stdout, "</SentenciaReturn>\n");
}

void printFunctionCallStatement(Statement_4* funcCallStm)
{
    fprintf(stdout, "<LlamadaFuncion>\n");

    fprintf(stdout, "Identifier: %s\n", funcCallStm->funcCall->attr.funcCall->identifier->attr.simpleToken->symbol);
    if (funcCallStm->funcCall->attr.funcCall->args->ruleType != EPSILON_RULE)
        printFunctionCallArgs(funcCallStm->funcCall->attr.funcCall->args->attr.args);

    fprintf(stdout, "</LlamadaFuncion>\n");
}

void printExpression(GenericSyntaxTreeNode* expression)
{
    fprintf(stdout, "<Expression>\n");

    int ruleType = expression->ruleType;
    switch (ruleType)
    {
    case EXPRESSION:
        fprintf(stdout, "(\n");
        printExpression(expression->attr.expression->expression);
        fprintf(stdout, ")\n");
        break;
    case EXPRESSION_1:
        fprintf(stdout, "Sign: %s\n", expression->attr.expression_1->additionOperator->attr.simpleToken->symbol);
        printExpression(expression->attr.expression_1->expression);
        break;
    case EXPRESSION_2:
        fprintf(stdout, "Sign: %s\n", expression->attr.expression_2->notOperator->attr.simpleToken->symbol);
        printExpression(expression->attr.expression_2->expression);
        break;
    case EXPRESSION_3:
        printExpression(expression->attr.expression_3->expressionLeft);
        fprintf(stdout, "Sign: %s\n", expression->attr.expression_3->multOperator->attr.simpleToken->symbol);
        printExpression(expression->attr.expression_3->expressionRight);
        break;
    case EXPRESSION_4:
        printExpression(expression->attr.expression_4->expressionLeft);
        fprintf(stdout, "Sign: %s\n", expression->attr.expression_4->additionOperator->attr.simpleToken->symbol);
        printExpression(expression->attr.expression_4->expressionRight);
        break;
    case EXPRESSION_5:
        printExpression(expression->attr.expression_5->expressionLeft);
        fprintf(stdout, "Sign: %s\n", expression->attr.expression_5->relationalOperator->attr.simpleToken->symbol);
        printExpression(expression->attr.expression_5->expressionRight);
        break;
    case EXPRESSION_6:
        printExpression(expression->attr.expression_6->expressionLeft);
        fprintf(stdout, "Sign: %s\n", expression->attr.expression_6->equalsComparisonOperator->attr.simpleToken->symbol);
        printExpression(expression->attr.expression_6->expressionRight);
        break;
    case EXPRESSION_7:
        printExpression(expression->attr.expression_7->expressionLeft);
        fprintf(stdout, "Sign: %s\n", expression->attr.expression_7->andOperator->attr.simpleToken->symbol);
        printExpression(expression->attr.expression_7->expressionRight);
        break;
    case EXPRESSION_8:
        printExpression(expression->attr.expression_8->expressionLeft);
        fprintf(stdout, "Sign: %s\n", expression->attr.expression_8->orOperator->attr.simpleToken->symbol);
        printExpression(expression->attr.expression_8->expressionRight);
        break;
    case EXPRESSION_9:
        printTerm(expression->attr.expression_9->term);
    }
    fprintf(stdout, "</Expression>\n");
}

void printTerm(GenericSyntaxTreeNode* term)
{
    fprintf(stdout, "<Term>\n");

    int ruleType = term->ruleType;
    switch(ruleType)
    {
    case TERM:
        printFunctionCallTerm(term->attr.term->funcCall->attr.funcCall);
        break;
    case TERM_1:
        fprintf(stdout, "%s\n", term->attr.term_1->identifier->attr.simpleToken->symbol);
        break;
    case TERM_2:
        fprintf(stdout, "%s\n", term->attr.term_2->integer->attr.simpleToken->symbol);
        break;
    case TERM_3:
        fprintf(stdout, "%s\n", term->attr.term_3->realNumber->attr.simpleToken->symbol);
        break;
    case TERM_4:
        fprintf(stdout, "%s\n", term->attr.term_4->constChar->attr.simpleToken->symbol);
    }

    fprintf(stdout, "</Term>\n");
}

void printFunctionCallTerm(FuncCall* funcCall)
{
    fprintf(stdout, "<LlamadaFuncion>\n");

    fprintf(stdout, "Identifier: %s\n", funcCall->identifier->attr.simpleToken->symbol);
    if (funcCall->args->ruleType != EPSILON_RULE)
        printFunctionCallArgs(funcCall->args->attr.args);

    fprintf(stdout, "</LlamadaFuncion>\n");
}

void printBlockStatement(GenericSyntaxTreeNode* blockStm)
{
    fprintf(stdout, "<SentenciaBloque>\n");

    int ruleType = blockStm->ruleType;
    switch(ruleType)
    {
    case BLOCK_STM:
        printSingleStatement(blockStm->attr.blockStm->statement);
        break;
    case BLOCK_STM_1:
        printBlock(blockStm->attr.blockStm_1->block->attr.block);
    }

    fprintf(stdout, "</SentenciaBloque>\n");
}

void printBlock(Block* block)
{
    fprintf(stdout, "<Block>\n");

    printMultipleStatements(block->statements->attr.statements);

    fprintf(stdout, "</Block>\n");
}

void printMultipleStatements(Statements* multipleStm)
{
    fprintf(stdout, "<Sentencias>\n");

    printSingleStatement(multipleStm->statement);
    if (multipleStm->statements->ruleType != EPSILON_RULE)
        printMultipleStatements(multipleStm->statements->attr.statements);

    fprintf(stdout, "</Sentencias>\n");
}

void printElsePart(ElsePart* elsePart)
{
    fprintf(stdout, "<ElsePart>\n");

    printBlockStatement(elsePart->blockStm);

    fprintf(stdout, "</ElsePart>\n");
}

void printFunctionCallArgs(Args* args)
{
    fprintf(stdout, "<Args>\n");

    printExpression(args->expression);
    if (args->argList->ruleType != EPSILON_RULE)
        printFunctionCallArgList(args->argList->attr.argList);

    fprintf(stdout, "</Args>\n");
}

void printFunctionCallArgList(ArgList* argList)
{
    fprintf(stdout, "<ListaArgumentos>\n");

    printExpression(argList->expression);
    if (argList->argList->ruleType != EPSILON_RULE)
        printFunctionCallArgList(argList->argList->attr.argList);

    fprintf(stdout, "</ListaArgumentos>\n");
}*/
}
