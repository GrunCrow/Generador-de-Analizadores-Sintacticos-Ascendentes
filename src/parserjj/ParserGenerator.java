package parserjj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserGenerator {
    private SymbolTable symbolTable;
    private TokenTable tokenTable;
    private Map<String, Integer> nonTerminalIndices;
    
    public ParserGenerator() {
        symbolTable = new SymbolTable();
        tokenTable = new TokenTable();
        nonTerminalIndices = new HashMap<>();
    }

    public void generateParser(String grammarFilePath) throws IOException {
        loadGrammar(grammarFilePath);
        generateParserFile();
    }

    public void loadGrammar(String grammarFilePath) throws IOException {
        int nonTerminalIndex = 0;
        
        Lexer lexer = new Lexer(grammarFilePath);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            TokenKind kind = token.getKind();
            String lexeme = token.getLexeme();

            if (kind == TokenKind.NOTERMINAL) {
                String nonTerminal = lexeme.substring(1, lexeme.length() - 1);
                nonTerminalIndices.put(nonTerminal, nonTerminalIndex);
                symbolTable.addNonTerminal(nonTerminalIndex);
                nonTerminalIndex++;
            } else if (kind == TokenKind.TERMINAL) {
                tokenTable.addTerminal(lexeme);
            } else if (kind == TokenKind.EQ) {
                processProduction(lexeme);
            }
        }

        lexer.close();
    }

    private void processProduction(String production) {
        String[] parts = production.split("::=");
        String leftHandSide = parts[0].trim();
        String rightHandSide = parts[1].trim();

        if (rightHandSide.equals("epsilon")) {
            rightHandSide = "";
        }

        String[] symbols = rightHandSide.split("\\s+");
        int productionIndex = symbolTable.getNonTerminalIndex(leftHandSide);

        for (String symbol : symbols) {
            if (symbol.startsWith("<")) {
                // Símbolo no terminal
                int symbolIndex = symbolTable.getNonTerminalIndex(symbol.substring(1, symbol.length() - 1));
                symbolTable.addNonTerminal(symbolIndex);
            } else {
                // Símbolo terminal
                tokenTable.addTerminal(symbol);
            }
        }

        symbolTable.addProduction(productionIndex, symbols.length);
    }

    private void generateParserFile() throws IOException {
        String parserCode = generateParserCode();
        String parserFilePath = "src/generated/Parser.java";
        BufferedWriter writer = new BufferedWriter(new FileWriter(parserFilePath));
        writer.write(parserCode);
        writer.close();
    }

    private String generateParserCode() {
        StringBuilder codeBuilder = new StringBuilder();

        codeBuilder.append("public class Parser extends SLRParser implements TokenConstants, SymbolConstants {\n\n");
        codeBuilder.append("  public Parser() {\n");
        codeBuilder.append("    initRules();\n");
        codeBuilder.append("    initActionTable();\n");
        codeBuilder.append("    initGotoTable();\n");
        codeBuilder.append("  }\n\n");

        codeBuilder.append("  private void initRules() {\n");
        codeBuilder.append("    int[][] initRule = {\n");

        for (int i = 0; i < symbolTable.getNumProductions(); i++) {
            String leftHandSide = symbolTable.getNonTerminal(i);
            int rightHandSideLength = symbolTable.getProductionLength(i);
            codeBuilder.append("      { ").append(leftHandSide).append(", ").append(rightHandSideLength).append(" },\n");
        }

        codeBuilder.append("    };\n\n");
        codeBuilder.append("    this.rule = initRule;\n");
        codeBuilder.append("  }\n\n");

        codeBuilder.append("  private void initActionTable() {\n");
        codeBuilder.append("    actionTable = new ActionElement[").append(symbolTable.getNumStates()).append("][")
                .append(tokenTable.getNumTokens()).append("];\n\n");

        for (int i = 0; i < symbolTable.getNumStates(); i++) {
            for (int j = 0; j < tokenTable.getNumTokens(); j++) {
                ActionElement action = symbolTable.getAction(i, j);
                if (action != null) {
                    codeBuilder.append("    actionTable[").append(i).append("][").append(j).append("] = new ActionElement(ActionElement.")
                            .append(action.getType()).append(", ").append(action.getValue()).append(");\n");
                }
            }
        }

        codeBuilder.append("  }\n\n");

        codeBuilder.append("  private void initGotoTable() {\n");
        codeBuilder.append("    gotoTable = new int[").append(symbolTable.getNumStates()).append("][")
                .append(symbolTable.getNumNonTerminals()).append("];\n\n");

        for (int i = 0; i < symbolTable.getNumStates(); i++) {
            for (int j = 0; j < symbolTable.getNumNonTerminals(); j++) {
                int goTo = symbolTable.getGoto(i, j);
                if (goTo > 0) {
                    codeBuilder.append("    gotoTable[").append(i).append("][").append(j).append("] = ").append(goTo).append(";\n");
                }
            }
        }

        codeBuilder.append("  }\n\n");

        codeBuilder.append("}\n");

        return codeBuilder.toString();
    }

    public static void main(String[] args) {
        try {
            String grammarFilePath = "Main.txt";
            
            ParserGenerator parserGenerator = new ParserGenerator();
            parserGenerator.generateParser(grammarFilePath);
            System.out.println("Parser.java generado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al generar Parser.java: " + e.getMessage());
        }
    }


}
