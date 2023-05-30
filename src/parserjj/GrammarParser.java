package parserjj;

import java.io.File;
import java.io.IOException;

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

    /**
     * Método de análisis de un fichero
     *
     * @param file Fichero a analizar
     * @return Resultado del análisis sintáctico
     */
    public boolean parse(File file) throws IOException, SintaxException {
        this.lexer = new GrammarLexer(file);
        this.nextToken = lexer.getNextToken();
        parseGrammar();
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
    private void parseGrammar() throws SintaxException {
        switch (nextToken.getKind()) {
            case TokenKind.NOTERMINAL:
                parseDefinition();
                parseGrammar();
                break;
            case TokenKind.EOF:
                break;
            default:
                int[] expected = { TokenKind.NOTERMINAL, TokenKind.EOF };
                throw new SintaxException(nextToken, expected);
        }
    }

    /**
     * Analiza el símbolo <Definition>
     *
     * @throws SintaxException
     */
    private void parseDefinition() throws SintaxException {
        match(TokenKind.NOTERMINAL);
        match(TokenKind.EQ);
        parseRuleList();
        match(TokenKind.SEMICOLON);
    }

    /**
     * Analiza el símbolo <RuleList>
     *
     * @throws SintaxException
     */
    private void parseRuleList() throws SintaxException {
        switch (nextToken.getKind()) {
            case TokenKind.NOTERMINAL:
                parseRule();
                parseRuleListPrime();
                break;
            default:
                int[] expected = { TokenKind.NOTERMINAL };
                throw new SintaxException(nextToken, expected);
        }
    }

    /**
     * Analiza el símbolo <RuleListPrime>
     *
     * @throws SintaxException
     */
    private void parseRuleListPrime() throws SintaxException {
        switch (nextToken.getKind()) {
            case TokenKind.BAR:
                match(TokenKind.BAR);
                parseRule();
                parseRuleListPrime();
                break;
            case TokenKind.SEMICOLON:
                break;
            default:
                int[] expected = { TokenKind.BAR, TokenKind.SEMICOLON };
                throw new SintaxException(nextToken, expected);
        }
    }

    /**
     * Analiza el símbolo <Rule>
     *
     * @throws SintaxException
     */
    private void parseRule() throws SintaxException {
        switch (nextToken.getKind()) {
            case TokenKind.NOTERMINAL:
            case TokenKind.TERMINAL:
                parseElement();
                parseRule();
                break;
            case TokenKind.BAR:
            case TokenKind.SEMICOLON:
                break;
            default:
                int[] expected = { TokenKind.NOTERMINAL, TokenKind.TERMINAL, TokenKind.BAR, TokenKind.SEMICOLON };
                throw new SintaxException(nextToken, expected);
        }
    }

    /**
     * Analiza el símbolo <Element>
     *
     * @throws SintaxException
     */
    private void parseElement() throws SintaxException {
        switch (nextToken.getKind()) {
            case TokenKind.NOTERMINAL:
                match(TokenKind.NOTERMINAL);
                break;
            case TokenKind.TERMINAL:
                match(TokenKind.TERMINAL);
                break;
            default:
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

    public static void main(String[] args) {
        GrammarParser parser = new GrammarParser();

        try {
            File file = new File("Main.txt");
            boolean result = parser.parse(file);

            if (result) {
                System.out.println("Análisis sintáctico exitoso. La gramática es válida.");
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
