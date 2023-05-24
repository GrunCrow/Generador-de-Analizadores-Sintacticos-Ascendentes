package Proy;

import parserjj.*;

import java.io.IOException;

public class TestLexico {
    public static void main(String[] args) {
        try {
            Lexer lexer = new Lexer("TestLexico.txt");
            Token token;

            do {
                lexer.getNextToken();
                Token currentToken = lexer.getCurrentToken();
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
