
package parserjj;

import java.io.*;

import generated.TokenConstants;

/**
 * Clase que desarrolla el analizador lexico
 *
 */
public class GrammarLexer extends Lexer implements TokenConstants {

	/**
	 * Transiciones del aut�mata del analizador l�xico
	 * 
	 * @param state Estado inicial
	 * @param symbol S�mbolo del alfabeto
	 * @return Estado final
	 */
	protected int transition(int state, char symbol) {
	    switch (state) {
	        case 0:
	            if (symbol == ' ' || symbol == '\r' || symbol == '\n' || symbol == '\t') return 1;
	            else if (symbol == '/') return 2;
	            else if (symbol == '_' || (symbol >= 'a' && symbol <= 'z') || (symbol >= 'A' && symbol <= 'Z')) return 11;
	            else if (symbol == '<') return 8;
	            else if (symbol == ':') return 14;
	            else if (symbol == '|') return 13;
	            else if (symbol == ';') return 12;
	            else return -1;
	        case 1:
	            if (symbol == ' ' || symbol == '\r' || symbol == '\n' || symbol == '\t') return 1;
	            else return -1;
	        case 2:
	            if (symbol == '*') return 3;
	            else if (symbol == '/') return 6;
	            else return -1;
	        case 3:
	            if (symbol == '*') return 4;
	            else return 3;
	        case 4:
	            if (symbol == '*') return 4;
	            else if (symbol == '/') return 5;
	            else return 3;
	        case 6:
	            if (symbol == '\n') return 7;
	            else return 6;
	        case 8:
	        	if (symbol == '_' || (symbol >= 'a' && symbol <= 'z') || (symbol >= 'A' && symbol <= 'Z')) return 9;
	            else return -1;
	        case 9:
	        	if (symbol == '_' || (symbol >= 'a' && symbol <= 'z') || (symbol >= 'A' && symbol <= 'Z') || (symbol >= '0' && symbol <= '9')) return 9;
	        	else if (symbol == '>') return 10;
	        	else return -1;
	        case 11:
	        	if (symbol == '_' || (symbol >= 'a' && symbol <= 'z') || (symbol >= 'A' && symbol <= 'Z') || (symbol >= '0' && symbol <= '9')) return 11;
	        	else return -1;
	        case 14:
	        	if (symbol == ':') return 15;
	        	else return -1;
	        case 15:
	        	if (symbol == '=') return 16;
	        	else return -1;
        	default: return -1;
	    }
	}



	
	/**
	 * Verifica si un estado es final
	 * 
	 * @param state Estado
	 * @return true, si el estado es final
	 */
	protected boolean isFinal(int state) {
	    switch (state) {
	        case 1:
	        case 5:
	        case 7:
	        case 10:
	        case 11:
	        case 12:
	        case 13:
	        case 16:
	            return true;
	        default:
	            return false;
	    }
	}


	
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
	protected Token getToken(int state, String lexeme, int row, int column) {
	    switch (state) {
	    	//case 1: return new Token(TokenKind.BLANCO, lexeme, row, column);
	        case 5: return new Token(TokenKind.COMENTARIO, lexeme, row, column);
	        case 7: return new Token(TokenKind.COMENTARIO, lexeme, row, column);
	        case 10: return new Token(TokenKind.TERMINAL, lexeme, row, column);
	        case 11: return new Token(TokenKind.NOTERMINAL, lexeme, row, column);
	        case 12: return new Token(TokenKind.SEMICOLON, lexeme, row, column);
	        case 13: return new Token(TokenKind.BAR, lexeme, row, column);
	        case 16: return new Token(TokenKind.EQ, lexeme, row, column);
	        default: return null;
	    }
	}

	
	/**
	 * Estudia si un identificador corresponde a una palabra clave del lenguaje
	 * y devuelve el c�digo del token adecuado
	 * @param id
	 * @return
	 */
	private int getKind(String lexeme) {
	    /*if (lexeme.matches("<[_a-zA-Z][_a-zA-Z0-9]*>")) {
	        return TokenKind.TERMINAL;
	    } else if (lexeme.matches("[_a-zA-Z][_a-zA-Z0-9]*")) {
	        return TokenKind.NOTERMINAL;
	    } else if (lexeme.equals("::=")) {
	        return TokenKind.EQ;
	    } else if (lexeme.equals("|")) {
	        return TokenKind.BAR;
	    } else if (lexeme.equals(";")) {
	        return TokenKind.SEMICOLON;
	    } else if (lexeme.matches("\\s+")) {
	        return TokenKind.BLANCO; */
	    //} else if (lexeme.matches("/\\*(.|\\n)*?\\*/")) {
		/*    return TokenKind.COMENTARIO;
	    } else {
	        return TokenKind.UNKNOWN;
	    }*/
	
		return TokenKind.UNKNOWN;
	}



	
	/**
	 * Constructor
	 * @param filename Nombre del fichero fuente
	 * @throws IOException En caso de problemas con el flujo de entrada
	 */
	public GrammarLexer(File file) throws IOException 
	{
		super(file);
	}
	
	
	public static void main(String[] args) {
        try {
            File inputFile = new File("Main.txt");
            GrammarLexer lexer = new GrammarLexer(inputFile);
            
            Token token = lexer.getNextToken();
            while (token.getKind() != TokenKind.EOF) {
            	//if (token.getKind() != TokenKind.BLANCO) {
            		System.out.println(token);
            	//}
                token = lexer.getNextToken();
            }
            
            lexer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
