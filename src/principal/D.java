package principal;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class D //Clase para debug 
{
	private static int contador=0;
	private static boolean debug=true;
	
	public static int getContador() 
	{
		return contador;
	}

	public static void setContador(int contador) 
	{
		D.contador = contador;
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) 
	{
		D.debug = debug;
	}

	static public void msg (String mensaje)
	{
		if(debug) System.out.println(mensaje);
	}
	
	static public void msg (String mensaje, Object out)
	{
		if(debug) ((JTextArea) out).append(mensaje);
	}	

	static public void error(String mensaje)
	{
		if(debug) JOptionPane.showMessageDialog
		(null, mensaje, "¡error!",JOptionPane.ERROR_MESSAGE);
	}

	static public void ok(String mensaje)
	{
		if(debug) JOptionPane.showMessageDialog
		(null, mensaje,"todo ok",JOptionPane.INFORMATION_MESSAGE);

	}
	static public void contador()
	{
		if(debug) System.out.println(++contador);

	}
	static public void reset()
	{
		contador=0;
	}
}//clsas D