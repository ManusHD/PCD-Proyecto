import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ControlVentasMain {

	public static void main(String[] args) {
		try {
			ControlVentasImpl ControlVentas = new ControlVentasImpl();
			Registry registry = LocateRegistry.createRegistry(1542);
			System.out.println(registry.toString());
			registry.rebind("ControlVentas", ControlVentas);	
			System.out.println("Servicio ControlVentas ACTIVO");
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
}