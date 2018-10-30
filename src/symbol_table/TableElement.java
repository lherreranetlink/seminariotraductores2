package symbol_table;

public class TableElement {
	public String identifier;
	public String type;
	public String scope;
	public String paramsPattern;
	
	public TableElement() {}
	
	public TableElement(String type, String identifier, String scope, String paramsPattern) {
		this.identifier = identifier;
		this.type = type;
		this.scope = scope;
		this.paramsPattern = paramsPattern;
	}
	
	public TableElement(String type, String identifier, String scope) {
		this.identifier = identifier;
		this.type = type;
		this.scope = scope;
		this.paramsPattern = "";
	}
}
