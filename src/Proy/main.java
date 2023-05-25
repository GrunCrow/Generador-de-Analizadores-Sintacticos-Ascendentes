package Proy;

import parserjj.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class main {
	public static void main(String[] args) {
        try {
            String fileinput = "Main.txt";
            GrammarParser grammarParser = new GrammarParser();
            grammarParser.parseGrammar(fileinput);
            // Aquí continuará el procesamiento de la gramática
            // ...
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
