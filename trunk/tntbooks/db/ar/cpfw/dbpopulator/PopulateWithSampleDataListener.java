package ar.cpfw.dbpopulator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ar.cpfw.tntbooks.model.Book;
import ar.cpfw.tntbooks.model.BookCatalog;
import ar.cpfw.tntbooks.model.CreditCard;
import ar.cpfw.tntbooks.model.Customer;
import ar.cpfw.tntbooks.model.CustomerAgenda;
import ar.cpfw.tntbooks.model.TimeProviderImpl;

public class PopulateWithSampleDataListener  implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// nothing to do here ...
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ApplicationContext springContext = new ClassPathXmlApplicationContext("master-beans.xml");
		CustomerAgenda customerAgenda = (CustomerAgenda) springContext.getBean("customerAgenda");

		CreditCard creditCard = new CreditCard("123456789123456", "Angus Young", 9, 2016, new TimeProviderImpl());
		Customer customer = new Customer("Angus", "Young", creditCard, /*cashier not necesary*/ null);
		customerAgenda.newCustomer(customer);

		BookCatalog bookCatalog = (BookCatalog) springContext.getBean("bookCatalog");
		Book aBook = new Book("AR123456", 65.50f, "AC/DC at River Plate: chat with a fan");

		bookCatalog.newBook(aBook);
	}
}
