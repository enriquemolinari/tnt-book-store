package ar.cpfw.tntbooks.model;

import java.util.List;

/**
 * @author Enrique Molinari
 */
public interface SalesNotebook {
	List<Ticket> allSales();

	void newSale(Ticket ticket);
}
