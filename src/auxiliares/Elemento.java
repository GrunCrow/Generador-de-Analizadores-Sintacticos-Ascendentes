package auxiliares;

import java.util.ArrayList;

public class Elemento {

	String identificador;
	ArrayList<Expresion> produccion;
	Expresion marcador;
	
	public Elemento(Elemento elemento) {
		this.identificador = elemento.identificador;
		this.produccion = new ArrayList<>();
		this.produccion.addAll(elemento.produccion);
		marcador = new Expresion("#",false);
	}
	
	public Elemento(String identificador,ArrayList<Expresion> produccion, boolean marcado) {
		this.identificador = identificador;
		this.produccion = produccion;
		marcador = new Expresion("#",false);
		if(!marcado) {
			ArrayList<Expresion> aux = new ArrayList<>();
			aux.add(marcador);
			aux.addAll(produccion);
			this.produccion = aux;
		}
	}
	
	public boolean adelantaCursor() {
		int indice = indiceMarcador();
		
		if(produccion.size()-1 > indice) {
			Expresion aux = produccion.get(indice+1);
			produccion.set(indice, aux);
			produccion.set(indice+1, marcador);
			return true;
		}	
		return false;
	}
	
	public int indiceMarcador() {
		int indice = -1;
		
		for(int i=0; i<produccion.size(); i++) {
			if(produccion.get(i).expresion.equals("#"))
				indice = i;
		}
		
		return indice;
	}
	
	public boolean marcadorAlFinal() {
		int indiceMarcador = indiceMarcador();
		
		if(indiceMarcador == produccion.size()-1)
			return true;
			
		return false;
	}
	
	public boolean elementosIguales(Elemento elementoAnalizar) {
		
		if(this.produccion.size() != elementoAnalizar.produccion.size())
			return false;
		if(!this.identificador.equals(elementoAnalizar.identificador))
			return false;
		
		for(int i=0; i<this.produccion.size(); i++) {
			if(!this.produccion.get(i).expresion.equals(elementoAnalizar.produccion.get(i).expresion))
				return false;
		}
		
		
		return true;
	}
	
	
	public String toString() {
		String devolver = identificador + " -> ";
		
		for(Expresion expresion : produccion) {
			devolver += expresion.expresion + " ";
		}
		
		
		return devolver;
	}
}
