package grammar_parser;

public class RuleTable {
    private Rule[][] table;
    private int rowCount;
    private int columnCount;

    public RuleTable() {
        table = new Rule[0][0];
        rowCount = 0;
        columnCount = 0;
    }
    
    public RuleTable(int rows, int columns) {
        table = new Rule[rows][columns];
        rowCount = rows;
        columnCount = columns;
    }

    public void setRule(int row, int column, Rule rule) {
        expandTable(row, column);
        table[row][column] = rule;
    }

    public Rule getRule(int row, int column) {
        return table[row][column];
    }
    
    public Rule[] getRulebyRow(int row) {
        return table[row];
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    private void expandTable(int row, int column) {
        if (row >= rowCount || column >= columnCount) {
            int newRowCount = Math.max(row + 1, rowCount);
            int newColumnCount = Math.max(column + 1, columnCount);

            Rule[][] newTable = new Rule[newRowCount][newColumnCount];

            // Copiar las reglas existentes a la nueva tabla
            for (int i = 0; i < rowCount; i++) {
                System.arraycopy(table[i], 0, newTable[i], 0, columnCount);
            }

            table = newTable;
            rowCount = newRowCount;
            columnCount = newColumnCount;
        }
    }
}
