
package grammar_parser;

import java.io.IOException;
import java.util.Stack;

/**
 * Clase que desarrolla el comportamiento común de los analizadores
 * sintácticos ascendentes basados en tablas SLR
 *
 */

// programacion generica del algoritmo de desplazamiento - reducción

public abstract class SLRParser {

	private Lexer lexer;

	private Token nextToken;

	private Stack<Integer> stack;
	
	// protected porque cuando hagamos tintoparser lo extenderá dandole contenido 
	//a las variables y campos como protected
	// los hijos pueden acceder a ellos
	protected ActionElement[][] actionTable;
	
	protected int[][] gotoTable;
	
	protected int[][] rule;
	
	protected boolean parse(Lexer lexer) throws SintaxException, IOException
	{
		// ejecuto lexer
		this.lexer = lexer;
		lexer.getNextToken();
		this.nextToken = lexer.getCurrentToken();
		// construyo la pila
		this.stack = new Stack<Integer>();
		// apilo el estado 0
		//this.stack.push(new Integer(0)); // lo cambiaron en una versión de java y da error, cambiar a como en la siguiente linea
		this.stack.push(Integer.valueOf(0));
		while(true) {
			// una iteración, true si es aceptar, false si desplazar o reducir, 
			//al llegar al 2º aceptar, para, así encadenamos pasos del algoritmo 
			//(o si encontramos una excepcion que lanzará step, y saldremos de oparser 
			//lanzando una excepción
			if(step()) break;
		}
		return true;
	}
	
	private void shiftAction(ActionElement action) throws IOException 
	{
		lexer.getNextToken();
		nextToken = lexer.getCurrentToken();
		//stack.add(new Integer(action.getValue()));
		stack.add(Integer.valueOf(action.getValue()));
	}
	

	// Reducción
	private void reduceAction(ActionElement action) 
	{
		int ruleIndex = action.getValue();
		
		// simbolo de la izq
		int numSymbols = rule[ruleIndex][1];
		// simbolo de la dcha
		int leftSymbol = rule[ruleIndex][0];
		// desapilar y ver que queda en la cima de la pila
		while(numSymbols > 0) {
			// desapilar
			stack.pop();
			numSymbols --;
		}
		// ver lo que queda en la cima de la pila
		int state = ((Integer) stack.lastElement()).intValue();
		// nuevo estado a apilar
		int gotoState = gotoTable[state][leftSymbol];
		//stack.push(new Integer(gotoState));
		stack.push(Integer.valueOf(gotoState));
	}
	

	private boolean step() throws SintaxException, IOException 
	{
		int state = ((Integer) stack.lastElement()).intValue();
		// de la tabla de acción action es la acción a ejecutar
		ActionElement action = actionTable[state][nextToken.getKind()];
		
		// este if es un error, que es el caso de que action sea null -> lanzar excepcion
		if(action == null) {
			int count = 0;
			// nos recorremos filas de actionTable con el state para saber cuantas tienen contenido
			for(int i=0; i<actionTable[state].length; i++) if(actionTable[state][i] != null) count++;
			// creamos lista tan grande como filas de contenido haya
			int[] expected = new int[count];
			// recorremos la lista otra vez guardando los valores (codigos de las columnas con contenido)
			for(int i=0,j=0; i<actionTable[state].length; i++) if(actionTable[state][i] != null) {
				expected[j] = i;
				j++;
			}
			// decimos que esperabamos en el siguiente token, lo que hubiera estado bien
			throw new SintaxException(nextToken,expected);
		}
		
		
		
		// a partir de aqui es si no hay error
		int actionType = action.getType();
		
		// si aceptar
		if(actionType == ActionElement.ACCEPT) {
			return true;
		} 
		// si desplazar
		else if(actionType == ActionElement.SHIFT) {
			shiftAction(action);
			return false;
		} 
		// si reducir
		else if(actionType == ActionElement.REDUCE) {
			reduceAction(action);
			return false;
		}
		return false;
	}
}
