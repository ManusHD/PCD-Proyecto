import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalTime;
import java.util.List;

import bankExample1.*;
import pcd.util.Traza;

 class Restaurante {
	 
	private String nombre;						// nombre del restaurante
	private Account account;					// cuenta bancaria para registrar la recaudación
	private LocalTime maxTime;					// máximo tiempo de reparto
	private Cocina cocina;						// la cocina de este restaurante
	private ControlMoteros controlMoteros; 
	
	public Restaurante (Account _ac, String _nombre, int _numeroMoteros, int _maxTime, List<Contenedor> contenedor) {
		account = _ac;
		nombre = _nombre;
		maxTime = LocalTime.of (0,_maxTime);
		controlMoteros = new ControlMoteros (this, Config.numeroMoteros);
		cocina = new Cocina (this, contenedor);
		Traza.traza('=',3,"Creando restaurante: "+nombre);
	}

	public String getNombre () {
		return nombre;
	}
	
	public LocalTime getMaxTime () {
		return maxTime;
	}
	
	public double getBalance (){
		return account.getBalance();
	}
	
	public Account getAccount () {
		return account;
	}
	
	private void enviarDatosPagoPedidoNoPagado(DatosPagoPedido dpp) throws IOException{
		byte[] enviarDatos = new byte[5000];
		DatagramSocket socket = new DatagramSocket();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream escritura = new ObjectOutputStream(baos);
		escritura.writeObject(dpp);
		enviarDatos = new byte[baos.toByteArray().length];
		enviarDatos = baos.toByteArray();
		
		//Construimos y enviamos el Datagrama a enviar
		DatagramPacket enviarPaquete = new DatagramPacket(enviarDatos, enviarDatos.length, InetAddress.getLocalHost(), 10000);
		socket.send(enviarPaquete);
		
		//Cerramos el socket
		socket.close();
	}
	
	private boolean pagarPedido(DatosPagoPedido dpp) throws IOException, ClassNotFoundException{
		boolean acepto = false;		
		int port = 9999;
		String host = InetAddress.getLocalHost().getHostName();
		Socket connection = new Socket(host, port);		
		
		System.out.println();
		System.out.println("Conectando a ........ " + connection.getInetAddress().getHostName());
		System.out.println("Comprobando si se acepta el pago del pedido: " + dpp.getId());
		System.out.println();
		
		//Creamos el flujo de escritura y mandamos el Objeto al Server
		ObjectOutputStream escritura = new ObjectOutputStream(connection.getOutputStream());
		escritura.writeObject(dpp);
		
		//Creamos el flujo de lectura de los datos que vienen del servidor usando el socket
		ObjectInputStream lectura = new ObjectInputStream(connection.getInputStream());
			
		//Leeamos los mensajes que llegan del servidor
		String msg = (String) lectura.readObject();	
		
		if(msg.equals("OK\n")) {
			System.out.println("Se acepta el pago del pedido: " + dpp.getId());
			acepto =  true;
		}else {
			enviarDatosPagoPedidoNoPagado(dpp);
			System.out.println("No se acepta el pago del pedido: " + dpp.getId());
		}
		
		connection.close();	
		escritura.close();
		lectura.close();
			
		return acepto;
	}
	
	public synchronized void tramitarPedido (Pedido _p){
		Pedido p =_p;
		DatosPagoPedido dpp = new DatosPagoPedido(p.getId(), p.getPrecioPedido());
		try {
			if(pagarPedido(dpp)) {
				account.deposit(p.getPrecioPedido());
				cocina.cocinar(p);
				controlMoteros.asignarPedido(p);
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
}
