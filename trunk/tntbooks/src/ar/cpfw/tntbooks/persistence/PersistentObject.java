package ar.cpfw.tntbooks.persistence;

public interface PersistentObject<T> {

	T findById(String guid);

	void store(T persistentObject);
	
	void delete(T persistentObject);
	
}
