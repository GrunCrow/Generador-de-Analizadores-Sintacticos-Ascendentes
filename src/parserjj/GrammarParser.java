package parserjj;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import AST.GrammarAST;
import AST.NonTerminalNode;
import AST.TerminalNode;
import AST.ASTNode;
import AST.DefinitionNode;

import parserjj.TokenKind;

/**
 * Analizador sintáctico basado en una gramática BNF y LL(1)
 */
public class GrammarParser extends SLRParser {
    /**
     * Analizador léxico
     */
    private GrammarLexer lexer;

    /**
     * Siguiente token de la cadena de entrada
     */
    private Token nextToken;
    
    GrammarAST ast;

    /**
     * Método de análisis de un fichero
     *
     * @param file Fichero a analizar
     * @return Resultado del análisis sintáctico
     */
    public boolean parse(File file) throws IOException, SintaxException {
        this.lexer = new GrammarLexer(file);
        this.nextToken = lexer.getNextToken();
        ast = parseGrammar();
        if (nextToken.getKind() == TokenKind.EOF)
            return true;
        else
            return false;
    }

    /**
     * Analiza el símbolo <Grammar>
     *
     * @throws SintaxException
     */
    private GrammarAST parseGrammar() throws SintaxException {
        List<DefinitionNode> definitions = new ArrayList<>();

        while (nextToken.getKind() != TokenKind.EOF) {
            if (nextToken.getKind() == TokenKind.COMENTARIO) {
                match(TokenKind.COMENTARIO);
            } else if (nextToken.getKind() == TokenKind.BLANCO) {
                match(TokenKind.BLANCO);
            } else if (nextToken.getKind() == TokenKind.NOTERMINAL) {
                DefinitionNode definition = parseDefinition();
                definitions.add(definition);
            } else {
                int[] expected = { TokenKind.NOTERMINAL, TokenKind.EOF };
                throw new SintaxException(nextToken, expected);
            }
        }

        return new GrammarAST(definitions);
    }


    /**
     * Analiza el símbolo <Definition>
     *
     * @throws SintaxException
     */
    private DefinitionNode parseDefinition() throws SintaxException {
        NonTerminalNode nonTerminal = (NonTerminalNode) parseElement();
        match(TokenKind.EQ);
        List<ASTNode> ruleList = parseRuleList();
        match(TokenKind.SEMICOLON);

        return new DefinitionNode(nonTerminal, ruleList);
    }


    /**
     * Analiza el símbolo <RuleList>
     *
     * @throws SintaxException
     */
    private List<ASTNode> parseRuleList() throws SintaxException {
        List<ASTNode> ruleList = new ArrayList<>();

        ASTNode element = parseElement();
        ruleList.add(element);

        List<ASTNode> ruleListPrime = parseRuleListPrime();
        ruleList.addAll(ruleListPrime);

        return ruleList;
    }


    /**
     * Analiza el símbolo <RuleListPrime>
     *
     * @throws SintaxException
     */
    private List<ASTNode> parseRuleListPrime() throws SintaxException {
        List<ASTNode> ruleListPrime = new ArrayList<>();

        if (nextToken.getKind() == TokenKind.BAR) {
            match(TokenKind.BAR);
            while(nextToken.getKind() == TokenKind.TERMINAL || nextToken.getKind() == TokenKind.NOTERMINAL) {
	            ASTNode element = parseElement();
	            ruleListPrime.add(element);
            }
	            List<ASTNode> ruleListPrimeRecursive = parseRuleListPrime();
	            ruleListPrime.addAll(ruleListPrimeRecursive);
        }

        return ruleListPrime;
    }

    /**
     * Analiza el símbolo <Rule>
     *
     * @return Lista de nodos del AST que representan la regla
     * @throws SintaxException
     */
    private List<ASTNode> parseRule() throws SintaxException {
        List<ASTNode> rule = new ArrayList<>();

        if (nextToken.getKind() == TokenKind.NOTERMINAL || nextToken.getKind() == TokenKind.TERMINAL) {
            ASTNode element = parseElement();
            rule.add(element);
            List<ASTNode> remainingRule = parseRule();
            rule.addAll(remainingRule);
        }

        return rule;
    }

    /**
     * Analiza el símbolo <Element>
     *
     * @throws SintaxException
     */
    private ASTNode parseElement() throws SintaxException {
        if (nextToken.getKind() == TokenKind.NOTERMINAL) {
            String nonTerminal = nextToken.getLexeme();
            match(TokenKind.NOTERMINAL);
            return new NonTerminalNode(nonTerminal);
        } else if (nextToken.getKind() == TokenKind.TERMINAL) {
            String terminal = nextToken.getLexeme();
            match(TokenKind.TERMINAL);
            return new TerminalNode(terminal);
        } else {
            int[] expected = { TokenKind.NOTERMINAL, TokenKind.TERMINAL };
            throw new SintaxException(nextToken, expected);
        }
    }

    /**
     * Método que consume un token de la cadena de entrada
     *
     * @param kind Tipo de token a consumir
     * @throws SintaxException Si el tipo no coincide con el token
     */
    private void match(int kind) throws SintaxException {
        if (nextToken.getKind() == kind)
            nextToken = lexer.getNextToken();
        else
            throw new SintaxException(nextToken, kind);
    }
    
    
    
    
    
    
    
    
    /**
     * Imprime el árbol AST generado a partir de la gramática
     *
     * @param ast El árbol AST a imprimir
     */
    public void printAST() {
        List<DefinitionNode> definitions = ast.getDefinitions();
        for (DefinitionNode definition : definitions) {
            printDefinition(definition, 0);
            System.out.println(); // Salto de línea entre definiciones
        }
    }

    /**
     * Imprime una definición de la gramática y sus reglas de forma jerárquica
     *
     * @param definition La definición a imprimir
     * @param indent     La indentación para mostrar la jerarquía
     */
    private void printDefinition(DefinitionNode definition, int indent) {
        printIndent(indent);
        NonTerminalNode nonTerminal = definition.getNonTerminal();
        System.out.println(nonTerminal.getSymbol() + " = ");

        List<ASTNode> ruleList = definition.getRuleList();
        for (ASTNode rule : ruleList) {
            printRule(rule, indent + 1);
        }

        printIndent(indent);
        System.out.print(";");
    }

    /**
     * Imprime una regla de la gramática de forma jerárquica
     *
     * @param rule   La regla a imprimir
     * @param indent La indentación para mostrar la jerarquía
     */
    private void printRule(ASTNode rule, int indent) {
        printIndent(indent);

        if (rule instanceof NonTerminalNode) {
            NonTerminalNode nonTerminal = (NonTerminalNode) rule;
            System.out.println(nonTerminal.getSymbol());
        } else if (rule instanceof TerminalNode) {
            TerminalNode terminal = (TerminalNode) rule;
            System.out.println(terminal.getSymbol());
        } else {
            // Error: Tipo de nodo desconocido
            System.out.println("UNKNOWN");
        }
    }

    /**
     * Imprime la indentación
     *
     * @param indent La cantidad de espacios de indentación
     */
    private void printIndent(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  "); // 2 espacios de indentación
        }
    }

    

    public static void main(String[] args) {
        GrammarParser parser = new GrammarParser();

        try {
            File file = new File("Main.txt");
            boolean result = parser.parse(file);

            if (result) {
                System.out.println("Análisis sintáctico exitoso. La gramática es válida.");
                parser.printAST();
            } else {
                System.out.println("Análisis sintáctico fallido. La gramática no es válida.");
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } catch (SintaxException e) {
            System.out.println("Error de sintaxis en el archivo: " + e.getMessage());
        }
    }
}
