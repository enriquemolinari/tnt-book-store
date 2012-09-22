package ar.cpfw.tntbooks.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

/**
 * @author Enrique Molinari
 */
public class Ticket {

	private String id;
	private float amount;
	private Status status;
	private Set<TicketItem> items;
	private DateTime dateTime;


	public static enum Status {
		PAID, UNPAID
	}

	public int itemsSize() {
		return items.size();
	}

	public Ticket(float totalAmount, Map<Book, Integer> books, Status status) {
		this.amount = totalAmount;
		this.status = status;
		this.items = new HashSet<TicketItem>();
		this.dateTime = DateTime.now();

		for (Map.Entry<Book, Integer> entry : books.entrySet()) {
			items.add(new TicketItem(entry.getKey(), entry.getValue()));
		}
	}

	
	@JsonIgnore
	public String transactionId() {
		return id;
	}
	
	// just required by Hibernate
	@SuppressWarnings("unused")
	private String getId() {
		return id;
	}

	@JsonProperty("salesDate")
	public String getSalesDate() {
		return this.dateTime.toString();
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private Date getDateTime() {
		return this.dateTime.toDate();
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setDateTime(Date date) {
		this.dateTime = new DateTime(date);
	}

	@JsonProperty("totalAmount")
	public float getAmount() {
		return amount;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setItems(Set<TicketItem> items) {
		this.items = items;
	}

	@JsonProperty("ticketItems")
	public Set<TicketItem> getItems() {
		return this.items;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private Ticket() {
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setId(String id) {
		this.id = id;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setAmount(float totalAmount) {
		this.amount = totalAmount;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setStatus(Status status) {
		this.status = status;
	}

	@JsonIgnore
	public Status getStatus() {
		return this.status;
	}

	@JsonIgnore
	@Override
	public String toString() {
		return "date: " + this.getSalesDate() + " total: " + this.getAmount() + "|"
				+ this.getItems();
	}
}
