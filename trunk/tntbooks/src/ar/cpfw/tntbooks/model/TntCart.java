package ar.cpfw.tntbooks.model;

import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;

import ar.cpfw.core.Cart;
import ar.cpfw.core.Validatable;
import ar.cpfw.tntbooks.model.exceptions.BusinessException;

/**
 * @author Enrique Molinari
 */
public class TntCart {

	private static final int TIME_OUT = 30;
	private TimeProvider timeProvider;
	private long lastTimeUsed;
	private Cart<Book> cart = new Cart<Book>();
	
	public TntCart(TimeProvider timeProvider) {
		this.timeProvider = timeProvider;
		keepAlive();
	}

	public void add(Book aBook) {
		add(aBook, 1);
	}

	public void add(Book aBook, int quantity) {
		checkTimeOut();
		this.cart.add(aBook, quantity);	
	}

	boolean contains(Book aBook) {
		return this.cart.contains(aBook);
	}
	 
	private void checkTimeOut() {
		if (isTimeout()) {
			throw new BusinessException("timeout...");
		}
		keepAlive();
	}

	private void keepAlive() {
		this.lastTimeUsed = timeProvider.nowAsMillisecs();
	}
	
	public boolean isTimeout() {
		float elapsedTime = ((timeProvider.nowAsMillisecs() - lastTimeUsed) / 1000f) / 60f;
		return elapsedTime >= TIME_OUT; 
	}

	int quantityFor(Book aBook) {
		return this.cart.quantityFor(aBook);
	}

	int size() {
		checkTimeOut();
		
		return this.cart.size();
	}

	public Set<Book> allBooks() {
		checkTimeOut();
		
		return this.cart.allItems();
	}
	
	@JsonProperty(value = "books")
	public Map<Book, Integer> getBooks() {
		keepAlive();
		return this.cart.itemsAndQuantity();
	}
	
	public void setValidator(Validatable<Book> validatable) {
		this.cart.setValidator(validatable);
	}
}
