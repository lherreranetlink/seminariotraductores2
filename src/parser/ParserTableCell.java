package parser;

public class ParserTableCell {
	public int transitionType;
	public int goTo;
	
	public ParserTableCell() {}
	
	public ParserTableCell(int transitionType, int goTo) {
		this.transitionType = transitionType;
		this.goTo = goTo;
	}
}