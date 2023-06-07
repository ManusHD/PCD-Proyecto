import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import pcd.util.Traza;

public class Motero implements Runnable {

	private int idInt;
	private String idMotero;
	private ControlMoteros controlMoteros;
	private Pedido pedido;
	private Semaphore todosListos[];
	private CyclicBarrier barrier;
	
	public Motero(int idInt, String id, ControlMoteros cMoteros, Pedido p, CyclicBarrier barrier) {
		super();
		this.idInt = idInt;
		this.idMotero = id;
		this.controlMoteros = cMoteros;
		this.pedido = p;
		todosListos = controlMoteros.getSemaforos();
		this.barrier = barrier;
	}

	
	public String getId() {
		return idMotero;
	}

	public void setId(String id) {
		this.idMotero = id;
	}

	public ControlMoteros getControlMoteros() {
		return controlMoteros;
	}

	public void setControlMoteros(ControlMoteros controlMoteros) {
		this.controlMoteros = controlMoteros;
	}

	public Pedido getP() {
		return pedido;
	}

	public void setP(Pedido p) {
		this.pedido = p;
	}

	public int getIdInt() {
		return idInt;
	}


	public void setIdInt(int idInt) {
		this.idInt = idInt;
	}


	@Override
	public void run() {		
		
		while(true) {
			
			try {
				barrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}

			pedido = this.controlMoteros.getPedido(idMotero);			
			controlMoteros.enviarPedido(pedido);
			controlMoteros.regresaMotero(this);
		}
	}

}
