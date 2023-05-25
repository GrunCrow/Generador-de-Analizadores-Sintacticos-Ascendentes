package Proy;

import java.io.IOException;

import parserjj.ActionElement;
import parserjj.GrammarParser;
import parserjj.Lexer;
import parserjj.SLRParser;
import parserjj.SintaxException;
import parserjj.Token;

public class TestGrammarParser {
	
	public static void main(String[] args) throws SintaxException {
	    try {
	        GrammarParser grammarParser = new GrammarParser();
	        grammarParser.parseGrammar("Main.txt");

	        ActionElement[][] actionTable = grammarParser.generateActionTable();
	        int[][] gotoTable = grammarParser.generateGotoTable();
	        int[][] rules = grammarParser.generateRules();

	        SLRParser parser = new SLRParser() {
	            // Implementar métodos abstractos de SLRParser si los hay
	        };

	        parser.setActionTable(actionTable);
	        parser.setGotoTable(gotoTable);
	        parser.setRule(rules);

	        Lexer lexer = new Lexer("input.txt"); // Reemplazar "input.txt" con el nombre de tu archivo de entrada
	        boolean result = parser.parse(lexer);

	        if (result) {
	            System.out.println("Análisis sintáctico exitoso");
	        } else {
	            System.out.println("Error en el análisis sintáctico");
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	
}
