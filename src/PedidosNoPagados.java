import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class PedidosNoPagados {

	public PedidosNoPagados() {
		
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		int localPort = 10000;
		DatagramSocket socket = new DatagramSocket(localPort);	
		socket.setSoTimeout(10000);
		DatagramPacket recibirPaquetes;
		byte[] recibirDatos = new byte[2000];
		List<DatosPagoPedido> pedidosNoPagados = new ArrayList<>();
		
		while(true){
			try {
			System.out.println("Esperando peticiones del cliente........");
			recibirPaquetes = new DatagramPacket(recibirDatos, recibirDatos.length);
			
			//Lee una petición del DatagramSocket y se queda bloqueado hasta que reciba una petición
			socket.receive(recibirPaquetes);
			ObjectInputStream lectura = new ObjectInputStream(new ByteArrayInputStream(recibirPaquetes.getData()));
			System.out.println("Petición recibida");
			
			//Lee el objeto recibido y cierra la lectura
			DatosPagoPedido dpp = (DatosPagoPedido) lectura.readObject();
			lectura.close();
			
			System.out.println("Insertando pedido no pagado en la lista.");
			pedidosNoPagados.add(dpp);
						
			} catch(SocketTimeoutException e) {
				System.out.println();
				System.out.println("Espera mayor a 10s.");
				System.out.println("Cerrando conexión...........");
				System.out.println();
				socket.close();
				System.out.println("Pedidos que no han podido ser pagados: ");
				for(DatosPagoPedido dpp: pedidosNoPagados) {
					System.out.println("Pedido [id=" + dpp.getId() + ", importe=" + dpp.getImporte() + "]");
				}			
				break;
			}		
		}
	}
}
