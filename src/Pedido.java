import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.core.Observable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Pedido implements Runnable, Serializable {
	
	private static final long serialVersionUID = 1L;
	String idPedido;
	String direccion;			// Dirección del cliente
	int restaurante;			// Restaurante al que va el pedido
	Canal canal;				// Canal por el que viene el pedido (web, móvil, etc)
	LocalDate fecha;			// Fecha del pedido
	LocalTime hora;				// Hora del pedido
	List<Producto> productos;	// Productos que conforman este pedido
	Restaurante restauranteObjeto;
	
	public Pedido (String _idPedido, String _direccion, int _restaurante, Canal _canal, LocalDate _fecha, LocalTime _hora, List<Producto> _productos) {
		idPedido = _idPedido;
		direccion = _direccion;
		restaurante = _restaurante;
		canal =_canal;
		fecha =_fecha;
		hora = _hora;
		productos = _productos;
		restauranteObjeto = null;
	}
	
	public String getId() {
		return idPedido;
	}

	public int getRestaurante () {
		return restaurante;
	}
	
	public String getDireccion () {
		return direccion;
	}
	
	public List<Producto> getProductos() {
		return productos;
	}
	
	public Canal getCanal() {
		return canal;
	}
	
	public LocalDate getFecha () {
		return fecha;
	}
	
	public LocalTime getHora () {
		return hora;
	}
	
	public double getPrecioPedido () {
		double suma=0;
		
		List<Producto> productos = this.getProductos();
		for (Producto prod:productos) {
			suma+=prod.getPrecio();
		}
		return suma;
	}
	
	public void print() {
		System.out.println ("Pedido:    ");
		System.out.println ("Id: "+idPedido+" "+direccion+" "+restaurante+" "+canal+" "+fecha+" "+hora);
	}	
	
	public String printConRetorno () {
		return "Pedido: "+idPedido+" "+direccion+" "+restaurante+" "+canal+" "+fecha+" "+hora;
	}
	
	public Restaurante getRestauranteObjeto() {
		return restauranteObjeto;
	}

	public void setRestauranteObjeto(Restaurante res) {
		this.restauranteObjeto = res;
	}
	
	// GENERAR PEDIDOS
	public static List<Pedido> generaPedidos (int numPedidos, Canal _canal) {
		List<Pedido> listaPedidos = new ArrayList<>(numPedidos);
		String id;
		Canal canal;
		LocalDate d;
		LocalTime t;
		String direccion;
		int restaurante;
		Random r = new Random();

		for (int i=0;i<numPedidos;i++) {
			
			canal = _canal;
			direccion = "Dirección 1";
			restaurante = r.nextInt(Config.numeroRestaurantes);
			id = canal+"_"+"R"+restaurante;
			d = LocalDate.now();
			t = LocalTime.now();
			Pedido p = new Pedido (id, direccion, restaurante, canal, d, t, Producto.generaProductos(Config.numeroProductos));
			listaPedidos.add(p);
		
		}
		return listaPedidos;
	}

	public static List<Pedido> pedidosDesdeFichero () {
		// La lista de pedidos que vamos a retornar
		List<Pedido> l = new ArrayList<>();
		Pedido pedido;
		try {
			FileInputStream fin =new FileInputStream ("D:/Uni/2do/PCD/Laboratorios/pedidos.bin");
			// Sustituye ahí la ruta y nombre de tu fichero
			ObjectInputStream o = new ObjectInputStream (fin);
			pedido = (Pedido) o.readObject();
			while (pedido!=null) {
				l.add(pedido);
				pedido = (Pedido) o.readObject();
			}
			o.close();
			fin.close();
		} catch (Exception e) {e.printStackTrace();}
			return l;
	}
	
	public static Observable<Pedido> pedidosDesdeFicheroObservable () {
		Observable <Pedido> obs = Observable.create(p->{
			// La lista de pedidos que vamos a retornar
			List<Pedido> l = new ArrayList<>();
			Pedido pedido;
			try {
				FileInputStream fin =new FileInputStream ("D:/Uni/2do/PCD/Laboratorios/pedidos.bin");
				ObjectInputStream o = new ObjectInputStream (fin);
				pedido = (Pedido) o.readObject();
				while (p!=null) {
					l.add(pedido);
					pedido = (Pedido) o.readObject();
					p.onNext(pedido);
				}
				o.close();
				fin.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		return obs;
	}

	@Override
	public void run() {
		if (restauranteObjeto != null) {
			restauranteObjeto.tramitarPedido(this);
		}
	}
}


