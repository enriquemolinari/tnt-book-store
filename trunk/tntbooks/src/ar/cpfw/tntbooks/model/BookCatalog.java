package ar.cpfw.tntbooks.model;

/**
 * @author Enrique Molinari
 */
public interface BookCatalog {

	void newBook(Book aBook);
	Book bookByIsbn(String isbn);
}
