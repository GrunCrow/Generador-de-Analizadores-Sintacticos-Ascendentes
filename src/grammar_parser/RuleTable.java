package grammar_parser;

public class RuleTable {
    private Rule[][] table;

    public RuleTable(int rows, int columns) {
        table = new Rule[rows][columns];
    }

    public void setRule(int row, int column, Rule rule) {
        table[row][column] = rule;
    }

    public Rule getRule(int row, int column) {
        return table[row][column];
    }

    public int getRowCount() {
        return table.length;
    }

    public int getColumnCount() {
        if (table.length == 0) {
            return 0;
        }
        return table[0].length;
    }
}
