package lex;

public class Token {
	public int key;
	public int extraAttr;
	public String value;
	
	public Token() {
		
	}
	
	public Token(int key, String value) {
		this.key = key;
		this.value = value;
	}
}
