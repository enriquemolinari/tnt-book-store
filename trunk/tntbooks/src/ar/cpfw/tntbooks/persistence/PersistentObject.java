package ar.cpfw.tntbooks.persistence;

/**
 * @author Enrique Molinari
 */
public interface PersistentObject<T> {

	T findById(String guid);

	void store(T persistentObject);
	
	void delete(T persistentObject);
	
}
