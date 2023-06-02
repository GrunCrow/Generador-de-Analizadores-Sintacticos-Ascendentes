package auxiliares;

import java.util.*;

public class Estado {
	
	ArrayList<Transicion> transiciones;
	ArrayList<Elemento> elementos; //Reglas punteadas * para ver por donde va la lectura...
	
	public Estado() {
		transiciones = new ArrayList<>();
		elementos = new ArrayList<>();
	}
	
	public void anadirElemento(Elemento elemento) {//Añadimos elementos excluynedo estados repetidos
		boolean existeElemento = false;
		
		for(Elemento elementoExistente : elementos) {
			if(elemento.elementosIguales(elementoExistente))
				existeElemento = true;
		}
		if(!existeElemento)
			elementos.add(elemento);
	}
	
	public void anadirTransicion(Transicion transicion) {//Añadimos transiciones excluyendo repetidas
		boolean existeTransicion = false;
		
		for(Transicion transicionExistente : transiciones) {
			if(transicion.transicionesIguales(transicionExistente))
				existeTransicion = true;
		}
		
		if(!existeTransicion)
			transiciones.add(transicion);
	}
	
	public String toString() {
		String devolver = "";
		
		devolver += "ELEMENTOS: \n";
		for(Elemento elemento : elementos) {
			devolver += elemento.toString() + "\n";
		}
		devolver += "TRANSICIONES: \n";
		for(Transicion transicion : transiciones) {
			devolver += transicion.toString() + "\n";
		}
		
		return devolver;
		
	}
}
