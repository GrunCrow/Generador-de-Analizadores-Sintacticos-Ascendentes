package Proy;

import java.io.IOException;
import java.util.List;

import grammar_parser.*;

public class TestSymbolGenerator {

    public static void main(String[] args) {
        try {
            Lexer lexer = new Lexer("Main.txt");
            List<Token> tokens = lexer.tokenize();
            lexer.close();

            SymbolConstantsGenerator.generateSymbolConstants(tokens, "src/generated/SymbolConstants.java");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

