package grammar_parser;

import java.io.FileWriter;
import java.io.IOException;

public class ParserGenerator {
    private ActionTable actionTable;
    private GotoTable gotoTable;
    private RuleTable ruleTable;

    public ParserGenerator(ActionTable actionTable, GotoTable gotoTable, RuleTable rule) {
        this.actionTable = actionTable;
        this.gotoTable = gotoTable;
        this.ruleTable = rule;
    }

    public void generateParser(String outputFilePath) {
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write("package generated;\n\n");
            writer.write("import grammar_parser.*;\n\n");
            writer.write("public class Parser extends SLRParser {\n\n");
            writer.write("    public Parser(Lexer lexer) throws SintaxException, IOException {\n");
            writer.write("        super.parse(lexer);\n");
            writer.write("    }\n\n");
            writer.write("    @Override\n");
            writer.write("    protected ActionElement[][] getActionTable() {\n");
            writeActionTable(writer);
            writer.write("        return actionTable;\n");
            writer.write("    }\n\n");
            writer.write("    @Override\n");
            writer.write("    protected int[][] getGotoTable() {\n");
            writeGotoTable(writer);
            writer.write("        return gotoTable;\n");
            writer.write("    }\n\n");
            writer.write("    @Override\n");
            writer.write("    protected int[][] getRuleTable() {\n");
            writeRuleTable(writer);
            writer.write("        return rule;\n");
            writer.write("    }\n\n");
            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	private void writeActionTable(FileWriter writer) throws IOException {
        writer.write("        ActionElement[][] actionTable = new ActionElement[][] {\n");
        for (int row = 0; row < actionTable.getRowCount(); row++) {
            writer.write("            {");
            for (int column = 0; column < actionTable.getColumnCount(); column++) {
                ActionElement actionElement = actionTable.getAction(row, column);
                if (actionElement != null) {
                    writer.write("new ActionElement(" + actionElement.getType() + ", " + actionElement.getValue() + ")");
                } else {
                    writer.write("null");
                }
                if (column < actionTable.getColumnCount() - 1) {
                    writer.write(", ");
                }
            }
            writer.write("}");
            if (row < actionTable.getRowCount() - 1) {
                writer.write(",\n");
            }
        }
        writer.write("\n        };\n");
    }

	private void writeGotoTable(FileWriter writer) throws IOException {
	    writer.write("        int[][] gotoTable = new int[][] {\n");
	    for (int row = 0; row < gotoTable.getRowCount(); row++) {
	        writer.write("            {");
	        for (int column = 0; column < gotoTable.getColumnCount(); column++) {
	            GotoElement gotoElement = gotoTable.getGoto(row, column);
	            if (gotoElement != null) {
	                writer.write(Integer.toString(gotoElement.getValue()));
	            } else {
	                writer.write("-1");
	            }
	            if (column < gotoTable.getColumnCount() - 1) {
	                writer.write(", ");
	            }
	        }
	        writer.write("}");
	        if (row < gotoTable.getRowCount() - 1) {
	            writer.write(",");
	        }
	        writer.write("\n");
	    }
	    writer.write("        };\n\n");
	}

	
	private void writeRuleTable(FileWriter writer) throws IOException {
	    writer.write("        String[][] ruleTable = new String[][] {\n");
	    for (int row = 0; row < ruleTable.getRowCount(); row++) {
	        writer.write("            {");
	        for (int column = 0; column < ruleTable.getColumnCount(); column++) {
	            Rule rule = ruleTable.getRule(row, column);
	            if (rule != null) {
	                writer.write("\"" + rule.toString() + "\"");
	            } else {
	                writer.write("\"\"");
	            }
	            if (column < ruleTable.getColumnCount() - 1) {
	                writer.write(", ");
	            }
	        }
	        writer.write("}");
	        if (row < ruleTable.getRowCount() - 1) {
	            writer.write(",");
	        }
	        writer.write("\n");
	    }
	    writer.write("        };\n\n");
	}

	
	
}
