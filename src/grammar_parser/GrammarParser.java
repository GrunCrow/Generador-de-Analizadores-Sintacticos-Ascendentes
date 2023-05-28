package grammar_parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

import generated.SymbolConstants;
import generated.TokenConstants;

public class GrammarParser extends SLRParser{
    private Lexer lexer;
    private Token currentToken;
    private RuleTable ruleTable;
    private ActionTable actionTable;
    private GotoTable gotoTable;

    public GrammarParser(String filePath) {
        try {
            lexer = new Lexer(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getNextToken();
        ruleTable = new RuleTable(); // Inicializar con una tabla vacía
        actionTable = new ActionTable(); // Inicializar con una tabla vacía
        gotoTable = new GotoTable(); // Inicializar con una tabla vacía
    }

    private void getNextToken() {
        try {
            lexer.getNextToken();
            currentToken = lexer.getCurrentToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parse() {
        while (currentToken != null) {
            definicion();
        }
    }

    private void definicion() {
    	System.out.println(currentToken.getLexeme());
    	System.out.println(currentToken.getKind());
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

    private void listaReglas(String leftHandSide) {
        // regla(leftHandSide);
        while (currentToken.getKind() != TokenKind.SEMICOLON) {     
            regla(leftHandSide);
            match(TokenKind.BAR);
        }
    }

    private void regla(String leftHandSide) {
        String[] rightHandSide = new String[0];
        while (currentToken.getKind() == TokenKind.NOTERMINAL || currentToken.getKind() == TokenKind.TERMINAL) {
        	String symbol;
            if (currentToken.getKind() == TokenKind.TERMINAL) {
                symbol = currentToken.getLexeme();
                getNextToken();
            } else {
                symbol = currentToken.getLexeme();
                getNextToken();
            }
            rightHandSide = appendToArray(rightHandSide, symbol);
        }

        // Obtener el índice de fila y columna en la tabla de reglas
        int rowIndex = getRuleRowIndex(leftHandSide);
        int columnIndex = getRuleColumnIndex(rightHandSide);

        // Crear y guardar la regla en la tabla de reglas
        Rule rule = new Rule(leftHandSide, rightHandSide);
        ruleTable.setRule(rowIndex, columnIndex, rule);
    }
    
    private String[] appendToArray(String[] array, String element) {
        int length = array.length;
        String[] newArray = Arrays.copyOf(array, length + 1);
        newArray[length] = element;
        return newArray;
    }
    
    private int getRuleRowIndex(String leftHandSide) {
        for (int row = 0; row < ruleTable.getRowCount(); row++) {
            Rule rule = ruleTable.getRule(row, 0);
            if (rule != null && rule.getLeftHandSide().equals(leftHandSide)) {
                return row;
            }
        }
        return ruleTable.getRowCount(); // Si no se encuentra el leftHandSide en la tabla de reglas
    }

    private int getRuleColumnIndex(String[] rightHandSide) {
        for (int column = 0; column < ruleTable.getColumnCount(); column++) {
            Rule rule = ruleTable.getRule(0, column);
            if (rule != null && rule.getRightHandSide().length == rightHandSide.length) {
                boolean matches = true;
                for (int i = 0; i < rightHandSide.length; i++) {
                    if (!rule.getRightHandSide()[i].equals(rightHandSide[i])) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    return column;
                }
            }
        }
        return ruleTable.getColumnCount(); // Si no se encuentra el rightHandSide en la tabla de reglas
    }
    
    
    private int getTokenCount() {
        int maxToken = -1;
        
        // Itera sobre los campos de la interfaz TokenConstants
        // para encontrar el máximo valor de categoría léxica (token)
        Class<TokenConstants> tokenConstantsClass = TokenConstants.class;
        java.lang.reflect.Field[] fields = tokenConstantsClass.getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            if (field.getType() == int.class) {
                try {
                    int tokenValue = field.getInt(null);
                    if (tokenValue > maxToken) {
                        maxToken = tokenValue;
                    }
                } catch (IllegalAccessException e) {
                    // Manejo de excepciones si es necesario
                }
            }
        }
        
        // El número de columnas es el máximo valor de categoría léxica (token) + 1
        return maxToken + 1;
    }
    
    private int getSymbolCount() {
        int maxSymbol = -1;
        
        // Itera sobre los campos de la interfaz TokenConstants
        // para encontrar el máximo valor de categoría léxica (token)
        Class<SymbolConstants> SymbolConstantsClass = SymbolConstants.class;
        java.lang.reflect.Field[] fields = SymbolConstantsClass.getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            if (field.getType() == int.class) {
                try {
                    int symbolValue = field.getInt(null);
                    if (symbolValue > maxSymbol) {
                    	maxSymbol = symbolValue;
                    }
                } catch (IllegalAccessException e) {
                    // Manejo de excepciones si es necesario
                }
            }
        }
        
        // El número de columnas es el máximo valor de categoría léxica (token) + 1
        return maxSymbol + 1;
    }
    
    
	


    private GotoTable getGoto(int state, int symbol) {
        return gotoTable;
    }

    
    
    
    
    public RuleTable getRulesTable() {
    	return ruleTable;
    }
    
    
    public ActionTable getActionsTable() {
    	return actionTable;
    }
    
    
    
    
    
    
    
    
    

    private void match(int expectedTokenKind) {
        if (currentToken != null && currentToken.getKind() == expectedTokenKind) {
            getNextToken();
        } else {
            reportError("Se esperaba " + expectedTokenKind);
        }
    }

    private void reportError(String message) {
        System.err.println("Error: " + message);
    }

    public void close() {
        lexer.close();
    }
}
