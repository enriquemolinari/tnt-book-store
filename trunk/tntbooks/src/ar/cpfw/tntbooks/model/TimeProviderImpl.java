package ar.cpfw.tntbooks.model;

import org.joda.time.DateTime;

public class TimeProviderImpl implements TimeProvider {

	public DateTime now() {
		return new DateTime();
	}
	
	public long nowAsMillisecs() {
		return System.currentTimeMillis();
	}
}
