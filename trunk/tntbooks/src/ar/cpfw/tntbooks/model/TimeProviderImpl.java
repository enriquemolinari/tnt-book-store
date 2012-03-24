package ar.cpfw.tntbooks.model;

import org.joda.time.DateTime;

/**
 * @author Enrique Molinari
 */
public class TimeProviderImpl implements TimeProvider {

	public DateTime now() {
		return new DateTime();
	}
	
	public long nowAsMillisecs() {
		return System.currentTimeMillis();
	}
}
