package ar.cpfw.tntbooks.model;

import org.junit.Assert;
import org.junit.Test;

public class CustomerTest {

	@Test
	public void customer_name_is_empty_then_is_not_created() {
		Customer customer = null;
		try {
			customer = new Customer("", "", null, null);
			Assert.assertNull(customer);
		} catch (Exception e) {
			Assert.assertNull(customer);
		}
	}

	@Test
	public void customer_name_is_too_large_then_is_not_created() {
		Customer customer = null;
		try {
			customer = new Customer("Joaquin Sebastian Armando Jose Antorio",
					"Gonzalez", null, null);
			Assert.assertNull(customer);
		} catch (Exception e) {
			Assert.assertNull(customer);
		}
	}

	@Test
	public void customer_surname_is_too_large_then_is_not_created() {
		Customer customer = null;
		try {
			customer = new Customer("Antorio",
					"Gonzalez De La Holla Hernandez Young", null, null);
			Assert.assertNull(customer);
		} catch (Exception e) {
			Assert.assertNull(customer);
		}
	}

	@Test
	public void customer_name_and_surname_are_ok_then_is_created() {
		Customer customer = new Customer("Diego", "Armando", null, null);
		Assert.assertNotNull(customer);
		Assert.assertEquals("Diego", customer.getName());
		Assert.assertEquals("Armando", customer.getSurname());
	}
}
