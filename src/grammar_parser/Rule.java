package grammar_parser;

public class Rule {
    private String leftHandSide;
    private String[] rightHandSide;

    public Rule(String leftHandSide, String[] rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    public String getLeftHandSide() {
        return leftHandSide;
    }

    public String[] getRightHandSide() {
        return rightHandSide;
    }
}
