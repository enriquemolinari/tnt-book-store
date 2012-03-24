package ar.cpfw.tntbooks.model;

import ar.cpfw.core.Validatable;
import ar.cpfw.tntbooks.model.exceptions.BusinessException;

/**
 * To validate that a Book belongs to the Tnt
 * Editorial
 * 
 * @author Enrique Molinari
 */
public class TntBookStore implements Validatable<Book> {

	private static final String TNTEDITORIAL_ISBN_PREFIX = "AR123";

	public TntBookStore() {

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
