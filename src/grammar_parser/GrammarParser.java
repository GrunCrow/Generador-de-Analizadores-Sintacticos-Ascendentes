package grammar_parser;

import java.io.IOException;

public class GrammarParser {
    private Lexer lexer;
    private Token currentToken;

    public GrammarParser(String filePath) {
        try {
            lexer = new Lexer(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getNextToken();
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
        if (currentToken.getKind() == TokenKind.NOTERMINAL) {
            match(TokenKind.NOTERMINAL);
            match(TokenKind.EQ);
            listaReglas();
            match(TokenKind.SEMICOLON);
        } else if (currentToken.getKind() == TokenKind.COMENTARIO) {
            parseComment();
        } else {
            reportError("Se esperaba un simbolo no terminal");
        }
    }

    private void parseComment() {
        // Ignorar el comentario y obtener el siguiente token
        getNextToken();
    }

    private void listaReglas() {
        regla();
        while (currentToken.getKind() == TokenKind.BAR) {
            match(TokenKind.BAR);
            regla();
        }
    }

    private void regla() {
        while (currentToken.getKind() == TokenKind.NOTERMINAL || currentToken.getKind() == TokenKind.TERMINAL) {
            if (currentToken.getKind() == TokenKind.NOTERMINAL) {
                match(TokenKind.NOTERMINAL);
            } else {
                match(TokenKind.TERMINAL);
            }
        }
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