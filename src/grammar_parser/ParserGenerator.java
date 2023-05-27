package grammar_parser;

import java.io.FileWriter;
import java.io.IOException;

public class ParserGenerator {

    public static void generateParser(String grammar, String outputFilePath) {
        GrammarParser parser = new GrammarParser();
        Grammar grammarObj = parser.parse(grammar);

        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write("import generated.*;\n\n");
            writer.write("public class Parser extends SLRParser implements TokenConstants, SymbolConstants {\n\n");
            writer.write("    public Parser() {\n");
            writer.write("        super(new SLRTableBuilder().build());\n");
            writer.write("    }\n\n");
            writer.write("    @Override\n");
            writer.write("    public void parse(Lexer lexer) throws ParseException {\n");
            writer.write("        super.parse(lexer);\n");
            writer.write("        System.out.println(\"Parsing completed.\");\n");
            writer.write("    }\n\n");

            // Generate productions
            writer.write("    // Productions\n");
            for (Production production : grammarObj.getProductions()) {
                writer.write("    // " + production + "\n");
                writer.write("    public void " + production.getName() + "(");
                for (int i = 0; i < production.getRhs().size(); i++) {
                    Symbol symbol = production.getRhs().get(i);
                    writer.write(symbol.getName());
                    if (i < production.getRhs().size() - 1) {
                        writer.write(", ");
                    }
                }
                writer.write(") {\n");
                writer.write("        // TODO: Implement production action\n");
                writer.write("    }\n\n");
            }

            // Generate parse actions
            writer.write("    // Parse actions\n");
            for (ParseAction action : grammarObj.getParseActions()) {
                writer.write("    // " + action + "\n");
                if (action instanceof ShiftAction) {
                    ShiftAction shiftAction = (ShiftAction) action;
                    writer.write("    public void shift" + shiftAction.getState().getIndex() + "() {\n");
                    writer.write("        shift(" + shiftAction.getState().getIndex() + ");\n");
                    writer.write("    }\n\n");
                } else if (action instanceof ReduceAction) {
                    ReduceAction reduceAction = (ReduceAction) action;
                    Production production = reduceAction.getProduction();
                    writer.write("    public void reduce" + reduceAction.getProductionIndex() + "() {\n");
                    writer.write("        " + production.getName() + "(");
                    for (int i = 0; i < production.getRhs().size(); i++) {
                        writer.write("pop()");
                        if (i < production.getRhs().size() - 1) {
                            writer.write(", ");
                        }
                    }
                    writer.write(");\n");
                    writer.write("        reduce(" + reduceAction.getProductionIndex() + ");\n");
                    writer.write("    }\n\n");
                }
            }

            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
