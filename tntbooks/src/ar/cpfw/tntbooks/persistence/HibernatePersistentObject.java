package ar.cpfw.tntbooks.persistence;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;

/**
 * @author Enrique Molinari
 */
public abstract class HibernatePersistentObject<T> implements PersistentObject<T> {

	private Class<T> persistentClass;
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public HibernatePersistentObject() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session getSession() {
			return sessionFactory.getCurrentSession();
	}

	public Class<T> getPersistentClass() {
		return this.persistentClass;
	}

	public void flush() {
		getSession().flush();
	}
	
	@Override
	public void delete(T persistentObject) {
		getSession().delete(persistentObject);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T findById(String guid) {
		return (T) getSession().get(getPersistentClass(), guid);
	}

	@Override
	public void store(T persistentObject) {
		getSession().saveOrUpdate(persistentObject);
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();
	}
}
