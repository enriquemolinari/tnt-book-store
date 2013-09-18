package ar.cpfw.tntbooks.persistence;

import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import ar.cpfw.tntbooks.model.Book;
import ar.cpfw.tntbooks.model.BookCatalog;
import ar.cpfw.tntbooks.model.Cashier;
import ar.cpfw.tntbooks.model.CreditCard;
import ar.cpfw.tntbooks.model.CreditCardPaymentTerminal;
import ar.cpfw.tntbooks.model.Customer;
import ar.cpfw.tntbooks.model.CustomerAgenda;
import ar.cpfw.tntbooks.model.MerchantProcessor;
import ar.cpfw.tntbooks.model.SalesNotebook;
import ar.cpfw.tntbooks.model.Ticket;
import ar.cpfw.tntbooks.model.TimeProvider;
import ar.cpfw.tntbooks.model.TntCart;

/**
 * @author Enrique Molinari
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-beans.xml",
		"/persistence-beans.xml", "/model-beans.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@ActiveProfiles("dev")
@Transactional
public class HibernatePaidSalesTest implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	private CustomerAgenda customerAgenda;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Before
	public void setUp() {
		this.customerAgenda = (CustomerAgenda) applicationContext
				.getBean("customerAgenda");

		BookCatalog bookCatalog = (BookCatalog) applicationContext
				.getBean("bookCatalog");
		bookCatalog.newBook(new Book("AR1234567", 157.50f, "book title"));

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
				return 200;
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

		MerchantProcessor mp = new MerchantProcessor(posnet);

		SalesNotebook salesNotebook = (SalesNotebook) applicationContext
				.getBean("salesNotebook");

		Cashier cashier = new Cashier(salesNotebook, mp);
		Customer customer = new Customer("Estelita", "Artua", creditCard,
				cashier);

		TntCart cart = new TntCart(timeProvider);
		cart.add(bookCatalog.bookByIsbn("AR1234567"), 2);

		customer.purchase(cart);

		customerAgenda.newCustomer(customer);

		// another purchase
		TntCart anotheCart = new TntCart(timeProvider);
		anotheCart.add(bookCatalog.bookByIsbn("AR1234567"), 3);

		customer.purchase(anotheCart);

	}

	@Test
	public void i_have_a_customer_in_db_then_i_should_find_her_by_name() {
		List<Customer> l = customerAgenda.customersByName("Estelita");
		Assert.assertEquals(1, l.size());
	}

	@Test
	public void i_have_the_creditcard_mapped_with_join_then_i_should_get_it_when_load_a_customer() {
		List<Customer> l = customerAgenda.customersByName("Estelita");
		Assert.assertEquals(1, l.size());
		Assert.assertNotNull(l.get(0).getCreditCard());
	}

	@Test
	public void the_paid_was_done_then_both_tickets_were_persisted() {

		SalesNotebook salesNotebook = (SalesNotebook) applicationContext
				.getBean("salesNotebook");
		List<Ticket> tickets = salesNotebook.allSales();

		Assert.assertEquals(2, tickets.size());
		
		Assert.assertEquals(1, tickets.get(0).itemsSize());
		Assert.assertEquals(1, tickets.get(1).itemsSize());
		Assert.assertEquals(Ticket.Status.PAID, tickets.get(0).getStatus());
		Assert.assertEquals(Ticket.Status.PAID, tickets.get(1).getStatus());
	}

	@Test
	public void the_customer_exists_then_it_is_persisted() {
		List<Customer> l = customerAgenda.customersByName("Estelita");
		Customer customer = l.get(0);
		
		String id = (String) ReflectionTestUtils.getField(customer, "id");
		Assert.assertTrue(customerAgenda.exists(id));
	}

	@Test
	public void the_customer_does_not_exists_the_it_is_not_persisted() {
		Assert.assertFalse(customerAgenda.exists("doesnotexists"));
	}

	
}
