package parserjj;

import java.util.ArrayList;
import java.util.List;

public class Gramatica {

    private List<Regla> reglas;

    public Gramatica() {
        reglas = new ArrayList<>();
    }

    public void agregarRegla(Regla regla) {
        reglas.add(regla);
    }

    // Getters y setters

    public List<Regla> getReglas() {
        return reglas;
    }

    public void setReglas(List<Regla> reglas) {
        this.reglas = reglas;
    }

    public static class Regla {
        private String identificador;
        private List<Expresion> produccion;

        public Regla(String identificador) {
            this.identificador = identificador;
            produccion = new ArrayList<>();
        }

        public void agregarExpresion(String expresion, boolean esTerminal) {
            produccion.add(new Expresion(expresion, esTerminal));
        }

        // Getters y setters

        public String getIdentificador() {
            return identificador;
        }

        public void setIdentificador(String identificador) {
            this.identificador = identificador;
        }

        public List<Expresion> getProduccion() {
            return produccion;
        }

        public void setProduccion(List<Expresion> produccion) {
            this.produccion = produccion;
        }
    }

    public static class Expresion {
        private String expresion;
        private boolean terminal;

        public Expresion(String expresion, boolean terminal) {
            this.expresion = expresion;
            this.terminal = terminal;
        }

        // Getters y setters

        public String getExpresion() {
            return expresion;
        }

        public void setExpresion(String expresion) {
            this.expresion = expresion;
        }

        public boolean esTerminal() {
            return terminal;
        }

        public void setTerminal(boolean terminal) {
            this.terminal = terminal;
        }
    }

    // Otros métodos y lógica de la clase
}

