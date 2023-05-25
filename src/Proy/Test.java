package Proy;

import parserjj.*;

public class Test {
    public static void main(String[] args) {
        // Construir la gramática
        Gramatica grammar = buildGrammar();

        // Resto del código
        // ...
    }

    private static Gramatica buildGrammar() {
    	Gramatica grammar = new Gramatica();

        // Agregar las reglas de la gramática utilizando el método addRule de la clase Grammar
        grammar.addRule(new Regla("Expr", new String[]{"Term"}));
        grammar.addRule(new Regla("Expr", new String[]{"Expr", "<PLUS>", "Term"}));
        grammar.addRule(new Regla("Expr", new String[]{"Expr", "<MINUS>", "Term"}));
        // Agregar las demás reglas de la gramática

        return grammar;
    }
}
