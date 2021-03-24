package modelo;
import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;
import principal.D;
import controlador.Controlador;
//
public class Modelo  
{
	public Controlador controlador;
	private String nombre;
	private int baudios;
	private String pin;
	private String puerto;
	//
	public  SerialPort COM=null;
	private boolean estadoPuerto = false;
	//
	private Thread thread;
	//
	public void inicializa(Controlador cont)
	{
		this.controlador=cont;
	}
	//
	public SerialPort getCOM() {return this.COM;}
	public void setCOM(SerialPort COM) {this.COM = COM;}
	public String getNombre() {return nombre;}
	public void setNombre(String nombre) {this.nombre = nombre;}
	public int getBaudios() {return baudios;}
	public void setBaudios(int i) {this.baudios = i;}
	public String getPin() {return pin;}
	public void setPin(String pin) {this.pin = pin;}
	public String getPuerto() {return puerto;}
	public void setPuerto(String puerto) {this.puerto = puerto;}
	
	/***********************************************
	 * Puerto serie 
	***********************************************/
	/**********************************************
	 *  Método para enviar cadenas por el puerto
	 **********************************************/
	public void enviar(String envio) 
	{
		//String comandoEnviar = cadenaEnviar.getText()+'\r';
		//String comandoEnviar = "AT+NAME?"+'\r'+'\n';
		byte[] bitesEnviar=envio.getBytes();
		int numeroDeBites=bitesEnviar.length;
		//Evita enviar a un puerto que no exista o cerrado
		if(COM!=null && COM.isOpen() && numeroDeBites>1)  
		{
			COM.writeBytes(bitesEnviar,numeroDeBites);
		}
	}
	/**********************************************
	 * Método para abrir el puerto y arrancar el
	 *  thread de lectura del puerto
	***********************************************/
	public void abrirPuerto() 
	{
		if(puertoAbierto()) return;//Si hay un puerto abierto no lo reabre ni abre otro
		if(COM.openPort())//Si consigue abrir el puerto lo configura y lanza el hilo 
			{
				COM.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 100);//100ms timeout escritura
				//puertoSerie.setFlowControl(puertoSerie.FLOW_CONTROL_DISABLED);
				int baud= getBaudios();
				COM.setComPortParameters(baud, 8, 1, 0);
				estadoPuerto=true;
				//Le pasa el puerto abierto al contrlador para que lo ponga de titulo en la ventana
				controlador.tituloFrame("puerto "+ COM.getSystemPortName()+ " abierto "+ " a "+ baud + " baudios: programador del módulo DX-BT18" );
			/*------------------------------------------------------------
			Crea y ejecuta un thered que escucha el puerto serie abierto
			 y escribe lo recibido en "recibido"
			-------------------------------------------------------------*/
				thread = new Thread()
				{
					@Override public void run() 
					{
						Scanner scanner = new Scanner(COM.getInputStream());
						while(scanner.hasNextLine())//Si encuentra '\n' cerrando la cadena recibida... 
						{
							try 
							{
								String respuesta = scanner.nextLine();
								controlador.respuestaSerial(respuesta);//Envía la respuesta al controlador	
							} 
							catch(Exception e) 
							{
								D.error("recepción con error ");
							}
						}//while
						scanner.close();
					}//run
				};//Thread
				/*-----------------------------------------------------------*/
				thread.start();//Ejecuta el thread
			}
		//Si no consigue habrir el puerto sale informando del error
		else 
		{
			estadoPuerto=false;
			D.error("el puerto no ha abierto");
		}
	}// método abrirPuerto()
	/***********************************************
	 * Método para interrumpir el thread de lectura
	 *  y cerrar el puerto
	 **********************************************/
	public void cerrarPuerto()
	{
		
		if(puertoAbierto())//if(COM.isOpen())
		{
			thread.interrupt();
			COM.closePort();
			estadoPuerto=false;
			controlador.tituloFrame("puerto "+ COM.getSystemPortName()+ " cerrado: programador del módulo DX-BT18");
		}
		else D.error("El puerto no está abierto");
	}
	/*************************************************************************
	 * Método devuelve booleano a true su el puerto está abierto y false si no 
	 *************************************************************************/
	public boolean puertoAbierto() 
	{
		return estadoPuerto;
	}
	/*************************************************************************/
}
