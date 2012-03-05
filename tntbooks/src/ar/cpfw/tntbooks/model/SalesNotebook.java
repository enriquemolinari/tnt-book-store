package ar.cpfw.tntbooks.model;

import java.util.List;

public interface SalesNotebook {
	List<Ticket> allSales();

	void newSale(Ticket ticket);
}
