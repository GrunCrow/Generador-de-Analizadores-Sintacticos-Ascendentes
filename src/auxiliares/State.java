package auxiliares;

import java.util.*;

public class State {
	// TODO Uso de set para evitar duplicados
	private ArrayList<Transition> transitions;
	//Reglas punteadas * para ver por donde va la lectura...
	private ArrayList<GrammarElement> grammarElements; 
	
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



	public void addElement(GrammarElement grammarElement) {//Añadimos elementos excluynedo estados repetidos
		boolean existeElemento = false;
		
		for(GrammarElement elementoExistente : grammarElements) {
			if(grammarElement.isEqualElement(elementoExistente))
				existeElemento = true;
		}
		if(!existeElemento)
			grammarElements.add(grammarElement);
	}
	
	public void addTransition(Transition transition) {
		boolean transitionExists = false;
		
		// Añadir transiciones excluyendo repetidas
		for(Transition existingTransition : transitions) {
			if(transition.areTransitionsEqual(existingTransition))
				transitionExists = true;
		}
		
		if(!transitionExists)
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
