package ar.cpfw.tntbooks.persistence;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import ar.cpfw.tntbooks.model.Cashier;
import ar.cpfw.tntbooks.model.CreditCard;
import ar.cpfw.tntbooks.model.Customer;
import ar.cpfw.tntbooks.model.TimeProvider;

/**
 * This is to inject transient dependencies (non persisted colaborators) to
 * persistent objects
 */
@SuppressWarnings("serial")
public class HibernateDIInterceptor extends EmptyInterceptor implements ApplicationContextAware {

	private Cashier cashier;
	private TimeProvider timeProvider;
	private ApplicationContext springContext;
	
	public HibernateDIInterceptor() {
		
	}
	
	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		
		if (this.cashier == null) {
			this.cashier = (Cashier)this.springContext.getBean("cashier");
		}
		
		if (entity instanceof Customer) {
			((Customer) entity).setCashier(cashier);
			return true;
		}

		if (entity instanceof CreditCard) {
			((CreditCard) entity).setTimeProvider(timeProvider);
			return true;
		}

		return false;
	}

	public void setTimeProvider(TimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.springContext = applicationContext;
	}
}
