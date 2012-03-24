package ar.cpfw.tntbooks.model;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import ar.cpfw.tntbooks.model.CreditCard;
import ar.cpfw.tntbooks.model.exceptions.BusinessException;

/**
 * @author Enrique Molinari
 */
public class CreditCardTest {

	@Test
	public void the_time_has_passed_then_the_creditcard_has_expired() {
		TestChangeableTimeProvider timeProvider = new TestChangeableTimeProvider(
				new DateTime(2011, 12, 6, 12, 34), 0);

		CreditCard aCreditCard = new CreditCard("123456789876543", "Dave Evans",
				11, 2011, timeProvider);
		Assert.assertTrue(aCreditCard.hasExpired());
	}

	@Test
	public void due_to_the_expiry_date_is_invalid_the_credit_card_is_not_created() {

		try {
			new CreditCard("123456789876543", "Mark Evans", 13, 2011, null);
		} catch (BusinessException e) {
			// do nothing as I'm expecting this...
		}
	}

}
