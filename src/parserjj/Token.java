package parserjj;

public class Token implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public TokenKind kind;
    public int beginLine;
    public int beginColumn;
    public int endLine;
    public int endColumn;
    public String lexeme;
    public Token next;
    public Token specialToken;

    public Token() {}

    public Token(TokenKind kind) {
        this(kind, null);
    }

    public Token(TokenKind kind, String lexeme) {
        this.kind = kind;
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }
    
    public TokenKind getKind() {
        return kind;
    }
    
    public String toString() {
        return lexeme;
    }

    public static Token newToken(TokenKind kind, String lexeme) {
        return new Token(kind, lexeme);
    }

    public static Token newToken(TokenKind kind) {
        return newToken(kind, null);
    }

	public int getRow() {
		return beginLine;
	}

	public int getColumn() {
		return beginColumn;
	}
}
