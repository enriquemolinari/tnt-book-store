package ar.cpfw.tntbooks.model.exceptions;

@SuppressWarnings("serial")
public class CreditCardProcessorTerminalException extends RuntimeException {

	public CreditCardProcessorTerminalException(Throwable t) {
		super(t);
	}

	public CreditCardProcessorTerminalException(String msg) {
		super(msg);
	}

}
