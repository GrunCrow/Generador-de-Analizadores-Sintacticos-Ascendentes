package main;

import java.io.*;

import auxiliares.*;

public class ProyectoCompiler {
    public static void main(String[] args) {
        // Rutas de los archivos de entrada y salida
        String mainname = "Main2.txt";
        String rutaSalidaLexer = "src/generated/Lexer.txt";
        boolean salidaCorrecta = false;
        String rutaSalidaErrores = "src/generated/ProyectoErrors.txt";

        File mainfile = new File(mainname);

        try {
            // Generar archivo de tokens
            FileOutputStream outputfile = new FileOutputStream(new File(rutaSalidaLexer));
            PrintStream stream = new PrintStream(outputfile);
            
            GrammarLexer lexer = new GrammarLexer(mainfile);
            Token tk;
            do {
                tk = lexer.getNextToken();
                stream.println(tk.toString());
            } while (tk.getKind() != TokenKind.EOF);

            stream.close();
        } catch (Error err) {
            printError(rutaSalidaErrores, err);
        } catch (Exception ex) {
            printError(rutaSalidaErrores, ex);
        }

        try {
            GrammarParser parser = new GrammarParser(mainname);
            parser.parse();
            salidaCorrecta = true;
        } catch (Error err) {
            printError(rutaSalidaErrores, err);
            salidaCorrecta = false;
        } catch (Exception ex) {
            printError(rutaSalidaErrores, ex);
            salidaCorrecta = false;
        }

        try {
            // Generar todos los archivos necesarios (SymbolConstants, TokenConstants y Parser)
            if (salidaCorrecta) {
                Generator aux = new Generator();
                aux.generateOutput(rutaSalidaLexer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printError(String rutaSalidaErrores, Throwable e) {
        try {
            FileOutputStream errorfile = new FileOutputStream(new File(rutaSalidaErrores));
            PrintStream errorStream = new PrintStream(errorfile);
            errorStream.println("[File Main.Proyecto] 1 error found:");
            errorStream.println(e.toString());
            errorStream.close();
        } catch (Exception ex) {
            // Manejo de errores en caso de fallo al imprimir el error
        }
    }
}
