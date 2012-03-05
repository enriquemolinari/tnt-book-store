package ar.cpfw.tntbooks.model;

import java.util.List;

import ar.cpfw.core.Validatable;
import ar.cpfw.tntbooks.model.exceptions.BusinessException;

// A bit of data oriented programming in this class, as this class 
// is the link between the non OO world to the OO world
public class TntBookStore implements Validatable<Book> {

	private static final String TNTEDITORIAL_ISBN_PREFIX = "AR123";
	private CustomerAgenda customerAgenda;
	private BookCatalog bookCatalog;

	public TntBookStore() {

	}

	public boolean isValidCustomer(String clientId) {
		return customerAgenda.exists(clientId);
	}

	public Book bookByIsbn(String isbn) {
		return bookCatalog.bookByIsbn(isbn);
	}

	public void checkout(TntCart cart, String customerId) {
		customerAgenda.purchase(customerId, cart);
	}

	public List<Ticket> purchases(String customerId) {
		return customerAgenda.purchases(customerId);
	}

	// Spring needs this ...
	public void setCustomerAgenda(CustomerAgenda customerAgenda) {
		this.customerAgenda = customerAgenda;
	}

	public void setBookCatalog(BookCatalog bookCatalog) {
		this.bookCatalog = bookCatalog;
	}

	@Override
	public void validate(Book book) {
		belongsToTntBooks(book);
	}

	private void belongsToTntBooks(Book aBook) {
		if (!aBook.getIsbn().startsWith(TNTEDITORIAL_ISBN_PREFIX)) {
			throw new BusinessException(
					"The book does not belong to our editorial... ");
		}
	}
}
