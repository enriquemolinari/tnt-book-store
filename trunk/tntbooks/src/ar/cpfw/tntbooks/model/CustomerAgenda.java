package ar.cpfw.tntbooks.model;

import java.util.List;

/**
 * @author Enrique Molinari
 */
public interface CustomerAgenda {

	void newCustomer(Customer aClient);

	List<Customer> customersByName(String name);

	Customer customerById(String customerId);

	boolean exists(String customerId);
}
