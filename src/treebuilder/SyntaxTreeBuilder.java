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
	
}
