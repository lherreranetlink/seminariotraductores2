package semantic;

import fileutils.FileManager;
import rules.Program;
import symbol_table.SymbolTable;

public class SemanticAnalyzer {
	private Program program;
	private SymbolTable symbolTable;
	private FileManager errorLog;
	
	public SemanticAnalyzer(Program program, FileManager errorLog) {
		this.errorLog = errorLog;
		this.program = program;
		this.symbolTable = new SymbolTable();
	}
	
	public void analyzeInput() {
		this.program.symbolTableReference = this.symbolTable;
		this.program.errorLog = this.errorLog;
		this.program.scope = "";
		String output = this.program.getType();
		System.out.println(output);
		if (output.equals(SemanticType.VOID_TYPE)) {
			//Continuar con la generacion de código
		}
	}
}
