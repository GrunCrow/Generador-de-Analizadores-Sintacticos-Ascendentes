package parserjj;

import generated.TokenConstants;

/**
 * Clase que describe un excepci�n sint�ctica
 */
public class SintaxException extends Exception implements TokenConstants {

	/**
	 * Constante asignada al objeto serializable
	 */
	private static final long serialVersionUID = 20080002L;

	/**
	 * Mensaje de error
	 */
	private String msg;
	
	/**
	 * Constructor con un solo tipo esperado
	 * @param token
	 * @param expected
	 */
	public SintaxException(Token token, int expected) 
	{
		this.msg = "Sintax exception at row "+token.getRow();
		msg += ", column "+token.getColumn()+".\n";
		msg += "  Found "+token.getLexeme()+"\n";
		msg += "  while expecting "+getLexemeForKind(expected)+".\n";
	}
	
	/**
	 * Constructor con una lista de tipos esperados
	 * @param token
	 * @param expected
	 */
	public SintaxException(Token token, int[] expected) 
	{
		this.msg = "Sintax exception at row "+token.getRow();
		msg += ", column "+token.getColumn()+".\n";
		msg += "  Found "+token.getLexeme()+"\n";
		msg += "  while expecting one of\n";
		for(int i=0; i<expected.length; i++) 
		{
			msg += "    "+getLexemeForKind(expected[i])+"\n";
		}
	}
	
	/**
	 * Obtiene el mensaje de error
	 */
	public String toString() 
	{
		return this.msg;
	}
	
	/**
	 * Descripci�n del token
	 * @param kind
	 * @return
	 */
	private String getLexemeForKind(int kind) {
		switch(kind) {
			case NUM: return "NUM";
			case IDENTIFIER: return "IDENTIFIER";
			case LPAREN: return "(";
			case RPAREN: return ")";
			case COMMA: return "COMMA";
			case PLUS: return "+";
			case MINUS: return "-";
			case PROD: return "*";
			case DIV: return "/";
			default: return "";
		}
	}
}
