package AST;

import java.util.List;

/**
 * Nodo del AST que representa una definición de la gramática
 */
public class DefinitionNode extends ASTNode {
    private NonTerminalNode nonTerminal;
    private List<ASTNode> ruleList;

    public DefinitionNode(NonTerminalNode nonTerminal, List<ASTNode> ruleList) {
        this.nonTerminal = nonTerminal;
        this.ruleList = ruleList;
    }

    public NonTerminalNode getNonTerminal() {
        return nonTerminal;
    }

    public List<ASTNode> getRuleList() {
        return ruleList;
    }
}

