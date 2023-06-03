package auxiliares;

import java.util.ArrayList;
import java.util.List;

public class Grammar {

	ArrayList<Rule> rules;

	public Grammar() {
		rules = new ArrayList<>();
	}

	public Grammar(Grammar grammar) {
		rules = new ArrayList<>();
		rules.addAll(grammar.rules);
	}

	public void addRule(Rule rule) {
		rules.add(rule);
	}

	private void calcularFirsts() {
		for (Rule reg : rules) {
			Expression analizar = reg.production.get(0);
			calculaFirstsRule(reg, analizar);
		}
		unificarFirsts();
	}

	private void calculaFirstsRule(Rule rule, Expression expression) {

		if (expression.isTerminal()) {
			rule.addFirsts(expression.expression);
		} else {
			for (Rule regs : rules) {
				// Buscamos en las reglas con identifciador igual a la expresion noterminal que
				// estamos analizando
				if (regs.identifier.equals(expression.expression)) { // Seguimos con la misma regla con distintas
																		// expresiones, para que vaya anadiendo todas
																		// las
					Expression aux = regs.production.get(0); // terminales que correspondan
					if (!aux.expression.equals(expression.expression)) // Evitar recursividad infinita
						calculaFirstsRule(rule, aux);
				}
			}
		}
	}

	private void unificarFirsts() {
		ArrayList<String> identificadores = getIdentifiers();

		for (String id : identificadores) {
			ArrayList<String> primerosUnificados = new ArrayList<String>();

			for (Rule reg : rules) {
				if (id.equals(reg.identifier)) {
					primerosUnificados = unionConjuntos(primerosUnificados, reg.firsts);
				}
			}

			for (Rule reg : rules) {
				if (id.equals(reg.identifier)) {
					reg.firsts = primerosUnificados;
				}
			}
		}

	}

	private void calcularFollows() {
		ArrayList<String> identificadores = getIdentifiers();

		for (Rule reg : rules) {
			if (reg.identifier.equals(identificadores.get(0))) {
				reg.addFollows("<EOF>");
			}
		}

		for (String id : identificadores) {
			ArrayList<String> aux = calcularNextIdentifier(id);

			for (Rule reg : rules) {
				if (reg.identifier.equals(id)) {
					reg.follows = unionConjuntos(reg.follows, aux);
				}
			}
		}
	}

	private ArrayList<String> calcularNextIdentifier(String identificador) {
		ArrayList<String> devolver = new ArrayList<String>();

		for (Rule rule : rules) {

			List<Expression> prod = rule.production;

			for (Expression expr : prod) {
				if (identificador.equals(expr.expression)) {
					ArrayList<String> aux = calcularNextIdentifierRule(identificador, rule);
					devolver = unionConjuntos(devolver, aux);
				}
			}

		}

		return devolver;
	}

	private ArrayList<String> calcularNextIdentifierRule(String identificador, Rule reg) {
		ArrayList<String> devolver = new ArrayList<String>();

		List<Expression> expresiones = reg.production;

		for (int i = 0; i < expresiones.size(); i++) {

			if (identificador.equals(expresiones.get(i).expression)) {

				if (i == expresiones.size() - 1) { // Esta al final de la expresion, añadimos siguientes de el
													// identificador de la regla.
					if (!interbloqueo(identificador, reg.identifier)) {
						ArrayList<String> conjuntoSiguientesDeLaRegla = calcularNextIdentifier(reg.identifier);
						devolver = unionConjuntos(devolver, reg.follows);
						devolver = unionConjuntos(devolver, conjuntoSiguientesDeLaRegla);
					}else {
						devolver = unionConjuntos(devolver, reg.follows);
					}

				} else {

					if (expresiones.get(i + 1).terminal) { // Terminal se añade y fuera
						if (!devolver.contains(expresiones.get(i + 1).expression))
							devolver.add(expresiones.get(i + 1).expression);

					} else { // No terminal se mira el conjunto primeros del siguiente simbolo. si contiene
								// lambda sig y primeros de ese, sino solo primeros.
						ArrayList<String> primerosAgregar = getFirstsByIdentifier(
								expresiones.get(i + 1).expression);
						devolver = unionConjuntos(devolver, primerosAgregar);
						if (lambdainFirsts(primerosAgregar)) { // Hay que añadir el primeros y el siguienes de
																		// la regla que pasamos por parametro
							// Aqui añadir tambien los siguientes que se supone que lo saco con recursividad
							ArrayList<String> conjuntoSiguientesDeLaRegla = calcularNextIdentifier(
									reg.identifier);
							devolver = unionConjuntos(devolver, reg.follows);
							devolver = unionConjuntos(devolver, conjuntoSiguientesDeLaRegla);

						}
					}
				}
			}
		}

		return devolver;
	}

	private boolean interbloqueo(String id1, String id2) {
		
		ArrayList<Rule> reglasId1 = getRulesByIdentifier(id1);
		ArrayList<Rule> reglasId2 = getRulesByIdentifier(id2);
		
		for(Rule regs1 : reglasId1) {
			//Alugna de las regs1 acaba con el id2.
			if(regs1.production.get(regs1.production.size()-1).expression.equals(id2)) {
				for(Rule regs2 : reglasId2) {
					if(regs2.production.get(regs2.production.size()-1).expression.equals(id1)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private ArrayList<Rule> getRulesByIdentifier(String id) {
		ArrayList<Rule> devolver = new ArrayList<>();

		for (Rule reg : rules) {
			if (reg.identifier.equals(id)) {
				devolver.add(reg);
			}
		}

		return devolver;
		
	}
	
	private ArrayList<String> getFirstsByIdentifier(String id) {
		ArrayList<String> devolver = new ArrayList<>();

		for (Rule reg : rules) {
			if (reg.identifier.equals(id)) {
				devolver = reg.firsts;
				break;
			}
		}

		return devolver;
	}

	private boolean lambdainFirsts(ArrayList<String> primeros) {

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

	private ArrayList<String> getIdentifiers() {
		ArrayList<String> identificadores = new ArrayList<>();

		String identificador = "";
		for (Rule reg : rules) {
			String aux = reg.identifier;
			if (!identificador.equals(aux)) {
				identificador = aux;
				identificadores.add(identificador);
			}
		}
		return identificadores;
	}

	public int numRule(Rule rule) {
		int numeroRegla = -1;

		for (int i = 0; i < rules.size(); i++) {
			if (rule.equalRules(rules.get(i)))
				numeroRegla = i;
		}

		return numeroRegla;
	}
	
	public void addLambda() {

		for (Rule reg : rules) {
			if (reg.production.size() == 0) {
				reg.addExpressions("lambda", true);
			}
		}

		calcularFirsts();
		calcularFollows();

	}
	
	public void removeLambda() {
		
		for(Rule reg : rules) {
			if(reg.production.size() == 1) {
				if(reg.production.get(0).expression.equals("lambda")) {
					reg.production.clear();
				}
			}
		}
		
	}

	public String toString() {
	    StringBuilder builder = new StringBuilder();
	    for (Rule reg : rules) {
	        builder.append(reg.toString()).append("\n");
	    }
	    return builder.toString();
	}
}
