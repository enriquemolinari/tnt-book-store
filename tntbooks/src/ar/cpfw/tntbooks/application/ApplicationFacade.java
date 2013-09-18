package ar.cpfw.tntbooks.application;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;

import ar.cpfw.tntbooks.model.Customer;
import ar.cpfw.tntbooks.model.CustomerAgenda;
import ar.cpfw.tntbooks.model.Ticket;
import ar.cpfw.tntbooks.model.TicketItem;
import ar.cpfw.tntbooks.model.TntCart;

/**
 * This is an Application Service as defined by Domain Driven Design (DDD).
 * 
 * It promotes coordination between model objects and manage transactions
 * */
public class ApplicationFacade implements IApplicationFacade {

	private CustomerAgenda customerAgenda;

	public ApplicationFacade(CustomerAgenda customerAgenda) {
		this.customerAgenda = customerAgenda;
	}

	@Transactional
	public Ticket purchase(String customerId, TntCart cart) {
		if (StringUtils.isEmpty(customerId)) {
			throw new IllegalArgumentException("You need to provide a customer id");
		}
		
		if (cart == null) {
			throw new IllegalArgumentException("You need to provide a shopping cart");
		}

		//TODO: Need to handle not valid customers Ids
		Customer customer = customerAgenda.customerById(customerId);
		return customer.purchase(cart);
	}

	@Transactional(readOnly = true)
	public List<Ticket> purchases(String customerId) {
		Customer c = customerAgenda.customerById(customerId);
		hibernateInitialize(c.getPurchases());
		return new ImmutableList.Builder<Ticket>().addAll(c.getPurchases())
				.build();
	}

	// I need this to initialize the purchase collection defined as lazy
	// There are other ways of doing this though
	private void hibernateInitialize(Set<Ticket> purchases) {
		for (Ticket ticket : purchases) {
			Set<TicketItem> items = ticket.getItems();

			for (@SuppressWarnings("unused")
			TicketItem ticketItem : items) {
				break;
			}
		}
	}
}
