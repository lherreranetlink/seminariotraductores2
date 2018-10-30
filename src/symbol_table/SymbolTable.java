package symbol_table;

import java.util.ArrayList;
import java.util.Iterator;

import semantic.SemanticType;

public class SymbolTable {
	
	private ArrayList<TableElement> list;
	
	public SymbolTable() {
		this.list = new ArrayList<TableElement>();
	}
	
	public void add(String dataType, String identifier, String scope, String paramsPattern) {
		TableElement element = new TableElement(dataType, identifier, scope, paramsPattern);
		this.list.add(element);
	}
	
	public void add(String dataType, String identifier, String scope) {
		TableElement element = new TableElement(dataType, identifier, scope);
		this.list.add(element);
	}
	
	public void add(TableElement element) {
		this.list.add(element);
	}
	
	public boolean existsSymbol(String identifier, String scope) {
		Iterator<TableElement> i = this.list.iterator();
		while (i.hasNext()) {
			TableElement element = i.next();
			if (element.identifier.equals(identifier) && element.scope.equals(scope)) {
				return true;
			}
		}
		
		return false;
	}
	
	public String getType(String identifier, String scope) {
		Iterator<TableElement> i = this.list.iterator();
		while (i.hasNext()) {
			TableElement element = i.next();
			if (element.identifier.equals(identifier) && element.scope.equals(scope)) {
				switch (element.type) {
					case "void":
						return SemanticType.VOID_TYPE;
					case "int":
						return SemanticType.INTEGER_TYPE;
					case "float":
						return SemanticType.FLOAT_TYPE;
				}
			}
		}
		
		return SemanticType.ERROR_TYPE;
	}
	
	public void printSymbolTable() {
		Iterator<TableElement> i = this.list.iterator();
		while(i.hasNext()) {
			TableElement element = i.next();
			System.out.println("Data type: " + element.type);
			System.out.println("Identifier: " + element.identifier);
			System.out.println("scope: " + element.scope);
			System.out.println("params pattern: " + element.paramsPattern + "\n");
		}
	}

}
