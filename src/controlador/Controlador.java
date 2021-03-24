package controlador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JTextArea;
import com.fazecast.jSerialComm.SerialPort;
import modelo.Modelo;
import vista.Vista;
import principal.D;
//
public class Controlador 
{
	Thread hiloEnviar;//Hilo para enviar comandos
	int contadorHilo=0;//Para contar los comandos
	boolean hiloCorriendo=false;//Para saber si el hilo se ha inicializado
	String nombre, bauds, pin, mac, ver;//Valores para programar
	//Modelo y vista de la aplicación
	private Modelo modelo;
	private Vista vista;
	private int DELAY = 50;//Retardo entre escrituras en el puerto serie
	private JTextArea out;
	//Constructor
	public Controlador(Vista vist,Modelo model)
	{
		D.setDebug(true);//Para ver las cadenas de depuración
		hiloEnviar=new Thread(new Hilo());//Hilo para enviar comandos
		//Apunta a la vista y el modelo
		this.vista = vist;
		this.modelo = model;
		out = vista.taRespuesta;//El area de texto de la vista tiene el alias "out"
		/*............................................
		 * Inicializa la vista 
		 .............................................*/
		//Listeners de los combos de baudios y puertos
		vista.cbBaudios.addActionListener(baudios);
		vista.cbPuertos.addActionListener(puerto);
		//rellena el combo de baudios
		//vista.cbBaudios.addItem(110);
		//vista.cbBaudios.addItem(300);
		//vista.cbBaudios.addItem(1200);
		//vista.cbBaudios.addItem(2400);
		//vista.cbBaudios.addItem(4800);
		vista.cbBaudios.addItem(9600);
		vista.cbBaudios.addItem(19200);
		vista.cbBaudios.addItem(38400);
		vista.cbBaudios.addItem(56000);
		vista.cbBaudios.addItem(57600);
		vista.cbBaudios.addItem(115200);
		//vista.cbBaudios.addItem(128000);
		//vista.cbBaudios.addItem(246000);
		//rellena el combo de puertos
		leePuertos();
		//Listeners de los botones de la vista
		this.vista.btLeer.addActionListener(leer);
		this.vista.btConfigurar.addActionListener(configurar);
		this.vista.btAbrirPuerto.addActionListener(abrirPuerto);
	}
	/*********************************************
	 * Listener de la lista de baudios para
	 * seleccionar una frecuencia
	 *********************************************/
	ActionListener baudios = new ActionListener()
	{
		@Override public void actionPerformed(ActionEvent e) 
		{
			modelo.setBaudios((int)vista.cbBaudios.getSelectedItem());
		}
	};
	/********************************************
	 * Listener de la lista de puertos
	 * para seleccionar un puerto
	 *********************************************/
	ActionListener puerto = new ActionListener()
	{
		@Override public void actionPerformed(ActionEvent e) 
		{
			modelo.setCOM(SerialPort.getCommPort(vista.cbPuertos.getSelectedItem().toString()));
		}
	};
	/********************************************
	 * Listener del botón btAbrirPuerto
	 *********************************************/
	ActionListener abrirPuerto = new ActionListener()
	{
		@Override public void actionPerformed(ActionEvent e) 
		{
			if(!modelo.puertoAbierto())
			{
				modelo.abrirPuerto();
				
				vista.btAbrirPuerto.setText("cerrar puerto");
				vista.btLeer.setEnabled(true);
				vista.btConfigurar.setEnabled(true);
				vista.cbPuertos.setEnabled(false);
				vista.taRespuesta.setText("");
				modelo.enviar("AT+TEST"+"\r"+"\n");
				vista.setIcono(true);//Icono conectado
			}
			else //Si el puerto está abierto lo cierra
			{
				//hiloEnviar=null;
				modelo.cerrarPuerto();
				vista.btAbrirPuerto.setText("abrir puerto");
				vista.btLeer.setEnabled(false);
				vista.btConfigurar.setEnabled(false);
				vista.cbPuertos.setEnabled(true);
				vista.setIcono(false);//Icono desconectado
			}
		}
	};
	/********************************************
	 * Listener del botón de leer dispositivo
	 *********************************************/
	ActionListener leer = new ActionListener()
	{
		
		@Override public void actionPerformed(ActionEvent e) 
		{
			vista.taRespuesta.setText("");
			nombre = "AT+NAME?"+"\r"+"\n";
			pin = "AT+PIN?"+"\r"+"\n";
			bauds="AT+BAUD?"+"\r"+"\n";
			ver="AT+VERS?"+"\r"+"\n";
			mac="AT+ADSS?"+"\r"+"\n"; 
			contadorHilo=5;
			//Si el hilo no está vivo lo arranca
			if(!hiloCorriendo)
			{
				hiloEnviar.start();
				hiloCorriendo=true;
			}
		}
	};
	/********************************************
	 * Listener del botón para configurar
	 * el dispositivo
	 *********************************************/
	ActionListener configurar = new ActionListener()
	{
		@Override public void actionPerformed(ActionEvent e) 
		{
			Integer bd;	
			nombre =vista.tfNombre.getText();
			if (nombre.length() < 1 || nombre.length() >19)
			{
				D.error("el nombre debe tener entre 1 y 19 caracteres");
				return;
			}
			pin=vista.tfPin.getText();
			if(pin.length()!=4)
			{
				D.error("El pin debe tener 4 caracteres");
				return;
			}
			vista.taRespuesta.setText("");
			nombre = "AT+NAME="+vista.tfNombre.getText()+"\r"+"\n";
			pin="AT+PIN="+pin+"\r"+"\n";
			
			bd= (Integer)vista.cbBaudios.getSelectedItem();
			switch(bd)
			{
			case 9600:
				bauds="AT+BAUD="+"1"+"\r"+"\n";
			break;
			case 19200:
				bauds="AT+BAUD="+"2"+"\r"+"\n";
			break;
			case 38400:
				bauds="AT+BAUD="+"3"+"\r"+"\n";
			break;
			case 57600:
				bauds="AT+BAUD="+"4"+"\r"+"\n";
			break;
			case 115200:
				bauds="AT+BAUD="+"5"+"\r"+"\n";
			break;	
			}
			contadorHilo=3;
		}
	};
	/*************************************************
	 * Función que se ejecuta desde la clase SerieSPM 
	 *  cuando se recibe algo por el puerto. Recibe
	 *  un scanner con la respuesta del módulo. 
	 *  Se recibe dentro del Thread secundario de
	 * recepción del puerto serie.
	 **************************************************/
	public  void respuestaSerial(String resp)
	{
		vista.taRespuesta.append(" ");
		vista.taRespuesta.append(resp +"\n");
       	return;
    }
	/*************************************************
	 * 	Pone como título de la vista el puerto COM abierto
	 * 
	 *********************************************** */
	public void tituloFrame(String titulo)
	{
		vista.setTitle(titulo);
	}

