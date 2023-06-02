package parserjj;

import java.io.*;
import java.util.ArrayList;

public class Generator {

	ArrayList<String> symbols;
	ArrayList<String> tokens;
	Gramatica gramatica;
	AutomataReconocedor automata;

	public Generator() {
		symbols = new ArrayList<String>();
		tokens = new ArrayList<String>();
		gramatica = new Gramatica();
		automata = new AutomataReconocedor(gramatica);
	}

	public void generaSalida(String ruta) throws IOException {
		leerFicheroSalida(ruta);
		generaTokenConstants();
		generaSymbolConstants();
		
		gramatica.eliminaLambda();
		automata.creaEstadoCero();
		generaParser();
	}

	private void leerFicheroSalida(String ruta) throws IOException {
		String linea;
		FileReader f;
		f = new FileReader(ruta);
		BufferedReader b = new BufferedReader(f);

		boolean terminal = false;
		boolean agregarExpresion = false;
		boolean empiezaRegla = true;
		boolean nuevoIdentificador = true;
		String identificador = null;

		while ((linea = b.readLine()) != null && linea.length() > 0) {
			if (empiezaRegla) {
				if (nuevoIdentificador) {
					identificador = linea;
					nuevoIdentificador = false;
					agregarExpresion = false;
				}
				Regla aux = new Regla(identificador);
				gramatica.anadirRegla(aux);
				empiezaRegla = false;
			}
			// Generamos TokenConstant y SymbolConstant y
			if (!esIrrelevante(linea)) {
				if (esToken(linea)) {
					String aux = linea.substring(1, linea.length() - 1);
					if (!tokens.contains(aux))
						tokens.add(aux);
					terminal = true;
				} else if (linea.length() > 1) { // Para que no coja fin de fichero
					if (!symbols.contains(linea))
						symbols.add(linea);
					terminal = false;
				}

				if (agregarExpresion) { // Agregamos la expresion a la ultima regla de la gramatica
					gramatica.reglas.get(gramatica.reglas.size() - 1).anadirExpresion(linea, terminal);
				}
				agregarExpresion = true;
			} else {
				// Vemos cuando empezamos una nueva regla con el identificador antiguo o con el
				// nuevo
				if (linea.equals("|")) {
					empiezaRegla = true;
				}
				if (linea.equals(";")) {
					empiezaRegla = true;
					nuevoIdentificador = true;
				}
			}

		}
		b.close();
//		automata.creaEstadoCero();
	}

	private boolean esIrrelevante(String linea) {
		if (linea.length() > 3)
			return false;
		if (linea.equals("|") || linea.equals("::=") || linea.equals(";"))
			return true;

		return false;
	}

	private boolean esToken(String linea) {
		if (!esIrrelevante(linea) && linea.length() > 2) {
			if (linea.charAt(0) == '<' && linea.charAt(linea.length() - 1) == '>')
				return true;
		}
		return false;
	}

	private void generaSymbolConstants() {
		File workingdir = directorioTrabajo();

		try {
			FileOutputStream outputfile = new FileOutputStream(new File(workingdir, "SymbolConstants.java"));
			PrintStream stream = new PrintStream(outputfile);

			stream.println("public interface SymbolConstants {\n");

			int i = 0;
			for (String symbol : symbols) {
				stream.println("\t public int " + symbol + " = " + i + ";");
				i++;
			}

			stream.println("\n}");
			stream.close();
		} catch (Error err) {
			printError(workingdir, err);
		} catch (Exception ex) {
			printError(workingdir, ex);
		}

	}

	private void generaTokenConstants() {
		File workingdir = directorioTrabajo();

		try {
			FileOutputStream outputfile = new FileOutputStream(new File(workingdir, "TokenConstants.java"));
			PrintStream stream = new PrintStream(outputfile);

			stream.println("public interface TokenConstants {\n");
			stream.println("\t public int EOF = 0;");
			int i = 1;
			for (String token : tokens) {
				stream.println("\t public int " + token + " = " + i + ";");
				i++;
			}
			stream.println("\n}");

			
			
			
			Gramatica gramaticaConLambda = gramatica;
			gramaticaConLambda.insertaLambda();
			
			// BORRAR ESTO DESPUES DE LA PRUEBA //
			stream.println(gramaticaConLambda.toString());
			// ES PARA VER COMO ALMACENO LAS REGLAS INTERNAMENTE
			stream.println("CONJUNTO PRIMEROS");
			String identificador = "";
			for (Regla reg : gramaticaConLambda.reglas) {
				if(!identificador.equals(reg.identificador)) {
					stream.println(reg.toString() + "PRIMEROS(" + reg.primeros.toString() + ")");
					identificador = reg.identificador;
				}
			}
			// PARA VER EL CONJUNTO PRIMEROS CALCULADO
			stream.println("\nCONJUNTO SIGUIENTES");
			for (Regla reg : gramaticaConLambda.reglas) {
				if(!identificador.equals(reg.identificador)) {
					stream.println(reg.toString() + "SIGUIENTES(" + reg.siguientes.toString() + ")");
					identificador = reg.identificador;
				}
			}
			// PARA VER EL CONJUNTO SIGUIENTES CALCULADO
			stream.close();
		} catch (Error err) {
			printError(workingdir, err);
		} catch (Exception ex) {
			printError(workingdir, ex);
		}

	}

