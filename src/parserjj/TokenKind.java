package parserjj;

public interface TokenKind{
	public int EOF = 0;
	
    public int NOTERMINAL = 1;
    public int TERMINAL = 2;
    public int EQ = 3;
    public int BAR = 4;
    public int SEMICOLON = 5;
    public int BLANCO = 6;
    public int COMENTARIO = 7;
    
    // Otros especiales
    public int UNKNOWN = 8;
    
    // Agregar mas tipos de tokens si es necesario
}
