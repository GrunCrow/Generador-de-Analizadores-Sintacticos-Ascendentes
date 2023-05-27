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

         // Generar las tablas de análisis de acción y desplazamiento
        ActionElement[][] actionTable = grammarParser.generateActionTable();
        int[][] gotoTable = grammarParser.generateGotoTable();
        int[][] rules = generateRules()

        // Aquí puedes implementar el analizador sintáctico utilizando las tablas generadas

        // Por ejemplo, analizar una cadena de entrada
        // Por ejemplo, analizar una cadena de entrada
        String input = "1 + 2 * 3;";
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(grammarParser, actionTable, gotoTable);
        boolean isValid = syntaxAnalyzer.analyze(input);

        if (isValid) {
            System.out.println("La cadena es válida.");
        } else {
            System.out.println("La cadena no es válida. Se encontró un error en el análisis.");
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
