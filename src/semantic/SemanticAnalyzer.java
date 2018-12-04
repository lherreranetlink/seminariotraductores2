package semantic;

import asm_generator.AsmGenerator;
import fileutils.FileManager;
import rules.Program;
import symbol_table.SymbolTable;

public class SemanticAnalyzer {
	private Program program;
	private SymbolTable symbolTable;
	private FileManager errorLog;
	private AsmGenerator asmGenerator;
	
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
		if (output.equals(SemanticType.VOID_TYPE)) {
			this.asmGenerator = new AsmGenerator(this.program);
			this.asmGenerator.generateCode();
		}
	}
}
