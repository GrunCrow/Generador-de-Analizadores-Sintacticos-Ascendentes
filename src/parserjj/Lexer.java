package parserjj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lexer {
    private BufferedReader reader;
    private String currentLine;
    private int currentLineIndex;
    private int currentColumnIndex;

    public Lexer(String filePath) throws IOException {
        reader = new BufferedReader(new FileReader(filePath));
        currentLine = reader.readLine();
        currentLineIndex = 0;
        currentColumnIndex = 0;
    }

    public Token getNextToken() throws IOException {
        if (currentLine == null) {
            reader.close();
            return null;
        }

        skipWhitespace();

        if (currentLineIndex >= currentLine.length()) {
            currentLine = reader.readLine();
            currentLineIndex = 0;
            currentColumnIndex = 0;
            return getNextToken();
        }

        char currentChar = currentLine.charAt(currentLineIndex);
        Token token;

        // Reconocimiento de tokens según el lenguaje de Fran Moreno
        if (Character.isDigit(currentChar)) {
            String numValue = parseNumber();
            token = new Token(TokenKind.NUM, numValue);
        } else if (Character.isLetter(currentChar)) {
            String identifier = parseIdentifier();
            int tokenKind = getIdentifierTokenKind(identifier);
            token = new Token(tokenKind, identifier);
        } else {
            // Carácter desconocido
            token = new Token(TokenKind.UNKNOWN, String.valueOf(currentChar));
        }

        currentLineIndex++;
        currentColumnIndex++;
        return token;
    }

    private String parseNumber() {
        StringBuilder sb = new StringBuilder();
        while (currentLineIndex < currentLine.length() &&
                Character.isDigit(currentLine.charAt(currentLineIndex))) {
            sb.append(currentLine.charAt(currentLineIndex));
            currentLineIndex++;
            currentColumnIndex++;
        }
        return sb.toString();
    }

    private String parseIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (currentLineIndex < currentLine.length() &&
                (Character.isLetterOrDigit(currentLine.charAt(currentLineIndex)) ||
                 currentLine.charAt(currentLineIndex) == '_')) {
            sb.append(currentLine.charAt(currentLineIndex));
            currentLineIndex++;
            currentColumnIndex++;
        }
        return sb.toString();
    }

    private int getIdentifierTokenKind(String identifier) {
        // Asigna los token kinds según las reglas del lenguaje
        switch (identifier) {
	        case "EOF":
	            return TokenKind.EOF;
	        case "NUM":
	            return TokenKind.NUM;
	        case "PLUS":
                return TokenKind.PLUS;
	        case "MINUS":
                return TokenKind.MINUS;
	        case "MULTIPLY":
                return TokenKind.PROD;    
	        case "DIV":
                return TokenKind.DIVIDE;
            case "LPAREN":
                return TokenKind.LPAREN;
            case "RPAREN":
                return TokenKind.RPAREN;
            case "ASSIGN":
                return TokenKind.ASSIGN;
            case "SEMICOLON":
                return TokenKind.SEMICOLON;
            case "if":
                return TokenKind.IF;
            case "else":
                return TokenKind.ELSE;
            case "while":
                return TokenKind.WHILE;
            case "print":
                return TokenKind.PRINT;
            case "<COMMA>":
                return TokenKind.COMMA;
            default:
                return TokenKind.IDENTIFIER;
        }
    }


    
    
    
    
    private void skipWhitespace() {
        while (currentLineIndex < currentLine.length() &&
                Character.isWhitespace(currentLine.charAt(currentLineIndex))) {
            currentLineIndex++;
            currentColumnIndex++;
        }
    }
}
