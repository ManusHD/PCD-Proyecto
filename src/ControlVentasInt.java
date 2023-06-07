import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControlVentasInt extends Remote{
	public void registrarVenta(String id, int unidades) throws RemoteException;
}
