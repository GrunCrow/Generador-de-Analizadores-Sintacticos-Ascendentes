package parserjj;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TokenConstantsGenerator {

    public static void generateTokenConstants(List<Token> tokens, String outputFilePath) {
        try (FileWriter writer = new FileWriter(outputFilePath)) {
        	
        	writer.write("package generated; \n\n");
            writer.write("public interface TokenConstants {\n");
            //writer.write("\n");

            // Agregar el token EOF con valor 0 al principio de la lista
            writer.write("    public int EOF = 0;\n");
            //writer.write("\n");

            // Obtener los tipos de tokens únicos y ordenarlos por ordinal
            List<TokenKind> uniqueTokenKinds = new ArrayList<>();
            for (Token token : tokens) {
                if (!uniqueTokenKinds.contains(token.getKind())) {
                    uniqueTokenKinds.add(token.getKind());
                }
            }
            Collections.sort(uniqueTokenKinds, (tk1, tk2) -> Integer.compare(tk1.ordinal(), tk2.ordinal()));

            // Generar las constantes de los tokens terminales con los valores de lexema
            int terminalTokenIndex = 1;
            Set<String> terminalLexemes = new HashSet<>();
            for (Token token : tokens) {
                if (token.getKind() == TokenKind.TERMINAL && !terminalLexemes.contains(token.getLexeme().toString())) {
                    String lexeme = token.getLexeme().toString();
                    terminalLexemes.add(lexeme);
                    String extractedWord = lexeme.substring(1, lexeme.length() - 1); // para quitar < y >
                    writer.write("    public int " + extractedWord + " = " + terminalTokenIndex + ";\n");
                    terminalTokenIndex++;
                }
            }

            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
