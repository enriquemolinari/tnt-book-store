package ar.cpfw.tntbooks.main;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ar.cpfw.tntbooks.model.Book;
import ar.cpfw.tntbooks.model.BookCatalog;
import ar.cpfw.tntbooks.model.Cashier;
import ar.cpfw.tntbooks.model.CreditCard;
import ar.cpfw.tntbooks.model.CreditCardPaymentTerminal;
import ar.cpfw.tntbooks.model.Customer;
import ar.cpfw.tntbooks.model.CustomerAgenda;
import ar.cpfw.tntbooks.model.MerchantProcessor;
import ar.cpfw.tntbooks.model.SalesNotebook;
import ar.cpfw.tntbooks.model.TimeProvider;
import ar.cpfw.tntbooks.model.TntCart;

public class Main {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"master-beans.xml");

		CustomerAgenda customerDAO = (CustomerAgenda) context
				.getBean("customerAgenda");
		BookCatalog bookDAO = (BookCatalog) context.getBean("bookCatalog");
		Book aBook = new Book("AR123456", 12.50f, "book title 2");
		bookDAO.newBook(aBook);
		//		
		// // List<Customer> l = customerDAO.customersByName("Estelita");
		// // Customer customer = l.get(0);
		//
		// timeProvider in the past
		TimeProvider timeProvider = new TimeProvider() {
			public DateTime now() {
				return new DateTime(2010, 12, 6, 12, 34);
			}
			public long nowAsMillisecs() {
				return 0;
			}
		};

		// generate data ...
		CreditCard creditCard = new CreditCard("123456789123456",
				"Estelita Artua", 9, 2012, timeProvider);

		HttpClient httpClient = new HttpClient() {
			@Override
			public int executeMethod(HttpMethod method) {
				return 202;
			}
		};

		GetMethod method = new GetMethod() {
			@Override
			public String getResponseBodyAsString() {
				return "0|OK";
			}
		};

		CreditCardPaymentTerminal posnet = new CreditCardPaymentTerminal(
				httpClient, method);

		SalesNotebook salesNotebook = (SalesNotebook) context
				.getBean("salesNotebook");

		MerchantProcessor mp = new MerchantProcessor(posnet);
		Cashier cashier = new Cashier(salesNotebook, mp);
		
		Customer customer = new Customer("Jack", "Black", creditCard,
				cashier);

//		List<Customer> l = customerDAO.customersByName("Estelita");
//		Customer customer = l.get(0);
		TntCart cart = new TntCart(timeProvider);

		cart.add(bookDAO.bookByIsbn("AR123456"), 2);

		customer.purchase(cart);
		customerDAO.newCustomer(customer);
	
		//		
		// List<Customer> c = customerDAO.customersByName("Estelita");
		// System.out.println(c.get(0).getSurname());

		// CreditCardPaymentTerminal c = new
		// CreditCardPaymentTerminal.BuildAFake().build();
//		List<Customer> l = customerDAO.customersByName("Estelita");
//		for (Customer cliente2 : l) {
//			System.out.println(cliente2);
//			// System.out.println(cliente2.getId());
//			// System.out.println(cliente2.getNombre());
//			// System.out.println(cliente2.getApellido());
//		}

		// TntBookStore facade = (TntBookStore)context.getBean("facade");
		// // facade.checkout();
		//		
		// //ventas
		// List<Ticket> ventas = facade.ventas();
		// for (Ticket ticket3 : ventas) {
		// System.out.println(ticket3.getAmount());
		// }
	}
}