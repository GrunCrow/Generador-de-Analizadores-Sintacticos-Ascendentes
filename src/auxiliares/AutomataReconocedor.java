package auxiliares;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;


//Automata reconocedor de prefijos variables
public class AutomataReconocedor {

	ArrayList<Estado> estados;
	Gramatica gramatica;
	

	public AutomataReconocedor(Gramatica gramatica) {
		estados = new ArrayList<>();
		this.gramatica = gramatica;
	}

	public void creaEstadoCero() throws FileNotFoundException {
		Regla primeraRegla = gramatica.reglas.get(0);

		ArrayList<Expresion> produccionInicio = new ArrayList<>();
		produccionInicio.add(new Expresion(primeraRegla.identificador, false));
		Elemento primerElemento = new Elemento("Inicio", produccionInicio, false);
		ArrayList<Elemento> elementoParaCreacionEstados = new ArrayList<>();
		elementoParaCreacionEstados.add(primerElemento);

		creaEstadoAPartirUnElemento(elementoParaCreacionEstados);

		System.out.println("Final Alcanzado");
		
		// N estados = 
		//System.out.println(estados.size());
		
		FileOutputStream outputfile = new FileOutputStream(new File("src/generated/Automata.txt"));
		PrintStream stream = new PrintStream(outputfile);
		
		// System.out.println(estados.toString());
		stream.println(estados.toString());
		stream.close();
	}

	// A partir de un elemento saca un nuevo estado, devuelve -1 si lo ha creado, o
	// devuelve la posicion de un estado ya existente si existe un estado con
	// las mismos elementos.
	public int creaEstadoAPartirUnElemento(ArrayList<Elemento> elementosPartida) {
		//int posicionEstado = -1;
		Estado nuevoEstado = new Estado();

		int numElementosAnadidos = 0;
		for (Elemento elementoPartida : elementosPartida) {
			// Agrego el elemento de partida al estado. A partir de este todos los elementos
			// que incluya ese elemento segun su marcador
			int i = 0;
			nuevoEstado.anadirElemento(elementoPartida);
			do {
				ArrayList<Elemento> elementosAnadir = elementosNuevosPartiendoElementoActualMarcado(nuevoEstado.elementos.get(i + numElementosAnadidos));
				for (Elemento anadir : elementosAnadir) {
					nuevoEstado.anadirElemento(anadir);
				}
				i++;
			} while (nuevoEstado.elementos.size() - numElementosAnadidos > i); // Mientras queden elementos sin
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


		estados.add(nuevoEstado);
		int posicionEstadoAnadido = estados.size() - 1;
		
		ArrayList<Transicion> transicionesEstado = transicionesPosiblesEnUnEstado(estados.get(posicionEstadoAnadido));
		for (Transicion transicion : transicionesEstado) {
			estados.get(posicionEstadoAnadido).anadirTransicion(transicion);
		}

		return posicionEstadoAnadido;
	}

	// Devolvemos la posicion del estado, si ya existe, sino -1 indicando que no hay
	// un estado igual al pasado por parametro.
	public int existeEstado(Estado estado) {
		int posicionEstado = -1;

		for (int i = 0; i < estados.size(); i++) {
			if (estadosIguales(estado, estados.get(i))) {
				return i;
			}
		}
		return posicionEstado;
	}

	public boolean estadosIguales(Estado estado1, Estado estado2) {

		if (estado1.elementos.size() != estado2.elementos.size()) {
			return false;
		}

		for (Elemento elemento1 : estado1.elementos) {
			boolean existeElemento = false;

			for (Elemento elemento2 : estado2.elementos) {
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

	public ArrayList<Elemento> elementosNuevosPartiendoElementoActualMarcado(Elemento elementoAnalizar) {
		ArrayList<Elemento> devolver = new ArrayList<>();

		// Buscamos el marcado en el elemento a analizar
		int indiceMarcador = elementoAnalizar.indiceMarcador();

		// Si no esta al final el marcador, comprobamos si es un token o un simbolo
		if (!elementoAnalizar.marcadorAlFinal()) {
//			devolver.add(elementoAnalizar);
			Expresion produccionSiguienteMarcador = elementoAnalizar.produccion.get(indiceMarcador + 1);

			if (produccionSiguienteMarcador.terminal) {
				return devolver;
			} else {
				String identificador = produccionSiguienteMarcador.expresion;

				// Buscamos todas las reglas que tengan como identificador ese simbolo, y las
				// anadimos a los nuevos elementos
				for (Regla reg : gramatica.reglas) {
					if (reg.identificador.equals(identificador)) {
						ArrayList<Expresion> produccionNuevoElemento = new ArrayList<>();
						produccionNuevoElemento.addAll(reg.produccion);
						Elemento nuevoElemento = new Elemento(reg.identificador, produccionNuevoElemento, false);
						devolver.add(nuevoElemento);
					}
				}
			}
		}
		return devolver;
	}

	public ArrayList<Transicion> transicionesPosiblesEnUnEstado(Estado estado) {
		ArrayList<Transicion> transicionesNuevas = new ArrayList<>();
	
		for (Elemento elemento : estado.elementos) {
			Transicion transicionAuxiliar = new Transicion();
	
			if (elemento.marcadorAlFinal()) {// Tenemos que anadir una reduccion...
				transicionAuxiliar.anadirReduccion(); // El destino es la regla que reduce...
				transicionAuxiliar.anadirDestino(numeroReglaAPartirElemento(elemento));
				if(elemento.produccion.size() > 1) {
					transicionAuxiliar.anadirOrigen(new Expresion(elemento.produccion.get(elemento.produccion.size()-2).expresion,true));
				}else {
					transicionAuxiliar.anadirOrigen(new Expresion("lambda",true));

				}
				transicionesNuevas.add(transicionAuxiliar);

			} else { // Si no esta alfinal la transicion, crearla, apuntando a un estado existene o
						// el que se cree...
				Expresion origen = new Expresion(elemento.produccion.get(elemento.indiceMarcador() + 1));
				transicionAuxiliar.anadirOrigen(origen); // Origen la expresion del elemento siguiente al marcador
				ArrayList<Elemento> elementosParaCrearEstado = new ArrayList<>();

				for (Elemento elemento2 : estado.elementos) {// Conjunto de elementos para crear o ver si existe el
					if (elementoTieneTransicionEnEsaExpresion(elemento2, origen)) {
						Elemento elementoAux = new Elemento(elemento2);	
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

	public boolean elementoTieneTransicionEnEsaExpresion(Elemento elemento, Expresion simbolo) {
		int indiceMarcador = elemento.indiceMarcador();

		if (elemento.marcadorAlFinal())
			return false;
		if (elemento.produccion.get(indiceMarcador + 1).expresion.equals(simbolo.expresion))
			return true;

		return false;
	}

	public int numeroReglaAPartirElemento(Elemento elemento) {
		int devolver = -1;

		ArrayList<Expresion> produccionRegla = new ArrayList<>();
		produccionRegla.addAll(elemento.produccion);
		produccionRegla.remove(produccionRegla.size() - 1);
		Regla reglaAux = new Regla(elemento.identificador, produccionRegla);

		devolver = gramatica.numeroRegla(reglaAux);

		return devolver;
	}

}
