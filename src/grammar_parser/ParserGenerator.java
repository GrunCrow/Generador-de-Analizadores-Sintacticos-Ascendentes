package grammar_parser;

import java.io.FileWriter;
import java.io.IOException;

public class ParserGenerator {
    private ActionElement[][] actionTable;
    private int[][] gotoTable;
    private int[][] ruleTable;

    public ParserGenerator(int[][] gotoTable, int[][] rules) {
        //this.actionTable = actionTable;
        this.gotoTable = gotoTable;
        this.ruleTable = rules;
    }

    public void generateParser(String outputFilePath) {
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write("package generated;\n\n");
            writer.write("import grammar_parser.*;\n\n");
            writer.write(" public class Parser extends SLRParser implements TokenConstants, SymbolConstants {\n\n");
            writer.write("    public Parser() throws SintaxException, IOException {\n");
            writer.write("        initRules();\n");
            writer.write("        initActionTable();\n");
            writer.write("        initGotoTable();\n");
            writer.write("    }\n\n");
            
            
            writer.write("     private void initRules() {\n");
            writer.write("     		int[][] initRule = {\n");
            writeRuleTable(writer);
            writer.write("          };\n");
            writer.write("    		this.rule = initRule;;\n");
            writer.write("    }\n\n");
            
            
            writer.write("     private void initActionTable() {\n");
            writeActionTable(writer);
            writer.write("    }\n\n");
            
            
            writer.write("       private void initGotoTable() {\n");
            writeGotoTable(writer);
            writer.write("    }\n\n");

            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	private void writeActionTable(FileWriter writer) throws IOException {

    }

	private void writeGotoTable(FileWriter writer) throws IOException {

	}

	
	private void writeRuleTable(FileWriter writer) throws IOException {
		int num_rows = ruleTable.length;
		int i = 0;
				
		writer.write("     				{ 0, 0 } ,\n");
		for(i = 0; i<num_rows-1;i++) {
			writer.write("     				{ "); writer.write(ruleTable[i][0]); writer.write(", "); writer.write(ruleTable[i][1]); writer.write(" },\n");
		}
		writer.write("     				{ "); writer.write(ruleTable[i][0]); writer.write(", "); writer.write(ruleTable[i][1]); writer.write(" }\n");
	}

	
	
}
