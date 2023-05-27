package Proy;

import java.io.IOException;

import grammar_parser.*;

public class TestLexer {
    public static void main(String[] args) {
        try {
            Lexer lexer = new Lexer("Main.txt");
            Token currentToken;

            do {
                lexer.getNextToken();
                currentToken = lexer.getCurrentToken();
                if (currentToken != null) {
                    String currentLexeme = lexer.getCurrentLexeme();
                    System.out.println("Token: " + currentToken.getKind() + " | Lexema: " + currentLexeme);
                }
            } while (lexer.getCurrentToken() != null);

            lexer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
