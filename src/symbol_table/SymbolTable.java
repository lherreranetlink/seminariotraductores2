package symbol_table;

import java.util.ArrayList;
import java.util.Iterator;

public class SymbolTable {
	
	private ArrayList<TableElement> list;
	
	SymbolTable() {
		this.list = new ArrayList<TableElement>();
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
				return element.type;
			}
		}
		
		return "error";
	}

}
