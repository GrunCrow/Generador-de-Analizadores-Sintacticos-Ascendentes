package Proy;

import java.io.IOException;

import grammar_parser.*;

public class TestParserGenerator {

    public static void main(String[] args) {
        try {
            ParserGenerator.generateParserClass();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
