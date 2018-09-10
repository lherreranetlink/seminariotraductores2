package parser;

public class Rule {
	public int index;
	public int size;
	public String name;
	
	public Rule() {}
	
	public Rule(int index, String name, int size) {
		this.index = index;
		this.name = name;
		this.size = size;
	}
}
