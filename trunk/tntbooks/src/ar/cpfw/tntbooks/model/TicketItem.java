package ar.cpfw.tntbooks.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class TicketItem {

	private Book book;
	private Float price;
	private Integer quantity;

	public TicketItem(Book book, Integer quantity) {
		this.book = book;
		// I need the price here for persistence. Not very beautiful
		this.price = book.getPrice();
		this.quantity = quantity;
	}

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
		return this.getIsbn() + "|" + this.getPrice() + "|"
				+ this.getQuantity();
	}
}
