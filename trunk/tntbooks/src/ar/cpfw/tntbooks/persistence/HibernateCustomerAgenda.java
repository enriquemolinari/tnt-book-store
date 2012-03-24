package ar.cpfw.tntbooks.persistence;

import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import ar.cpfw.tntbooks.model.Customer;
import ar.cpfw.tntbooks.model.CustomerAgenda;
import ar.cpfw.tntbooks.model.Ticket;
import ar.cpfw.tntbooks.model.TicketItem;
import ar.cpfw.tntbooks.model.TntCart;

import com.google.common.collect.ImmutableList;

/**
 * @author Enrique Molinari
 */
public class HibernateCustomerAgenda extends
		HibernatePersistentObject<Customer> implements CustomerAgenda {

	@Transactional
	public void newCustomer(Customer aClient) {
		store(aClient);
	}

	@Transactional(readOnly = true)
	public List<Customer> customersByName(String name) {
		return new ImmutableList.Builder<Customer>().addAll(
				findByCriteria(Restrictions.ilike("name", name))).build();
	}

	@Transactional(readOnly = true)
	public Customer customerById(String customerId) {
		return findById(customerId);
	}

	@Transactional(readOnly = true)
	public List<Ticket> purchases(String customerId) {
		Customer c = findById(customerId);
		hibernateInitialize(c.getPurchases());
		return new ImmutableList.Builder<Ticket>().addAll(c.getPurchases())
				.build();
	}

	@Transactional(readOnly = true)
	public boolean exists(String customerId) {
		Object customer = getSession().createQuery(
				"select c.id from Customer c where c.id = :customerId")
				.setString("customerId", customerId).uniqueResult();

		return customer != null;
	}

	@Transactional
	public Ticket purchase(String customerId, TntCart aCart) {
		Customer customer = this.customerById(customerId);
		return customer.purchase(aCart);
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
