package ATS_Tree;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {
    private String label;
    private List<ASTNode> children;
    private ASTNode parent;
    private ASTNode nextSibling;
    
    private int index; // Índice del estado en la tabla de transiciones

    public ASTNode(String label) {
        this.label = label;
        this.children = new ArrayList<>();
        this.parent = null;
        this.nextSibling = null;
        this.index = -1; // Inicializar el índice como -1
    }

    public String getLabel() {
        return label;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public ASTNode getParent() {
        return parent;
    }

    public ASTNode getNextSibling() {
        return nextSibling;
    }

    public void setParent(ASTNode parent) {
        this.parent = parent;
    }

    public void addChild(ASTNode child) {
        children.add(child);
        child.setParent(this);

        // Establecer el siguiente hermano
        if (children.size() > 1) {
            int index = children.indexOf(child);
            ASTNode previousSibling = children.get(index - 1);
            previousSibling.nextSibling = child;
        }
        
        // Actualizar los índices de los nodos
        updateIndices();
    }
    
    private void updateIndices() {
        if (parent != null) {
            int currentIndex = parent.children.indexOf(this);
            int startIndex = currentIndex + 1;

            for (int i = startIndex; i < parent.children.size(); i++) {
                ASTNode sibling = parent.children.get(i);
                sibling.index = sibling.parent.index;
            }
        }

        // Actualizar los índices de los hijos recursivamente
        for (ASTNode child : children) {
            child.updateIndices();
        }
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
