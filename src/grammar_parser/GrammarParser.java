package grammar_parser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import ATS_Tree.ASTNode;
import generated.SymbolConstants;
import generated.TokenConstants;

public class GrammarParser extends SLRParser {
    private Lexer lexer;
    private Token currentToken;

    protected List<String[]> rules_symbols;
    protected List<int[]> rules;
    
    protected ActionElement[][] actionTable;
    protected List<String> nonTerminals;
    protected List<String> Terminals;
    
    private ASTNode ast;
    protected int[][] gotoTable; // Tabla Goto

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
    	ast = new ASTNode("ROOT"); // Crear un nodo raíz para el árbol de sintaxis abstracta
    	nonTerminals = obtenerSimbolosNoTerminales();
    	Terminals = obtenerSimbolosTerminales();
    	while (currentToken != null) {
            definicion();
        }
        //initGotoTable();
        
        // Imprimir el árbol de sintaxis abstracta
        //printAST(ast, 0);
        
    }

    private void definicion() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        // System.out.println(currentToken.getLexeme());
        if (currentToken.getKind() == TokenKind.NOTERMINAL) {
            String leftHandSide = currentToken.getLexeme();
            
            ASTNode ruleNode = new ASTNode(leftHandSide); // Crear un nodo para la regla
            ast.addChild(ruleNode); // Agregar el nodo al árbol
            
            match(TokenKind.NOTERMINAL);
            match(TokenKind.EQ);
            
            listaReglas(ruleNode);
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
    
    
    
    
    // ======================================================================================
    //									Auxiliares
    // ===========================================================================

    
    
    
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
    
    private int getNonTerminalIndex(String nonTerminal) {
        return nonTerminals.indexOf(nonTerminal);
    }

    private int getTerminalIndex(String terminal) {
        return Terminals.indexOf(terminal);
    }
    
    private boolean isNonTerminal(String symbol) {
        return nonTerminals.contains(symbol);
    }
    
    private boolean isTerminal(String symbol) {
        return Terminals.contains(symbol);
    }
    
    
    
    
    
    
    
    // ======================================================================================
    //									Reglas
    // ===========================================================================

    private void listaReglas(ASTNode parent) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        regla(parent);
        while (currentToken.getKind() == TokenKind.BAR) {
            match(TokenKind.BAR);
            regla(parent);
        }
    }

    private void regla(ASTNode parent) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        List<String> rightHandSide = new ArrayList<>();
        int n_right_elements = 0;
        while (currentToken.getKind() == TokenKind.NOTERMINAL || currentToken.getKind() == TokenKind.TERMINAL) {
            String symbol = currentToken.getLexeme();
            getNextToken();
            rightHandSide.add(symbol);
            n_right_elements++;
        }

        // Verificar si ya existe una regla con el mismo lado izquierdo
        int ruleIndex = findRuleIndex(parent.getLabel(), rightHandSide);
        if (ruleIndex == -1) {
            ruleIndex = rules_symbols.size(); // Asignar un nuevo índice para la regla

            // Agregar el lado izquierdo y derecho de la regla a las listas correspondientes
            rules_symbols.add(rightHandSide.toArray(new String[0]));
            rules.add(new int[n_right_elements + 1]);
            rules.get(ruleIndex)[0] = getNonTerminalIndex(parent.getLabel());
        }

        // Agregar el nodo de la regla como hijo del nodo padre en el árbol de sintaxis abstracta
        ASTNode ruleNode = new ASTNode(Arrays.toString(rules_symbols.get(ruleIndex)));
        parent.addChild(ruleNode);

        // Crear y agregar los nodos terminales y no terminales como hijos del nodo de la regla
        for (String symbol : rightHandSide) {
            ASTNode symbolNode;
            if (isNonTerminal(symbol)) {
                symbolNode = new ASTNode(symbol);
            } else {
                symbolNode = new ASTNode("'" + symbol + "'");
            }
            ruleNode.addChild(symbolNode);
        }
        // match(TokenKind.SEMICOLON);
    }
    
    private int findRuleIndex(String leftHandSide, List<String> rightHandSide) {
        for (int i = 0; i < rules_symbols.size(); i++) {
            String[] symbols = rules_symbols.get(i);
            if (symbols.length == rightHandSide.size() && symbols[0].equals(leftHandSide)) {
                boolean match = true;
                for (int j = 0; j < rightHandSide.size(); j++) {
                    if (!symbols[j + 1].equals(rightHandSide.get(j))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean ruleExists(String leftHandSide, String[] rightHandSide) {
        for (String[] rule : rules_symbols) {
            if (rule[0].equals(leftHandSide) && Arrays.equals(rule[1].split(" "), rightHandSide)) {
                return true;
            }
        }
        return false;
    }

    private int getRuleIndex(String symbol) {
        // Recorrer las definiciones en el árbol AST
        for (ASTNode definition : ast.getChildren()) {
            // Obtener el símbolo no terminal de la definición
            String nonTerminal = definition.getChildren().get(0).getLabel();

            // Verificar si el símbolo coincide con el símbolo no terminal de la definición
            if (symbol.equals(nonTerminal)) {
                // Devolver el índice de la definición como índice de regla
                return ast.getChildren().indexOf(definition);
            }
        }

        // Si no se encuentra una definición correspondiente, devolver -1
        return -1;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // ======================================================================================
    //									ActionTable
    // ===========================================================================
    
    public int countStates(ASTNode node) {
        int count = 1; // Contar el nodo actual

        // Recorrer los hijos del nodo actual y contar los nodos en cada uno
        for (ASTNode child : node.getChildren()) {
            count += countStates(child);
        }

        return count;
    }
    
    private Set<String> getFirstSet(ASTNode state) {
        Set<String> firstSet = new HashSet<>();

        // Obtener los hijos directos del estado actual
        List<ASTNode> children = state.getChildren();

        for (ASTNode child : children) {
            // Verificar si el hijo es un símbolo no terminal
            if (!isTerminal(child.getLabel())) {
                // Obtener el conjunto FIRST para el símbolo no terminal
                Set<String> childFirstSet = getFirstSet(child);

                // Agregar los elementos del conjunto FIRST del hijo al conjunto FIRST del estado actual
                firstSet.addAll(childFirstSet);
            } else {
                // El hijo es un símbolo terminal, agregarlo al conjunto FIRST del estado actual
                firstSet.add(child.getLabel());
            }
        }

        return firstSet;
    }
    
    private Set<String> getFollowSet(ASTNode state) {
        Set<String> followSet = new HashSet<>();

        // Obtener el padre del estado actual
        ASTNode parent = state.getParent();

        // Verificar si el estado actual es el hijo derecho de su padre
        boolean isRightChild = (parent != null && parent.getChildren().indexOf(state) == parent.getChildren().size() - 1);

        if (isRightChild) {
            // El estado actual es el hijo derecho de su padre, obtener el conjunto FOLLOW para el padre
            followSet = getFollowSet(parent);
        } else {
            // El estado actual no es el hijo derecho de su padre, buscar el siguiente hermano del estado actual
            ASTNode sibling = state.getNextSibling();

            while (sibling != null) {
                // Obtener el conjunto FIRST para el siguiente hermano
                Set<String> siblingFirstSet = getFirstSet(sibling);

                // Agregar los elementos del conjunto FIRST del siguiente hermano al conjunto FOLLOW del estado actual
                followSet.addAll(siblingFirstSet);

                // Buscar el siguiente hermano
                sibling = sibling.getNextSibling();
            }

            // Si no se encontró ningún siguiente hermano, obtener el conjunto FOLLOW para el padre del estado actual
            if (sibling == null) {
                followSet.addAll(getFollowSet(parent));
            }
        }

        return followSet;
    }
    
    private int getNextState(ASTNode state, String symbol) {
        List<ASTNode> children = state.getChildren();

        // Buscar el nodo hijo correspondiente al símbolo de entrada
        for (ASTNode child : children) {
            if (child.getLabel().equals(symbol)) {
                // Obtener el índice del estado hijo
                return child.getIndex();
            }
        }

        // Si no se encuentra el símbolo de entrada en los hijos, devolver -1 (estado inválido)
        return -1;
    }

    private String getFinalSymbolFromAST(ASTNode root) {
        // Recorrer el árbol AST hasta encontrar el último nodo terminal
        ASTNode currentNode = root;
        while (!currentNode.getChildren().isEmpty()) {
            List<ASTNode> children = currentNode.getChildren();
            currentNode = children.get(children.size() - 1);
        }

        // Devolver el nombre del último nodo terminal como símbolo final
        return currentNode.getLabel();
    }
    
    private boolean isFinalState(ASTNode state) {
        // Un estado final es aquel en el que no tiene hijos, es decir, es un nodo terminal
        return state.getChildren().isEmpty();
    }
    
    private void generateActionTable() {
        ASTNode root = ast;
        int numStates = countStates(root);
        int numTerminals = Terminals.size();
        int numNonTerminals = nonTerminals.size();

        actionTable = new ActionElement[numStates][numTerminals + numNonTerminals];

        // Inicializar la tabla de acciones con elementos de tipo ERROR
        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numTerminals + numNonTerminals; j++) {
                actionTable[i][j] = new ActionElement(ActionElement.ERROR, -1);
            }
        }

        // Generar las acciones SHIFT y REDUCE en la tabla de acciones
        for (int i = 0; i < numStates; i++) {
            // Obtener el conjunto FIRST y FOLLOW para el estado actual
            Set<String> firstSet = getFirstSet(root);
            Set<String> followSet = getFollowSet(root);

            // Calcular las acciones SHIFT y REDUCE para cada símbolo en el estado actual
            for (String symbol : firstSet) {
                if (isTerminal(symbol)) {
                    // Es un símbolo terminal, calcular el índice de columna correspondiente
                    int column = getTerminalIndex(symbol);

                    // Agregar la acción SHIFT a la tabla de acciones
                    int nextState = getNextState(root, symbol);
                    actionTable[i][column] = new ActionElement(ActionElement.SHIFT, nextState);
                }
            }

            // Agregar las acciones REDUCE para los símbolos no terminales en el estado actual
            for (String symbol : nonTerminals) {
                int column = getNonTerminalIndex(symbol);
                int ruleIndex = getRuleIndex(symbol);
                actionTable[i][column] = new ActionElement(ActionElement.REDUCE, ruleIndex);
            }
            
            // Obtener el símbolo final de entrada desde el árbol AST
            String finalSymbol = getFinalSymbolFromAST(root);
            
            // Agregar la acción ACCEPT si el estado actual es el estado final
            if (isFinalState(root)) {
                int column = getTerminalIndex(finalSymbol); // EOF_SYMBOL representa el símbolo de fin de entrada
                actionTable[i][column] = new ActionElement(ActionElement.ACCEPT, -1);
            }
        }
    }


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // ======================================================================================
    //									GotoTabla
    // ===========================================================================
    
    private void printAST(ASTNode node, int level) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append("  ");
        }
        System.out.println(indent.toString() + node.getLabel());
        for (ASTNode child : node.getChildren()) {
            printAST(child, level + 1);
        }
    }
    
    /*
     * recorriendo cada regla de la gramática y asignando los valores adecuados 
     * en la tabla. Se utiliza el índice del símbolo no terminal en la lista 
     * nonTerminalSymbols como la columna en la tabla gotoTable. 
     * El valor asignado es simplemente el índice de la regla más uno 
     * (considerando que las reglas se numeran a partir de 1).
     * */
    /*private void initGotoTable() {
        int numStates = actionTable.length;
        int numNonTerminals = nonTerminals.size();

        gotoTable = new int[numStates][numNonTerminals];

        // Inicializar la tabla Goto con -1
        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numNonTerminals; j++) {
                gotoTable[i][j] = -1;
            }
        }

        // Calcular los valores de la tabla Goto utilizando la tabla de acción existente
        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numNonTerminals; j++) {
                String nonTerminal = nonTerminals.get(j);
                int nextState = actionTable[i][nonTerminals.size() + Terminals.size() + j];
                if (nextState != -1 && isNonTerminal(nonTerminal)) {
                    gotoTable[i][j] = nextState;
                }
            }
        }
    }*/

    
    



    
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
        
        // parser.initGotoTable();
        parser.generateActionTable();
        
        ActionElement[][] actionTable = parser.getActionsTable();
        System.out.println("Tabla ActionTable:");
        System.out.println(Arrays.deepToString(actionTable));
        
        int[][] gotoTable = parser.getGotoTable();
        System.out.println("Tabla Goto:");
        System.out.println(Arrays.deepToString(gotoTable));
        
        // Imprimir el árbol de sintaxis abstracta
        // parser.printAST(parser.ast, 0);
    }
}
