package parserjj;

import java.util.Stack;

public class ParserGenerator {

    public static void generateParserClass() {
        StringBuilder code = new StringBuilder();

        code.append("public class Parser extends SLRParser implements TokenConstants, SymbolConstants {\n");
        code.append("\n");
        code.append("    public Parser() {\n");
        code.append("        initRules();\n");
        code.append("        initActionTable();\n");
        code.append("        initGotoTable();\n");
        code.append("    }\n");
        code.append("\n");
        code.append("    private void initRules() {\n");
        code.append("        int[][] initRule = {\n");
        code.append("            { 0, 0 },\n");
        code.append("            { Expr, 1 },\n");
        code.append("            { Expr, 3 },\n");
        code.append("            { Expr, 3 },\n");
        code.append("            { Term, 1 },\n");
        code.append("            { Term, 3 },\n");
        code.append("            { Term, 3 },\n");
        code.append("            { Factor, 1 },\n");
        code.append("            { Factor, 3 },\n");
        code.append("            { Factor, 4 },\n");
        code.append("            { Args, 1 },\n");
        code.append("            { Args, 0 },\n");
        code.append("            { ArgumentList, 1 },\n");
        code.append("            { ArgumentList, 3 }\n");
        code.append("        };\n");
        code.append("\n");
        code.append("        this.rule = initRule;\n");
        code.append("    }\n");
        code.append("\n");
        code.append("    private void initActionTable() {\n");
        code.append("        actionTable = new ActionElement[24][10];\n");
        code.append("\n");
        code.append("        actionTable[0][NUM] = new ActionElement(ActionElement.SHIFT, 4);\n");
        code.append("        actionTable[0][LPAREN] = new ActionElement(ActionElement.SHIFT, 5);\n");
        code.append("        actionTable[0][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 6);\n");
        code.append("\n");
        code.append("        actionTable[1][EOF] = new ActionElement(ActionElement.ACCEPT, 0);\n");
        code.append("        actionTable[1][PLUS] = new ActionElement(ActionElement.SHIFT, 7);\n");
        code.append("        actionTable[1][MINUS] = new ActionElement(ActionElement.SHIFT, 8);\n");
        code.append("\n");
        code.append("        // ...\n");
        code.append("    }\n");
        code.append("\n");
        code.append("    private void initGotoTable() {\n");
        code.append("        gotoTable = new int[24][5];\n");
        code.append("\n");
        code.append("        gotoTable[0][Expr] = 1;\n");
        code.append("        gotoTable[0][Term] = 2;\n");
        code.append("        gotoTable[0][Factor] = 3;\n");
        code.append("        // ...\n");
        code.append("    }\n");
        code.append("\n");
        code.append("}");

        System.out.println(code.toString());
    }

    public static void main(String[] args) {
        generateParserClass();
    }
}
