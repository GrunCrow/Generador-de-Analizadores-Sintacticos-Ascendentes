package grammar_parser;

// se representa la tabla de acciones generada por el algoritmo SLR
public class ActionTable {
    private ActionElement[][] table;
    private int rowCount;
    private int columnCount;

    public ActionTable() {
        table = new ActionElement[0][0];
        rowCount = 0;
        columnCount = 0;
    }
    
    public ActionTable(int rows, int columns) {
        table = new ActionElement[rows][columns];
        rowCount = rows;
        columnCount = columns;
    }

    public void setAction(int row, int column, ActionElement action) {
    	expandTable(row, column);
        table[row][column] = action;
    }

    public ActionElement getAction(int row, int column) {
        return table[row][column];
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

            ActionElement[][] newTable = new ActionElement[newRowCount][newColumnCount];

            // Copiar las reglas existentes a la nueva tabla
            for (int i = 0; i < rowCount; i++) {
                System.arraycopy(table[i], 0, newTable[i], 0, columnCount);
            }

            table = newTable;
            rowCount = newRowCount;
            columnCount = newColumnCount;
        }
    }

	public void initialize(int numStates, int numSymbols) {
		ActionElement[][] newTable = new ActionElement[numStates][numSymbols];
		
		table = newTable;
        rowCount = numStates;
        columnCount = numSymbols;
		
	}
}
