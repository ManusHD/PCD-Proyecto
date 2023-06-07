import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import pcd.util.Traza;

public class Cocina {

	private Restaurante r;
	private List<Contenedor> contenedores;

	public Cocina(Restaurante r, List<Contenedor> contenedores) {
		this.r = r;
		this.contenedores = contenedores;
	}

	public void cocinar(Pedido p) {

		String host = "rmi://localhost:1542/ControlVentas";
		ControlVentasInt cvServidor = null;
		try {
			cvServidor = (ControlVentasInt) Naming.lookup(host);

			boolean tienePollo = false;
			boolean tienePan = false;

			List<Producto> productos = p.getProductos();
			for (Producto pro : productos) {
				if (pro.getId().contains("1")) {
					tienePan = true;
				}
				if (pro.getId().contains("2")) {
					tienePollo = true;
				}
				
				//Mandamos los datos al ServidorRMI
				synchronized(cvServidor) {
					cvServidor.registrarVenta(pro.getId(), pro.getCantidad());
				}
			}

			// Como no tenemos nombre de productos, supongo que el id de PAN = 1 y el id de
			// POLLO = 2
			if (tienePan && tienePollo) {
				contenedores.get(0).coger(); // Contenedor de pan
				contenedores.get(1).coger(); // Contenedor de pollo
				Traza.traza('=', 2, "Cocinando pedido que contiene PAN y POLLO: " + p.printConRetorno());
			} else {
				if (!tienePan && !tienePollo) {
					Traza.traza('=', 2, "Cocinando pedido: " + p.printConRetorno());
				} else {
					if (tienePan) {
						contenedores.get(0).coger();
						Traza.traza('=', 2, "Cocinando pedido que contiene PAN: " + p.printConRetorno());
					}
					if (tienePollo) {
						contenedores.get(1).coger();
						Traza.traza('=', 2, "Cocinando pedido que contiene POLLO: " + p.printConRetorno());
					}
				}
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
}