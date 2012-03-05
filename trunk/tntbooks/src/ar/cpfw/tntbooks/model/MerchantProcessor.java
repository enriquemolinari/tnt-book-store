package ar.cpfw.tntbooks.model;

import static org.apache.commons.lang.StringUtils.split;
import ar.cpfw.tntbooks.model.exceptions.BusinessException;

public class MerchantProcessor {

	private CreditCardPaymentTerminal terminal;

	public MerchantProcessor(CreditCardPaymentTerminal terminal) {
		this.terminal = terminal;
	}

	public void transact(CreditCard aCreditCard, float totalAmount) {
		String output = terminal.transact(aCreditCard, totalAmount);
		String[] splitOutput = split(output, "|");
			
		if ("1".equals(splitOutput[0])) {
			throw new BusinessException(splitOutput[1]);
		}
	}
}
