package parserjj;

import java.io.IOException;

public class Parser {
    private Lexer lexer;
    private Token currentToken;

    public Parser(String filePath) {
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
        if (currentToken.kind == TokenKind.NOTERMINAL) {
            match(TokenKind.NOTERMINAL);
            match(TokenKind.EQ);
            listaReglas();
            match(TokenKind.SEMICOLON);
        } else if (currentToken.kind == TokenKind.COMENTARIO) {
            parseComment();
        } else {
            reportError("Se esperaba un símbolo no terminal");
        }
    }

    private void parseComment() {
        // Ignorar el comentario y obtener el siguiente token
        getNextToken();
    }

    private void listaReglas() {
        regla();
        while (currentToken.kind == TokenKind.BAR) {
            match(TokenKind.BAR);
            regla();
        }
    }

    private void regla() {
        while (currentToken.kind == TokenKind.NOTERMINAL || currentToken.kind == TokenKind.TERMINAL) {
            if (currentToken.kind == TokenKind.NOTERMINAL) {
                match(TokenKind.NOTERMINAL);
            } else {
                match(TokenKind.TERMINAL);
            }
        }
    }

    private void match(TokenKind expectedTokenKind) {
        if (currentToken != null && currentToken.kind == expectedTokenKind) {
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
