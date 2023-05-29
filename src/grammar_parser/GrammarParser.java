package grammar_parser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import generated.SymbolConstants;
import generated.TokenConstants;

public class GrammarParser extends SLRParser {
    private Lexer lexer;
    private Token currentToken;

    protected List<String[]> rules_symbols;
    protected List<int[]> rules;


    public GrammarParser(String filePath) {
        try {
            lexer = new Lexer(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getNextToken();

        rules_symbols = new ArrayList<>();
        rules = new ArrayList<>();
        gotoTable = new int[0][0];
    }

    private void getNextToken() {
        try {
            lexer.getNextToken();
            currentToken = lexer.getCurrentToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parse() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        while (currentToken != null) {
            definicion();
        }
        initGotoTable();
    }

    private void definicion() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        // System.out.println(currentToken.getLexeme());
        if (currentToken.getKind() == TokenKind.NOTERMINAL) {
            String leftHandSide = currentToken.getLexeme();
            match(TokenKind.NOTERMINAL);
            match(TokenKind.EQ);
            listaReglas(leftHandSide);
            match(TokenKind.SEMICOLON);
        } else if (currentToken.getKind() == TokenKind.COMENTARIO) {
            parseComment();
        } else {
            match(TokenKind.NOTERMINAL);
        }
    }

    private void parseComment() {
        // Ignorar el comentario y obtener el siguiente token
        getNextToken();
    }

    private void listaReglas(String leftHandSide) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        regla(leftHandSide);
        while (currentToken.getKind() == TokenKind.BAR) {
            match(TokenKind.BAR);
            regla(leftHandSide);
        }
    }

    private void regla(String leftHandSide) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        List<String> rightHandSide = new ArrayList<>();
        int n_right_elements = 0;
        while (currentToken.getKind() == TokenKind.NOTERMINAL || currentToken.getKind() == TokenKind.TERMINAL) {
            String symbol = currentToken.getLexeme();
            getNextToken();
            rightHandSide.add(symbol);
            n_right_elements++;
        }

        // Verificar si ya existe una regla con el mismo lado izquierdo y lado derecho
        if (!ruleExists(leftHandSide, rightHandSide.toArray(new String[0]))) {
            rules_symbols.add(new String[]{leftHandSide, String.join(" ", rightHandSide)});
            
            
            // Obtener el campo correspondiente a la variable en la interfaz
            Field field = SymbolConstants.class.getField(leftHandSide);
            // Obtener el valor int de la constante
            int left_number = field.getInt(null);
            rules.add(new int[]{left_number, n_right_elements});
        }
    }

    private boolean ruleExists(String leftHandSide, String[] rightHandSide) {
        for (String[] rule : rules_symbols) {
            if (rule[0].equals(leftHandSide) && Arrays.equals(rule[1].split(" "), rightHandSide)) {
                return true;
            }
        }
        return false;
    }

    private void match(int expectedKind) {
        if (currentToken.getKind() == expectedKind) {
            getNextToken();
        } else {
            throw new RuntimeException("Error de sintaxis. Se esperaba " + expectedKind
                    + ", se encontró " + currentToken.getKind());
        }
    }

    // Métodos abstractos

    protected int rules_getRowCount() {
        // Implementación para obtener el número de filas en la tabla de reglas
        return rules_symbols.size();
    }

    protected int rules_getColumnCount() {
        // Implementación para obtener el número de columnas en la tabla de reglas
        return 2;
    }

    protected String getLeftHandSide(int row) {
        // Implementación para obtener el lado izquierdo de una regla en la tabla de reglas
        if (row >= 0 && row < rules_getRowCount()) {
            return rules_symbols.get(row)[0];
        }
        return null;
    }

    protected String[] getRightHandSide(int row) {
        // Implementación para obtener el lado derecho de una regla en la tabla de reglas
        if (row >= 0 && row < rules_getRowCount()) {
            return rules_symbols.get(row)[1].split(" ");
        }
        return null;
    }

    protected void setRule(int rowIndex, String leftHandSide, String[] rightHandSide) {
        // No se implementa este método ya que la lógica se ha modificado para evitar la sobreescritura de reglas
    }
    
    
    private List<String> obtenerSimbolosNoTerminales() {
        List<String> nonTerminals = new ArrayList<>();
        try {
            Class<?> symbolConstantsClass = Class.forName("generated.SymbolConstants");
            Field[] fields = symbolConstantsClass.getDeclaredFields();
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && field.getType() == int.class) {
                    nonTerminals.add(field.getName());
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return nonTerminals;
    }
    
    private List<String> obtenerSimbolosTerminales() {
        List<String> Terminals = new ArrayList<>();
        try {
            Class<?> tokenConstantsClass = Class.forName("generated.TokenConstants");
            Field[] fields = tokenConstantsClass.getDeclaredFields();
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && field.getType() == int.class) {
                    Terminals.add(field.getName());
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Terminals;
    }
    
    // Generar Tabla Goto
    /*
     * recorriendo cada regla de la gramática y asignando los valores adecuados 
     * en la tabla. Se utiliza el índice del símbolo no terminal en la lista 
     * nonTerminalSymbols como la columna en la tabla gotoTable. 
     * El valor asignado es simplemente el índice de la regla más uno 
     * (considerando que las reglas se numeran a partir de 1).
     * */
    private void initGotoTable() {
        List<String> nonTerminalSymbols = obtenerSimbolosNoTerminales();
        List<String> TerminalSymbols = obtenerSimbolosTerminales();
        
        int numStates = rules.size();
        this.gotoTable = new int[numStates][nonTerminalSymbols.size()];

        for (int i = 0; i < rules.size(); i++) {
            String leftHandSide = getLeftHandSide(i);
            int leftHandSideIndex = nonTerminalSymbols.indexOf(leftHandSide);

            String[] rightHandSide = getRightHandSide(i);

            for (int j = 0; j < rightHandSide.length; j++) {
                String symbol = rightHandSide[j];
                int symbolIndex = nonTerminalSymbols.indexOf(symbol);

                if (symbolIndex != -1) {
                    this.gotoTable[i][symbolIndex] = i + 1;
                }
            }
        }
    }


    
    // Generar Tabla Goto
    
    
    // Devolver tablas

    public String[][] getRules_symbol() {
        String[][] rulesArray = new String[rules_symbols.size()][2];
        for (int i = 0; i < rules_symbols.size(); i++) {
            rulesArray[i] = rules_symbols.get(i);
        }
        return rulesArray;
    }
    
    public int[][] getRules() {
        int[][] rulesArray = new int[rules.size()][2];
        for (int i = 0; i < rules.size(); i++) {
            rulesArray[i] = rules.get(i);
        }
        return rulesArray;
    }
    
    public int[][] getGotoTable() {
        return gotoTable;
    }

    public ActionElement[][] getActionsTable() {
		return actionTable;
	}
    
    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        GrammarParser parser = new GrammarParser("Main.txt");
        parser.parse();
        int[][] rules = parser.getRules();
        System.out.println("Reglas:");
        System.out.println(Arrays.deepToString(rules));
        
        parser.initGotoTable();

        int[][] gotoTable = parser.getGotoTable();
        System.out.println("Tabla Goto:");
        System.out.println(Arrays.deepToString(gotoTable));
    }
}
