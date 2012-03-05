package ar.cpfw.tntbooks.model;

import ar.cpfw.tntbooks.model.exceptions.BusinessException;
import ar.cpfw.tntbooks.model.exceptions.CreditCardProcessorTerminalException;

public class Cashier {

	private MerchantProcessor merchantProcessor;
	private SalesNotebook salesNotebook;
	
	public Cashier(SalesNotebook salesNotebook, MerchantProcessor merchantProcessor) {
		this.merchantProcessor = merchantProcessor;
		this.salesNotebook = salesNotebook;
	}

	public Ticket checkout(TntCart aCart, CreditCard aCreditCard) {
		if (aCart.size() == 0) {
			throw new BusinessException("The cart must not be empty... ");
		}

		if (aCreditCard.hasExpired()) {
			throw new BusinessException("Your credit cart has expired... ");
		}

		float total = 0f;

		for (Book aBook : aCart.allBooks()) {
			int quantity = aCart.quantityFor(aBook);
			total += aBook.getPrice() * quantity;
		}

		Ticket ticket = null;
		
		try {
			this.merchantProcessor.transact(aCreditCard, total);
			ticket = new Ticket(total, aCart.getBooks(), Ticket.Status.PAID);
		} catch (CreditCardProcessorTerminalException e) {
			ticket = new Ticket(total, aCart.getBooks(), Ticket.Status.UNPAID);
		}

		salesNotebook.newSale(ticket);
		return ticket;
	}
}
