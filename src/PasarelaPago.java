import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class PasarelaPago {
	
	private ServerSocket listener;
	private int port;
	
	public PasarelaPago() {
		port = 9999;
		try {
			listener = new ServerSocket(port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			//Si pasa mas de 5000ms (5s) el socket se cierra
			listener.setSoTimeout(5000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public ServerSocket getListener() {
		return listener;
	}

	public void setListener(ServerSocket listener) {
		this.listener = listener;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {		
		PasarelaPago pp = new PasarelaPago();	
		
		while(true) {			
			try {
				System.out.println("Esperando conexiones de algún cliente ......");
				Socket connection = pp.getListener().accept();
				System.out.println("Conexión establecida con el cliente con IP: " + connection.getInetAddress() + "\n");				
				
				//Comprueba si se ha cerrado el socket, se cierra si han pasado mas de 5 segundos sin recibir nada
				if(pp.getListener().isClosed()) {
					break;
				}				
				
				//Flujo de lectura que leerá los objetos de DatosPagoPedido
				ObjectInputStream lectura = new ObjectInputStream(connection.getInputStream());
				
				DatosPagoPedido dpp = (DatosPagoPedido) lectura.readObject();		
				//Para imprimir el objeto lo convertimos a String
				System.out.println("Objeto recibido: " + dpp.toString());
				
				//Flujo de escritura que manda un String al cliente 
				ObjectOutputStream escritura = new ObjectOutputStream(connection.getOutputStream());
				
				if(dpp.getImporte() < 20) {
					System.out.println("Importe menor que 20. Enviando KO\n");
					escritura.writeObject("KO\n");
				}else {
					System.out.println("Importe mayor que 20. Enviando OK\n");
					escritura.writeObject("OK\n");
				}
				System.out.println("----------------------------------------------------------------------");	
				
			} catch(SocketTimeoutException e) {
				System.out.println("Espera mayor a 5s.");
				System.out.println("Cerrando conexión...........");
				break;
			}
		}	
	}
}
