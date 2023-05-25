package parserjj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GrammarParser {
    private Map<String, Integer> nonTerminalsMap;
    private Map<String, Integer> terminalsMap;
    private List<String> nonTerminals;
    private List<String> terminals;
    private List<ProductionRule> productionRules;
    
    ActionElement[][] actionTable;
    int[][] gotoTable;
    int[][] rules;

    public GrammarParser() {
        nonTerminalsMap = new HashMap<>();
        terminalsMap = new HashMap<>();
        nonTerminals = new ArrayList<>();
        terminals = new ArrayList<>();
        productionRules = new ArrayList<>();
    }

    public void parseGrammar(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int lineNumber = 1; // Agregar contador de línea
        while ((line = reader.readLine()) != null) {
        	if (line.trim().isEmpty() || line.startsWith("/*")) {
        		lineNumber++;
                continue;
            }
        	else {
        		line = "";
	            String next_line;
	            while (!line.trim().endsWith(";")) {
	            	if ((next_line = reader.readLine()) != null){
	            		if (next_line.trim().isEmpty() || next_line.startsWith("/*")) {
	            			lineNumber++;
	                        continue;
	                    }
	            		line = line + " " + next_line;
	            		lineNumber++;
	            	}
	            }
        	}
            
            try {
                processProductionRule(line);
            } catch (Exception e) {
                System.out.println("Error en la línea " + lineNumber + ": " + line);
                e.printStackTrace();
            }
        }
        reader.close();
    }


    private void processProductionRule(String rule) {
        String[] parts = rule.split("::=");
        String leftSide = parts[0].trim();
        String rightSide = parts[1].trim();
        String[] rightSideSymbols = rightSide.split("\\s+");
        
        System.out.println("Left Side: " + leftSide);
        System.out.println("Right Side Symbols: " + Arrays.toString(rightSideSymbols));


        int leftSideIndex = getNonTerminalIndex(leftSide);
        productionRules.add(new ProductionRule(leftSideIndex, rightSideSymbols));
    }


    private int getNonTerminalIndex(String nonTerminal) {
        if (!nonTerminalsMap.containsKey(nonTerminal)) {
            int index = nonTerminals.size();
            nonTerminalsMap.put(nonTerminal, index);
            nonTerminals.add(nonTerminal);
        }
        return nonTerminalsMap.get(nonTerminal);
    }

    private int getTerminalIndex(String terminal) {
        if (!terminalsMap.containsKey(terminal)) {
            int index = terminals.size();
            terminalsMap.put(terminal, index);
            terminals.add(terminal);
        }
        return terminalsMap.get(terminal);
    }

    public ActionElement[][] generateActionTable() {
        int numStates = nonTerminals.size() + terminals.size();
        ActionElement[][] actionTable = new ActionElement[numStates][terminals.size()];

        for (ProductionRule rule : productionRules) {
            int leftSide = rule.getLeftSide();
            String[] rightSideSymbols = rule.getRightSideSymbols();

            if (rightSideSymbols[0].equals("EPSILON")) {
                for (int i = 0; i < terminals.size(); i++) {
                    actionTable[leftSide][i] = new ActionElement(ActionElement.REDUCE, -1);
                }
                continue;
            }

            for (String symbol : rightSideSymbols) {
                int symbolIndex;
                if (nonTerminalsMap.containsKey(symbol)) {
                    symbolIndex = nonTerminalsMap.get(symbol) + terminals.size();
                } else {
                    symbolIndex = terminalsMap.get(symbol);
                }
                actionTable[leftSide][symbolIndex] = new ActionElement(ActionElement.REDUCE, -1);
            }
        }

        return actionTable;
    }

    public int[][] generateGotoTable() {
        int numStates = nonTerminals.size() + terminals.size();
        int[][] gotoTable = new int[numStates][nonTerminals.size()];

        for (ProductionRule rule : productionRules) {
            int leftSide = rule.getLeftSide();
            String[] rightSideSymbols = rule.getRightSideSymbols();

            if (rightSideSymbols[0].equals("EPSILON")) {
                continue;
            }

            String lastSymbol = rightSideSymbols[rightSideSymbols.length - 1];
            if (nonTerminalsMap.containsKey(lastSymbol)) {
                int symbolIndex = nonTerminalsMap.get(lastSymbol);
                gotoTable[leftSide][symbolIndex] = rule.getIndex();
            }
        }

        return gotoTable;
    }

    public int[][] generateRules() {
        int[][] rules = new int[productionRules.size()][2];

        for (int i = 0; i < productionRules.size(); i++) {
            ProductionRule rule = productionRules.get(i);
            rules[i][0] = rule.getLeftSide();
            rules[i][1] = rule.getRightSideSymbols().length;
        }

        return rules;
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public List<ProductionRule> getProductionRules() {
        return productionRules;
    }

	public ActionElement[][] getActionTable() {
		return actionTable;
	}

	public void setActionTable(ActionElement[][] actionTable) {
		this.actionTable = actionTable;
	}

	public int[][] getGotoTable() {
		return gotoTable;
	}

	public void setGotoTable(int[][] gotoTable) {
		this.gotoTable = gotoTable;
	}

	public int[][] getRules() {
		return rules;
	}

	public void setRules(int[][] rules) {
		this.rules = rules;
	}
    
    
}
