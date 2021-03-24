package principal;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import controlador.Controlador;
import modelo.Modelo;
import vista.Vista;

public class Principal {

	public static void main(String[] args) 
	{
		try{
			  JFrame.setDefaultLookAndFeelDecorated(true);
			  JDialog.setDefaultLookAndFeelDecorated(true);
			  //UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			  //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			  UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			  //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			}
			catch (Exception e)
			 {
			  e.printStackTrace();
			 }
		//Instancia las clases modelo, vista y contrlador
		Modelo modelo = new Modelo();
		Vista vista=new Vista();
		Controlador controlador = new Controlador(vista,modelo);
		//Genera las conexiones entre modelo, vista y controlador
		modelo.inicializa(controlador);//Para que el modelo conozca al controlador
		vista.inicializa(controlador);//Para que la vista conozca al controlador
		//
		//Ejecuta la aplicación
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					vista.setVisible(true);//Muestra la aplicación
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
}
