package parserjj;

import java.util.ArrayList;
import java.util.List;

public class Gramatica {
    private List<Regla> rules;

    public Gramatica() {
        rules = new ArrayList<>();
    }

    public void addRule(Regla rule) {
        rules.add(rule);
    }

    // Agrega otros métodos según sea necesario para acceder a las reglas gramaticales
}
