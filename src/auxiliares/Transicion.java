package auxiliares;

public class Transicion {
	
	Expresion origen;
	int destino;
	boolean reduccion;
	
	public Transicion() {
		this.reduccion = false;
	}
	
	public Transicion(Expresion expr, int destino) {
		this.origen = expr;
		this.destino = destino;
		this.reduccion = false;
	}
	
	public void anadirOrigen(Expresion origen) {
		this.origen = origen;
	}
	
	public void anadirDestino(int destino) {
		this.destino = destino;
	}
	
	public void anadirReduccion() {
		this.reduccion = true;
	}
	
	public boolean transicionesIguales(Transicion transicion) {
		
		if(this.destino == transicion.destino)
			return true;
		
		return false;
	}

	
	public String toString() {
		String devolver = "";
		if(this.reduccion) {
			devolver += "Reduccion R " + destino + "(" + origen.expresion + ")";
		}else {
			devolver += origen.expresion + " -> " + destino;
		}
			
		return devolver;
	}
}
