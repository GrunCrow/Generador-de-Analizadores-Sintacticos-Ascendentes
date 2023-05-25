package parserjj;

import java.io.IOException;
import java.util.Stack;

public class SyntaxAnalyzer extends SLRParser {
    private GrammarParser grammarParser;

    public SyntaxAnalyzer(GrammarParser grammarParser) {
        this.grammarParser = grammarParser;
        this.setActionTable(grammarParser.getActionTable());
        this.setGotoTable(grammarParser.getGotoTable());
        this.setRule(grammarParser.getRules());
    }

    public boolean analyze(String input) throws IOException, SintaxException {
        Lexer lexer = new Lexer(input);
        return parse(lexer);
    }

    @Override
    protected void shiftAction(ActionElement action) throws IOException {
        super.shiftAction(action);
        // Aquí puedes agregar cualquier lógica adicional después de realizar un desplazamiento
    }

    @Override
    protected void reduceAction(ActionElement action) {
        super.reduceAction(action);
        // Aquí puedes agregar cualquier lógica adicional después de realizar una reducción
    }

    public GrammarParser getGrammarParser() {
        return grammarParser;
    }

    public void setGrammarParser(GrammarParser grammarParser) {
        this.grammarParser = grammarParser;
    }
}

