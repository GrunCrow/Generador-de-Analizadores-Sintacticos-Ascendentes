package grammar_parser;

import java.io.FileWriter;
import java.io.IOException;

public class ParserGenerator {
    private ActionElement[][] actionTable;
    private int[][] gotoTable;
    private int[][] ruleTable;

    public ParserGenerator(ActionElement[][] actionTable, int[][] gotoTable, int[][] rules) {
        this.actionTable = actionTable;
        this.gotoTable = gotoTable;
        this.ruleTable = rules;
    }

    public void generateParser(String outputFilePath) {
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write("package generated;\n\n");
            writer.write("import java.io.IOException;\n\n");
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
            writer.write("     		};\n");
            writer.write("     		this.rules = initRule;;\n");
            writer.write("    }\n\n");
            
            
            writer.write("     private void initActionTable() {\n");
            writeActionTable(writer);
            writer.write("     }\n\n");
            
            
            writer.write("     private void initGotoTable() {\n");
            writeGotoTable(writer);
            writer.write("     }\n\n");

            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	private void writeActionTable(FileWriter writer) throws IOException {
		return;
    }

	private void writeGotoTable(FileWriter writer) throws IOException {
		int num_rows = gotoTable.length;
		int num_cols = gotoTable[0].length;
		int i = 0;

		writer.write("     				gotoTable = new int[");
		writer.write(Integer.toString(num_rows));
		writer.write("][");
		writer.write(Integer.toString(num_cols));
		writer.write("]; ,\n");
		
		for(i = 0; i<num_rows-1;i++) {
			writer.write("     				gotoTable["); 
			writer.write(Integer.toString(i)); 
			writer.write("]["); 
			writer.write(Integer.toString(i)); 
			writer.write("] = ");
			writer.write(Integer.toString(gotoTable[i][0]));
			writer.write(";\n");
		}
		writer.write("	}\n");
		
	}

	
	private void writeRuleTable(FileWriter writer) throws IOException {
		int num_rows = ruleTable.length;
		int i = 0;
				
		writer.write("     				{ 0, 0 } ,\n");
		for(i = 0; i<num_rows-1;i++) {
			writer.write("     				{ "); 
			writer.write(Integer.toString(ruleTable[i][0])); 
			writer.write(", "); 
			writer.write(Integer.toString(ruleTable[i][1])); 
			writer.write(" },\n");
		}
		writer.write("     				{ "); 
		writer.write(Integer.toString(ruleTable[i][0])); 
		writer.write(", "); 
		writer.write(Integer.toString(ruleTable[i][1])); 
		writer.write(" }\n");
	}

	
	
}
