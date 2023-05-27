
package grammar_parser;

/**
 * Elemento de la tabla de acción
 *
 */
public class ActionElement {
	
	// tipos de acciones posibles

	/**
	 * Constante que identifica un elemento de tipo aceptar
	 */
	public static final int ACCEPT = 0;
	
	/**
	 * Constannte que identifica un elemento de tipo shift
	 */
	public static final int SHIFT = 1;
	
	/**
	 * Constante que identifica un elemento de tipo reduce
	 */
	public static final int REDUCE = 2;
	
	/**
	 * Tipo de elemento (ACEPT, SHIFT o REDUCE)
	 */
	private int type;
	
	/**
	 * Valor del elemento
	 */
	private int value;
	
	/**
	 * Constructor
	 * @param kind
	 */
	public ActionElement(int type, int value) 
	{
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Obtiene el tipo de elemento: ACEPT, SHIFT o REDUCE
	 * @return
	 */
	public int getType() 
	{
		return type;
	}
	
	/**
	 * Obtiene el valor (estado a apilar o regla a reducir)
	 * @return
	 */
	public int getValue() 
	{
		return value;
	}
}
