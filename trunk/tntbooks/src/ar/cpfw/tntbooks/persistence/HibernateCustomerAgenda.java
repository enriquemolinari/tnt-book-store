package ar.cpfw.tntbooks.persistence;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import ar.cpfw.tntbooks.model.Customer;
import ar.cpfw.tntbooks.model.CustomerAgenda;

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
	public boolean exists(String customerId) {
		Object customer = getSession().createQuery(
				"select c.id from Customer c where c.id = :customerId")
				.setString("customerId", customerId).uniqueResult();

		return customer != null;
	}
}
