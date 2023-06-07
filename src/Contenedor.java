import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pcd.util.Traza;

public class Contenedor {
	private String tipo;
	private static int idContenedores = 0;
	private int idContenedor;
	private int capacidad;
	private BlockingQueue<Integer> queue;

	final Lock monitor = new ReentrantLock();
	final Condition lleno = monitor.newCondition();
	final Condition vacio = monitor.newCondition();

	public Contenedor(String tipo, int capacidad) {
		this.tipo = tipo;
		idContenedor = ++idContenedores;
		this.capacidad = capacidad;
		queue = new LinkedBlockingQueue<>(this.capacidad);
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public static int getIdContenedores() {
		return idContenedores;
	}

	public static void setIdContenedores(int idContenedores) {
		Contenedor.idContenedores = idContenedores;
	}

	public int getIdContenedor() {
		return idContenedor;
	}

	public void setIdContenedor(int idContenedor) {
		this.idContenedor = idContenedor;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public void poner(Robot r) {
		try {
			queue.put(0);
			Traza.traza('*', 2, "Robot está poniendo " + r.getTipo() + " en el contenedor");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void coger() {
		try {
			queue.take();
			Traza.traza('*', 2, "Cogiendo producto del contenedor de " + tipo);	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
