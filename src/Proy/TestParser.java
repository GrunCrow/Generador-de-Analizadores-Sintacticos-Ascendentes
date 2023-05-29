package Proy;

import grammar_parser.*;

public class TestParser {
    public static void main(String[] args) throws SintaxException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        String filePath = "Main.txt";

        GrammarParser parser = new GrammarParser(filePath);
		parser.parse();
		// RuleTable rules = parser.getRulesTable();
		// parser.close();
		System.out.println("Parsing completed successfully.");
    }
}
