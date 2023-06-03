package auxiliares;

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

    private void match(int expectedKind) throws ParseException {
        // si es comentario
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
        match(TokenKind.NOTERMINAL);
        match(TokenKind.EQ);
        rulesList();
        match(TokenKind.SEMICOLON);
    }

    private void rulesList() throws ParseException {
        rule();
        while (currentToken.getKind() == TokenKind.BAR) {
            match(TokenKind.BAR);
            rule();
        }
    }

    private void rule() throws ParseException {
        while (currentToken.getKind() == TokenKind.NOTERMINAL || currentToken.getKind() == TokenKind.TERMINAL) {
            if (currentToken.getKind() == TokenKind.NOTERMINAL) {
                match(TokenKind.NOTERMINAL);
            } else {
                match(TokenKind.TERMINAL);
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
}
