package Proy;

import parserjj.*;


import java.io.*;

public class TestLexico {
    public static void main(String[] args) {
        try {
            Lexer lexer = new Lexer("TestLexico.txt");
            Token token;

            do {
                lexer.getNextToken();
                String currentToken = lexer.getCurrentToken();
                String currentLexeme = lexer.getCurrentLexeme();
                System.out.println("Token: " + currentToken + " | Lexema: " + currentLexeme);
            } while (lexer.getCurrentToken() != null);

            lexer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
