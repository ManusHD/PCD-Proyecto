import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import pcd.util.Traza;

public class ControlMoteros{
	int numeroMoteros;
	Restaurante restaurante;
	Pedido pedido;
	int moterosRepartiendo;
	List<Motero> moteros;
	private Semaphore semaforos[];
	CyclicBarrier barrier;
	
	public ControlMoteros (Restaurante _r, int _numeroMoteros) {
		restaurante=_r;
		numeroMoteros = _numeroMoteros;	
		pedido = null;
		moterosRepartiendo = 0;	
		
		barrier = new CyclicBarrier(Config.numeroMoteros, new Runnable() {
			public void run() {
				System.out.println("TODOS LOS MOTEROS DEL RESTAURANTE " + restaurante.getNombre() + " ESTÁN LISTOS");
			}
		});
		
		// Creamos los Moteros en un Array
		// Lanzamos los hilos de los Moteros
		moteros = new ArrayList<>();
		for (int i = 0; i < numeroMoteros; i++) {
			Motero m = new Motero(i, "Motero:"+i+" R:"+restaurante.getNombre(), this, pedido, this.barrier);
			moteros.add(m);
			new Thread(m).start();
		}
	}
	
	
	public int getNumeroMoteros() {
		return numeroMoteros;
	}

	public void setNumeroMoteros(int numeroMoteros) {
		this.numeroMoteros = numeroMoteros;
	}

	public int getMoterosRepartiendo() {
		return moterosRepartiendo;
	}

	public void setMoterosRepartiendo(int moterosRepartiendo) {
		this.moterosRepartiendo = moterosRepartiendo;
	}

	public Semaphore[] getSemaforos() {
		return semaforos;
	}

	public void setSemaforos(Semaphore[] semaforos) {
		this.semaforos = semaforos;
	}

	
	public synchronized void asignarPedido(Pedido p) {
		
		while(moterosRepartiendo >= numeroMoteros) {
			try {
				Traza.traza('-',3,"PEDIDO EN ESPERA DE MOTERO... ");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
		pedido = p;
		notify();	
	}
	
	public void enviarPedido (Pedido p) {
		Traza.traza('>',2,"REPARTIENDO PEDIDO : "+p.getId());
		try {
			Thread.sleep(100);
			System.out.print('.');
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	public synchronized Pedido getPedido (String idMotero) {		
		while(this.pedido == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Traza.traza('<',2, idMotero + ", Ha obtenido el Pedido.");
		moterosRepartiendo++;
		Pedido p = this.pedido;
		pedido = null;
		return p;
	}
	
	public synchronized void regresaMotero (Motero m) {
		Traza.traza('-',2,"Motero regresa al restaurante.");
		this.moterosRepartiendo--;
		notify();
	}
	
//	public void puedoCocinar () {
//	}
	
}
