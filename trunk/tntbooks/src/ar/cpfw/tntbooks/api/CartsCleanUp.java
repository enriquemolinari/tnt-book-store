package ar.cpfw.tntbooks.api;

import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

import ar.cpfw.tntbooks.model.TntCart;

/**
 * @author Enrique Molinari
 */
public class CartsCleanUp extends TimerTask {

	private Map<String, TntCart> listOfCreatedCart;

	public CartsCleanUp(Map<String, TntCart> listOfCreatedCart) {
		this.listOfCreatedCart = listOfCreatedCart;
	}

	@Override
	public synchronized void run() {

		Iterator<Map.Entry<String, TntCart>> iterator = listOfCreatedCart
				.entrySet().iterator();

		while (iterator.hasNext()) {
			
			Map.Entry<String, TntCart> entry = iterator.next();
			
			if (entry.getValue().isTimeout()) {
				iterator.remove();
			}
		}
	}
}
