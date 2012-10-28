package ar.cpfw.tntbooks.model;

import java.util.List;

/**
 * @author Enrique Molinari
 */
public interface BookCatalog {

	void newBook(Book aBook);
	Book bookByIsbn(String isbn);
	List<Book> allBooks();
}
