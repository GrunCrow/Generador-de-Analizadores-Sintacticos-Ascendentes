package grammar_parser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class GrammarParser extends SLRParser {
    private Lexer lexer;
    private Token currentToken;

    protected List<String[]> rules;

    public GrammarParser(String filePath) {
        try {
            lexer = new Lexer(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getNextToken();

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

    public void parse() {
        while (currentToken != null) {
            definicion();
        }
    }

    private void definicion() {
        // System.out.println(currentToken.getLexeme());
        if (currentToken.getKind() == TokenKind.NOTERMINAL) {
            String leftHandSide = currentToken.getLexeme();
            match(TokenKind.NOTERMINAL);
            match(TokenKind.EQ);
            listaReglas(leftHandSide);
            match(TokenKind.SEMICOLON);
            
            // Generar la tabla Goto
            generarGotoTable();
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
        regla(leftHandSide);
        while (currentToken.getKind() == TokenKind.BAR) {
            match(TokenKind.BAR);
            regla(leftHandSide);
        }
    }

    private void regla(String leftHandSide) {
        List<String> rightHandSide = new ArrayList<>();
        while (currentToken.getKind() == TokenKind.NOTERMINAL || currentToken.getKind() == TokenKind.TERMINAL) {
            String symbol = currentToken.getLexeme();
            getNextToken();
            rightHandSide.add(symbol);
        }

        // Verificar si ya existe una regla con el mismo lado izquierdo y lado derecho
        if (!ruleExists(leftHandSide, rightHandSide.toArray(new String[0]))) {
            rules.add(new String[]{leftHandSide, String.join(" ", rightHandSide)});
        }
    }

    private boolean ruleExists(String leftHandSide, String[] rightHandSide) {
        for (String[] rule : rules) {
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

    // Métodos abstractos a implementar

    protected int rules_getRowCount() {
        // Implementación para obtener el número de filas en la tabla de reglas
        return rules.size();
    }

    protected int rules_getColumnCount() {
        // Implementación para obtener el número de columnas en la tabla de reglas
        return 2;
    }

    protected String getLeftHandSide(int row) {
        // Implementación para obtener el lado izquierdo de una regla en la tabla de reglas
        if (row >= 0 && row < rules_getRowCount()) {
            return rules.get(row)[0];
        }
        return null;
    }

    protected String[] getRightHandSide(int row) {
        // Implementación para obtener el lado derecho de una regla en la tabla de reglas
        if (row >= 0 && row < rules_getRowCount()) {
            return rules.get(row)[1].split(" ");
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
    
    
    // Generar Tabla Goto    
    private void generarGotoTable() {
    	// Obtener los símbolos no terminales
        List<String> nonTerminals = obtenerSimbolosNoTerminales();
        
        // Numero de estados
        n_estados = 0;
        
        // Crear una matriz para la tabla Goto
        gotoTable = new int[n_estados][nonTerminals.size()];

        
    }
    
    
    // Devolver tablas

    public String[][] getRules() {
        String[][] rulesArray = new String[rules.size()][2];
        for (int i = 0; i < rules.size(); i++) {
            rulesArray[i] = rules.get(i);
        }
        return rulesArray;
    }
    
    public int[][] getGotoTable() {
        return gotoTable;
    }

    public static void main(String[] args) {
        GrammarParser parser = new GrammarParser("Main.txt");
        parser.parse();
        String[][] rules = parser.getRules();
        System.out.println("Reglas:");
        System.out.println(Arrays.deepToString(rules));

        int[][] gotoTable = parser.getGotoTable();
        System.out.println("Tabla Goto:");
        System.out.println(Arrays.deepToString(gotoTable));
    }
}
