package auxiliares;

import java.util.ArrayList;

public class Gramatica {

	ArrayList<Regla> reglas;

	public Gramatica() {
		reglas = new ArrayList<>();
	}

	public Gramatica(Gramatica gramatica) {
		reglas = new ArrayList<>();
		reglas.addAll(gramatica.reglas);
	}

	public void anadirRegla(Regla regla) {
		reglas.add(regla);
	}

	private void calcularPrimeros() {
		for (Regla reg : reglas) {
			Expresion analizar = reg.produccion.get(0);
			calculaPrimerosRegla(reg, analizar);
		}
		unificarPrimeros();
	}

	private void calculaPrimerosRegla(Regla regla, Expresion expresion) {

		if (expresion.esTerminal()) {
			regla.anadirPrimeros(expresion.expresion);
		} else {
			for (Regla regs : reglas) {
				// Buscamos en las reglas con identifciador igual a la expresion noterminal que
				// estamos analizando
				if (regs.identificador.equals(expresion.expresion)) { // Seguimos con la misma regla con distintas
																		// expresiones, para que vaya anadiendo todas
																		// las
					Expresion aux = regs.produccion.get(0); // terminales que correspondan
					if (!aux.expresion.equals(expresion.expresion)) // Evitar recursividad infinita
						calculaPrimerosRegla(regla, aux);
				}
			}
		}
	}

	private void unificarPrimeros() {
		ArrayList<String> identificadores = obtenerIdentificadores();

		for (String id : identificadores) {
			ArrayList<String> primerosUnificados = new ArrayList<String>();

			for (Regla reg : reglas) {
				if (id.equals(reg.identificador)) {
					primerosUnificados = unionConjuntos(primerosUnificados, reg.primeros);
				}
			}

			for (Regla reg : reglas) {
				if (id.equals(reg.identificador)) {
					reg.primeros = primerosUnificados;
				}
			}
		}

	}

	private void calcularSiguientes() {
		ArrayList<String> identificadores = obtenerIdentificadores();

		for (Regla reg : reglas) {
			if (reg.identificador.equals(identificadores.get(0))) {
				reg.anadirSiguientes("<EOF>");
			}
		}

		for (String id : identificadores) {
			ArrayList<String> aux = calcularSiguienteIdentificador(id);

			for (Regla reg : reglas) {
				if (reg.identificador.equals(id)) {
					reg.siguientes = unionConjuntos(reg.siguientes, aux);
				}
			}
		}
	}

	private ArrayList<String> calcularSiguienteIdentificador(String identificador) {
		ArrayList<String> devolver = new ArrayList<String>();

		for (Regla regla : reglas) {

			ArrayList<Expresion> prod = regla.produccion;

			for (Expresion expr : prod) {
				if (identificador.equals(expr.expresion)) {
					ArrayList<String> aux = calcularSiguienteIdentificadorRegla(identificador, regla);
					devolver = unionConjuntos(devolver, aux);
				}
			}

		}

		return devolver;
	}

	private ArrayList<String> calcularSiguienteIdentificadorRegla(String identificador, Regla reg) {
		ArrayList<String> devolver = new ArrayList<String>();

		ArrayList<Expresion> expresiones = reg.produccion;

		for (int i = 0; i < expresiones.size(); i++) {

			if (identificador.equals(expresiones.get(i).expresion)) {

				if (i == expresiones.size() - 1) { // Esta al final de la expresion, añadimos siguientes de el
													// identificador de la regla.
					if (compruebaNoExisteInterbloqueo(identificador, reg.identificador)) {
						ArrayList<String> conjuntoSiguientesDeLaRegla = calcularSiguienteIdentificador(reg.identificador);
						devolver = unionConjuntos(devolver, reg.siguientes);
						devolver = unionConjuntos(devolver, conjuntoSiguientesDeLaRegla);
					}else {
						devolver = unionConjuntos(devolver, reg.siguientes);
					}

				} else {

					if (expresiones.get(i + 1).terminal) { // Terminal se añade y fuera
						if (!devolver.contains(expresiones.get(i + 1).expresion))
							devolver.add(expresiones.get(i + 1).expresion);

					} else { // No terminal se mira el conjunto primeros del siguiente simbolo. si contiene
								// lambda sig y primeros de ese, sino solo primeros.
						ArrayList<String> primerosAgregar = obtenerConjuntoPrimerosReglaPorIdentificador(
								expresiones.get(i + 1).expresion);
						devolver = unionConjuntos(devolver, primerosAgregar);
						if (primerosContieneLambda(primerosAgregar)) { // Hay que añadir el primeros y el siguienes de
																		// la regla que pasamos por parametro
							// Aqui añadir tambien los siguientes que se supone que lo saco con recursividad
							ArrayList<String> conjuntoSiguientesDeLaRegla = calcularSiguienteIdentificador(
									reg.identificador);
							devolver = unionConjuntos(devolver, reg.siguientes);
							devolver = unionConjuntos(devolver, conjuntoSiguientesDeLaRegla);

						}
					}
				}
			}
		}

		return devolver;
	}

