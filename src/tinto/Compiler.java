package tinto;

import grammar_parser.*;

import java.io.*;
import java.util.List;


/**
 * Clase que desarrolla el punto de entrada al compilador.
 * 
 */
public class Compiler {

	/**
	 * Punto de entrada de la aplicación
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// Busca el directorio de trabajo
		String path = (args.length == 0 ? System.getProperty("user.dir") : args[0]);
		File workingdir = new File(path);
		
		String filepath = "Main.txt";
		
		try {
			File mainfile = new File(workingdir, "Main.grammar");
			// Genera fichero de simbolos y tokens para la gramatica dada:
			 try {
	            Lexer lexer = new Lexer(filepath);
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
			// Comprueba que la sintaxis sea correcta
			ParserGrammar parser = new ParserGrammar(filepath);
			parser.parse();
			parser.close();
			System.out.println("Parsing completado correctamente.");
			
			// Crea analizador sintáctico
			Parser parser = new Parser();
			// si true en el fichero tintooutput escribe correcto
			if(parser.parse(mainfile)){
				printOutput(workingdir,"Correcto");
			} 
			// escribe en ese fichero incorrecto
			else{
				printOutput(workingdir,"Incorrecto");
			}
		} 
		// tambn lo escribe en el fichero de errores
		catch(Error err) {
			printError(workingdir, err);
			printOutput(workingdir,"Incorrecto");

		}
		catch(Exception ex) 
		{
			printError(workingdir, ex);
			printOutput(workingdir,"Incorrecto");
		}
	}
	
	/**
	 * Genera el fichero de error
	 * @param workingdir Directorio de trabajo
	 * @param e Error a mostrar
	 */
	private static void printError(File workingdir, Throwable e) 
	{
		try 
		{
			FileOutputStream errorfile =  new FileOutputStream(new File(workingdir, "GrammarcErrors.txt"));
			PrintStream errorStream = new PrintStream(errorfile);
			errorStream.println("[File Main.tinto] 1 error found:");
			errorStream.println(e.toString());
			errorStream.close();
		}
		catch(Exception ex)
		{
		}
	}
	
	/**
	 * Genera el fichero de salida
	 * @param workingdir Directorio de trabajo
	 * @param e Error a mostrar
	 */
	private static void printOutput(File workingdir, String msg) 
	{
		try 
		{
			FileOutputStream outputfile =  new FileOutputStream(new File(workingdir, "GrammarcOutput.txt"));
			PrintStream stream = new PrintStream(outputfile);
			stream.println(msg);
			stream.close();
		}
		catch(Exception ex)
		{
		}
	}
}
