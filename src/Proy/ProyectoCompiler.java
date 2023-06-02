package Proy;

import parserjj.*;


import java.io.*;

public class ProyectoCompiler {
	/**
	 * Punto de entrada de la aplicaci�n
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		// Busca el directorio de trabajo
		// String path = (args.length == 0 ? System.getProperty("user.dir") : args[0]);
		// File workingdir = new File(path);
		
		String mainname = "Main.txt";
		String rutaSalidaLexer = "src/generated/Lexer.txt";
		String rutaSalida = "src/generated/Salida.txt";
		
		File mainfile = new File(mainname);
		
		try
		{
			FileOutputStream outputfile =  new FileOutputStream(new File(rutaSalidaLexer));
			PrintStream stream = new PrintStream(outputfile);

			FileInputStream fis = new FileInputStream(mainfile);
			GrammarLexer lexer = new GrammarLexer(mainfile); //(new SimpleCharStream(fis));
			Token tk;
			do 
			{
				tk = lexer.getNextToken();
				stream.println(tk.toString());
			} 
			while(tk.getKind() != TokenKind.EOF);
			
			stream.close();
		} 
		catch(Error err) 
		{
			printError(mainfile, err);
		}
		catch(Exception ex) 
		{
			printError(mainfile, ex);
		}
		
		try
		{
			FileInputStream fis = new FileInputStream(mainfile);
			GrammarParser parser = new GrammarParser(mainname); //(fis);
			parser.parse();
			printOutput(mainfile ,"Correcto");
		} 
		catch(Error err) 
		{
			printError(mainfile, err);
			printOutput(mainfile,"Incorrecto");

		}
		catch(Exception ex) 
		{
			printError(mainfile, ex);
			printOutput(mainfile,"Incorrecto");
		}
	

		try {
			if(salidaCorrecta(rutaSalida)) {
				ProyectoGenerator aux = new ProyectoGenerator();
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
			if(linea.equals("Correcto")) {
				b.close();
				return true;
			}
		}
		b.close();
		return false;
	}
	/**
	 * 
	 * @param workingdir Directorio de trabajo
	 * @param e Error a mostrar
	 */
	private static void printError(File workingdir, Throwable e) 
	{
		String rutaSalidaErrores = "src/generated/ProyectocErrors.txt";
		
		try 
		{
			FileOutputStream errorfile =  new FileOutputStream(new File(rutaSalidaErrores));
			PrintStream errorStream = new PrintStream(errorfile);
			errorStream.println("[File Main.Proyecto] 1 error found:");
			errorStream.println(e.toString());
			errorStream.close();
		}
		catch(Exception ex)
		{
		}
	}
	
	private static void printOutput(File workingdir, String msg) 
	{
		String rutaSalida = "src/generated/Salida.txt";
		
		try 
		{
			FileOutputStream outputfile =  new FileOutputStream(new File(rutaSalida));
			PrintStream stream = new PrintStream(outputfile);
			stream.println(msg);
			stream.close();
		}
		catch(Exception ex)
		{
		}
	}
}
