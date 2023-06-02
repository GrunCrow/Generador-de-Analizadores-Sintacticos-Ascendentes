package auxiliares;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class Generator {

    ArrayList<String> symbols;
    ArrayList<String> tokens;
    Grammar grammar;
    Automaton automaton;

    public Generator() {
        symbols = new ArrayList<String>();
        tokens = new ArrayList<String>();
        grammar = new Grammar();
        automaton = new Automaton(grammar);
    }

    public void generaSalida(String ruta) throws IOException {
        leerFicheroSalida(ruta);
        generaTokenConstants();
        generaSymbolConstants();

        grammar.removeLambda();
        automaton.creaEstadoCero();
        generaParser();
        generaGrammar();
    }

    private void leerFicheroSalida(String ruta) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            boolean terminal = false;
            boolean agregarExpresion = false;
            boolean empiezaRegla = true;
            boolean nuevoIdentificador = true;
            String identificador = null;
            String linea;

            while ((linea = br.readLine()) != null && linea.length() > 0) {
                if (empiezaRegla) {
                    if (nuevoIdentificador) {
                        identificador = linea;
                        nuevoIdentificador = false;
                        agregarExpresion = false;
                    }
                    Rule aux = new Rule(identificador);
                    grammar.addRule(aux);
                    empiezaRegla = false;
                }

                if (!esIrrelevante(linea)) {
                    if (esToken(linea)) {
                        String aux = linea.substring(1, linea.length() - 1);
                        if (!tokens.contains(aux))
                            tokens.add(aux);
                        terminal = true;
                    } else if (linea.length() > 1) {
                        if (!symbols.contains(linea))
                            symbols.add(linea);
                        terminal = false;
                    }

                    if (agregarExpresion) {
                        grammar.rules.get(grammar.rules.size() - 1).addExpressions(linea, terminal);
                    }
                    agregarExpresion = true;
                } else {
                    if (linea.equals("|")) {
                        empiezaRegla = true;
                    }
                    if (linea.equals(";")) {
                        empiezaRegla = true;
                        nuevoIdentificador = true;
                    }
                }
            }
        }
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

        try (PrintStream stream = new PrintStream(new FileOutputStream(new File(workingdir, "src/generated/SymbolConstants.java")))) {
            stream.println("package generated;\n");
            stream.println("public interface SymbolConstants {\n");

            int i = 0;
            for (String symbol : symbols) {
                stream.println("\t public int " + symbol + " = " + i + ";");
                i++;
            }

            stream.println("\n}");
        } catch (Exception ex) {
            printError(workingdir, ex);
        }
    }

	private void generaTokenConstants() {
		File workingdir = directorioTrabajo();

		try {
			FileOutputStream outputfile = new FileOutputStream(new File(workingdir, "src/generated/TokenConstants.java"));
			PrintStream stream = new PrintStream(outputfile);
			
			// package
			stream.println("package generated;\n");

			stream.println("public interface TokenConstants {\n");
			stream.println("\t public int EOF = 0;");
			int i = 1;
			for (String token : tokens) {
				stream.println("\t public int " + token + " = " + i + ";");
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

	public void generaParser() {
		File workingdir = directorioTrabajo();

		try {
			FileOutputStream outputfile = new FileOutputStream(new File(workingdir, "src/generated/Parser.java"));
			PrintStream stream = new PrintStream(outputfile);
			
			// package
			stream.println("package generated;\n");
			
			// imports
			stream.println("import auxiliares.ActionElement;");
			stream.println("import auxiliares.SLRParser;\n\n");
			
			//Emepzamos con Parser() y lo cerramos
			stream.println("public class Parser extends SLRParser implements TokenConstants, SymbolConstants {\n\n "
					+ "\tpublic Parser() {\r\n\t\tinitRules();\r\n\t\tinitActionTable();\r\n\t\tinitGotoTable();\r\n\t}\n");

			
			// Emepzamos con initRules
			stream.println("\t"
					+ "private void initRules() {\n\t\t"
					+ "int[][] initRule = {\n" 
					+ "\t\t\t\t{ 0, 0 },");
			
			// Reglas
			for (Rule reg : grammar.rules) { 
				stream.println("\t\t\t\t{ " + reg.identifier + ", " + reg.production.size() + " },");
			}
			
			//Cerrar InitRule
			stream.println("\t\t};\n\n\t\t"
					+ "this.rule = initRule;\n\t}\n");

			// Empezamos con ActionTable
			stream.println("\tprivate void initActionTable(){");
				//Tamano de la tabla ActionTable
			stream.println("\t\tactionTable = new ActionElement["+ automaton.getStates().size() +"]["+ tokens.size() +"];");
				//Bucle que agrega a la tabla la informacion cuando es necesaria segund los estados.
			for (int i=0; i< automaton.getStates().size() ; i++) {
				stream.print(sacaInformacionActionTable(automaton.getStates().get(i), i));
			}
			//Cerramos ActionTable
			stream.println("\t}\n");
			
			// todo esta tabla podrá tener los enteros diferentes ya que depende de la implementación, pero el resto de valores deben ser iguales (depende de los estados del autom)
			// Empezar con GotoTable()
			stream.println("\tprivate void initGotoTable() {");
				// Tamano de la tabla con los valors de symbols y estados.
			stream.println("\t\tgotoTable = new int[" + automaton.getStates().size() + "][" + symbols.size() + "];");
				// Bucle que agrega informacion cuando es necesaria segun el estado
			for (int i=0; i< automaton.getStates().size() ; i++) {
				stream.print(sacaInformacionGoToEstado(automaton.getStates().get(i), i ));
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
	
	
	private void generaGrammar() {
		File workingdir = directorioTrabajo();

		try {
			FileOutputStream outputfile = new FileOutputStream(new File(workingdir, "src/generated/Grammar.txt"));
			PrintStream stream = new PrintStream(outputfile);
			
			Grammar gramaticaConLambda = grammar;
			gramaticaConLambda.addLambda();
			
			stream.println(gramaticaConLambda.toString());
			stream.println("CONJUNTO PRIMEROS");
			String identificador = "";
			for (Rule reg : gramaticaConLambda.rules) {
				if(!identificador.equals(reg.identifier)) {
					stream.println(reg.toString() + "PRIMEROS(" + reg.firsts.toString() + ")");
					identificador = reg.identifier;
				}
			}
			
			stream.println("\nCONJUNTO SIGUIENTES");
			for (Rule reg : gramaticaConLambda.rules) {
				if(!identificador.equals(reg.identifier)) {
					stream.println(reg.toString() + "SIGUIENTES(" + reg.follows.toString() + ")");
					identificador = reg.identifier;
				}
			}
			stream.close();
		} catch (Error err) {
			printError(workingdir, err);
		} catch (Exception ex) {
			printError(workingdir, ex);
		}

	}
	

	private String sacaInformacionActionTable(State state, int numEstado) {
		String devolver = "";
		ArrayList<Transition> transicionesEstado = state.getTransiciones();

		for(Transition transition : transicionesEstado) {
			if(transition.getSource().terminal) {
				if(transition.isReduce()) {//Si es reduccion o desplazamiento...
					devolver += generaReduccionActionTable(transition, numEstado);
				}else {//Es desplazamiento
					devolver += "\n\t\tactionTable["+ numEstado +"]["+ eliminaCuadrilla(transition.getSource().expression) +"] = new ActionElement(ActionElement.SHIFT, "+ transition.getDestination() +");";	
				}				
			}
		}
		
		
//		if(!devolver.isEmpty())
//			devolver += "\n";
		
		return devolver;
	}
	
	private String generaReduccionActionTable(Transition transition, int numEstado) {
		String devolver = "";
		
		if(transition.getDestination() == -1) {
			devolver +="\n\t\tactionTable["+ numEstado +"][EOF] = new ActionElement(ActionElement.ACCEPT, 0);";
		}else {
			ArrayList<String> siguientes = grammar.rules.get(transition.getDestination()).follows;
			for(String siguiente : siguientes) {
				devolver += "\n\t\tactionTable["+ numEstado +"]["+ eliminaCuadrilla(siguiente) +"] = new ActionElement(ActionElement.REDUCE, "+ (transition.getDestination()+1) +");";
			}		
		}		
		return devolver;
	}
	
	private String eliminaCuadrilla(String terminal) {
		return terminal.substring(1, terminal.length()-1);
	}
	
	private String sacaInformacionGoToEstado(State state, int numEstado) {
		String devolver = "";
		ArrayList<Transition> transicionesEstado = state.getTransiciones();
		
		for(Transition transition : transicionesEstado) {
			if(!transition.getSource().terminal) {
				devolver += "\n\t\tgotoTable[" + numEstado + "][" + transition.getSource().expression + "] = " + transition.getDestination() + ";";
			}
		}
		
//		if(!devolver.isEmpty())
//			devolver += "\n";
		
		return devolver;
	}

	private File directorioTrabajo() {
	    String currentPath = System.getProperty("user.dir");
	    return new File(currentPath);
	}

	private void printError(File workingdir, Throwable e) {
		try {
			FileOutputStream errorfile = new FileOutputStream(new File(workingdir, "src/generated/GeneratorErrors.txt"));
			PrintStream errorStream = new PrintStream(errorfile);
			errorStream.println("error found:");
			errorStream.println(e.toString());
			errorStream.close();
		} catch (Exception ex) {
		}
	}
}
