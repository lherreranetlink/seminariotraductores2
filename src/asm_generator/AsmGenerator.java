package asm_generator;

import java.io.IOException;

import fileutils.FileManager;
import rules.Program;

public class AsmGenerator {
	public static String globalVars = "";
	private Program program;
	private FileManager output;
	
	public AsmGenerator(Program program) {
		try {
			this.program = program;
			this.output = new FileManager("output", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generateCode() {
		try {
			String code = "", initialCode;
			code = ((Program) this.program).generateAsm();
			initialCode = this.generateInitialCode();
			initialCode += ".code\n";
			initialCode += "inicio:\n";
			initialCode += "\tcall main\n";
			initialCode += "\tpush 0\n";
			initialCode += "\tcall ExitProcess\n";
			initialCode += code;
			initialCode += "\nend inicio";
			output.append_content(initialCode);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String generateInitialCode() {
		String code = "";
		code += ".386\n";
		code += ".model flat, stdcall\n";
		code += "option casemap:none \n\n";
		code += "include \\masm32\\macros\\macros.asm\n";
		code += "include \\masm32\\include\\masm32.inc\n";
		code += "include \\masm32\\include\\kernel32.inc\n\n";
		code += "includelib \\masm32\\lib\\masm32.lib\n";
		code += "includelib \\masm32\\lib\\kernel32.lib\n\n";
		code += ".data\n\n" + AsmGenerator.globalVars + "\n\n\n";
		
		return code;
	}
}
