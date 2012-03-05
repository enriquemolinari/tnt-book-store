package ar.cpfw.tntbooks.model;

import java.util.List;

public interface CustomerAgenda {

	void newCustomer(Customer aClient);

	List<Customer> customersByName(String name);

	Customer customerById(String customerId);

	boolean exists(String customerId);

	List<Ticket> purchases(String customerId);
	
	void purchase(String customerId, TntCart cart);

}
