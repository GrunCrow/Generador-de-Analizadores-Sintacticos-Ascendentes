package ATS_Tree;

public class TerminalNode extends ASTNode {
    private String symbol;

    public TerminalNode(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
