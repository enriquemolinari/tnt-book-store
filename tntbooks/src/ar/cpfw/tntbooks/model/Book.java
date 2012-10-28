package ar.cpfw.tntbooks.model;

import static org.apache.commons.lang.StringUtils.length;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import ar.cpfw.tntbooks.model.exceptions.BusinessException;

/**
 * @author Enrique Molinari
 */
public class Book {

	private String id;
	private String isbn;
	private Float price;
	private String title;
	
	public Book(String isbn, Float price, String title) {
		checkTitle(title);
		checkIsbn(isbn);
		checkPrice(price);
		
		this.isbn = isbn;
		this.price = price;
		this.title = title;
	}

	public Book() {
		
	}

	private void checkPrice(Float price) {
		if (price  == null) {
			throw new BusinessException("The price must not be null");
		}
	}

	private void checkIsbn(String isbn) {
		if (length(isbn) == 0 || length(isbn) > 15) {
			throw new BusinessException("The ISBN must have less than 15 characters");
		}		
	}

	private void checkTitle(String string) {
		if (length(string) == 0 || length(string) > 60) {
			throw new BusinessException("The " + string + "must have less than 60 characters");
		}		
	}
	
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setTitle(String title) {
		this.title = title;
	}
	
	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setPrice(Float price) {
		this.price = price;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setId(String id) {
		this.id = id;
	}
	
	// just required by Hibernate
	@SuppressWarnings("unused")
	private String getId() {
		return id;
	}
	
	@JsonProperty("isbn")
	public String getIsbn() {
		return isbn;
	}
	
	@JsonProperty("price")
	public Float getPrice() {
		return price;
	}
	
	@Override
	public boolean equals(Object libro) {
		if (libro instanceof Book) {
			return this.isbn.equals(((Book)libro).isbn);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.isbn.hashCode();
	}

	@Override
	@JsonIgnore
	public String toString() {
		return this.isbn; 
	}
}
