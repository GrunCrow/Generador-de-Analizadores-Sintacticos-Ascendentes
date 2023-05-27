package ATS_Tree;

public class NonTerminalNode extends ASTNode {
    private String symbol;

    public NonTerminalNode(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
