package Proy;

import grammar_parser.*;

public class TestParser {
    public static void main(String[] args) {
        String filePath = "Main.txt";

        GrammarParser parser = new GrammarParser(filePath);
		parser.parse();
		parser.close();
		System.out.println("Parsing completed successfully.");
    }
}
