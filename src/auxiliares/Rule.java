package auxiliares;

import java.util.ArrayList;

public class Rule {

	String identifier;
	ArrayList<Expression> production;
	ArrayList<String> firsts;
	ArrayList<String> follows;
	
	public Rule(String identifier) {
		this.identifier = identifier;
		production = new ArrayList<>();
		firsts = new ArrayList<>();
		follows = new ArrayList<>();
	}
	
	public Rule(String identifier, ArrayList<Expression> production) {
		this.identifier = identifier;
		this.production = production;
	}
	
	
	public boolean reglasIguales(Rule ruleAnalizar) {
		
		if(this.production.size() != ruleAnalizar.production.size())
			return false;
		if(!this.identifier.equals(ruleAnalizar.identifier))
			return false;
		
		for(int i=0; i<this.production.size(); i++) {
			if(!this.production.get(i).expression.equals( ruleAnalizar.production.get(i).expression )) {
				return false;
			}
		}
		
		
		return true;
	}
	
	public void anadirExpresion(String expression, boolean terminal) {
		Expression aux = new Expression(expression, terminal);
		production.add(aux);
	}
	
	public void anadirPrimeros(String token) {
		if(!firsts.contains(token))
			firsts.add(token);
	}
	
	public void addFollows(String token) {
		if(!follows.contains(token))
			follows.add(token);
	}
	
	public String toString() {
		return identifier + " ->" + production.toString();
	}
}
