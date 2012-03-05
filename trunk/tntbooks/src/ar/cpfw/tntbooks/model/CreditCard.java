package ar.cpfw.tntbooks.model;

import static java.lang.String.valueOf;
import static org.apache.commons.lang.StringUtils.length;

import org.joda.time.DateTime;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ar.cpfw.tntbooks.model.exceptions.BusinessException;

public class CreditCard {

	private static final int YEAR_FIXED_SIZE = 4;
	private static final int NAME_LENGTH = 30;
	private static final int CREDIT_CARD_MINIMUM_LENGTH = 15;

	private String id;
	private String number;
	private String owner;
	private int expiryMonth;
	private int expiryYear;

	private transient DateTime expiryDate;
	private transient TimeProvider timeProvider;

	public CreditCard(String number, String owner, int month, int year,
			TimeProvider timeProvider) {

		this.number = number;
		this.owner = owner;
		this.expiryMonth = month;
		this.expiryYear = year;
		this.timeProvider = timeProvider;

		checkValid();

		expiryDate = checkValidAndCreate(this.expiryYear,
				this.expiryMonth);
	}

	public boolean hasExpired() {
		return new YearMonth(timeProvider.now()).isAfter(new YearMonth(
				expiryDate));
	}

	public String getExpireDateAsString() {
		return valueOf(expiryMonth) + valueOf(expiryYear);
	}

	private DateTime checkValidAndCreate(int year, int month) {
		checkCreditCardExpiryYear();

		DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM");
		try {
			return dateFormatter.parseDateTime(valueOf(year) + "-"
					+ valueOf(month));
		} catch (IllegalArgumentException e) {
			throw new BusinessException("Invalid date... ", e);
		}
	}

	private void checkCreditCardExpiryYear() {
		if (length(valueOf(expiryYear)) != YEAR_FIXED_SIZE) {
			throw new BusinessException("The year must have four digits... ");
		}
	}

	private void checkValid() {
		checkCreditCardNumber();
		checkCreditCardOwnerName();
	}

	private void checkCreditCardOwnerName() {
		if (this.owner.length() > NAME_LENGTH) {
			throw new BusinessException(
					"The owner's name cannot have more than " + NAME_LENGTH
							+ " characters ... ");
		}
	}

	private void checkCreditCardNumber() {
		if (this.number.length() < CREDIT_CARD_MINIMUM_LENGTH) {
			throw new BusinessException(
					"The credit card number must not have less than "
							+ CREDIT_CARD_MINIMUM_LENGTH + " digits ...");
		}
	}

	// just required by Hibernate
	public CreditCard() {

	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setId(String id) {
		this.id = id;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private String getId() {
		return this.id;
	}

	public String getNumber() {
		return number;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setNumber(String number) {
		this.number = number;
	}

	public String getOwner() {
		return owner;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setOwner(String owner) {
		this.owner = owner;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private int getExpiryMonth() {
		return expiryMonth;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private int getExpiryYear() {
		return expiryYear;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setExpiryMonth(int month) {
		this.expiryMonth = month;
	}

	// just required by Hibernate
	@SuppressWarnings("unused")
	private void setExpiryYear(int year) {
		this.expiryYear = year;
	}

	// When retrieving a CreditCard from DB, I have to inject this colaborator
	public void setTimeProvider(TimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CreditCard) {
			return ((CreditCard) obj).number.equals(this.number);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return number.hashCode();
	}

}
