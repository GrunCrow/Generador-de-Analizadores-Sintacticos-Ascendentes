package grammar_parser;

// se representa la tabla Goto generada por el algoritmo SLR
public class GotoTable {
    private GotoElement[][] table;

    public GotoTable(int rows, int columns) {
        table = new GotoElement[rows][columns];
    }

    public void setGoto(int row, int column, GotoElement gotoElement) {
        table[row][column] = gotoElement;
    }

    public GotoElement getGoto(int row, int column) {
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
