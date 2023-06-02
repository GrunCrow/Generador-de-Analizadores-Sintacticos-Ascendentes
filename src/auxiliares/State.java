package auxiliares;

import java.util.*;

public class State {
	// TODO Uso de set para evitar duplicados
	private ArrayList<Transition> transitions;
	private ArrayList<GrammarElement> grammarElements; //Reglas punteadas * para ver por donde va la lectura...
	
	public State() {
		transitions = new ArrayList<>();
		grammarElements = new ArrayList<>();
	}
	
	
	
	public ArrayList<Transition> getTransiciones() {
		return transitions;
	}



	public ArrayList<GrammarElement> getElements() {
		return grammarElements;
	}



	public void anadirElemento(GrammarElement grammarElement) {//Añadimos elementos excluynedo estados repetidos
		boolean existeElemento = false;
		
		for(GrammarElement elementoExistente : grammarElements) {
			if(grammarElement.elementosIguales(elementoExistente))
				existeElemento = true;
		}
		if(!existeElemento)
			grammarElements.add(grammarElement);
	}
	
	public void anadirTransicion(Transition transition) {
		boolean existeTransicion = false;
		
		// Añadir transiciones excluyendo repetidas
		for(Transition transicionExistente : transitions) {
			if(transition.areTransitionsEqual(transicionExistente))
				existeTransicion = true;
		}
		
		if(!existeTransicion)
			transitions.add(transition);
	}
	
	public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append("ELEMENTS:\n");
	    for (GrammarElement grammarElement : grammarElements) {
	        builder.append(grammarElement.toString()).append("\n");
	    }
	    builder.append("TRANSITIONS:\n");
	    for (Transition transition : transitions) {
	        builder.append(transition.toString()).append("\n");
	    }
	    return builder.toString();
	}
}
