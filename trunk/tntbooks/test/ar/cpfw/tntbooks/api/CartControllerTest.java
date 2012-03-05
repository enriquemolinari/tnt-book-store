package ar.cpfw.tntbooks.api;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;

public class CartControllerTest {

	private static final String HOST = "http://localhost:8080/tnt/webapi/";
	private String cartId;
	private static final String clientId = "ff80808135df2a720135df2abf800002";

	@Before
	public void setUp() {
		cartId = RestAssured.post(
				HOST + "/cart?clientId=" + clientId)
				.jsonPath().getString("cartId");
	}

	@Test
	public void addingABookToTheCart_Then_TheBookIsInTheCart() {
		JsonPath jsonPath = RestAssured.post(
				HOST + "/cart/{cartId}?isbn=AR1234567&quantity=2", cartId)
				.jsonPath();

		String value = jsonPath.getString("cartContent");
		System.out.println(value);
		
		Assert.assertThat(value,
				is("{\"AR1234567|157.5\":2}"));
	}

	@Test
	public void addingABookToTheCart_Then_TheListCartShowThatBook() {
		// set up
		RestAssured.post(HOST + "/cart/{cartId}?isbn=AR1234567&quantity=2",
				cartId);

		String cartValue = RestAssured.get(HOST + "/cart/{cartId}", cartId).jsonPath().getString("cartContent");
		Assert.assertThat(cartValue,
				is("{\"AR1234567|157.5\":2}"));
		}

	@Test
	public void usingAValidClientId_Then_theCartIsCreated() {
		expect().body("cartId", notNullValue()).when().post(
				HOST + "/cart?clientId=" + clientId);
	}

	@Test
	public void usingAnIvalidClientId_Then_theCartIsNotCreated() {
		expect().body("error",
				is("The Id: notValidClientId is not a valid customer.")).when()
				.post(HOST + "/cart?clientId=notValidClientId");
	}

	@Test
	public void allIsValid_Then_theCheckoutIsDone() {
		// set up
		RestAssured.post(HOST + "/cart/{cartId}?isbn=AR1234567&quantity=2",
				cartId);

		RestAssured.post(HOST + "/cart/{cartId}?isbn=AR12309876&quantity=1",
				cartId);

		expect().body("checkout", is("OK")).when().post(HOST + "/cart/{cartId}/checkout?clientId=" + clientId, cartId);
	}
}
