package AST;

import java.util.List;

/**
 * Clase que representa el AST de la gramática BNF
 */
class GrammarAST {
    private List<DefinitionNode> definitions;

    public GrammarAST(List<DefinitionNode> definitions) {
        this.definitions = definitions;
    }

    public List<DefinitionNode> getDefinitions() {
        return definitions;
    }
}