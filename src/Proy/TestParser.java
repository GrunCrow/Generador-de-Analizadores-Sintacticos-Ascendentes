package Proy;

import parserjj.*;

public class TestParser {
    public static void main(String[] args) {
        String filePath = "TestParser.txt";

        Parser parser = new Parser(filePath);
		parser.parse();
		parser.close();
		System.out.println("Parsing completed successfully.");
    }
}
