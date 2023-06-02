package parserjj;

import java.io.File;
import java.io.IOException;


public class GrammarParser {
    private Lexer lexer;
    private Token currentToken;

    public GrammarParser(String filePath) {
        try {
        	File file = new File(filePath);
            lexer = new GrammarLexer(file);
            currentToken = lexer.getNextToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void consume(int expectedKind) throws ParseException {
        // si comentario
    	if (currentToken.getKind() == TokenKind.COMENTARIO) {
    		// no hacer nada
    		currentToken = lexer.getNextToken();
    	}
    	
    	if (currentToken.getKind() == expectedKind) {
            currentToken = lexer.getNextToken();
    	}
        else {
        	throw new ParseException("Expected " + expectedKind + " but found " + currentToken.getKind());
            
        }
    }

    public void parse() throws ParseException {
        while (currentToken.getKind() != TokenKind.EOF) {
            definicion();
        }
    }

    private void definicion() throws ParseException {
        consume(TokenKind.NOTERMINAL);
        consume(TokenKind.EQ);
        listaReglas();
        consume(TokenKind.SEMICOLON);
    }

    private void listaReglas() throws ParseException {
        regla();
        while (currentToken.getKind() == TokenKind.BAR) {
            consume(TokenKind.BAR);
            regla();
        }
    }

    private void regla() throws ParseException {
        while (currentToken.getKind() == TokenKind.NOTERMINAL || currentToken.getKind() == TokenKind.TERMINAL) {
            if (currentToken.getKind() == TokenKind.NOTERMINAL) {
                consume(TokenKind.NOTERMINAL);
            } else {
                consume(TokenKind.TERMINAL);
            }
        }
    }
    
    public static void main(String[] args) {
        String filePath = "Main.txt";
        
        GrammarParser parser = new GrammarParser(filePath);
        
        try {
            parser.parse();
            System.out.println("Análisis sintáctico completado exitosamente.");
        } catch (ParseException e) {
            System.err.println("Error de análisis sintáctico: " + e.getMessage());
        }
    }

    /*public static void main(String[] args) {
        GrammarParser parser = new GrammarParser();

        try {
            File file = new File("Main.txt");
            boolean result = parser.parse(file);

            if (result) {
                System.out.println("Análisis sintáctico exitoso. La gramática es válida.");
            } else {
                System.out.println("Análisis sintáctico fallido. La gramática no es válida.");
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } catch (SintaxException e) {
            System.out.println("Error de sintaxis en el archivo: " + e.getMessage());
        }
    }*/
}
