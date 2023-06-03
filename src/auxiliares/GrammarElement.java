package auxiliares;

import java.util.ArrayList;
import java.util.List;

public class GrammarElement {

	private String identifier;
	private List<Expression> production;
	private Expression marker;
	
	public GrammarElement(GrammarElement grammarElement) {
		this.identifier = grammarElement.identifier;
		this.production = new ArrayList<>();
		this.production.addAll(grammarElement.production);
		marker = new Expression(".",false);
	}
	
	public GrammarElement(String identifier,List<Expression> produccionInicio, boolean marked) {
		this.identifier = identifier;
		this.production = produccionInicio;
		marker = new Expression(".",false);
		if(!marked) {
			ArrayList<Expression> aux = new ArrayList<>();
			aux.add(marker);
			aux.addAll(produccionInicio);
			this.production = aux;
		}
	}
	
	
	
	public String getIdentifier() {
		return identifier;
	}

	public List<Expression> getProduction() {
		return production;
	}

	public Expression getMarker() {
		return marker;
	}

	public boolean adelantaCursor() {
		int indice = indiceMarcador();
		
		if(production.size()-1 > indice) {
			Expression aux = production.get(indice+1);
			production.set(indice, aux);
			production.set(indice+1, marker);
			return true;
		}	
		return false;
	}
	
	public int indiceMarcador() {
		int indice = -1;
		
		for(int i=0; i<production.size(); i++) {
			if(production.get(i).expression.equals("."))
				indice = i;
		}
		
		return indice;
	}
	
	public boolean marcadorAlFinal() {
		int indiceMarcador = indiceMarcador();
		
		if(indiceMarcador == production.size()-1)
			return true;
			
		return false;
	}
	
	public boolean elementosIguales(GrammarElement elementAnalizar) {
		
		if(this.production.size() != elementAnalizar.production.size())
			return false;
		if(!this.identifier.equals(elementAnalizar.identifier))
			return false;
		
		for(int i=0; i<this.production.size(); i++) {
			if(!this.production.get(i).expression.equals(elementAnalizar.production.get(i).expression))
				return false;
		}
		return true;
	}
	
	
	public String toString() {
		String devolver = identifier + " -> ";
		
		for(Expression expression : production) {
			devolver += expression.expression + " ";
		}
		
		
		return devolver;
	}
}
