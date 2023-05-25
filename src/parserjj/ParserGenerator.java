import parserjj.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ParserGenerator {

    private static final String HEADER = "import generated.*;\nimport parserjj.*;\n\npublic class Parser extends SLRParser implements TokenConstants, SymbolConstants {\n\n";

    private static final String CONSTRUCTOR = "    public Parser() {\n" +
            "        initRules();\n" +
            "        initActionTable();\n" +
            "        initGotoTable();\n" +
            "    }\n\n";

    private static final String INIT_RULES = "    private void initRules() {\n" +
            "        int[][] initRule = {\n" +
            "            { 0, 0 },\n";

    private static final String INIT_ACTION_TABLE = "    private void initActionTable() {\n" +
            "        actionTable = new ActionElement[][] {\n";

    private static final String INIT_GOTO_TABLE = "    private void initGotoTable() {\n" +
            "        gotoTable = new int[][] {\n";

    private static final String FOOTER = "        };\n" +
            "    }\n\n" +
            "}";
    

    public static void generateParser(int[][] rules, ActionElement[][] actionTable, int[][] gotoTable, String outputFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(HEADER);
            writer.write(CONSTRUCTOR);
            writer.write(INIT_RULES);
            writeRuleTable(writer, rules);
            writer.write(INIT_ACTION_TABLE);
            writeActionTable(writer, actionTable);
            writer.write(INIT_GOTO_TABLE);
            writeGotoTable(writer, gotoTable);
            writer.write(FOOTER);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeRuleTable(BufferedWriter writer, int[][] rules) throws IOException {
        for (int i = 0; i < rules.length; i++) {
            writer.write("            { ");
            writer.write(rules[i][0] + ", " + rules[i][1] + " },\n");
        }
        writer.write("        };\n");
    }

    private static void writeActionTable(BufferedWriter writer, ActionElement[][] actionTable) throws IOException {
        writer.write("            ");
        for (int i = 0; i < actionTable.length; i++) {
            writer.write("new ActionElement[] { ");
            for (int j = 0; j < actionTable[i].length; j++) {
                ActionElement action = actionTable[i][j];
                if (action != null) {
                    writer.write("new ActionElement(" + action.getType() + ", " + action.getValue() + "), ");
                } else {
                    writer.write("null, ");
                }
            }
            writer.write("},\n            ");
        }
        writer.write("};\n");
    }

    private static void writeGotoTable(BufferedWriter writer, int[][] gotoTable) throws IOException {
        writer.write("            ");
        for (int i = 0; i < gotoTable.length; i++) {
            writer.write("new int[] { ");
            for (int j = 0; j < gotoTable[i].length; j++) {
                writer.write(gotoTable[i][j] + ", ");
            }
            writer.write("},\n            ");
        }
        writer.write("};\n");
    }

    public static void main(String[] args) {
        int[][] rules = {
            { 0, 0 },
            { Expr, 1 },
            { Expr, 3 },
            { Expr, 3 },
            { Term, 1 },
            { Term, 3 },
            { Term, 3 },
            { Factor, 1 },
            { Factor, 3 },
            { Factor, 4 },
            { Args, 1 },
            { Args, 0 },
            { ArgumentList, 1 },
            { ArgumentList, 3 }
        };

        ActionElement[][] actionTable = {
            { null, null, null, null, null, null, null, null, null, null },
            { null, null, null, null, null, null, null, null, null, null },
            // ...
        };

        int[][] gotoTable = {
            { 1, 2, 3, 0, 0 },
            { 0, 0, 0, 0, 0 },
            // ...
        };

        generateParser(rules, actionTable, gotoTable);
        System.out.println("Parser.java generated successfully.");
    }
}
