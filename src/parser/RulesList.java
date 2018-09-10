package parser;

import java.util.ArrayList;

public class RulesList {
	private ArrayList<Rule> list;
	
	public RulesList() {}
	
	public void insertRule(Rule rule) {
		list.add(rule);
	}
	
	public Rule getRuleByPosition(int position) {
		return list.get(position);
	}
	
	public int getRuleSize(Rule rule) {
		int index = list.indexOf(rule);
		return list.get(index).size;
	}
	
	public void insertInitialTransition() {
		Rule initialRule = new Rule(0, "Initial", 1);
		list.add(initialRule);
	}
	
}
