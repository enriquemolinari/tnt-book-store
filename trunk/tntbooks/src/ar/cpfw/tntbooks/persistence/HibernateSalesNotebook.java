package ar.cpfw.tntbooks.persistence;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import ar.cpfw.tntbooks.model.SalesNotebook;
import ar.cpfw.tntbooks.model.Ticket;

import com.google.common.collect.ImmutableList;

public class HibernateSalesNotebook extends HibernatePersistentObject<Ticket>
		implements SalesNotebook {

	@Transactional(readOnly = true)
	public List<Ticket> allSales() {
		return new ImmutableList.Builder<Ticket>().addAll(
				findByCriteria(Restrictions.eq("status", Ticket.Status.PAID)))
				.build();
	}

	@Transactional
	public void newSale(Ticket ticket) {
		store(ticket);
	}
}
