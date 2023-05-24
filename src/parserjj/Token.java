package parserjj;

public class Token implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public TokenKind kind;
    public int beginLine;
    public int beginColumn;
    public int endLine;
    public int endColumn;
    public String image;
    public Token next;
    public Token specialToken;

    public Token() {}

    public Token(TokenKind kind) {
        this(kind, null);
    }

    public Token(TokenKind kind, String image) {
        this.kind = kind;
        this.image = image;
    }

    public Object getValue() {
        return image;
    }
    
    public TokenKind getKind() {
        return kind;
    }
    
    public String toString() {
        return image;
    }

    public static Token newToken(TokenKind kind, String image) {
        return new Token(kind, image);
    }

    public static Token newToken(TokenKind kind) {
        return newToken(kind, null);
    }
}
