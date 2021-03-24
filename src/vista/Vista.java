package vista;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.border.SoftBevelBorder;
import controlador.Controlador;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Toolkit;
/****************************************
 * Vista en consola de la aplicación
 * **************************************/
public class Vista extends JFrame 
{
	private static final long serialVersionUID = 1L;//Componente serializable
	public JTextArea taRespuesta;
	public JPanel frameVista;
	public JTextField tfPin;
	public JTextField tfNombre;
	public JComboBox<String> cbPuertos;
	public JButton btLeer;
	public JButton btConfigurar;
	public JComboBox<Integer> cbBaudios;
	public JButton btAbrirPuerto;
	//Controlador de la aplicación
	private Controlador controlador;
	/***************************************
	 * Método que hace que "vista" tenga
	 * una referencia de controlador 
	 ************************************ */
	public void inicializa(Controlador cont)
	{
		this.controlador=cont;//Apunta al controlador
	}
	/**************************************
	 * Dibuja el frame
	 **************************************/
	public Vista() 
	{
		//Listener de eventos del form principal
		addWindowListener(new LocalWindowListener());
		//Configuración del form
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);//La ventana de tamaño fijo
		setBounds(100, 100, 726, 376);
		//Panel del Frame
		frameVista = new JPanel();
		frameVista.setToolTipText("si el dispositivo comunica, al abrir el puerto, se recibe \"OK\" ");
		frameVista.setBackground(new Color(220, 220, 220));
		frameVista.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(frameVista);//frameVista es le JPanel donde se dibuja todo
		frameVista.setLayout(null);//El panel tiene un  layout "relative"
		setTitle("no hay ningún puerto abierto: programador del módulo DX-BT18");
		//Icono
		//URL iconURL = getClass().getResource("/recursos/desconectado.png");
		//ImageIcon icon = new ImageIcon(iconURL);
		setIcono(false);//Icono desconectado
		//setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\PC.5014885\\Proyectos\\Java\\Configura_DX_BT_18\\src\\recursos\\DXBT18_A.png"));
		/*..................................
		//Controles a dibujar sobre el panel
		....................................*/
		//respuestas del dispositivo
		taRespuesta = new JTextArea();
		taRespuesta.setForeground(new Color(253, 245, 230));
		taRespuesta.setFont(new Font("Consolas", Font.PLAIN, 14));
		taRespuesta.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(128, 0, 128), new Color(255, 215, 0), null, null));
		taRespuesta.setToolTipText("una vez configurado el dispositivo con un nuevo baudrate hay que cerrar el puerto y abrirlo con el nuevo baudrate");
		taRespuesta.setBackground(new Color(0, 0, 0));
		taRespuesta.setBounds(370, 11, 309, 306);
		frameVista.add(taRespuesta);
		//Nombre a programar en el dispositivo		
		tfNombre = new JTextField();
		tfNombre.setToolTipText("nombre que tendr\u00E1 el dispositivo (max. 19 caracteres)");
		tfNombre.setBounds(85, 45, 235, 20);
		frameVista.add(tfNombre);
		tfNombre.setColumns(10);
		//Pin a programar en el dispositovo
		tfPin = new JTextField();
		tfPin.setToolTipText("pin que queremos que tenga el dipositivo (max. 4 caracteres)");
		tfPin.setBounds(85, 97, 101, 20);
		frameVista.add(tfPin);
		tfPin.setColumns(10);
		// Lista de puertos
		cbPuertos = new JComboBox<String>();
		cbPuertos.setBounds(248, 149, 89, 22);
		frameVista.add(cbPuertos);
		//Botón para leer el dispositivo
		btLeer = new JButton("leer");
		btLeer.setToolTipText("lee los par\u00E1metros del dispositivo");
		btLeer.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.ORANGE, new Color(255, 105, 180), new Color(210, 105, 30), new Color(138, 43, 226)));
		btLeer.setBounds(29, 260, 130, 34);
		frameVista.add(btLeer);
		//Botón de configurar
		btConfigurar = new JButton("configurar");
		btConfigurar.setToolTipText("configura el dispositivo con los valores en pantalla");
		btConfigurar.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.ORANGE, new Color(255, 105, 180), new Color(210, 105, 30), new Color(138, 43, 226)));
		btConfigurar.setBounds(207, 260, 130, 34);
		frameVista.add(btConfigurar);
		//Botón para abrir el puerto seleccionado
		btAbrirPuerto = new JButton("abrir puerto");
		btAbrirPuerto.setToolTipText("abre el puerto seleccionado con los baudios seleccionados");
		btAbrirPuerto.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.ORANGE, new Color(255, 105, 180), new Color(210, 105, 30), new Color(138, 43, 226)));
		btAbrirPuerto.setBounds(29, 202, 308, 34);
		frameVista.add(btAbrirPuerto);
		//Selección de Baudios 
		cbBaudios = new JComboBox<Integer>();
		cbBaudios.setToolTipText("baudios con que se abre el puerto y con que se programa el dispositivo");
		cbBaudios.setBounds(85, 149, 86, 22);
		frameVista.add(cbBaudios);
		//Simples etiquetas
		//
		JLabel lblNewLabel = new JLabel("nombre");
		lblNewLabel.setBounds(29, 48, 46, 14);
		frameVista.add(lblNewLabel);
		//
		JLabel lblNewLabel_1 = new JLabel("pin");
		lblNewLabel_1.setBounds(29, 100, 46, 14);
		frameVista.add(lblNewLabel_1);
		//
		JLabel lblNewLabel_2 = new JLabel("baudios");
		lblNewLabel_2.setBounds(29, 153, 46, 14);
		frameVista.add(lblNewLabel_2);
		//
		JLabel lblNewLabel_3 = new JLabel("puerto");
		lblNewLabel_3.setBounds(198, 153, 46, 14);
		frameVista.add(lblNewLabel_3);
	}
	public void setIcono(boolean estado) 
	{
		if (estado)
			setIconImage(Toolkit.getDefaultToolkit().getImage(Vista.class.getResource("/recursos/on.png")));
		else
			setIconImage(Toolkit.getDefaultToolkit().getImage(Vista.class.getResource("/recursos/off.png")));
		
	}
	/*****************************************
	 *  Clase interna que implementa la
	 *  interface WindowListener. Responde
	 *  a eventos del JFrame principal.
	 *****************************************/
	class LocalWindowListener implements WindowListener
	{
		/********************************************
		 * Utilizamos el event windowsOpened para 
		 * ..... .
		 *******************************************/
		@Override	public void windowOpened(WindowEvent e) 
		{
			controlador.abriendoAplicacion();
		}
		@Override	public void windowClosed(WindowEvent e){}
		@Override	public void windowIconified(WindowEvent e) {}
		@Override	public void windowDeiconified(WindowEvent e){}
		@Override	public void windowDeactivated(WindowEvent e) {}
		@Override	public void windowActivated(WindowEvent e){}
		/**************************************************
		 * Utilizamos el event windowsClosing para ....
		 * 
		 **************************************************/
		//@SuppressWarnings("unchecked")
		@Override	public void windowClosing(WindowEvent e) 
		{
			controlador.cerrandoAplicacion();
		}
	}
}
/************************************************************/