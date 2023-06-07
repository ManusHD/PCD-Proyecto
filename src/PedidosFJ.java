import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class PedidosFJ extends RecursiveTask<List<Pedido>>{

	private List<Pedido> pedidos;
	
	public PedidosFJ(List<Pedido> pedidos){
		this.pedidos = pedidos;
	}
	
	@Override
	protected List<Pedido> compute() {
		 List<Pedido> pedidosEncontrados = new ArrayList<>();
		
		for(Pedido p: pedidos) {
			if(p.getPrecioPedido() > 20){
				pedidosEncontrados.add(p);
			}
		}
		
		return pedidosEncontrados;
	}
}
