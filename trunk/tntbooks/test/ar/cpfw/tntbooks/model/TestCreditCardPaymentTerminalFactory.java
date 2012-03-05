package ar.cpfw.tntbooks.model;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import ar.cpfw.tntbooks.model.CreditCardPaymentTerminal;

public class TestCreditCardPaymentTerminalFactory {

	public static CreditCardPaymentTerminal create() {
		
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
		
		return new CreditCardPaymentTerminal(httpClient, method);
	}
}
