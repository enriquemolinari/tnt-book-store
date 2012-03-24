package ar.cpfw.tntbooks.model;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import ar.cpfw.tntbooks.model.CreditCard;
import ar.cpfw.tntbooks.model.CreditCardPaymentTerminal;
import ar.cpfw.tntbooks.model.TimeProvider;

/**
 * @author Enrique Molinari
 */
public class CreditCardPaymentTerminalTest {

	@Test
	public void uri_to_merchant_processor_is_well_formed() throws URIException {
		// set up
		GetMethod method = new GetMethod("http://www.cla.com/path");

		HttpClient httpClient = new HttpClient() {
			@Override
			public int executeMethod(HttpMethod method) {
				return 200;
			}
		};

		// timeProvider in the past
		TimeProvider timeProvider = new TimeProvider() {
			public DateTime now() {
				return new DateTime(2010, 12, 6, 12, 34);
			}

			public long nowAsMillisecs() {
				return 0;
			}
		};

		CreditCardPaymentTerminal posnet = new CreditCardPaymentTerminal(
				httpClient, method);

		CreditCard aCreditCard = new CreditCard("123456789123456",
				"Simon Wright", 10, 2015, timeProvider);

		// exercise 
		// if not throws an exception means the URI was built successful
		posnet.transact(aCreditCard, 12.9f);

		// verify
		URI uri = method.getURI();
		Assert.assertEquals("www.cla.com", uri.getHost());
		Assert.assertEquals("/path", uri.getPath());

		String[] queryParameters = StringUtils.split(uri.getQuery(), "&");
		Assert.assertEquals("There must be four query parameters", 4,
				queryParameters.length);
		Assert.assertEquals("creditCardNumber=123456789123456",
				queryParameters[0]);
		Assert.assertEquals("creditCardExpiration=102015", queryParameters[1]);
		Assert
				.assertEquals("creditCardOwner=Simon Wright",
						queryParameters[2]);
		Assert.assertEquals("transactionAmount=12.9", queryParameters[3]);
	}
}
