package auxiliares;

import java.util.ArrayList;

public class Regla {

	String identificador;
	ArrayList<Expresion> produccion;
	ArrayList<String> primeros;
	ArrayList<String> siguientes;
	
	public Regla(String identificador) {
		this.identificador = identificador;
		produccion = new ArrayList<>();
		primeros = new ArrayList<>();
		siguientes = new ArrayList<>();
	}
	
	public Regla(String identificador, ArrayList<Expresion> produccion) {
		this.identificador = identificador;
		this.produccion = produccion;
	}
	
	
	public boolean reglasIguales(Regla reglaAnalizar) {
		
		if(this.produccion.size() != reglaAnalizar.produccion.size())
			return false;
		if(!this.identificador.equals(reglaAnalizar.identificador))
			return false;
		
		for(int i=0; i<this.produccion.size(); i++) {
			if(!this.produccion.get(i).expresion.equals( reglaAnalizar.produccion.get(i).expresion )) {
				return false;
			}
		}
		
		
		return true;
	}
	
	public void anadirExpresion(String expresion, boolean terminal) {
		Expresion aux = new Expresion(expresion, terminal);
		produccion.add(aux);
	}
	
	public void anadirPrimeros(String token) {
		if(!primeros.contains(token))
			primeros.add(token);
	}
	
	public void anadirSiguientes(String token) {
		if(!siguientes.contains(token))
			siguientes.add(token);
	}
	
	public String toString() {
		return identificador + " ->" + produccion.toString();
	}
}
