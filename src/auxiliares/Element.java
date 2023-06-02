package auxiliares;

import java.util.ArrayList;

public class Element {

	String identifier;
	ArrayList<Expression> production;
	Expression marker;
	
	public Element(Element element) {
		this.identifier = element.identifier;
		this.production = new ArrayList<>();
		this.production.addAll(element.production);
		// TODO poner un punto en vez de hashtag??
		marker = new Expression(".",false);
	}
	
	public Element(String identifier,ArrayList<Expression> production, boolean marked) {
		this.identifier = identifier;
		this.production = production;
		// TODO poner un punto en vez de hashtag??
		marker = new Expression(".",false);
		if(!marked) {
			ArrayList<Expression> aux = new ArrayList<>();
			aux.add(marker);
			aux.addAll(production);
			this.production = aux;
		}
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
			// TODO poner un punto en vez de hashtag??
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
	
	public boolean elementosIguales(Element elementAnalizar) {
		
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
