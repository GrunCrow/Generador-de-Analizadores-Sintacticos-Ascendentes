package auxiliares;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;


//Automata reconocedor de prefijos variables
public class Automaton {

	ArrayList<State> states;
	Grammar grammar;
	

	public Automaton(Grammar grammar) {
		states = new ArrayList<>();
		this.grammar = grammar;
	}

	public void creaEstadoCero() throws FileNotFoundException {
		Rule primeraRegla = grammar.rules.get(0);

		ArrayList<Expression> produccionInicio = new ArrayList<>();
		produccionInicio.add(new Expression(primeraRegla.identifier, false));
		Element primerElemento = new Element("Inicio", produccionInicio, false);
		ArrayList<Element> elementoParaCreacionEstados = new ArrayList<>();
		elementoParaCreacionEstados.add(primerElemento);

		creaEstadoAPartirUnElemento(elementoParaCreacionEstados);

		System.out.println("Final Alcanzado");
		
		// N estados = 
		//System.out.println(estados.size());
		
		FileOutputStream outputfile = new FileOutputStream(new File("src/generated/Automata.txt"));
		PrintStream stream = new PrintStream(outputfile);
		
		// System.out.println(estados.toString());
		stream.println(states.toString());
		stream.close();
	}

	// A partir de un elemento saca un nuevo estado, devuelve -1 si lo ha creado, o
	// devuelve la posicion de un estado ya existente si existe un estado con
	// las mismos elementos.
	public int creaEstadoAPartirUnElemento(ArrayList<Element> elementosPartida) {
		//int posicionEstado = -1;
		State nuevoEstado = new State();

		int numElementosAnadidos = 0;
		for (Element elementoPartida : elementosPartida) {
			// Agrego el elemento de partida al estado. A partir de este todos los elementos
			// que incluya ese elemento segun su marcador
			int i = 0;
			nuevoEstado.anadirElemento(elementoPartida);
			do {
				ArrayList<Element> elementosAnadir = elementosNuevosPartiendoElementoActualMarcado(nuevoEstado.elements.get(i + numElementosAnadidos));
				for (Element anadir : elementosAnadir) {
					nuevoEstado.anadirElemento(anadir);
				}
				i++;
			} while (nuevoEstado.elements.size() - numElementosAnadidos > i); // Mientras queden elementos sin
																				// recorrer.
			numElementosAnadidos += i;
		}

		
		// Tenemos un nuevo estado, hay que sacarle las transiciones. Pero primero
		// comprobar si este estado existe ya entre todos los
		// estados que tenemos para no tener estados duplicados.
		int posicionEstado = existeEstado(nuevoEstado);

		if (posicionEstado != -1) {
			return posicionEstado;
		} // Si no existe lo creamos, le agregamos las transiciones y devolvemos su
			// posicion(es decir la ultima del ArrayList)


		states.add(nuevoEstado);
		int posicionEstadoAnadido = states.size() - 1;
		
		ArrayList<Transition> transicionesEstado = transicionesPosiblesEnUnEstado(states.get(posicionEstadoAnadido));
		for (Transition transition : transicionesEstado) {
			states.get(posicionEstadoAnadido).anadirTransicion(transition);
		}

		return posicionEstadoAnadido;
	}

	// Devolvemos la posicion del estado, si ya existe, sino -1 indicando que no hay
	// un estado igual al pasado por parametro.
	public int existeEstado(State state) {
		int posicionEstado = -1;

		for (int i = 0; i < states.size(); i++) {
			if (estadosIguales(state, states.get(i))) {
				return i;
			}
		}
		return posicionEstado;
	}

	public boolean estadosIguales(State estado1, State estado2) {

		if (estado1.elements.size() != estado2.elements.size()) {
			return false;
		}

		for (Element elemento1 : estado1.elements) {
			boolean existeElemento = false;

			for (Element elemento2 : estado2.elements) {
				if (elemento1.elementosIguales(elemento2)) {
					existeElemento = true;
				}
			}
			if (!existeElemento) {
				return false;
			}
		}
		return true;
	}

