import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ControlVentasImpl extends UnicastRemoteObject implements ControlVentasInt{
	
	private static final long serialVersionUID = 1L;
	
	protected Map <String, Integer> productosVendidos;
	
	public ControlVentasImpl() throws RemoteException{
		super();
		productosVendidos = new HashMap<>();
	}
	
	@Override
	public void registrarVenta(String id, int unidades) throws RemoteException{
		int productos = 0;
		if(productosVendidos.containsKey(id)) {
			productos = unidades + productosVendidos.get(id);
			productosVendidos.replace(id, productos);
			System.out.println("Añadiendo unidades a producto [ID=" + id + ", UnidadesAñadidas=" + unidades + ", UnidadesTotales=" + productos + "]");
		}else {
			productosVendidos.put(id, unidades);
			System.out.println("Añadiendo a registro nuevo producto [ID=" + id + ", Unidades=" + unidades + "]");
		}
	}
}
