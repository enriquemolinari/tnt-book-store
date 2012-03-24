package ar.cpfw.tntbooks.model.exceptions;

/**
 * @author Enrique Molinari
 */
@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {

	public BusinessException(String msg) {
		super(msg);
	}

	public BusinessException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
