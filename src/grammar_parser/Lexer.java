package grammar_parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private BufferedReader reader;
    private String currentLine;
    private int currentLineIndex;
    private int currentColumnIndex;
    private Token currentToken;
    private String currentLexeme;
    private Map<String, Integer> tokenMap;

    public Lexer(String filePath) throws IOException {
        reader = new BufferedReader(new FileReader(filePath));
        currentLine = reader.readLine();
        currentLineIndex = 0;
        currentColumnIndex = 0;
        currentToken = null;
        currentLexeme = null;
        tokenMap = new HashMap<>();
        tokenMap.put("blanco", TokenKind.BLANCO);
        tokenMap.put("comentario", TokenKind.COMENTARIO);
        tokenMap.put("NOTERMINAL", TokenKind.NOTERMINAL);
        tokenMap.put("TERMINAL", TokenKind.TERMINAL);
        tokenMap.put("EQ", TokenKind.EQ);
        tokenMap.put("BAR", TokenKind.BAR);
        tokenMap.put("SEMICOLON", TokenKind.SEMICOLON);
    }

    public void getNextToken() throws IOException {
        if (currentLine == null) {
            currentToken = null;
            currentLexeme = null;
            return;
        }

        skipWhitespace();

        if (currentLineIndex >= currentLine.length()) {
            currentLine = reader.readLine();
            currentLineIndex = 0;
            currentColumnIndex = 0;
            getNextToken();
            return;
        }

        char currentChar = currentLine.charAt(currentLineIndex);

        if (currentChar == '/') {
            if (currentLineIndex + 1 < currentLine.length() && currentLine.charAt(currentLineIndex + 1) == '*') {
                parseComment();
                currentToken = Token.newToken(TokenKind.COMENTARIO, currentLexeme);
                return;
            }
        }

        if (currentChar == '<') {
            parseTerminal();
        } else if (Character.isLetter(currentChar) || currentChar == '_') {
            parseNonTerminal();
        } else if (currentChar == ':') {
            if (currentLineIndex + 2 < currentLine.length() && currentLine.substring(currentLineIndex, currentLineIndex + 3).equals("::=")) {
                currentLexeme = "::=";
            	currentToken = Token.newToken(TokenKind.EQ, currentLexeme);
                currentLineIndex += 2;
                currentColumnIndex += 2;
            } else {
            	currentLexeme = String.valueOf(currentChar);
                currentToken = Token.newToken(TokenKind.UNKNOWN, currentLexeme);
            }
        } else if (currentChar == '|') {
        	currentLexeme = String.valueOf(currentChar);
            currentToken = Token.newToken(TokenKind.BAR, currentLexeme);
        } else if (currentChar == ';') {
        	currentLexeme = String.valueOf(currentChar);
            currentToken = Token.newToken(TokenKind.SEMICOLON, currentLexeme);
        } else {
        	currentLexeme = String.valueOf(currentChar);
            currentToken = Token.newToken(TokenKind.UNKNOWN, currentLexeme);
        }

        currentLineIndex++;
        currentColumnIndex++;
    }


    private void parseComment() {
        StringBuilder sb = new StringBuilder();
        sb.append(currentLine.charAt(currentLineIndex)); // '/'
        sb.append(currentLine.charAt(currentLineIndex + 1)); // '*'
        currentLineIndex += 2;
        currentColumnIndex += 2;

        boolean commentEndFound = false;
        while (currentLineIndex < currentLine.length() && !commentEndFound) {
            char currentChar = currentLine.charAt(currentLineIndex);
            sb.append(currentChar);

            if (currentChar == '*') {
                if (currentLineIndex + 1 < currentLine.length() && currentLine.charAt(currentLineIndex + 1) == '/') {
                    commentEndFound = true;
                    sb.append('/');
                    currentLineIndex++;
                    currentColumnIndex++;
                }
            }

            currentLineIndex++;
            currentColumnIndex++;
        }

        currentLexeme = sb.toString();
    }

    private void parseNonTerminal() {
        StringBuilder sb = new StringBuilder();
        sb.append(currentLine.charAt(currentLineIndex));

        currentLineIndex++;
        currentColumnIndex++;

        while (currentLineIndex < currentLine.length() &&
                (Character.isLetterOrDigit(currentLine.charAt(currentLineIndex)) || currentLine.charAt(currentLineIndex) == '_')) {
            sb.append(currentLine.charAt(currentLineIndex));
            currentLineIndex++;
            currentColumnIndex++;
        }

        currentLexeme = sb.toString();
        
        currentToken = Token.newToken(TokenKind.NOTERMINAL, String.valueOf(currentLexeme));
    }

    private void parseTerminal() {
        StringBuilder sb = new StringBuilder();
        sb.append(currentLine.charAt(currentLineIndex)); // '<'

        currentLineIndex++;
        currentColumnIndex++;

        while (currentLineIndex < currentLine.length() && currentLine.charAt(currentLineIndex) != '>') {
            sb.append(currentLine.charAt(currentLineIndex));
            currentLineIndex++;
            currentColumnIndex++;
        }

        if (currentLineIndex < currentLine.length()) {
            sb.append(currentLine.charAt(currentLineIndex)); // '>'
            currentLineIndex++;
            currentColumnIndex++;
        }

        currentLexeme = sb.toString();
        
        currentToken = Token.newToken(TokenKind.TERMINAL, String.valueOf(currentLexeme));
    }

    private void skipWhitespace() {
        while (currentLineIndex < currentLine.length() &&
                (currentLine.charAt(currentLineIndex) == ' ' || currentLine.charAt(currentLineIndex) == '\r' ||
                        currentLine.charAt(currentLineIndex) == '\n' || currentLine.charAt(currentLineIndex) == '\t')) {
            currentLineIndex++;
            currentColumnIndex++;
        }
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public String getCurrentLexeme() {
        return currentLexeme;
    }
    
    public List<Token> tokenize() throws IOException {
        List<Token> tokens = new ArrayList<>();

        getNextToken();
        while (currentToken != null) {
            tokens.add(currentToken);
            getNextToken();
        }

        return tokens;
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

