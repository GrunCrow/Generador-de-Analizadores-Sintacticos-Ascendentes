package ATS_Tree;

public class RuleNode extends ASTNode {
    private ASTNode[] children;

    public RuleNode(ASTNode... children) {
        this.children = children;
    }

    public ASTNode[] getChildren() {
        return children;
    }
}