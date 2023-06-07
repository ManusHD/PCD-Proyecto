import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import bankExample1.Bank;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pcd.util.Traza;

public class MyDelivery {
	
	static Config config;
	
	public static void lanzarPedidos (List<Pedido> lp, List<Restaurante> _lr) {
		List<Restaurante> lr = _lr;
		
		for (Pedido p:lp) {
			p.setRestauranteObjeto(lr.get(p.getRestaurante()));	
		}
		//lp.stream().parallel().forEach(p->p.getRestauranteObjeto().tramitarPedido(p));
	}
		
	public static void main(String[] args) {
		System.out.println("V11");
		
		// para facilitar las trazas
		Traza traza = new Traza (Config.modoTraza);
		
		// CREAR RESTAURANTES
		// Creando el banco de los restaurantes con 0 euros.
		Bank b = new Bank(Config.numeroRestaurantes,0);
		
		
		//Creando ROBOTS y CONTENEDORES
		Contenedor pan = new Contenedor("PAN", 3);
		Contenedor pollo = new Contenedor("POLLO", 1);
		Robot pa = new Robot("PAN", pan);
		Robot po = new Robot("POLLO", pollo);
		List<Contenedor> contenedores = new ArrayList<>();
		contenedores.add(pan);
		contenedores.add(pollo);
		
		
		// Creando los restaurantes
		LinkedList<Restaurante> listaRestaurantes = new LinkedList<>();
		CrearRestaurantes crearRestaurantes = new CrearRestaurantes (b,listaRestaurantes,config.numeroRestaurantes, contenedores);
		crearRestaurantes.crear();
		
		//Se lanzan los robots
		new Thread(pa).start();
		new Thread(po).start();
		
		// CREAR PEDIDOS
		List<Pedido> pedidos = new ArrayList<>();
		pedidos = Pedido.pedidosDesdeFichero();
		
		
		// LANZAR PEDIDOS
		long initialTime = new Date().getTime();
		//lanzarPedidos (pedidos,listaRestaurantes);
		
		//LANZAMOS LOS PEDIDOS CON OBSERVABLE
		Observable<Pedido> pedidoObservable = Pedido.pedidosDesdeFicheroObservable();
		pedidoObservable.flatMap(p->{return Observable.just(p);}).doOnNext(p2->listaRestaurantes.get(p2.getRestaurante()).tramitarPedido(p2)).subscribe();			
		
		
		ExecutorService pedidoMasCaro = Executors.newFixedThreadPool(1);
		Future<Double> pedidoMasAlto = pedidoMasCaro.submit(new PedidoCaro(pedidos));	
			
		//Mostramos cual es el pedido mas caro tras asegurarnos que todos han acabado
		try {
			System.out.println("\nPEDIDO MAS CARO: " + pedidoMasAlto.get());
		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}
		
		//Ejecutamos el ForkJoin para obtener los pedidos por encima de 20€
		ForkJoinPool pool = ForkJoinPool.commonPool();
		PedidosFJ fj = new PedidosFJ(pedidos);
		pool.execute(fj);
		
		//Guardamos los pedidos mas caros de 20€ en una variable
		List<Pedido> listaPedidosFJ = fj.join();
		
		//Mostramos los pedidos obtenidos
		System.out.println("\nPEDIDOS MAS CAROS DE 20€: ");
		for(Pedido p: listaPedidosFJ) {
			System.out.println(p.printConRetorno());
		}
		
		//Mostramos los pedidos mas baratos de 20€
		System.out.println("\nPEDIDOS MAS BARATOS DE 20€: ");
		pedidos.stream().parallel().filter(p->p.getPrecioPedido()<20).forEach(p->System.out.println(p.getId() + " " + p.getPrecioPedido()));
		System.out.println();
		
		//Mostramos el pedido mas caro
		System.out.println("\nPRECIO DEL PEDIDO MAS CARO: ");
		System.out.println(pedidos.stream().parallel().map(p->p.getPrecioPedido()).max(Double::compare).get());
		
		//Creamos los Observables
		Observable <List<Pedido>> obs1;
		Observable <Pedido> obs2 = Observable.fromIterable(pedidos);
		
		//Muestra los pedidos mas caros de 70€
		System.out.println("\nPEDIDOS MAS CAROS DE 70€: ");
		obs2.flatMap(p -> Observable.just(p).filter(p2->p2.getPrecioPedido()>70).subscribeOn(Schedulers.computation()).doOnNext(p2->System.out.println(p2.getId() + " " + p2.getPrecioPedido() + " ---" + Thread.currentThread().getName()))).subscribe();
		
		//Muestra la suma de todos los pedidos
		obs1 = 	Observable.just(pedidos).subscribeOn(Schedulers.computation());
		obs1.subscribe(onNext -> {
			double sumaPedidos = 0;
			for (Pedido p: onNext) {
				sumaPedidos += p.getPrecioPedido();
			}
			System.out.println("\nSUMA DE TODOS LOS PEDIDOS: ");
			System.out.println(sumaPedidos + "  Thread: " + Thread.currentThread().getName());
		});
		
							
		// Auditorías
		for (Restaurante r:listaRestaurantes) 
			System.out.print ("\nAuditoría Restaurante "+r.getNombre()+" "+r.getBalance());
		
		System.out.println ("\nAuditoria Cadena: "+ b.audit(0, config.numeroRestaurantes));
		System.out.println ("Tiempo total invertido en la tramitación: "+(new Date().getTime() - initialTime));
	}
}
