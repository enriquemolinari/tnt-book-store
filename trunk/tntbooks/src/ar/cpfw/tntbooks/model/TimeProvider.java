package ar.cpfw.tntbooks.model;

import org.joda.time.DateTime;

/**
 * @author Enrique Molinari
 */
public interface TimeProvider {
	DateTime now();
	long nowAsMillisecs();
}
