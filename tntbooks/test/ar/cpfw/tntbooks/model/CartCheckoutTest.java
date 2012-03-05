package ar.cpfw.tntbooks.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import ar.cpfw.tntbooks.model.Book;
import ar.cpfw.tntbooks.model.Cashier;
import ar.cpfw.tntbooks.model.CreditCard;
import ar.cpfw.tntbooks.model.CreditCardPaymentTerminal;
import ar.cpfw.tntbooks.model.Customer;
import ar.cpfw.tntbooks.model.MerchantProcessor;
import ar.cpfw.tntbooks.model.SalesNotebook;
import ar.cpfw.tntbooks.model.Ticket;
import ar.cpfw.tntbooks.model.TimeProvider;
import ar.cpfw.tntbooks.model.TntCart;
import ar.cpfw.tntbooks.model.Ticket.TicketItem;
import ar.cpfw.tntbooks.model.exceptions.BusinessException;

import com.google.common.collect.ImmutableSet;

public class CartCheckoutTest {

	private TimeProvider timeProvider = new TimeProvider() {
		public DateTime now() {
			return new DateTime(2010, 12, 6, 12, 34);
		}

		public long nowAsMillisecs() {
			return 0;
		}
	};

	private SalesNotebook salesNotebook = new SalesNotebook() {

		private List<Ticket> sales = new ArrayList<Ticket>();

		@Override
		public List<Ticket> allSales() {
			return sales;
		}

		@Override
		public void newSale(Ticket ticket) {
			this.sales.add(ticket);
		}
	};

	@Test
	public void the_cart_is_empty_then_i_get_an_exception() {

		// I'm not testing anything here related with the MerchantProcessor or
		// the SalesNotebook, null means that.
		Cashier cashier = new Cashier(null, null);

		Customer customer = new Customer("Malcom", "Young", new CreditCard(
				"1111111111111111", "Malcom Young", 12, 2020, timeProvider),
				cashier);

		try {
			cashier.checkout(new TntCart(timeProvider), customer.getCreditCard());
			Assert.fail("The checkout pass and the cart is empty...");
		} catch (BusinessException e) {
			// expecting to enter here...
		}
	}

	@Test
	public void the_creditcard_is_expired_then_the_purchase_cannot_be_done() {

		// I'm not testing anything here related with the MerchantProcessor or
		// the SalesNotebook,
		// null means that.
		Cashier cashier = new Cashier(null, null);

		TntCart cart = new TntCart(timeProvider);

		cart.add(new Book("AR123", 100f, "book title"), 3);
		Customer customer = new Customer("Phil", "Rudd", new CreditCard(
				"1111111111111111", "Phil Rudd", 12, 2000, timeProvider),
				cashier);

		try {
			customer.purchase(cart);
			Assert
					.fail("The purchase was done even when the credit card is expired...");
		} catch (BusinessException e) {
			Assert.assertEquals(0, customer.numberOfPurchases());
		}
	}

	@Test
	public void the_cart_contains_books_then_the_amount_of_the_sale_is_correct() {

		GetMethod method = new GetMethod() {
			@Override
			public String getResponseBodyAsString() {
				return "0|OK";
			}
		};

		HttpClient httpClient = new HttpClient() {
			@Override
			public int executeMethod(HttpMethod method) {
				return 200;
			}
		};

		MerchantProcessor mp = new MerchantProcessor(
				new CreditCardPaymentTerminal(httpClient, method));

		Cashier cashier = new Cashier(salesNotebook, mp);
		TntCart cart = new TntCart(timeProvider);

		cart.add(new Book("AR123", 100f, "book title"), 3);

		Customer customer = new Customer("Cliff", "Williams", new CreditCard(
				"1111111111111111", "Cliff Williams", 12, 2020, timeProvider),
				cashier);

		customer.purchase(cart);

		List<Ticket> sales = salesNotebook.allSales();
		Ticket sale = sales.get(0);
		
		Assert.assertEquals(300f, sale.getAmount(), 0f);
		Assert.assertEquals("PAID", sale.getStatus().name());
		Assert.assertEquals(1, customer.numberOfPurchases());
		Assert.assertNotSame("UNPAID", sale.getStatus().name());
		Assert.assertEquals(1, salesNotebook.allSales().size());

		Ticket.TicketItem ticketItem = new Ticket.TicketItem(new Book("AR123", 100f, "book title"), 3);

		Set<TicketItem> items = new ImmutableSet.Builder<TicketItem>().add(
				ticketItem).build();
		Assert.assertEquals(items, sale.getItems());
	}

