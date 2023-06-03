package auxiliares;

import java.io.*;

/**
 * Clase que desarrolla el funcionamiento de una Maquina Discriminadora
 * Determinista
 */
public abstract class Lexer {

	/**
	 * Flujo de entrada que permite el retroceso
	 */
	private BufferedCharStream stream;
	
	/**
	 * Transiciones del automata de la maquina
	 * 
	 * @param state Estado inicial
	 * @param symbol S�mbolo del alfabeto
	 * @return Estado final
	 */
	protected abstract int transition(int state, char symbol);
	
	/**
	 * Verifica si un estado es final
	 * 
	 * @param state Estado
	 * @return true, si el estado es final
	 */
	protected abstract boolean isFinal(int state);
	
	/**
	 * Genera el componente l�xico correspondiente al estado final y
	 * al lexema encontrado. Devuelve null si la acci�n asociada al
	 * estado final es omitir (SKIP).
	 * 
	 * @param state Estado final alcanzado
	 * @param lexeme Lexema reconocido
	 * @param row Fila de comienzo del lexema
	 * @param column Columna de comienzo del lexema
	 * @return Componente l�xico correspondiente al estado final y al lexema
	 */
	protected abstract Token getToken(int state, String lexeme, int row, int column);
	
	/**
	 * Constructor
	 * @param file Nombre del fichero fuente
	 * @throws IOException En caso de problemas con el flujo de entrada
	 */
	public Lexer(File file) throws IOException 
	{
		this.stream = new BufferedCharStream(file);
	}
	
	/**
	 * Obtiene el siguiente componente l�xico del flujo de entrada
	 * @return Siguiente componente l�xico
	 */
	public Token getNextToken() 
	{
		Token nextToken = tokenize();
		while(nextToken == null) nextToken = tokenize();
		return nextToken;
	}
	
	/**
	 * Cierra el flujo de entrada
	 */
	public void close() 
	{
		this.stream.close();
	}
	
	/**
	 * Ejecuta la m�quina discriminadora determinista para extaer el siguiente token
	 * de la cadena de entrada. Si el lexema extraido corresponde a un blanco o
	 * comentario el token devuelto ser� nulo.
	 * 
	 * @return
	 */
	private Token tokenize() 
	{
		int finalState = -1;
		StringBuffer lexeme = new StringBuffer();
		StringBuffer tainting = new StringBuffer();
		char newChar = stream.getNextChar();
		int state = transition(0,newChar);
		int row = stream.getRow();
		int column = stream.getColumn();
		while(state!=-1 && newChar != '\0') 
		{
			tainting.append(newChar);
			if(isFinal(state)) 
			{
				finalState = state;
				lexeme.append(tainting);
				tainting.delete(0,tainting.length());
			}
			newChar = stream.getNextChar();
			state = transition(state,newChar);
		}
		
		if(finalState != -1) 
		{
			stream.retract(1+tainting.length());
			return getToken(finalState,lexeme.toString(),row,column);
		} 
		else if(newChar != '\0') 
		{
			stream.retract(tainting.length());
			throw new LexicalError(newChar,row,column);
		} 
		else 
		{
			stream.retract(1);
			return new Token(Token.EOF,"",row,column);
		}	
	}
}
