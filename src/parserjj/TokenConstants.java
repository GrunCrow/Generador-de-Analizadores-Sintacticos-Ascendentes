package parserjj;

/**
* Interfaz que define los c�digos de las diferentes categor�as l�xicas
*  
* * @author Francisco Jos� Moreno Velo
*
*/
public interface TokenConstants {

	/**
	* Final de fichero
	*/
	public int EOF = 0;
	
	//--------------------------------------------------------------//
	// Palabras clave
	//--------------------------------------------------------------//
	
	/**
	* Palabra clave "boolean"
	*/
	public int BOOLEAN = 1;
	
	/**
	* Palabra clave "char"
	*/
	public int CHAR = 2;
	
	/**
	* Palabra clave "else"
	*/
	public int ELSE = 3;
	
	/**
	* Palabra clave "false"
	*/
	public int FALSE = 4;
	
	/**
	* Palabra clave "if"
	*/
	public int IF = 5;
	
	/**
	* Palabra clave "import"
	*/
	public int IMPORT = 6;
	
	/**
	* Palabra clave "int"
	*/
	public int INT = 7;
	
	/**
	* Palabra clave "library"
	*/
	public int LIBRARY = 8;
	
	/**
	* Palabra clave "native"
	*/
	public int NATIVE = 9;
	
	/**
	* Palabra clave "private"
	*/
	public int PRIVATE = 10;
	
	/**
	* Palabra clave "public"
	*/
	public int PUBLIC = 11;
	
	/**
	* Palabra clave "return"
	*/
	public int RETURN = 12;
	
	/**
	* Palabra clave "true"
	*/
	public int TRUE = 13;
	
	/**
	* Palabra clave "void"
	*/
	public int VOID = 14;
	
	/**
	* Palabra clave "while"
	*/
	public int WHILE = 15;
	
	//--------------------------------------------------------------//
	// Identificadores y literales
	//--------------------------------------------------------------//
	
	/**
	* Identificador
	*/
	public int IDENTIFIER = 16;
	
	/**
	* Literal de tipo int
	*/
	public int INTEGER_LITERAL = 17;
	
	/**
	* Literal de tipo char
	*/
	public int CHAR_LITERAL = 18;
	
	//--------------------------------------------------------------//
	// Separadores
	//--------------------------------------------------------------//
	
	/**
	* Par�ntesis abierto "("
	*/
	public int LPAREN = 19;
	
	/**
	* Par�ntesis cerrado ")"
	*/
	public int RPAREN = 20;
	
	/**
	* Llave abierta "{"
	*/
	public int LBRACE = 21;
	
	/**
	* Llave cerrada "}"
	*/
	public int RBRACE = 22;
	
	/**
	* Punto y coma ";"
	*/
	public int SEMICOLON = 23;
	
	/**
	* Coma ","
	*/
	public int COMMA = 24;
	
	/**
	* Punto "."
	*/
	public int DOT = 25;
	
	//--------------------------------------------------------------//
	// Operadores
	//--------------------------------------------------------------//
	
	/**
	* Asignaci�n "="
	*/
	public int ASSIGN = 26;
	
	/**
	* Igualdad "=="
	*/
	public int EQ = 27;
	
	/**
	* Menor "<"
	*/
	public int LT = 28;
	
	/**
	* Menor o igual "<="
	*/
	public int LE = 29;
	
	/**
	* Mayor ">"
	*/
	public int GT = 30;
	
	/**
	* Mayor o igual ">="
	*/
	public int GE = 31;
	
	/**
	* Distinto "!="
	*/
	public int NE = 32;
	
	/**
	* Disyunci�n "||"
	*/
	public int OR = 33;
	
	/**
	* Conjunci�n "&&"
	*/
	public int AND = 34;
	
	/**
	* Negaci�n "!"
	*/
	public int NOT = 35;
	
	/**
	* Suma "+"
	*/
	public int PLUS = 36;
	
	/**
	* Resta "-"
	*/
	public int MINUS = 37;
	
	/**
	* Producto "*"
	*/
	public int PROD = 38;
	
	/**
	* Divisi�n "/"
	*/
	public int DIV = 39;
	
	/**
	* M�dulo "%"
	*/
	public int MOD = 40;
}

