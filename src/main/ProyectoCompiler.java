package main;

import java.io.*;

import auxiliares.*;

public class ProyectoCompiler {
    public static void main(String[] args) {
        // Rutas de los archivos de entrada y salida
        String mainname = "Main.txt";
        String rutaSalidaLexer = "src/generated/Lexer.txt";
        String rutaSalida = "src/generated/Salida.txt";
        String rutaSalidaErrores = "src/generated/ProyectoErrors.txt";

        File mainfile = new File(mainname);

        try {
            // Generar archivo de tokens
            FileOutputStream outputfile = new FileOutputStream(new File(rutaSalidaLexer));
            PrintStream stream = new PrintStream(outputfile);

            FileInputStream fis = new FileInputStream(mainfile);
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
            // Realizar análisis sintáctico
            FileInputStream fis = new FileInputStream(mainfile);
            GrammarParser parser = new GrammarParser(mainname);
            parser.parse();
            printOutput(rutaSalida, "Correcto");
        } catch (Error err) {
            printError(rutaSalidaErrores, err);
            printOutput(rutaSalida, "Incorrecto");
        } catch (Exception ex) {
            printError(rutaSalidaErrores, ex);
            printOutput(rutaSalida, "Incorrecto");
        }

        try {
            // Generar salida si el análisis fue correcto
            if (salidaCorrecta(rutaSalida)) {
                Generator aux = new Generator();
                aux.generaSalida(rutaSalidaLexer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean salidaCorrecta(String ruta) throws IOException {
        String linea;
        FileReader f;
        f = new FileReader(ruta);
        BufferedReader b = new BufferedReader(f);
        if ((linea = b.readLine()) != null) {
            if (linea.equals("Correcto")) {
                b.close();
                return true;
            }
        }
        b.close();
        return false;
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

    private static void printOutput(String rutaSalida, String msg) {
        try {
            FileOutputStream outputfile = new FileOutputStream(new File(rutaSalida));
            PrintStream stream = new PrintStream(outputfile);
            stream.println(msg);
            stream.close();
        } catch (Exception ex) {
            // Manejo de errores en caso de fallo al imprimir la salida
        }
    }
}