	@Test
	public void the_creditcard_has_exceeded_its_limit_then_the_purchase_is_not_done() {

		GetMethod method = new GetMethod() {
			@Override
			public String getResponseBodyAsString() {
				return "1|LIMIT_EXCEEDED";
			}
		};

		HttpClient httpClient = new HttpClient() {
			@Override
			public int executeMethod(HttpMethod method) {
				return 200;
			}
		};

		MerchantProcessor mp = new MerchantProcessor(
				new CreditCardPaymentTerminal(httpClient, method));

		Cashier cashier = new Cashier(salesNotebook, mp);
		TntCart cart = new TntCart(timeProvider);

		cart.add(new Book("AR123", 100f, "book title"), 3);

		Customer customer = new Customer("Brian", "Jhonson", new CreditCard(
				"1111111111111111", "Brian Jhonson", 12, 2020, timeProvider),
				cashier);

		try {
			customer.purchase(cart);
			Assert.fail("The credit card has exceeded its limit...");
		} catch (BusinessException e) {
			Assert.assertEquals("LIMIT_EXCEEDED", e.getMessage());
			Assert.assertEquals(0, customer.numberOfPurchases());
		}
	}

	@Test
	public void the_cart_contain_books_and_the_merchant_approve_the_sale_then_the_purchase_is_done() {

		GetMethod method = new GetMethod() {
			@Override
			public String getResponseBodyAsString() {
				return "0|OK";
			}
		};

		HttpClient httpClient = new HttpClient() {
			@Override
			public int executeMethod(HttpMethod method) {
				return 200;
			}
		};

		MerchantProcessor mp = new MerchantProcessor(
				new CreditCardPaymentTerminal(httpClient, method));

		Cashier cashier = new Cashier(salesNotebook, mp);
		TntCart aCart = new TntCart(timeProvider);

		aCart.add(new Book("AR123", 100f, "book title"), 3);

		Customer customer = new Customer("Angus", "Young", new CreditCard(
				"1111111111111111", "Angus Young", 12, 2020, timeProvider),
				cashier);
		customer.purchase(aCart);
		
		List<Ticket> sales = salesNotebook.allSales();
		Ticket sale = sales.get(0);
		Assert.assertEquals("PAID", sale.getStatus().name());
		Assert.assertEquals(1, customer.numberOfPurchases());
	}

	@Test
	public void the_merchant_is_down_then_the_unpaid_purchase_is_done() {

		HttpClient httpClient = new HttpClient() {
			@Override
			public int executeMethod(HttpMethod method) throws IOException {
				throw new IOException();
			}
		};

		MerchantProcessor mp = new MerchantProcessor(
				new CreditCardPaymentTerminal(httpClient, null));

		Cashier cashier = new Cashier(salesNotebook, mp);
		TntCart cart = new TntCart(timeProvider);

		cart.add(new Book("AR123", 100f, "book title"), 3);

		Customer customer = new Customer("Chris", "Slade", new CreditCard(
				"1111111111111111", "Chris Slade", 12, 2020, timeProvider),
				cashier);

		customer.purchase(cart);
		List<Ticket> sales = salesNotebook.allSales();
		Ticket sale = sales.get(0);

		Assert.assertEquals(1, customer.numberOfPurchases());
		Assert.assertEquals("UNPAID", sale.getStatus().name());
	}
}
