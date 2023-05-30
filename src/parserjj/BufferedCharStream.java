package parserjj;


import java.io.*;

/**
 * Flujo de entrada basado en un doble buffer que optimiza el acceso
 * a un fichero de caracteres y permite retroceder en la lectura
 * 
 * @author Francisco Jos� Moreno Velo
  */
public class BufferedCharStream {

	/**
	 * Tama�o de la mitad del buffer
	 */
	private static final int HALFSIZE = 1024;
	
	/**
	 * Tama�o del buffer
	 */
	private static final int SIZE = 2*HALFSIZE;

	/**
	 * Flujo de entrada
	 */
	private InputStream stream;
	
	/**
	 * Buffer de datos
	 */
	private byte[] buffer;
	
	/**
	 * Contador de l�nea de los caracteres del buffer
	 */
	private int[] row;
	
	/**
	 * Contador de columna de los caracteres del buffer
	 */
	private int[] column;
	
	/**
	 * Flag que indica si el �ltimo trozo en llenarse ha sido
	 * el que empieza al principio (false) o en la mitad (true)
	 */
	private boolean half;
	
	/**
	 * Marcador del �ltimo car�cter leido
	 */
	private int index;
	
	/**
	 * Constructor
	 * 
	 * @param file
	 * @throws IOException
	 */
	public BufferedCharStream(File file) throws IOException 
	{
		this.stream = new FileInputStream(file);
		this.buffer = new byte[SIZE];
		this.row = new int[SIZE];
		this.column = new int[SIZE];
		this.half = true; // para que comience llenando la parte baja
		load();
		this.index = -1;
	}
	
	/**
	 * Cierra el flujo de entrada
	 */
	public void close() 
	{
		try { this.stream.close(); }
		catch(IOException ex) {}
	}
	
	/**
	 * Carga un bloque de medio buffer en la zona correspondiente
	 * y calcula los valores de l�neas y columnas
	 */
	private void load() 
	{
		int begin = (half? 0 : HALFSIZE);
		int read = 0;
		try { read = stream.read(buffer,begin,HALFSIZE); }
		catch(IOException ex) {}
		if(read < HALFSIZE) 
		{
			for(int i=read; i<HALFSIZE; i++) buffer[begin+i] = 0;
		}
		int prev = (half? SIZE-1 : HALFSIZE-1);
		int newrow = row[prev];
		int newcolumn = column[prev]+1;
		if(buffer[prev] == '\n') { newrow++; newcolumn = 0; }
		for(int i=begin; i<begin+HALFSIZE; i++)
		{
			row[i] = newrow;
			column[i] = newcolumn;
			newcolumn++;
			if(buffer[i] == '\n') { newrow++; newcolumn = 0; }
		}
		half  = !half;
	}
	
	/**
	 * Obtiene el siguiente car�cter del flujo de entrada.
	 * El m�todo es responsable de cargar un nuevo trozo del
	 * buffer cuando sea necesario
	 * @return Siguiente car�cter
	 */
	public char getNextChar() 
	{
		index++;
		if(index==HALFSIZE && !half) load();
		else if(index == SIZE && half) { index=0; load(); }
		else if(index == SIZE) { index = 0; }
		return (char) buffer[index];
	}
	
	/**
	 * Obtiene el n�mero de fila correspondiente al �ltimo car�cter leido
	 * @return N�mero de fila
	 */
	public int getRow() 
	{
		return row[index];
	}
	
	/**
	 * Obtiene el n�mero de columna correspondiente al �ltimo car�cter leido
	 * @return N�mero de columna
	 */
	public int getColumn() 
	{
		return column[index];
	}
	
	/**
	 * Retrocede un n�mero de caracteres en el flujo de entrada
	 * @param disp N�mero de caracteres a retroceder
	 */
	public void retract(int disp) 
	{
		index -= disp;
		if(index <0) index += SIZE;
	}
}
