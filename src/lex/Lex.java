package lex;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import fileutils.FileManager;

public class Lex {
	
	private FileManager file_manager;
	private int currentState;

	public Lex(String filename) throws FileNotFoundException {
		this.file_manager = new FileManager(filename);
	}
	
	public Lex() {
		this.file_manager = new FileManager();
	}
	
	public Token getTokenFromFile() {
		Token newToken = null;
		try {
			char c;
			this.currentState = States.INITIAL_STATE;
			String token = "";
			boolean continueLoop = true;
			while (continueLoop) {
				
		        if (this.currentState == States.INITIAL_STATE)
		            for (c = file_manager.get_byte(); Character.isWhitespace(c) && (int) c != FileManager.EOF_MARK; c = file_manager.get_byte());
		        else
		            c = file_manager.get_byte();
		        
				switch(this.currentState) {
					case States.INITIAL_STATE:
						token += c;
						if (Character.isDigit(c)) {
							this.currentState = Constants.INTEGER;
							break;
						}
						if (Character.isLetter(c) || c == '_') {
							currentState = Constants.IDENTIFIER;
							break;
						}
						this.setStateByToken(c);
						break;
					case Constants.ADD_OPERATOR:
					case Constants.MULT_OPERATOR:
					case Constants.LEFT_PARENTHESIS:
					case Constants.RIGHT_PARENTHESIS:
					case Constants.LEFT_BRACE:
					case Constants.RIGHT_BRACE:
					case Constants.SEMICOLON:
					case Constants.COMA:
						continueLoop = false;
						this.file_manager.ungetchar(c);
						break;
					case Constants.RELATIONAL_OPERATOR:
					case Constants.NEGATION_OPERATOR:
					case Constants.ASSIGNMENT_OPERATOR:
						if (c == '=') {
							this.currentState = (currentState == Constants.NEGATION_OPERATOR || this.currentState == Constants.ASSIGNMENT_OPERATOR)
									          ? Constants.EQUALS_COMPARISON_OPERATOR
									          : this.currentState;
							token += c;
							break;
						}
						this.file_manager.ungetchar(c);
						continueLoop = false;
						break;
					case States.BEGIN_LOGIC_AND_STATE:
						if (c == '&') {
							token += c;
							currentState = Constants.LOGIC_AND_OPERATOR;
							break;
						}
						this.file_manager.ungetchar(c);
						continueLoop = false;
						break;
					case Constants.LOGIC_AND_OPERATOR:
						continueLoop = false;
						this.file_manager.ungetchar(c);
						break;
					case States.BEGIN_LOGIC_OR_STATE:
						if (c == '|') {
							token += c;
							this.currentState = Constants.LOGIC_OR_OPERATOR;
							break;
						}
						this.file_manager.ungetchar(c);
						continueLoop = false;
						break;
					case Constants.LOGIC_OR_OPERATOR:
						continueLoop = false;
						this.file_manager.ungetchar(c);
						break;
					case States.BEGIN_REAL_NUMBER_STATE:
						if (Character.isDigit(c)) {
							token += c;
							this.currentState = Constants.REAL_NUMBER;
							break;
						}
						this.file_manager.ungetchar(c);
						continueLoop = false;
						break;
					case Constants.REAL_NUMBER:
						if (Character.isDigit(c)) {
							token += c;
							break;
						}
						this.file_manager.ungetchar(c);
						continueLoop = false;
						break;
					case Constants.INTEGER:
						if (c == '.') {
							token += c;
							this.currentState = States.BEGIN_REAL_NUMBER_STATE;
							break;
						} else if (Character.isDigit(c)) {
							token += c;
							break;
						}
						this.file_manager.ungetchar(c);
						continueLoop = false;
						break;
					case Constants.IDENTIFIER:
						if (Character.isAlphabetic(c) || c == '_') {
							token += c;
							break ;
						}
						this.file_manager.ungetchar(c);
						continueLoop = false;
						break;
					case States.ERROR_STATE:
						continueLoop = false;
						break;
					case Constants.EOF_SIGN:
						token += '$';
						continueLoop = false;
						break;
				}
			}
			
			this.setErrorIfExists();
			
			if (this.currentState == States.ERROR_STATE) {
				this.lexicalError(token);
			}
			
			newToken = new Token(this.currentState, token);
			
			if (this.currentState == Constants.IDENTIFIER) {
				this.comprobeAndSetKeyWord(newToken);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return newToken;
	}
	
	private void setStateByToken(char token){
		
		if ((int) token == FileManager.EOF_MARK) {
			this.currentState = Constants.EOF_SIGN;
		}
		
		switch(token){
	    case '+':
	    case '-':
	        this.currentState = Constants.ADD_OPERATOR;
	        break;
	    case '*':
	    case '/':
	    	this.currentState = Constants.MULT_OPERATOR;
	        break;
	    case '<':
	    case '>':
	    	this.currentState = Constants.RELATIONAL_OPERATOR;
	        break;
	    case '!':
	    	this.currentState = Constants.NEGATION_OPERATOR;
	        break;
	    case '=':
	    	this.currentState = Constants.ASSIGNMENT_OPERATOR;
	        break;
	    case '&':
	    	this.currentState = States .BEGIN_LOGIC_AND_STATE;
	        break;
	    case '|':
	    	this.currentState = States.BEGIN_LOGIC_OR_STATE;
	        break;
	    case '(':
	    	this.currentState = Constants.LEFT_PARENTHESIS;
	        break;
	    case ')':
	    	this.currentState = Constants.RIGHT_PARENTHESIS;
	        break;
	    case '{':
	    	this.currentState = Constants.LEFT_BRACE;
	        break;
	    case '}':
	    	this.currentState = Constants.RIGHT_BRACE;
	        break;
	    case ';':
	    	this.currentState = Constants.SEMICOLON;
	        break;
	    case ',':
	    	this.currentState = Constants.COMA;
	        break;
	    case '"':
	    	this.currentState = States.BEGIN_STRING_STATE;
	        break;
	    default:
	    	this.currentState = States.ERROR_STATE;
	    }
	}
	
	private void comprobeAndSetKeyWord(Token token)
	{
		if (token.value.equals("if"))
	        token.key = Constants.IF_KEYWORD;
		else if (token.value.equals("while"))
			token.key = Constants.WHILE_KEYWORD;
		else if (token.value.equals("else"))
			token.key = Constants.ELSE_KEYWORD;
		else if (token.equals("return"))
			token.key = Constants.RETURN_KEYWORD;
		else if (token.value.equals("int") || token.value.equals("float") || token.value.equals("void"))
			token.key = Constants.DATA_TYPE;
	}
	
	private void setErrorIfExists()
	{
	    switch(this.currentState)
	    {
	    case States.BEGIN_REAL_NUMBER_STATE:
	    case States.BEGIN_LOGIC_AND_STATE:
	    case States.BEGIN_LOGIC_OR_STATE:
	        this.currentState= States.ERROR_STATE;
	    }
	}
	
	private void lexicalError(String invalidSymbol)
	{
		JOptionPane.showMessageDialog(null, "Invalid symbol " + invalidSymbol, "Lexical Error", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}
	
	
}
