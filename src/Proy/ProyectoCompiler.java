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
		String path = (args.length == 0 ? System.getProperty("user.dir") : args[0]);
		File workingdir = new File(path);
		
		try
		{
			FileOutputStream outputfile =  new FileOutputStream(new File(workingdir, "ProyectocOutput1.txt"));
			PrintStream stream = new PrintStream(outputfile);

			File mainfile = new File(workingdir, "Main.txt");
			FileInputStream fis = new FileInputStream(mainfile);
			ProyectoParserTokenManager lexer = new ProyectoParserTokenManager(new SimpleCharStream(fis));
			Token tk;
			do 
			{
				tk = lexer.getNextToken();
				stream.println(tk.toString());
			} 
			while(tk.kind != ProyectoParserConstants.EOF);
			
			stream.close();
		} 
		catch(Error err) 
		{
			printError(workingdir, err);
		}
		catch(Exception ex) 
		{
			printError(workingdir, ex);
		}
		
		try
		{
			File mainfile = new File(workingdir, "Main.txt");
			FileInputStream fis = new FileInputStream(mainfile);
			ProyectoParser parser = new ProyectoParser(fis);
			parser.Gramatica();
			printOutput(workingdir,"Correcto");
		} 
		catch(Error err) 
		{
			printError(workingdir, err);
			printOutput(workingdir,"Incorrecto");

		}
		catch(Exception ex) 
		{
			printError(workingdir, ex);
			printOutput(workingdir,"Incorrecto");
		}
	
		
		String rutaSintactico = workingdir.getPath() + "/ProyectocOutput2.txt";
		try {
			if(salidaCorrecta(rutaSintactico)) {
				String rutaLexico = workingdir.getPath() + "/ProyectocOutput1.txt";
				ProyectoGenerator aux = new ProyectoGenerator();
				aux.generaSalida(rutaLexico);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		try 
		{
			FileOutputStream errorfile =  new FileOutputStream(new File(workingdir, "ProyectocErrors.txt"));
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
		try 
		{
			FileOutputStream outputfile =  new FileOutputStream(new File(workingdir, "ProyectocOutput2.txt"));
			PrintStream stream = new PrintStream(outputfile);
			stream.println(msg);
			stream.close();
		}
		catch(Exception ex)
		{
		}
	}
}
