import java.util.LinkedList;
import java.util.List;

import bankExample1.Bank;

public class CrearRestaurantes   {
	LinkedList <Restaurante> lista=null;
	int numRestaurantes;
	Bank b;	// todos los restaurantes comparten el mismo banco, pero cada uno tiene su cuenta
	private List <Contenedor> contenedores;
	
	public CrearRestaurantes (Bank _b, LinkedList <Restaurante> _lista, int _numRestaurantes, List <Contenedor> contenedores) {
		lista=_lista;	
		numRestaurantes = _numRestaurantes;
		b = _b;
		this.contenedores = contenedores;
	}
	
	public void crear () {
		for (int i=0;i<numRestaurantes;i++) {
			Restaurante r = new Restaurante (b.getAccount(i),
											""+i, 						// el nombre del Restaurante
											Config.numeroMoteros,  		// número de moteros del restaurante
											Config.maxTimeRestaurante,	// máximo tiempo de reparto
											contenedores);
			lista.add(r);
		}
	}
}
