package AST;

/**
 * Nodo del AST que representa un símbolo no terminal
 */
public class NonTerminalNode extends ASTNode {
    private String symbol;

    public NonTerminalNode(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}


