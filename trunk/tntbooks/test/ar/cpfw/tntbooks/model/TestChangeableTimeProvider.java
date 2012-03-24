package ar.cpfw.tntbooks.model;

import org.joda.time.DateTime;

import ar.cpfw.tntbooks.model.TimeProvider;

/**
 * @author Enrique Molinari
 */
public class TestChangeableTimeProvider implements TimeProvider {

	private DateTime currentTime;
	private long currentMs;
	
	public TestChangeableTimeProvider(DateTime startTime, long currentMs) {
		this.currentTime = startTime;
		this.currentMs = currentMs;
	}

	public DateTime now() {
		return this.currentTime;
	}

	public long nowAsMillisecs() {
		return this.currentMs;
	}
	
	public void setTime(DateTime newTime) {
		this.currentTime = newTime;
	}
	
	public void setTime(long newTime) {
		this.currentMs = newTime;
	}
}
