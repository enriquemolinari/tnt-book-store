package ar.cpfw.tntbooks.model;


public interface BookCatalog {

	void newBook(Book aBook);
	Book bookByIsbn(String isbn);
}
