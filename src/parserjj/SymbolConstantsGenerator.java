package parserjj;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SymbolConstantsGenerator {

    public static void generateSymbolConstants(List<Token> tokens, String outputFilePath) {
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write("public interface SymbolConstants {\n");
            //writer.write("\n");

            // Agregar el token EOF con valor 0 al principio de la lista
            //writer.write("    public int EOF = 0;\n");
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
            int terminalTokenIndex = 0;
            Set<String> terminalLexemes = new HashSet<>();
            for (Token token : tokens) {
                if (token.getKind() == TokenKind.NOTERMINAL && !terminalLexemes.contains(token.getValue().toString())) {
                    String lexeme = token.getValue().toString();
                    terminalLexemes.add(lexeme);
                    writer.write("    public int " + lexeme + " = " + terminalTokenIndex + ";\n");
                    terminalTokenIndex++;
                }
            }

            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
