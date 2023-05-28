package tinto;

import grammar_parser.*;

import java.io.*;
import java.util.List;

import generated.Parser;

public class Compiler {

    public static void main(String[] args) {
        // Buscar el directorio de trabajo
        String path = (args.length == 0 ? System.getProperty("user.dir") : args[0]);
        File workingdir = new File(path);

        String grammarFilePath = "Main.grammar";

        try {
            // Generar el archivo de tokens y símbolos para la gramática dada
            try {
                Lexer lexer = new Lexer(grammarFilePath);
                List<Token> tokens = lexer.tokenize();
                lexer.close();

                System.out.println("Generando Tokens\n");
                TokenConstantsGenerator.generateTokenConstants(tokens, "src/generated/TokenConstants.java");
                System.out.println("Tokens generados\n");

                System.out.println("Generando Simbolos\n");
                SymbolConstantsGenerator.generateSymbolConstants(tokens, "src/generated/SymbolConstants.java");
                System.out.println("Simbolos generados\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Comprobar que la sintaxis sea correcta
            GrammarParser parser = new GrammarParser(grammarFilePath);
            parser.parse();
            // parser.close();
            System.out.println("Parsing completado correctamente.");

            // Generar el analizador sintáctico utilizando ParserGenerator
            int[][] rules = parser.getRules();
            int[][] gototable = parser.getGotoTable();
            ActionElement[][] actions = parser.getActionsTable();
            
            ParserGenerator generator = new ParserGenerator(actions, gototable, rules);
            
            generator.generateParsingTable("src/generated/Parser.java");

            // Crear instancia del analizador sintáctico
            Parser parserInstance  = new Parser();

            // Si el análisis es exitoso, imprimir "Correcto"
            if (parserInstance.parse(new File(workingdir, "Main.txt"))) {
                printOutput(workingdir, "Correcto");
            }
            // Si el análisis es fallido, imprimir "Incorrecto"
            else {
                printOutput(workingdir, "Incorrecto");
            }
        } catch (Error err) {
            printError(workingdir, err);
            printOutput(workingdir, "Incorrecto");
        } catch (Exception ex) {
            printError(workingdir, ex);
            printOutput(workingdir, "Incorrecto");
        }
    }

    private static void printError(File workingdir, Throwable e) {
        try {
            FileOutputStream errorfile = new FileOutputStream(new File(workingdir, "GrammarcErrors.txt"));
            PrintStream errorStream = new PrintStream(errorfile);
            errorStream.println("[File Main.tinto] 1 error found:");
            errorStream.println(e.toString());
            errorStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void printOutput(File workingdir, String msg) {
        try {
            FileOutputStream outputfile = new FileOutputStream(new File(workingdir, "GrammarcOutput.txt"));
            PrintStream stream = new PrintStream(outputfile);
            stream.println(msg);
            stream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
