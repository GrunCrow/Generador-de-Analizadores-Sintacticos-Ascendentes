package parserjj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Lexer {
    private BufferedReader reader;
    private String currentLine;
    private int currentLineIndex;
    private int currentColumnIndex;
    private String currentToken;
    private String currentLexeme;
    private Map<String, String> tokenMap;

    public Lexer(String filePath) throws IOException {
        reader = new BufferedReader(new FileReader(filePath));
        currentLine = reader.readLine();
        currentLineIndex = 0;
        currentColumnIndex = 0;
        currentToken = null;
        currentLexeme = null;
        tokenMap = new HashMap<>();
        tokenMap.put("blanco", "BLANCO");
        tokenMap.put("comentario", "COMENTARIO");
        tokenMap.put("NOTERMINAL", "NOTERMINAL");
        tokenMap.put("TERMINAL", "TERMINAL");
        tokenMap.put("EQ", "EQ");
        tokenMap.put("BAR", "BAR");
        tokenMap.put("SEMICOLON", "SEMICOLON");
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
                getNextToken();
                return;
            }
        }

        if (currentChar == '<') {
            parseTerminal();
        } else if (Character.isLetter(currentChar) || currentChar == '_') {
            parseNonTerminal();
        } else if (currentChar == ':') {
            if (currentLineIndex + 2 < currentLine.length() && currentLine.substring(currentLineIndex, currentLineIndex + 3).equals("::=")) {
                currentToken = "EQ";
                currentLexeme = "::=";
                currentLineIndex += 2;
                currentColumnIndex += 2;
            } else {
                currentToken = "UNKNOWN";
                currentLexeme = String.valueOf(currentChar);
            }
        } else if (currentChar == '|') {
            currentToken = "BAR";
            currentLexeme = String.valueOf(currentChar);
        } else if (currentChar == ';') {
            currentToken = "SEMICOLON";
            currentLexeme = String.valueOf(currentChar);
        } else {
            currentToken = "UNKNOWN";
            currentLexeme = String.valueOf(currentChar);
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

        currentToken = "COMENTARIO";
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

        currentToken = "NOTERMINAL";
        currentLexeme = sb.toString();
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

        currentToken = "TERMINAL";
        currentLexeme = sb.toString();
    }

    private void skipWhitespace() {
        while (currentLineIndex < currentLine.length() &&
                (currentLine.charAt(currentLineIndex) == ' ' || currentLine.charAt(currentLineIndex) == '\r' ||
                        currentLine.charAt(currentLineIndex) == '\n' || currentLine.charAt(currentLineIndex) == '\t')) {
            currentLineIndex++;
            currentColumnIndex++;
        }
    }

    public String getCurrentToken() {
        return currentToken;
    }

    public String getCurrentLexeme() {
        return currentLexeme;
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
