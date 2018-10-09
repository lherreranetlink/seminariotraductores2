package lex;

import java.io.IOException;

import javax.swing.JTextArea;

import fileutils.FileManager;

public class Lex {
	
	private FileManager file_manager;
	private JTextArea errorLog;
	private int currentState;
	private int buffpos;
	private Token[] buffer;
	public boolean error;

	public Lex(String filename, JTextArea errorLog) throws IOException {
		this.errorLog = errorLog;
		this.file_manager = new FileManager(filename);
		this.buffpos = -1; 
		this.buffer = new Token[FileManager.BUFFSIZ];
		this.error = false;
	}
	
	public Lex(JTextArea errorLog) {
		this.errorLog = errorLog;
		this.file_manager = new FileManager();
		this.buffpos = -1;
		this.buffer = new Token[FileManager.BUFFSIZ];
		this.error = false;
	}
	
	public Token getNextToken() {
		return (this.buffpos >= 0) ? buffer[this.buffpos--] : this.getTokenFromFile(); 
	}
	
	public void ungetToken(Token token) {
		this.buffer[++this.buffpos] = token;
	}
	
	private Token getTokenFromFile() {
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
					case Constants.COLON:
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
						} else 
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
					case Constants.INTEGER:
						if (Character.isDigit(c)) {
							token += c;
							break;
						}
						this.file_manager.ungetchar(c);
						continueLoop = false;
						break;
					case Constants.IDENTIFIER:
						if (Character.isLetter(c) || Character.isDigit(c) || c == '_') {
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
			
			if (this.currentState == Constants.IDENTIFIER) 
				this.comprobeAndSetKeyWord(newToken);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return newToken;
	}
	
	private void setStateByToken(char c){
		if ((int) c == FileManager.EOF_MARK) {
			this.currentState = Constants.EOF_SIGN;
			return;
		}
		
		switch(c){
	    case '+':
	    case '-':
	        this.currentState = Constants.ADD_OPERATOR;
	        break;
	    case '*':
	    case '/':
	    case '%':
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
	    case ':':
	    	this.currentState = Constants.COLON;
	    	break;
	    case ',':
	    	this.currentState = Constants.COMA;
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
		else if (token.value.equals("return"))
			token.key = Constants.RETURN_KEYWORD;
		else if (token.value.equals("int") || token.value.equals("float") || token.value.equals("void"))
			token.key = Constants.DATA_TYPE;
		else if (token.value.equals("do"))
			token.key = Constants.DO_KEYWORD;
		else if (token.value.equals("for"))
			token.key = Constants.FOR_KEYWORD;
	}
	
	public void close_input_file() {
		try {
			this.file_manager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setErrorIfExists()
	{
	    switch(this.currentState)
	    {
	    case States.BEGIN_LOGIC_AND_STATE:
	    case States.BEGIN_LOGIC_OR_STATE:
	        this.currentState= States.ERROR_STATE;
	    }
	}
	
	public void lexicalError(String invalidSymbol)
	{
		String errors = this.errorLog.getText();
		this.errorLog.setText(errors + "Lexical Error: " + invalidSymbol + " invalid token\n");
		this.error = true;
	}
	
	
}
