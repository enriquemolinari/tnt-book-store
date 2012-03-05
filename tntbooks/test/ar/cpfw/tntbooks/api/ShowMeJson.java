package ar.cpfw.tntbooks.api;

import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class ShowMeJson {

	private static final String HOST = "http://localhost:8080/tnt/webapi/";
	private static final String clientId = "ff80808135df2a720135df2abf800002";

	@Test
	public void showAll() {

		// create a cart
//		Response response = RestAssured.post(HOST + "/cart?clientId="
//				+ clientId);
//		
//		response.print();
//		
//		// add to cart
//		String cartId = RestAssured.post(
//				HOST + "/cart?clientId=" + clientId)
//				.jsonPath().getString("cartId");
//		
//		RestAssured.post(
//				HOST + "/cart/{cartId}?isbn=AR123trew&quantity=2", cartId);
//		
//		response = RestAssured.post(
//				HOST + "/cart/{cartId}?isbn=AR123456&quantity=2", cartId);
//				
//		response.print();
//		
//		//list cart
//		response = RestAssured.get(HOST + "/cart/{cartId}", cartId);
//		response.print();
//		
//		//checkout cart
//		response = RestAssured.post(HOST + "/cart/{cartId}/checkout?clientId=" + clientId, cartId);
//		response.print();
		
		//list purchases
		Response response = RestAssured.get(HOST + "/purchases?clientId=" + clientId);
		response.print();
		
	}

}
