package auxiliares;

import java.io.*;
import java.util.ArrayList;

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

    public void generateOutput(String ruta) throws IOException {
        readOutputFile(ruta);
        generateTokenConstants();
        generateSymbolConstants();

        grammar.removeLambda();
        automaton.createState0();
        generateParser();
        generateGrammar();
    }

    private void readOutputFile(String ruta) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            boolean terminal = false;
            boolean addExpression = false;
            boolean startRule = true;
            boolean newIdentifier = true;
            String identifier = null;
            String line;

            while ((line = br.readLine()) != null && line.length() > 0) {
                if (startRule) {
                    if (newIdentifier) {
                        identifier = line;
                        newIdentifier = false;
                        addExpression = false;
                    }
                    Rule aux = new Rule(identifier);
                    grammar.addRule(aux);
                    startRule = false;
                }

                if (!irrelevant(line)) {
                    if (isToken(line)) {
                        String aux = line.substring(1, line.length() - 1);
                        if (!tokens.contains(aux))
                            tokens.add(aux);
                        terminal = true;
                    } else if (line.length() > 1) {
                        if (!symbols.contains(line))
                            symbols.add(line);
                        terminal = false;
                    }

                    if (addExpression) {
                        grammar.rules.get(grammar.rules.size() - 1).addExpressions(line, terminal);
                    }
                    addExpression = true;
                } else {
                    if (line.equals("|")) {
                        startRule = true;
                    }
                    if (line.equals(";")) {
                        startRule = true;
                        newIdentifier = true;
                    }
                }
            }
        }
    }

    private boolean irrelevant(String line) {
        if (line.length() > 3)
            return false;
        if (line.equals("|") || line.equals("::=") || line.equals(";"))
            return true;
        return false;
    }

    private boolean isToken(String line) {
        if (!irrelevant(line) && line.length() > 2) {
            if (line.charAt(0) == '<' && line.charAt(line.length() - 1) == '>')
                return true;
        }
        return false;
    }

    private void generateSymbolConstants() {
    	
    	System.out.println("Generando SymbolConstants.java");
    	
        File workingdir = workingDir();

        try (PrintStream stream = new PrintStream(new FileOutputStream(new File(workingdir, "src/generated/SymbolConstants.java")))) {
            stream.println("package generated;\n");
            stream.println("public interface SymbolConstants {\n");

            int i = 0;
            for (String symbol : symbols) {
                stream.println("\t public int " + symbol + " = " + i + ";");
                i++;
            }

            stream.println("\n}");
            
            // stream.close();
            
            System.out.println("SymbolConstants.java Generado\n\n");
            
        } catch (Exception ex) {
            printError(workingdir, ex);
        }
    }

	private void generateTokenConstants() {
		
		System.out.println("Generando TokenConstants.java");
		
		File workingdir = workingDir();

		try {
			FileOutputStream outputfile = new FileOutputStream(new File(workingdir, "src/generated/TokenConstants.java"));
			PrintStream stream = new PrintStream(outputfile);
			
			// package
			stream.println("package generated;\n");
			
			// Declarar clase
			stream.println("public interface TokenConstants {\n");
			// Incluir siempre al inicio el Token EOF = 0
			stream.println("\t public int EOF = 0;");
			// Al empezar con EOF = 0, iniciar int de tokens a 1
			int i = 1;
			// Añadir cada token
			for (String token : tokens) {
				stream.println("\t public int " + token + " = " + i + ";");
				i++;
			}
			stream.println("\n}");

			stream.close();
			
			System.out.println("TokenConstants.java Generado\n\n");
			
		} catch (Error err) {
			printError(workingdir, err);
		} catch (Exception ex) {
			printError(workingdir, ex);
		}

	}

	public void generateParser() {
		
		System.out.println("Generando Parser.java");
		
		File workingdir = workingDir();

		try {
			FileOutputStream outputfile = new FileOutputStream(new File(workingdir, "src/generated/Parser.java"));
			PrintStream stream = new PrintStream(outputfile);
			
			// package
			stream.println("package generated;\n");
			
			// imports
			stream.println("import auxiliares.ActionElement;");
			stream.println("import auxiliares.SLRParser;\n\n");
			
			// Declarar clase
			stream.println("public class Parser extends SLRParser implements TokenConstants, SymbolConstants {\n\n "
					+ "\tpublic Parser() {\r\n\t\tinitRules();\r\n\t\tinitActionTable();\r\n\t\tinitGotoTable();\r\n\t}\n");

			
			// Declarar InitRules
			stream.println("\t"
					+ "private void initRules() {\n\t\t"
					+ "int[][] initRule = {\n" 
					+ "\t\t\t\t{ 0, 0 },");
			
			// Reglas
			for (Rule reg : grammar.rules) { 
				stream.println("\t\t\t\t{ " + reg.identifier + ", " + reg.production.size() + " },");
			}
			
			// Cerrar InitRule
			stream.println("\t\t};\n\n\t\t"
					+ "this.rule = initRule;\n\t}\n");

			// ActionTable
			stream.println("\tprivate void initActionTable(){");
				//	Declarar ActionTable (size)
			stream.println("\t\tactionTable = new ActionElement["+ automaton.getStates().size() +"]["+ tokens.size() +"];");
				// Información ActionTable a generar
			for (int i=0; i< automaton.getStates().size() ; i++) {
				stream.print(generateActionTable(automaton.getStates().get(i), i));
			}
			// Cerrar ActionTable
			stream.println("\t}\n");
			
			// TODO esta tabla podrá tener los enteros diferentes ya que depende de la implementación, pero el resto de valores deben ser iguales (depende de los estados del autom)
			// GotoTable()
			stream.println("\tprivate void initGotoTable() {");
				// Declarar GotoTable (size)
			stream.println("\t\tgotoTable = new int[" + automaton.getStates().size() + "][" + symbols.size() + "];");
				// Información GotoTable a generar
			for (int i=0; i< automaton.getStates().size() ; i++) {
				stream.print(generateGotoTable(automaton.getStates().get(i), i ));
			}
			// Cerrar GoToTable
			stream.println("\t}\n");

			// Cerrar clase
			stream.println("}");
			stream.close();
			
			System.out.println("Parser.java Generado\n\n");
			
		} catch (Error err) {
			printError(workingdir, err);
		} catch (Exception ex) {
			printError(workingdir, ex);
		}

	}
	
	
	private void generateGrammar() {
		File workingdir = workingDir();

		try {
			FileOutputStream outputfile = new FileOutputStream(new File(workingdir, "src/generated/Grammar.txt"));
			PrintStream stream = new PrintStream(outputfile);
			
			// Gramatica
			Grammar gramaticaConLambda = grammar;
			gramaticaConLambda.addLambda();
			
			stream.println("==================================================================================");
			stream.println("\t\t\t\t\t\t\t\t\tGRAMATICA");
			stream.println("==================================================================================");
			stream.println(gramaticaConLambda.toString());
			
			// Firsts
			stream.println("==================================================================================");
			stream.println("\t\t\t\t\t\t\t\t\tFIRSTS");
			stream.println("==================================================================================");
			String identificador = "";
			for (Rule reg : gramaticaConLambda.rules) {
				if(!identificador.equals(reg.identifier)) {
					stream.println(reg.toString() + "FIRSTS(" + reg.firsts.toString() + ")");
					identificador = reg.identifier;
				}
			}
			
			stream.println("\n");
			
			// Follows
			stream.println("==================================================================================");
			stream.println("\t\t\t\t\t\t\t\t\tFOLLOWS");
			stream.println("==================================================================================");
			for (Rule reg : gramaticaConLambda.rules) {
				if(!identificador.equals(reg.identifier)) {
					stream.println(reg.toString() + "FOLLOWS(" + reg.follows.toString() + ")");
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
	

	private String generateActionTable(State state, int numEstado) {
		String devolver = "";
		ArrayList<Transition> transicionesEstado = state.getTransiciones();

		for(Transition transition : transicionesEstado) {
			if(transition.getSource().terminal) {
				// Reduccion
				if(transition.isReduce()) {
					devolver += generateReduceActionTable(transition, numEstado);
				// Desplazamiento
				}else {
					devolver += "\n\t\tactionTable["+ numEstado +"]["+ eliminaCuadrilla(transition.getSource().expression) +"] = new ActionElement(ActionElement.SHIFT, "+ transition.getDestination() +");";	
				}				
			}
		}
		
		
		if(!devolver.isEmpty())
			devolver += "\n";
		
		return devolver;
	}
	
	private String generateReduceActionTable(Transition transition, int numEstado) {
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
	
	private String generateGotoTable(State state, int numEstado) {
		String devolver = "";
		ArrayList<Transition> transicionesEstado = state.getTransiciones();
		
		for(Transition transition : transicionesEstado) {
			if(!transition.getSource().terminal) {
				devolver += "\n\t\tgotoTable[" + numEstado + "][" + transition.getSource().expression + "] = " + transition.getDestination() + ";";
			}
		}
		
		if(!devolver.isEmpty())
			devolver += "\n";
		
		return devolver;
	}

	private File workingDir() {
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
