package Proy;

import parserjj.*;

import java.io.IOException;
import java.util.List;

public class TestSymbolGenerator {

    public static void main(String[] args) {
        try {
            Lexer lexer = new Lexer("Main.txt");
            List<Token> tokens = lexer.tokenize();
            lexer.close();

            SymbolConstantsGenerator.generateSymbolConstants(tokens, "SymbolConstants.java");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

