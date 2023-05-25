package parserjj;

// Modificada y adaptada al codigo

import java.io.IOException;
import java.util.Stack;

/**
* Clase que desarrolla el comportamiento común de los analizadores
* sintácticos ascendentes basados en tablas SLR
*
*/
public abstract class SLRParser {

	/**
	* Analizador léxico
	*/
	private Lexer lexer;
	
	/**
	* Siguiente token de la cadena de entrada
	*/
	private Token nextToken;
	
	/**
	* Pila de estados
	*/
	private Stack<Integer> stack;
	
	/**
	* Tabla de acciones
	*/
	protected ActionElement[][] actionTable;
	
	/**
	* Tabla de Ir_a
	*/
	protected int[][] gotoTable;
	
	/**
	* Lista de reglas
	*/
	protected int[][] rule;
	
	/**
	* Realiza el análisis sintáctico a partir del léxico
	* @param filename
	* @return
	 * @throws IOException 
	*/
	@SuppressWarnings("removal")
	public boolean parse(Lexer lexer) throws SintaxException, IOException
	{
		this.lexer = lexer;
		lexer.getNextToken();
		this.nextToken = lexer.getCurrentToken();
		this.stack = new Stack<Integer>();
		this.stack.push(new Integer(0));
		while(true) {
			if(step()) break;
		}
		return true;
	}
	
	/**
	* Método que realiza una acción de desplazamiento
	* @param action
	 * @throws IOException 
	*/
	@SuppressWarnings("removal")
	private void shiftAction(ActionElement action) throws IOException 
	{
		lexer.getNextToken();
		nextToken = lexer.getCurrentToken();
		stack.add(new Integer(action.getValue()));
	}
	
	/**
	* Método que realiza una acción de reducción
	* @param action
	*/
	@SuppressWarnings("removal")
	private void reduceAction(ActionElement action) {
		int ruleIndex = action.getValue();
		int numSymbols = rule[ruleIndex][1];
		int leftSymbol = rule[ruleIndex][0];
		while(numSymbols > 0) {
			stack.pop();
			numSymbols --;
		}
		int state = ((Integer) stack.lastElement()).intValue();
		int gotoState = gotoTable[state][leftSymbol];
		stack.push(new Integer(gotoState));
	}
	
	/**
	* Ejecuta un paso en el análisis sintáctico, es decir, extrae
	* un elemento de la pila y lo analiza.
	* @throws SintaxException
	 * @throws IOException 
	*/
	private boolean step() throws SintaxException, IOException 
	{
		int state = ((Integer) stack.lastElement()).intValue();
		ActionElement action = actionTable[state][nextToken.getKind().ordinal()];
		if(action == null) {
			int count = 0;
			for(int i=0; i<actionTable[state].length; i++) if(actionTable[state][i] != null) count++;
			int[] expected = new int[count];
			for(int i=0,j=0; i<actionTable[state].length; i++) if(actionTable[state][i] != null) {
				expected[j] = i;
				j++;
			}
			throw new SintaxException(nextToken,expected);
		}
		int actionType = action.getType();
		if(actionType == ActionElement.ACCEPT) {
			return true;
		} 
		else if(actionType == ActionElement.SHIFT) {
			shiftAction(action);
			return false;
		} 
		else if(actionType == ActionElement.REDUCE) {
			reduceAction(action);
			return false;
		}
		return false;
	}
	
	public ActionElement[][] getActionTable() {
		return actionTable;
	}

	public void setActionTable(ActionElement[][] actionTable) {
		this.actionTable = actionTable;
	}

	public int[][] getGotoTable() {
		return gotoTable;
	}

	public void setGotoTable(int[][] gotoTable) {
		this.gotoTable = gotoTable;
	}

	public int[][] getRule() {
		return rule;
	}

	public void setRule(int[][] rule) {
		this.rule = rule;
	}

}

