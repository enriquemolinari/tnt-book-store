package ar.cpfw.tntbooks.model;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import ar.cpfw.tntbooks.model.exceptions.CreditCardProcessorTerminalException;

/**
 * @author Enrique Molinari
 */
public class CreditCardPaymentTerminal {

	private static final int HTTP_BAD_REQUEST_CODE = 400;
	private HttpClient httpClient;
	private GetMethod getMethod;

	// GetMethod Spring initialized with protocol + host + path
	public CreditCardPaymentTerminal(HttpClient httpClient, GetMethod getMethod) {
		this.httpClient = httpClient;
		this.getMethod = getMethod;
	}

	public String transact(CreditCard aCreditCard, float amount) {
		try {

			this.getMethod
					.setQueryString(buildQueryString(aCreditCard, amount));

			int httpCode = httpClient.executeMethod(this.getMethod);

			if (httpCode == HTTP_BAD_REQUEST_CODE) {
				throw new CreditCardProcessorTerminalException("Bad request ...");
			}
			return getMethod.getResponseBodyAsString();

		} catch (Exception e) {
			throw new CreditCardProcessorTerminalException(e);
		}
	}

	private NameValuePair[] buildQueryString(CreditCard aCreditCard, float amount) {
		// creditCardNumber=5400000000000001
		// &creditCardExpiration=072011
		// &creditCardOwner=PEPE%20SANCHEZ
		// &transactionAmount=123.50		
		NameValuePair[] params = new NameValuePair[4];
		params[0] = new NameValuePair("creditCardNumber", aCreditCard.getNumber());
		params[1] = new NameValuePair("creditCardExpiration", aCreditCard.getExpireDateAsString());
		params[2] = new NameValuePair("creditCardOwner", aCreditCard.getOwner());
		params[3] = new NameValuePair("transactionAmount", String.valueOf(amount));
	
		return params;
	}
}
