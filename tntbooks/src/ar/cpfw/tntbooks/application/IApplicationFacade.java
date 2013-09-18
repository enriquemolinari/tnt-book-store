package ar.cpfw.tntbooks.application;

import java.util.List;

import ar.cpfw.tntbooks.model.Ticket;
import ar.cpfw.tntbooks.model.TntCart;

/**
 * This interface is only to support JDK proxies. I preffer to avoid CGLIB.
 * */
public interface IApplicationFacade {

	Ticket purchase(String customerId, TntCart cart);

	List<Ticket> purchases(String customerId);
}