	public ArrayList<Element> elementosNuevosPartiendoElementoActualMarcado(Element elementoAnalizar) {
		ArrayList<Element> devolver = new ArrayList<>();

		// Buscamos el marcado en el elemento a analizar
		int indiceMarcador = elementoAnalizar.indiceMarcador();

		// Si no esta al final el marcador, comprobamos si es un token o un simbolo
		if (!elementoAnalizar.marcadorAlFinal()) {
//			devolver.add(elementoAnalizar);
			Expression produccionSiguienteMarcador = elementoAnalizar.production.get(indiceMarcador + 1);

			if (produccionSiguienteMarcador.terminal) {
				return devolver;
			} else {
				String identificador = produccionSiguienteMarcador.expression;

				// Buscamos todas las reglas que tengan como identificador ese simbolo, y las
				// anadimos a los nuevos elementos
				for (Rule reg : grammar.rules) {
					if (reg.identifier.equals(identificador)) {
						ArrayList<Expression> produccionNuevoElemento = new ArrayList<>();
						produccionNuevoElemento.addAll(reg.production);
						Element nuevoElemento = new Element(reg.identifier, produccionNuevoElemento, false);
						devolver.add(nuevoElemento);
					}
				}
			}
		}
		return devolver;
	}

	public ArrayList<Transition> transicionesPosiblesEnUnEstado(State state) {
		ArrayList<Transition> transicionesNuevas = new ArrayList<>();
	
		for (Element element : state.elements) {
			Transition transicionAuxiliar = new Transition();
	
			if (element.marcadorAlFinal()) {// Tenemos que anadir una reduccion...
				transicionAuxiliar.addReduce(); // El destino es la regla que reduce...
				transicionAuxiliar.anadirDestino(numeroReglaAPartirElemento(element));
				if(element.production.size() > 1) {
					transicionAuxiliar.setSource(new Expression(element.production.get(element.production.size()-2).expression,true));
				}else {
					transicionAuxiliar.setSource(new Expression("lambda",true));

				}
				transicionesNuevas.add(transicionAuxiliar);

			} else { // Si no esta alfinal la transicion, crearla, apuntando a un estado existene o
						// el que se cree...
				Expression origen = new Expression(element.production.get(element.indiceMarcador() + 1));
				transicionAuxiliar.setSource(origen); // Origen la expresion del elemento siguiente al marcador
				ArrayList<Element> elementosParaCrearEstado = new ArrayList<>();

				for (Element elemento2 : state.elements) {// Conjunto de elementos para crear o ver si existe el
					if (elementoTieneTransicionEnEsaExpresion(elemento2, origen)) {
						Element elementoAux = new Element(elemento2);	
						elementoAux.adelantaCursor();
						elementosParaCrearEstado.add(elementoAux);
					}
				}

				int destino = creaEstadoAPartirUnElemento(elementosParaCrearEstado);
				// para bprrar mas tarde
				if (destino == -1) {
					System.out.println("ALGO HA IDO MAL EN LA CREACION DEL ESTADO");
				}
				//
				transicionAuxiliar.anadirDestino(destino);
				transicionesNuevas.add(transicionAuxiliar);
			}
		}

		return transicionesNuevas;
	}

	public boolean elementoTieneTransicionEnEsaExpresion(Element element, Expression simbolo) {
		int indiceMarcador = element.indiceMarcador();

		if (element.marcadorAlFinal())
			return false;
		if (element.production.get(indiceMarcador + 1).expression.equals(simbolo.expression))
			return true;

		return false;
	}

	public int numeroReglaAPartirElemento(Element element) {
		int devolver = -1;

		ArrayList<Expression> produccionRegla = new ArrayList<>();
		produccionRegla.addAll(element.production);
		produccionRegla.remove(produccionRegla.size() - 1);
		Rule reglaAux = new Rule(element.identifier, produccionRegla);

		devolver = grammar.numRule(reglaAux);

		return devolver;
	}

}