	public void generaParser() {
		File workingdir = directorioTrabajo();

		try {
			FileOutputStream outputfile = new FileOutputStream(new File(workingdir, "src/generated/Parser.java"));
			PrintStream stream = new PrintStream(outputfile);
			
			//Emepzamos con Parser() y lo cerramos
			stream.println("public class Parser extends SLRParser implements TokenConstants, SymbolConstants {\n\n "
					+ "\tpublic Parser() {\r\n\t\tinitRules();\r\n\t\tinitActionTable();\r\n\t\tinitGotoTable();\r\n\t}\n");

			
			// Emepzamos con initRules
			stream.println("\tprivate void initRules() {\n\t\tint[][] initRule = {\n" + "\t\t\t\t{ 0, 0 },");

			for (Regla reg : gramatica.reglas) { // Sacamos la informacion de todas las reglas.
				stream.println("\t\t\t\t{ " + reg.identificador + ", " + reg.produccion.size() + " },");
			}
			//Cerramos initRule
			stream.println("\t\t};\n\n\t\tthis.rule = initRule;\n\t}\n");

			// Empezamos con ActionTable
			stream.println("\tprivate void initActionTable(){");
				//Tamano de la tabla ActionTable
			stream.println("\t\tactiontable = new ActionElement["+ automata.estados.size() +"]["+ tokens.size() +"];");
				//Bucle que agrega a la tabla la informacion cuando es necesaria segund los estados.
			for (int i=0; i< automata.estados.size() ; i++) {
				stream.print(sacaInformacionActionTable(automata.estados.get(i), i));
			}
			//Cerramos ActionTable
			stream.println("\t}\n");

			// Empezamso con GoToTable()
			stream.println("\tprivate void initGoToTable() {");
				// Tamano de la tabla con los valors de symbols y estados.
			stream.println("\t\tgotoTable = new int[" + automata.estados.size() + "][" + symbols.size() + "];");
				// Bucle que agrega informacion cuando es necesaria segun el estado
			for (int i=0; i< automata.estados.size() ; i++) {
				stream.print(sacaInformacionGoToEstado(automata.estados.get(i), i ));
			}
			//Cerramos GoToTable
			stream.println("\t}\n");

			// Cerramos la clase
			stream.println("}");
			stream.close();
		} catch (Error err) {
			printError(workingdir, err);
		} catch (Exception ex) {
			printError(workingdir, ex);
		}

	}

	private String sacaInformacionActionTable(Estado estado, int numEstado) {
		String devolver = "";
		ArrayList<Transicion> transicionesEstado = estado.transiciones;

		for(Transicion transicion : transicionesEstado) {
			if(transicion.origen.terminal) {
				if(transicion.reduccion) {//Si es reduccion o desplazamiento...
					devolver += generaReduccionActionTable(transicion, numEstado);
				}else {//Es desplazamiento
					devolver += "\n\t\tactionTable["+ numEstado +"]["+ eliminaCuadrilla(transicion.origen.expresion) +"] = new ActionElement(ActionElement.SHIFT, "+ transicion.destino +");";	
				}				
			}
		}
		
		
//		if(!devolver.isEmpty())
//			devolver += "\n";
		
		return devolver;
	}
	
	private String generaReduccionActionTable(Transicion transicion, int numEstado) {
		String devolver = "";
		
		if(transicion.destino == -1) {
			devolver +="\n\t\tactionTable["+ numEstado +"][EOF] = new ActionElement(ActionElement.ACCEPT, 0);";
		}else {
			ArrayList<String> siguientes = gramatica.reglas.get(transicion.destino).siguientes;
			for(String siguiente : siguientes) {
				devolver += "\n\t\tactionTable["+ numEstado +"]["+ eliminaCuadrilla(siguiente) +"] = new ActionElement(ActionElement.REDUCE, "+ (transicion.destino+1) +");";
			}		
		}		
		return devolver;
	}
	
	private String eliminaCuadrilla(String terminal) {
		return terminal.substring(1, terminal.length()-1);
	}
	
	private String sacaInformacionGoToEstado(Estado estado, int numEstado) {
		String devolver = "";
		ArrayList<Transicion> transicionesEstado = estado.transiciones;
		
		for(Transicion transicion : transicionesEstado) {
			if(!transicion.origen.terminal) {
				devolver += "\n\t\tgotoTable[" + numEstado + "][" + transicion.origen.expresion + "] = " + transicion.destino + ";";
			}
		}
		
//		if(!devolver.isEmpty())
//			devolver += "\n";
		
		return devolver;
	}

	private File directorioTrabajo() {
		String path = System.getProperty("user.dir");
		File workingdir = new File(path);

		return workingdir;
	}

	private void printError(File workingdir, Throwable e) {
		try {
			FileOutputStream errorfile = new FileOutputStream(new File(workingdir, "GeneratorErrors.txt"));
			PrintStream errorStream = new PrintStream(errorfile);
			errorStream.println("error found:");
			errorStream.println(e.toString());
			errorStream.close();
		} catch (Exception ex) {
		}
	}
}
