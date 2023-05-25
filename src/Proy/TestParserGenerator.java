package Proy;

import parserjj.*;

import java.io.IOException;

public class TestParserGenerator {

    public static void main(String[] args) {
        try {
            ParserGenerator.generateParserClass();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
