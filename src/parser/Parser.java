package parser;

import java.io.FileNotFoundException;

import lex.Lex;
import lex.Token;

public class Parser {
	
	private Lex lexAnalyxer;
	private Token currentToken;
	
	public Parser() {
		this.lexAnalyxer = new Lex();
	}
	
	public Parser(String filename) throws FileNotFoundException {
		this.lexAnalyxer = new Lex(filename);
	}
	
	public void parse() {
		this.lexAnalyxer.getNextToken();
	}

}
