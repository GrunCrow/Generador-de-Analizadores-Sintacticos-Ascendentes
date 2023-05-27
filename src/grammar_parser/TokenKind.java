package grammar_parser;

public interface TokenKind{
    public int NOTERMINAL = 0;
    public int TERMINAL = 1;
    public int EQ = 2;
    public int BAR = 3;
    public int SEMICOLON = 4;
    public int BLANCO = 5;
    public int COMENTARIO = 6;
    public int UNKNOWN = 7;
    // Agregar mas tipos de tokens si es necesario
}
