package ar.cpfw.tntbooks.persistence;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import ar.cpfw.tntbooks.model.Book;
import ar.cpfw.tntbooks.model.BookCatalog;
import ar.cpfw.tntbooks.model.exceptions.BusinessException;

/**
 * @author Enrique Molinari
 */
public class HibernateBookCatalog extends HibernatePersistentObject<Book> implements
		BookCatalog {

	@Override
	@Transactional(readOnly = true)
	public Book bookByIsbn(String isbn) {
		Criteria criteria = getSession().createCriteria(Book.class);
		criteria.add(Restrictions.eq("isbn", isbn));
		Book aBook = (Book) criteria.uniqueResult();
		if (aBook == null) {
			throw new BusinessException("The book with isbn: " + isbn
					+ " does not exist");
		}
		return aBook;
	}

	@Override
	@Transactional
	public void newBook(Book aBook) {
		getSession().saveOrUpdate(aBook);
	}

}
