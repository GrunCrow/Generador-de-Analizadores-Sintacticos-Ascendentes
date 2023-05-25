package Proy;

import parserjj.*;

import java.io.IOException;

public class TestLexer {
    public static void main(String[] args) {
        try {
            Lexer lexer = new Lexer("TestLexer.txt");
            Token currentToken;

            do {
                lexer.getNextToken();
                currentToken = lexer.getCurrentToken();
                if (currentToken != null) {
                    String currentLexeme = lexer.getCurrentLexeme();
                    System.out.println("Token: " + currentToken.kind + " | Lexema: " + currentLexeme);
                }
            } while (lexer.getCurrentToken() != null);

            lexer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
