package parser;

import java.util.ArrayList;

public class RulesList {
	private ArrayList<Rule> list;
	
	public RulesList() {
		this.list = new ArrayList<Rule>();
	}
	
	public void insertRule(Rule rule) {
		list.add(rule);
	}
	
	public Rule getRuleByPosition(int position) {
		return list.get(position - 1);
	}
	
	public int getRuleSize(Rule rule) {
		int index = list.indexOf(rule);
		return list.get(index).size;
	}
	
	public void insertInitialTransition() {
		Rule initialRule = new Rule(0, 1, "Initial");
		list.add(initialRule);
	}
	
}
