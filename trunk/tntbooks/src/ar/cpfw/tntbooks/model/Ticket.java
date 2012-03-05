package ar.cpfw.tntbooks.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

public class Ticket {

	private String id;
	private float amount;
	private Status status;
	private Set<TicketItem> items;
	private DateTime dateTime;

	public static class TicketItem implements Serializable {

		private static final long serialVersionUID = -148341787929241110L;

		private Book book;
		private Float price;
		private Integer quantity;

		// just required by Hibernate
		@SuppressWarnings("unused")
		private TicketItem() {

		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}

			if (!(o instanceof TicketItem)) {
				return false;
			}

			TicketItem item = (TicketItem) o;
			return this.book.equals(item.book) && this.price.equals(item.price)
					&& this.quantity.equals(item.quantity);
		}

		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + book.hashCode();
			result = 31 * result + Float.floatToIntBits(price);
			result = 31 * result + quantity;
			return result;
		}

		public TicketItem(Book book, Integer quantity) {
			this.book = book;
			this.price = book.getPrice();
			this.quantity = quantity;
		}

		@JsonIgnore
		// just required by Hibernate
		@SuppressWarnings("unused")
		private Book getBook() {
			return book;
		}

		@JsonIgnore
		public String getIsbn() {
			return book.getIsbn();
		}

		// just required by Hibernate
		@SuppressWarnings("unused")
		private void setBook(Book book) {
			this.book = book;
		}

		@JsonIgnore
		public Float getPrice() {
			return price;
		}

		// just required by Hibernate
		@SuppressWarnings("unused")
		private void setPrice(Float price) {
			this.price = price;
		}

		@JsonIgnore
		public Integer getQuantity() {
			return quantity;
		}

		// just required by Hibernate
		@SuppressWarnings("unused")
		private void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}

		@JsonProperty("title")
		public String getTitle() {
			return book.getTitle();
		}

		@Override
		public String toString() {
			return this.getIsbn() + "|" + this.getPrice() + "|" + this.getQuantity();
		}
	}

	public static enum Status {
		PAID, UNPAID
	}

	public int itemsSize() {
		return items.size();
	}

	public Ticket(float totalAmount, Map<Book, Integer> books, Status status) {
		this.amount = totalAmount;
		this.status = status;
		this.items = new HashSet<Ticket.TicketItem>();
		this.dateTime = DateTime.now();

		for (Map.Entry<Book, Integer> entry : books.entrySet()) {
			items.add(new TicketItem(entry.getKey(), entry.getValue()));
		}
	}

	@JsonIgnore
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

	@JsonIgnore
	public float getAmount() {
		return amount;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setItems(Set<TicketItem> items) {
		this.items = items;
	}

	@JsonIgnore
	public Set<TicketItem> getItems() {
		return this.items;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private Ticket() {
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

	@JsonProperty("ticket")
	@Override
	public String toString() {
		return "date: " + this.getSalesDate() + " total: " + this.getAmount() + "|"
				+ this.getItems();
	}
}
