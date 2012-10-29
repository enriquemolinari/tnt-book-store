package ar.cpfw.tntbooks.model;


import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import ar.cpfw.tntbooks.model.Book;
import ar.cpfw.tntbooks.model.TimeProvider;
import ar.cpfw.tntbooks.model.TntBookStore;
import ar.cpfw.tntbooks.model.TntCart;
import ar.cpfw.tntbooks.model.exceptions.BusinessException;

/**
 * @author Enrique Molinari
 */
public class CarritoTest {

	private static final String ISBN_TNTBOOKS1 = "AR123456";
	private static final String ISBN_TNTBOOKS2 = "AR123457";
	private static final String ISBN_TNTBOOKS3 = "AR123458";
	private static final String ISBN_OTHER_EDITORIAL = "AR654321";

	TimeProvider timeProvider = new TimeProvider() {
		public DateTime now() {
			return new DateTime();
		}

		public long nowAsMillisecs() {
			return 0;
		}
	};

	@Test
	public void add_a_book_to_a_cart_then_the_cart_contains_that_book() {
		Book book = new Book(ISBN_TNTBOOKS1, 100f, "book title");
		TntCart cart = new TntCart(timeProvider);

		cart.add(book);

		Assert.assertTrue(cart.contains(book));
		Assert.assertEquals(1, cart.size());
	}

	@Test
	public void add_five_books_to_a_cart_then_the_cart_contains_five_books() {
		Book book = new Book(ISBN_TNTBOOKS1, 100f, "book title");
		TntCart cart = new TntCart(timeProvider);

		cart.add(book, 5);

		Assert.assertTrue(cart.contains(book));
		Assert.assertEquals(5, cart.quantityFor(book));
		Assert.assertEquals(5, cart.size());
	}

	@Test
	public void when_adding_a_book_from_other_editorial_to_a_cart_the_book_is_rejected() {
		Book book = new Book(ISBN_OTHER_EDITORIAL, 100f, "book title");
		TntCart cart = new TntCart(timeProvider);
		cart.setValidator(new TntBookStore());
		
		try {
			cart.add(book);
			Assert.fail("The book was added to the cart when it should not ...");
		} catch (BusinessException lie) {
			Assert.assertEquals(0, cart.size());
		}
	}

	@Test
	public void when_adding_a_nonpositive_number_of_books_the_book_is_not_added() {
		Book book = new Book(ISBN_TNTBOOKS1, 100f, "book title");
		TntCart cart = new TntCart(timeProvider);

		try {
			cart.add(book, -3);
			Assert.fail("The book was added to the cart when it should not ...");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals(0, cart.size());
		}
	}

	@Test
	public void when_adding_three_different_books_then_the_cart_has_three_books() {
		Book book1 = new Book(ISBN_TNTBOOKS1, 100f, "book title");
		Book book2 = new Book(ISBN_TNTBOOKS2, 100f, "book title");
		Book book3 = new Book(ISBN_TNTBOOKS3, 100f, "book title");

		TntCart cart = new TntCart(timeProvider);
		
		cart.add(book1);
		cart.add(book2);
		cart.add(book3);

		Assert.assertEquals(3, cart.size());
	}

	@Test
	public void when_adding_two_times_the_same_book_then_the_cart_contains_two_books() {
		Book book1 = new Book(ISBN_TNTBOOKS1, 100f, "book title");

		TntCart cart = new TntCart(timeProvider);
		
		cart.add(book1);
		cart.add(book1);

		Assert.assertEquals(2, cart.size());
	}
	
	@Test
	public void when_adding_two_book_and_removing_one_then_I_have_one_book() {
		Book book1 = new Book(ISBN_TNTBOOKS1, 100f, "book title");
	
		TntCart cart = new TntCart(timeProvider);

		cart.add(book1);
		cart.add(book1);
		
		cart.remove(book1);
		Assert.assertEquals(1, cart.size());
		Assert.assertTrue(cart.getBooks().get(book1).equals(1));
	}

	@Test
	public void when_adding_two_different_books_and_removing_one_then_I_have_one_book() {
		Book book1 = new Book(ISBN_TNTBOOKS1, 100f, "book title");
		Book book2 = new Book(ISBN_TNTBOOKS2, 100f, "book title");
	
		TntCart cart = new TntCart(timeProvider);

		cart.add(book1);
		cart.add(book2);
		
		cart.remove(book1);
		Assert.assertEquals(1, cart.size());
		Assert.assertTrue(cart.getBooks().get(book2).equals(1));
	}

	@Test
	public void when_removing_a_book_more_than_the_quantity_then_I_remove_the_book() {
		Book book1 = new Book(ISBN_TNTBOOKS1, 100f, "book title");
		Book book2 = new Book(ISBN_TNTBOOKS2, 100f, "book title");
	
		TntCart cart = new TntCart(timeProvider);

		cart.add(book1);
		cart.add(book1);
		cart.add(book2);
		
		cart.remove(book1);
		cart.remove(book1);	
		Assert.assertNull(cart.getBooks().get(book1));
	}
	
}
