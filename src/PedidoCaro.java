import java.util.concurrent.Callable;
import java.util.List;

public class PedidoCaro implements Callable<Double> {

	private List<Pedido> pedidos;
	
	public PedidoCaro(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
	
	@Override
	public Double call() throws Exception {
		Double precioMasAlto = 0.0;
		for(Pedido p: pedidos) {
			if(p.getPrecioPedido() > precioMasAlto) {
				precioMasAlto = p.getPrecioPedido();
			}
		}
		return precioMasAlto;
	}
}
