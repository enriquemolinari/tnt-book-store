package ar.cpfw.tntbooks.model;

import org.joda.time.DateTime;

public interface TimeProvider {
	DateTime now();
	long nowAsMillisecs();
}
