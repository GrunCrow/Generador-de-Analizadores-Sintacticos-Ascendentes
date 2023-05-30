package AST;

/**
 * Nodo del AST que representa un símbolo terminal
 */
class TerminalNode extends ASTNode {
    private String symbol;

    public TerminalNode(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}

