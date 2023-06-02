package auxiliares;

import java.util.*;

public class State {
	
	ArrayList<Transition> transiciones;
	ArrayList<Element> elements; //Reglas punteadas * para ver por donde va la lectura...
	
	public State() {
		transiciones = new ArrayList<>();
		elements = new ArrayList<>();
	}
	
	public void anadirElemento(Element element) {//Añadimos elementos excluynedo estados repetidos
		boolean existeElemento = false;
		
		for(Element elementoExistente : elements) {
			if(element.elementosIguales(elementoExistente))
				existeElemento = true;
		}
		if(!existeElemento)
			elements.add(element);
	}
	
	public void anadirTransicion(Transition transition) {//Añadimos transiciones excluyendo repetidas
		boolean existeTransicion = false;
		
		for(Transition transicionExistente : transiciones) {
			if(transition.areTransitionsEqual(transicionExistente))
				existeTransicion = true;
		}
		
		if(!existeTransicion)
			transiciones.add(transition);
	}
	
	public String toString() {
		String devolver = "";
		
		devolver += "ELEMENTOS: \n";
		for(Element element : elements) {
			devolver += element.toString() + "\n";
		}
		devolver += "TRANSICIONES: \n";
		for(Transition transition : transiciones) {
			devolver += transition.toString() + "\n";
		}
		
		return devolver;
		
	}
}
