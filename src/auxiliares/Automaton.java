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


	public void createState0() throws FileNotFoundException {
        Rule firstRule = grammar.rules.get(0);

        List<Expression> initProduction = new ArrayList<>();
        initProduction.add(new Expression(firstRule.identifier, false));
        GrammarElement firstElement = new GrammarElement("Inicio", initProduction, false);
        List<GrammarElement> stateElements = new ArrayList<>();
        stateElements.add(firstElement);

        createStateFromElement(stateElements);

        System.out.println("Final Alcanzado\n");

        try (PrintStream stream = new PrintStream(new FileOutputStream(new File("src/generated/Automata.txt")))) {
            stream.println(states.toString());
        }
    }

    public int createStateFromElement(List<GrammarElement> startingElements) {
        State newStates = new State();

        int newAddedElements = 0;
        for (GrammarElement startingElement : startingElements) {
            newStates.addElement(startingElement);
            int i = 0;
            do {
                List<GrammarElement> toAddElements = newElementsFromMarkedElement(newStates.getElements().get(i + newAddedElements));
                for (GrammarElement toAddElement : toAddElements) {
                    newStates.addElement(toAddElement);
                }
                i++;
            } while (newStates.getElements().size() - newAddedElements > i);
            newAddedElements += i;
        }

        int statePos = existsStates(newStates);
        if (statePos != -1) {
            return statePos;
        }

        states.add(newStates);
        int addedStatePos = states.size() - 1;

        List<Transition> stateTransitions = possibleTransitionsInState(states.get(addedStatePos));
        for (Transition transition : stateTransitions) {
            states.get(addedStatePos).addTransition(transition);
        }

        return addedStatePos;
    }

    public int existsStates(State state) {
        for (int i = 0; i < states.size(); i++) {
            if (equalStates(state, states.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public boolean equalStates(State state1, State state2) {
        if (state1.getElements().size() != state2.getElements().size()) {
            return false;
        }

        for (GrammarElement element1 : state1.getElements()) {
            boolean elementExists = false;

            for (GrammarElement element2 : state2.getElements()) {
                if (element1.isEqualElement(element2)) {
                    elementExists = true;
                    break;
                }
            }

            if (!elementExists) {
                return false;
            }
        }

        return true;
    }

    public List<GrammarElement> newElementsFromMarkedElement(GrammarElement elementToAnalyze) {
        List<GrammarElement> returnElements = new ArrayList<>();
        int markerIdx = elementToAnalyze.markerIdx();

        if (!elementToAnalyze.markerAtEnd()) {
            Expression nextMarkerProduction = elementToAnalyze.getProduction().get(markerIdx + 1);

            if (nextMarkerProduction.terminal) {
                return returnElements;
            } else {
                String identifier = nextMarkerProduction.expression;

                for (Rule reg : grammar.rules) {
                    if (reg.identifier.equals(identifier)) {
                        List<Expression> newElementProduction = new ArrayList<>(reg.production);
                        GrammarElement newElement = new GrammarElement(reg.identifier, newElementProduction, false);
                        returnElements.add(newElement);
                    }
                }
            }
        }

        return returnElements;
    }

    public List<Transition> possibleTransitionsInState(State state) {
        List<Transition> newTransitions = new ArrayList<>();

        for (GrammarElement grammarElement : state.getElements()) {
            Transition auxTransition = new Transition();

            if (grammarElement.markerAtEnd()) {
                auxTransition.addReduce();
                auxTransition.setDestination(numRuleFromElement(grammarElement));
                if (grammarElement.getProduction().size() > 1) {
                    auxTransition.setSource(new Expression(grammarElement.getProduction().get(grammarElement.getProduction().size() - 2).expression, true));
                } else {
                    auxTransition.setSource(new Expression("lambda", true));
                }
                newTransitions.add(auxTransition);
            } else {
                Expression origen = new Expression(grammarElement.getProduction().get(grammarElement.markerIdx() + 1));
                auxTransition.setSource(origen);
                List<GrammarElement> stateElements = new ArrayList<>();

                for (GrammarElement otherElement : state.getElements()) {
                    if (elementWithTransitionInExpression(otherElement, origen)) {
                        GrammarElement elementoAux = new GrammarElement(otherElement);
                        elementoAux.adelantaCursor();
                        stateElements.add(elementoAux);
                    }
                }

                int destino = createStateFromElement(stateElements);
                if (destino == -1) {
                    System.out.println("Creación del estado fallida");
                }

                auxTransition.setDestination(destino);
                newTransitions.add(auxTransition);
            }
        }

        return newTransitions;
    }

    public boolean elementWithTransitionInExpression(GrammarElement grammarElement, Expression symbol) {
        int markerIdx = grammarElement.markerIdx();

        if (grammarElement.markerAtEnd()) {
            return false;
        }

        return grammarElement.getProduction().get(markerIdx + 1).expression.equals(symbol.expression);
    }

    public int numRuleFromElement(GrammarElement grammarElement) {
        List<Expression> RuleProduction = new ArrayList<>(grammarElement.getProduction());
        RuleProduction.remove(RuleProduction.size() - 1);
        Rule reglaAux = new Rule(grammarElement.getIdentifier(), RuleProduction);
        return grammar.numRule(reglaAux);
    }
}
