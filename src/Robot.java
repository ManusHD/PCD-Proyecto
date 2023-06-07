
public class Robot implements Runnable{

	private String tipo;
	private int capacidad;
	private static int idRobots = 0;
	private int idRobot;
	private Contenedor contenedor;
	
	public Robot(String tipo, Contenedor contenedor) {
		this.tipo = tipo;
		if(this.tipo.contains("PAN")) {
			capacidad = 3;
		}else {
			capacidad = 1;
		}
		idRobot = ++idRobots;
		this.contenedor = contenedor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public static int getIdRobots() {
		return idRobots;
	}

	public int getIdRobot() {
		return idRobot;
	}

	public void setIdRobot(int idRobot) {
		this.idRobot = idRobot;
	}
	
	public Contenedor getContenedor() {
		return contenedor;
	}

	public void setContenedor(Contenedor contenedor) {
		this.contenedor = contenedor;
	}
	
	
	@Override
	public void run() {
		while(true) {
			contenedor.poner(this);
		}
		
	}

	
	
	
}
