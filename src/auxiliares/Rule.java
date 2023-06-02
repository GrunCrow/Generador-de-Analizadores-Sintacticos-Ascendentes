package auxiliares;

import java.util.ArrayList;
import java.util.List;

public class Rule {

	String identifier;
	List<Expression> production;
	ArrayList<String> firsts;
	ArrayList<String> follows;
	
	public Rule(String identifier) {
		this.identifier = identifier;
		production = new ArrayList<>();
		firsts = new ArrayList<>();
		follows = new ArrayList<>();
	}
	
	public Rule(String identifier, List<Expression> produccionRegla) {
		this.identifier = identifier;
		this.production = produccionRegla;
	}
	
	
	public boolean equalRules(Rule ruleAnalizar) {
	    return this.identifier.equals(ruleAnalizar.identifier) &&
	            this.production.equals(ruleAnalizar.production);
	}
	
	public void addExpressions(String expression, boolean terminal) {
		Expression aux = new Expression(expression, terminal);
		production.add(aux);
	}
	
	public void addFirsts(String token) {
		if(!firsts.contains(token))
			firsts.add(token);
	}
	
	public void addFollows(String token) {
		if(!follows.contains(token))
			follows.add(token);
	}
	
	public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append(identifier).append(" ->").append(production.toString());
	    return builder.toString();
	}
}