	private boolean compruebaNoExisteInterbloqueo(String id1, String id2) {
		
		ArrayList<Regla> reglasId1 = obtenerConjuntoReglasPorIdentificador(id1);
		ArrayList<Regla> reglasId2 = obtenerConjuntoReglasPorIdentificador(id2);
		
		for(Regla regs1 : reglasId1) {
			//Alugna de las regs1 acaba con el id2.
			if(regs1.produccion.get(regs1.produccion.size()-1).expresion.equals(id2)) {
				for(Regla regs2 : reglasId2) {
					if(regs2.produccion.get(regs2.produccion.size()-1).expresion.equals(id1)) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	private ArrayList<Regla> obtenerConjuntoReglasPorIdentificador(String id) {
		ArrayList<Regla> devolver = new ArrayList<>();

		for (Regla reg : reglas) {
			if (reg.identificador.equals(id)) {
				devolver.add(reg);
			}
		}

		return devolver;
		
	}
	
	private ArrayList<String> obtenerConjuntoPrimerosReglaPorIdentificador(String id) {
		ArrayList<String> devolver = new ArrayList<>();

		for (Regla reg : reglas) {
			if (reg.identificador.equals(id)) {
				devolver = reg.primeros;
				break;
			}
		}

		return devolver;
	}

	private boolean primerosContieneLambda(ArrayList<String> primeros) {

		for (String primero : primeros) {
			if (primero.equals("lambda"))
				return true;
		}

		return false;
	}

	private ArrayList<String> unionConjuntos(ArrayList<String> conjunto1, ArrayList<String> conjunto2) {
		ArrayList<String> conjuntoAux = new ArrayList<>();
		conjuntoAux.addAll(conjunto1);
		for (String token : conjunto2) {
			if (!conjunto1.contains(token))
				conjuntoAux.add(token);
		}

		return conjuntoAux;
	}

	private ArrayList<String> obtenerIdentificadores() {
		ArrayList<String> identificadores = new ArrayList<>();

		String identificador = "";
		for (Regla reg : reglas) {
			String aux = reg.identificador;
			if (!identificador.equals(aux)) {
				identificador = aux;
				identificadores.add(identificador);
			}
		}
		return identificadores;
	}

	public int numeroRegla(Regla regla) {
		int numeroRegla = -1;

		for (int i = 0; i < reglas.size(); i++) {
			if (regla.reglasIguales(reglas.get(i)))
				numeroRegla = i;
		}

		return numeroRegla;
	}
	
	public void insertaLambda() {

		for (Regla reg : reglas) {
			if (reg.produccion.size() == 0) {
				reg.anadirExpresion("lambda", true);
			}
		}

		calcularPrimeros();
		calcularSiguientes();

	}
	
	public void eliminaLambda() {
		
		for(Regla reg : reglas) {
			if(reg.produccion.size() == 1) {
				if(reg.produccion.get(0).expresion.equals("lambda")) {
					reg.produccion.clear();
				}
			}
		}
		
	}

	public String toString() {
		String devolver = "";

		for (Regla reg : reglas) {
			devolver += reg.toString() + "\n";
		}
		return devolver;
	}
}
