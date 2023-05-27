package parserjj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import generated.TokenConstants;

public class SymbolTable {
    private List<Integer> nonTerminals;
    private List<int[]> productions;
    private Map<String, Integer> nonTerminalIndices;

    public SymbolTable() {
        nonTerminals = new ArrayList<>();
        productions = new ArrayList<>();
        nonTerminalIndices = new HashMap<>();
    }

    public void addNonTerminal(int nonTerminalIndex) {
        nonTerminals.add(nonTerminalIndex);
    }

    public void addProduction(int nonTerminalIndex, int productionLength) {
        // int productionIndex = productions.size();
        productions.add(new int[]{nonTerminalIndex, productionLength});
    }

    public int getNonTerminalIndex(String nonTerminal) {
        if (!nonTerminalIndices.containsKey(nonTerminal)) {
            int index = nonTerminalIndices.size();
            nonTerminalIndices.put(nonTerminal, index);
        }
        return nonTerminalIndices.get(nonTerminal);
    }
    
    public String getNonTerminal(int nonTerminalIndex) {
        for (Map.Entry<String, Integer> entry : nonTerminalIndices.entrySet()) {
            if (entry.getValue() == nonTerminalIndex) {
                return entry.getKey();
            }
        }
        return null;
    }


    public int getNumStates() {
        return productions.size();
    }

    public int getNumProductions() {
        return productions.size();
    }

    public int getNumNonTerminals() {
        return nonTerminals.size();
    }

    public int getProductionLength(int productionIndex) {
        return productions.get(productionIndex)[1];
    }

    public ActionElement getAction(int state, int token) {
        for (int productionIndex = 0; productionIndex < productions.size(); productionIndex++) {
            int[] production = productions.get(productionIndex);
            int nonTerminal = production[0];
            int productionLength = production[1];
            if (nonTerminal == state && productionLength == 1 && token == TokenConstants.EOF) {
                return new ActionElement(ActionElement.ACCEPT, 0);
            } else if (nonTerminal == state && productionLength > 0 && token == TokenConstants.EOF) {
                return new ActionElement(ActionElement.REDUCE, productionIndex);
            }
        }
        return null;
    }

    public int getGoto(int state, int nonTerminal) {
        for (int productionIndex = 0; productionIndex < productions.size(); productionIndex++) {
            int[] production = productions.get(productionIndex);
            int leftHandSide = production[0];
            int productionLength = production[1];
            if (leftHandSide == state && productionLength > 0 && nonTerminal == nonTerminals.get(0)) {
                return productionIndex;
            }
        }
        return -1;
    }
}
