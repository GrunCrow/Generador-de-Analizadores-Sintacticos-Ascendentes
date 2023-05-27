package grammar_parser;

import grammar_parser.Token;

/**
 * Clase que describe un componente léxico
 *  
 * * @author Francisco José Moreno Velo
 *
 */
public class Token {
	
	/**
	 * Constante que identifica la categoría léxica de final de entrada
	 */
	public static final int EOF = 0;
	
	/**
	 * Tipo de componente léxico.
	 * Identificador de la categoría léxica del componente.
	 */
	private int kind;
	
	/**
	 * Lexema que da origen al componente
	 */
	private String lexeme;
	
	/**
	 * Número de fila en la que se encuentra el inicio del componente
	 */
	private int row;
	
	/**
	 * Número de columna en la que se encuentra el inicio del componente
	 */
	private int column;
	
	
	/**
	 * Constructor
	 * @param kind Identificador de la categoría léxica
	 * @param lexeme Lexema que origina el componente
	 * @param row Fila en la que comienza el componente
	 * @param column Columna en la que comienza el componente
	 */
	public Token(int kind, String lexeme, int row, int column) 
	{
		this.kind = kind;
		this.lexeme = lexeme;
		this.row = row;
		this.column = column;
	}
	
	public Token() {}

    public Token(int kind) {
        this(kind, null);
    }

    public Token(int kind, String lexeme) {
        this.kind = kind;
        this.lexeme = lexeme;
    }
	
	public static Token newToken(int kind, String lexeme) {
        return new Token(kind, lexeme);
    }

    public static Token newToken(int kind) {
        return newToken(kind, null);
    }
	
	/**
	 * Obtiene el identificador de categoría
	 * @return Tipo de componente
	 */
	public int getKind() 
	{
		return this.kind;
	}
	
	/**
	 * Obtiene el lexema del componente
	 * @return Lexema del componente
	 */
	public String getLexeme() 
	{
		return this.lexeme;
	}
	
	/**
	 * Obtiene la fila de inicio del componente
	 * @return Fila de inicio del componente
	 */
	public int getRow() 
	{
		return this.row;
	}
	
	/**
	 * Obtiene la columna de inicio del componente
	 * @return Columna de inicio del componente
	 */
	public int getColumn() 
	{
		return this.column;
	}
	
	/**
	 * Obtiene una descripción del Token
	 */
	public String toString() 
	{
		return "[Row: "+row+"][Column: "+column+"][Kind: "+kind+"] "+lexeme;
	}
	
	
}
