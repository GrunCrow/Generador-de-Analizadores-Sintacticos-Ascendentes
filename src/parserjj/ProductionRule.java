package parserjj;

class ProductionRule {
    private int leftSide;
    private String[] rightSideSymbols;

    public ProductionRule(int leftSide, String[] rightSideSymbols) {
        this.leftSide = leftSide;
        this.rightSideSymbols = rightSideSymbols;
    }

    public int getLeftSide() {
        return leftSide;
    }

    public String[] getRightSideSymbols() {
        return rightSideSymbols;
    }

    public int getIndex() {
        return leftSide + 1;
    }
}