	/*************************************************
	 * Rellena el JcomboBox de la vista con los 
	 * puertos presentes en el sistema
	 *************************************************/
	void leePuertos()
	{
		SerialPort[] puertosEnSistema = SerialPort.getCommPorts();//Lista de nombre de puertos
		int[] numeroPuertos;//Para guardar el número de puertos como int para poder ordenarlos
		int nPuertos=puertosEnSistema.length;//Longitud de la lista
		numeroPuertos = new int[nPuertos];//Array de enteros (ordenables) con el número de los puertos
		for(int i = 0; i < nPuertos; i++)//Lee los números de la lista de puertos
			numeroPuertos[i]=Integer.parseInt(puertosEnSistema[i].getSystemPortName().substring(3))+1;
		Arrays.parallelSort(numeroPuertos);//Ordena el array de enteros de números de puertos
		for(int j=0;j<numeroPuertos.length;j++)  	
			vista.cbPuertos.addItem("COM"+(numeroPuertos[j]-1));//Pone los nombres de los puertos en el combo
	}
	/***************************************************
	 * Función que se ejecuta cuando se abandona la 
	 * aplicación
	 ************************************************ */
	public void cerrandoAplicacion() 
	{
		if (modelo.puertoAbierto()) //Si hay un puerto abierto lo escribe en el fichero y lo cierra	
		{
			//guardaConfiguracion();
        	modelo.cerrarPuerto(); //Antes de salir cerramos el puerto
		}
	}
	/***************************************************
	 * Función que se ejecuta cuando se abre la 
	 * aplicación
	 **************************************************/
	public void abriendoAplicacion() 
	{
			//leerConfiguracion();
		vista.btLeer.setEnabled(false);
		vista.btConfigurar.setEnabled(false);
	}
	/***************************************************
	 * Hilo para enviar la secuencia de comandos AT
	 * por el puerto serie
	 * ************************************************/	
	
	class Hilo extends Thread
	{
		@Override public void run() 
		{
			while (true)
			{
				switch(contadorHilo) 
				{
					case 5:
						D.msg(mac,out);
						modelo.enviar(mac);
					break;
					case 4:
						D.msg(ver,out);
						modelo.enviar(ver);
					break;
					case 3:
						D.msg(nombre,out);
						modelo.enviar(nombre);
					break;
					case 2:
						D.msg(pin,out);
						modelo.enviar(pin); 
					break;
					case 1:
						D.msg(bauds,out);
						modelo.enviar(bauds);
					break;
					case 0:
						//Thread.currentThread().interrupt();
					break;
				}//switch
				if(contadorHilo>-1 ) contadorHilo--;
				try {Thread.sleep(DELAY);	} catch (InterruptedException e) {D.error("problemas envíando comandos");}
			}//while
			//return;
		}//run
	}//class hilo	
}
/*******************************************************/