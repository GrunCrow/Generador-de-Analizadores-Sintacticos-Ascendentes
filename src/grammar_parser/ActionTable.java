package grammar_parser;

// se representa la tabla de acciones generada por el algoritmo SLR
public class ActionTable {
    private ActionElement[][] table;

    public ActionTable(int rows, int columns) {
        table = new ActionElement[rows][columns];
    }

    public void setAction(int row, int column, ActionElement action) {
        table[row][column] = action;
    }

    public ActionElement getAction(int row, int column) {
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
