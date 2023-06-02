package auxiliares;

public class Expresion {

	String expresion;
	boolean terminal;

	public Expresion(Expresion expresion) {
		this.expresion = expresion.expresion;
		this.terminal = expresion.terminal;
	}
	
	public Expresion(String expresion, boolean terminal) {
		this.expresion = expresion;
		this.terminal = terminal;
	}
	
	public boolean esTerminal() {
		return terminal;
	}
	
	public String toString(){
		return expresion + " ";
		
		//return expresion + (terminal ? "(terminal)" : "(Noterminal)") +" ";
	}
}
