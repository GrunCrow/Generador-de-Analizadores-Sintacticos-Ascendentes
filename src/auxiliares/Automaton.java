package auxiliares;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

// Automata reconocedor de prefijos variables
public class Automaton {
    private List<State> states;
    private Grammar grammar;

    public Automaton(Grammar grammar) {
        states = new ArrayList<>();
        this.grammar = grammar;
    }

    
    
    public List<State> getStates() {
		return states;
	}



	public Grammar getGrammar() {
		return grammar;
	}



	public void creaEstadoCero() throws FileNotFoundException {
        Rule primeraRegla = grammar.rules.get(0);

        List<Expression> produccionInicio = new ArrayList<>();
        produccionInicio.add(new Expression(primeraRegla.identifier, false));
        GrammarElement primerElemento = new GrammarElement("Inicio", produccionInicio, false);
        List<GrammarElement> elementoParaCreacionEstados = new ArrayList<>();
        elementoParaCreacionEstados.add(primerElemento);

        creaEstadoAPartirUnElemento(elementoParaCreacionEstados);

        System.out.println("Final Alcanzado");

        try (PrintStream stream = new PrintStream(new FileOutputStream(new File("src/generated/Automata.txt")))) {
            stream.println(states.toString());
        }
    }

    public int creaEstadoAPartirUnElemento(List<GrammarElement> elementosPartida) {
        State nuevoEstado = new State();

        int numElementosAnadidos = 0;
        for (GrammarElement elementoPartida : elementosPartida) {
            nuevoEstado.anadirElemento(elementoPartida);
            int i = 0;
            do {
                List<GrammarElement> elementosAnadir = elementosNuevosPartiendoElementoActualMarcado(nuevoEstado.getElements().get(i + numElementosAnadidos));
                for (GrammarElement anadir : elementosAnadir) {
                    nuevoEstado.anadirElemento(anadir);
                }
                i++;
            } while (nuevoEstado.getElements().size() - numElementosAnadidos > i);
            numElementosAnadidos += i;
        }

        int posicionEstado = existeEstado(nuevoEstado);
        if (posicionEstado != -1) {
            return posicionEstado;
        }

        states.add(nuevoEstado);
        int posicionEstadoAnadido = states.size() - 1;

        List<Transition> transicionesEstado = transicionesPosiblesEnUnEstado(states.get(posicionEstadoAnadido));
        for (Transition transition : transicionesEstado) {
            states.get(posicionEstadoAnadido).anadirTransicion(transition);
        }

        return posicionEstadoAnadido;
    }

    public int existeEstado(State state) {
        for (int i = 0; i < states.size(); i++) {
            if (estadosIguales(state, states.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public boolean estadosIguales(State estado1, State estado2) {
        if (estado1.getElements().size() != estado2.getElements().size()) {
            return false;
        }

        for (GrammarElement elemento1 : estado1.getElements()) {
            boolean existeElemento = false;

            for (GrammarElement elemento2 : estado2.getElements()) {
                if (elemento1.elementosIguales(elemento2)) {
                    existeElemento = true;
                    break;
                }
            }

            if (!existeElemento) {
                return false;
            }
        }

        return true;
    }

    public List<GrammarElement> elementosNuevosPartiendoElementoActualMarcado(GrammarElement elementoAnalizar) {
        List<GrammarElement> devolver = new ArrayList<>();
        int indiceMarcador = elementoAnalizar.indiceMarcador();

        if (!elementoAnalizar.marcadorAlFinal()) {
            Expression produccionSiguienteMarcador = elementoAnalizar.getProduction().get(indiceMarcador + 1);

            if (produccionSiguienteMarcador.terminal) {
                return devolver;
            } else {
                String identificador = produccionSiguienteMarcador.expression;

                for (Rule reg : grammar.rules) {
                    if (reg.identifier.equals(identificador)) {
                        List<Expression> produccionNuevoElemento = new ArrayList<>(reg.production);
                        GrammarElement nuevoElemento = new GrammarElement(reg.identifier, produccionNuevoElemento, false);
                        devolver.add(nuevoElemento);
                    }
                }
            }
        }

        return devolver;
    }

    public List<Transition> transicionesPosiblesEnUnEstado(State state) {
        List<Transition> transicionesNuevas = new ArrayList<>();

        for (GrammarElement grammarElement : state.getElements()) {
            Transition transicionAuxiliar = new Transition();

            if (grammarElement.marcadorAlFinal()) {
                transicionAuxiliar.addReduce();
                transicionAuxiliar.setDestination(numeroReglaAPartirElemento(grammarElement));
                if (grammarElement.getProduction().size() > 1) {
                    transicionAuxiliar.setSource(new Expression(grammarElement.getProduction().get(grammarElement.getProduction().size() - 2).expression, true));
                } else {
                    transicionAuxiliar.setSource(new Expression("lambda", true));
                }
                transicionesNuevas.add(transicionAuxiliar);
            } else {
                Expression origen = new Expression(grammarElement.getProduction().get(grammarElement.indiceMarcador() + 1));
                transicionAuxiliar.setSource(origen);
                List<GrammarElement> elementosParaCrearEstado = new ArrayList<>();

                for (GrammarElement elemento2 : state.getElements()) {
                    if (elementoTieneTransicionEnEsaExpresion(elemento2, origen)) {
                        GrammarElement elementoAux = new GrammarElement(elemento2);
                        elementoAux.adelantaCursor();
                        elementosParaCrearEstado.add(elementoAux);
                    }
                }

                int destino = creaEstadoAPartirUnElemento(elementosParaCrearEstado);
                if (destino == -1) {
                    System.out.println("Creación del estado fallida");
                }

                transicionAuxiliar.setDestination(destino);
                transicionesNuevas.add(transicionAuxiliar);
            }
        }

        return transicionesNuevas;
    }

    public boolean elementoTieneTransicionEnEsaExpresion(GrammarElement grammarElement, Expression simbolo) {
        int indiceMarcador = grammarElement.indiceMarcador();

        if (grammarElement.marcadorAlFinal()) {
            return false;
        }

        return grammarElement.getProduction().get(indiceMarcador + 1).expression.equals(simbolo.expression);
    }

    public int numeroReglaAPartirElemento(GrammarElement grammarElement) {
        List<Expression> produccionRegla = new ArrayList<>(grammarElement.getProduction());
        produccionRegla.remove(produccionRegla.size() - 1);
        Rule reglaAux = new Rule(grammarElement.getIdentifier(), produccionRegla);
        return grammar.numRule(reglaAux);
    }
}
