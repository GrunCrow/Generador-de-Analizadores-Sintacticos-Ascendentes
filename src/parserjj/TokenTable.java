package parserjj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenTable {
    private List<String> terminals;
    private Map<String, Integer> terminalIndices;

    public TokenTable() {
        terminals = new ArrayList<>();
        terminalIndices = new HashMap<>();
    }

    public void addTerminal(String terminal) {
        if (!terminalIndices.containsKey(terminal)) {
            int index = terminalIndices.size();
            terminalIndices.put(terminal, index);
            terminals.add(terminal);
        }
    }

    public int getNumTokens() {
        return terminals.size();
    }

    public int getTokenIndex(String token) {
        return terminalIndices.get(token);
    }
}
