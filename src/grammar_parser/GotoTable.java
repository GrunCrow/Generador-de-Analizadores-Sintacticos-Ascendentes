package grammar_parser;

// se representa la tabla Goto generada por el algoritmo SLR
public class GotoTable {
	private GotoElement[][] table;
    private int rowCount;
    private int columnCount;

    public GotoTable() {
        table = new GotoElement[0][0];
        rowCount = 0;
        columnCount = 0;
    }

    public GotoTable(int rows, int columns) {
        table = new GotoElement[rows][columns];
        rowCount = rows;
        columnCount = columns;
    }

    public void setGoto(int row, int column, GotoElement gotoElement) {
    	expandTable(row, column);
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
    
    private void expandTable(int row, int column) {
        if (row >= rowCount || column >= columnCount) {
            int newRowCount = Math.max(row + 1, rowCount);
            int newColumnCount = Math.max(column + 1, columnCount);

            GotoElement[][] newTable = new GotoElement[newRowCount][newColumnCount];

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
		GotoElement[][] newTable = new GotoElement[numStates][numSymbols];
		
		table = newTable;
        rowCount = numStates;
        columnCount = numSymbols;
		
	}
}
