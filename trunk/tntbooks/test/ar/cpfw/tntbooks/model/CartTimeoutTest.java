package ar.cpfw.tntbooks.model;


import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import ar.cpfw.tntbooks.model.Book;
import ar.cpfw.tntbooks.model.TntCart;
import ar.cpfw.tntbooks.model.exceptions.BusinessException;

public class CartTimeoutTest {
	
	@Test
	public void after_30_minutes_without_use_a_timeout_exception_is_generated() {
		// create a cart
		TestChangeableTimeProvider changeableTimeProvider = new TestChangeableTimeProvider(new DateTime(), System.currentTimeMillis());
		TntCart cart = new TntCart(changeableTimeProvider);
		
		// adding a book
		cart.add(new Book("AR123", 100f, "book title"));
		
		// waiting 30 minutes...
		changeableTimeProvider.setTime(System.currentTimeMillis() + 1800000);
		
		// exercise some operation with the cart after 30 minutes
		try {
			cart.add(new Book("AR123", 100f, "book title"));
			Assert.fail("The book seems to be added, when we have timeout ...");
		} catch (BusinessException e) {
		}
	}

	@Test
	public void after_29_minutes_without_use_the_cart_can_still_be_used() {
		// create a cart
		TestChangeableTimeProvider changeableTimeProvider = new TestChangeableTimeProvider(new DateTime(), System.currentTimeMillis());
		TntCart cart = new TntCart(changeableTimeProvider);
		
		// waiting 29 minutes... 
		changeableTimeProvider.setTime(System.currentTimeMillis() + 1799000);
		
		// adding a books is still valid
		cart.add(new Book("AR123", 100f, "book title"));
		Assert.assertEquals(1,cart.size());
	}
}
