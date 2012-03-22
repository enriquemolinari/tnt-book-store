package ar.cpfw.tntbooks.model;

import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.length;

import ar.cpfw.tntbooks.model.exceptions.BusinessException;

public class Customer {

	private String id;
	private String name;
	private String surname;
	private CreditCard creditCard;
	private Set<Ticket> purchases;
	private Cashier cashier;

	public Customer(String name, String surname, CreditCard aCreditCard,
			Cashier cashier) {
	
		checkFullName(name, surname);

		this.name = name;
		this.surname = surname;
		this.creditCard = aCreditCard;
		this.purchases = new HashSet<Ticket>();		
		this.cashier = cashier;
	}

	private void checkFullName(String name, String surname) {
		checkLength(name);
		checkLength(surname);
	}
	
	private void checkLength(String string) {
		if (length(string) == 0 || length(string) > 30) {
			throw new BusinessException("The " + string + "must have less than 30 characters");
		}		
	}
	
	public Set<Ticket> getPurchases() {
		return purchases;
	}

	public int numberOfPurchases() {
		return purchases.size();
	}

	// required as when getting objects from DB, I have to set this colaborator
	public void setCashier(Cashier cashier) {
		this.cashier = cashier;
	}

	public Ticket purchase(TntCart aCart) {
		Ticket ticket = cashier.checkout(aCart, creditCard);
		purchases.add(ticket);
		return ticket;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private String getId() {
		return id;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setId(String id) {
		this.id = id;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setName(String name) {
		this.name = name;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setSurname(String surname) {
		this.surname = surname;
	}

	public void setCreditCard(CreditCard aCreditCard) {
		this.creditCard = aCreditCard;
	}

	public void setPurchases(Set<Ticket> purchases) {
		this.purchases = purchases;
	}

	public Customer() {

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Customer) {
			return ((Customer) obj).id.equals(this.id);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
