package ar.cpfw.tntbooks.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.cpfw.tntbooks.application.ApplicationFacade;
import ar.cpfw.tntbooks.application.IApplicationFacade;
import ar.cpfw.tntbooks.model.Book;
import ar.cpfw.tntbooks.model.BookCatalog;
import ar.cpfw.tntbooks.model.Customer;
import ar.cpfw.tntbooks.model.CustomerAgenda;
import ar.cpfw.tntbooks.model.Ticket;
import ar.cpfw.tntbooks.model.TimeProvider;
import ar.cpfw.tntbooks.model.TntBookStore;
import ar.cpfw.tntbooks.model.TntCart;
import ar.cpfw.tntbooks.model.exceptions.BusinessException;

/**
 * @author Enrique Molinari
 */
@Controller
public class TntController {

	private Map<String, TntCart> listOfCreatedCarts = new ConcurrentHashMap<String, TntCart>();
	private CustomerAgenda agendaOfCustomers;
	private IApplicationFacade appFacade;
	private BookCatalog catalogOfBooks;
	private TntBookStore tntBookStore;
	private TimeProvider timeProvider;

	@Autowired
	public TntController(CustomerAgenda agendaOfCustomer,
			BookCatalog catalogOfBook, TntBookStore bookStore,
			TimeProvider timeProvider, IApplicationFacade appFacade) {
		this.agendaOfCustomers = agendaOfCustomer;
		this.catalogOfBooks = catalogOfBook;
		this.tntBookStore = bookStore;
		this.timeProvider = timeProvider;
		this.appFacade = appFacade; 

		new Timer("carts_clean_up").schedule(new CartsCleanUp(
				listOfCreatedCarts), 10, 3600000);
	}

	@RequestMapping(value = "/customer/{customerName}", method = RequestMethod.GET)
	public ModelAndView validateCustomer(
			@PathVariable("customerName") String customerName) {
		// it is necesary to perform some validation on the customerName param
		// before send it to the agendaOfCustomer.
		List<Customer> customers = agendaOfCustomers
				.customersByName(customerName);
		if (customers.size() < 1) {
			throw new IllegalArgumentException(
					"There is no customer with name: " + customerName);
		}
		return new ModelAndView().addObject("customerId", customers.get(0)
				.getId());
	}

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public ModelAndView listBooks() {
		return new ModelAndView().addObject("catalog", this.catalogOfBooks
				.allBooks());
	}

	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public ModelAndView createCart(@RequestParam("clientId") String clientId) {

		if (agendaOfCustomers.exists(clientId)) {
			String cartId = UUID.randomUUID().toString();

			TntCart cart = new TntCart(timeProvider);
			cart.setValidator(tntBookStore);

			listOfCreatedCarts.put(cartId, cart);

			return new ModelAndView().addObject("cartId", cartId);
		}

		throw new IllegalArgumentException("The Id: " + clientId
				+ " is not a valid customer.");
	}

	@RequestMapping(value = "/cart/{cartId}", method = RequestMethod.GET)
	public ModelAndView listCart(@PathVariable("cartId") String cartId) {

		TntCart cart = getCart(cartId);

		return new ModelAndView().addObject("cartContent", buildCartContent(cart.getBooks()));
	}

	// I have to add this to make the JsonMapper happy, as it does not support
	// to have business objects as key in Maps.
	private List<BookAndQuantity> buildCartContent(Map<Book, Integer> cartContent) {
		List<BookAndQuantity> content = new ArrayList<BookAndQuantity>();
		for (Book book : cartContent.keySet()) {
			content.add(new BookAndQuantity(book, cartContent.get(book)));
		}
		return content;
	}
	
	private static class BookAndQuantity {
		private Book book;
		private Integer quantity;

		private BookAndQuantity(Book book, Integer quantity) {
			this.book = book;
			this.quantity = quantity;
		}

		@SuppressWarnings("unused")
		//used by JsonMapper
		public Book getBook() {
			return book;
		}
		
		@SuppressWarnings("unused")
		//used by JsonMapper		
		public Integer getQuantity() {
			return quantity;
		}
	}

	@RequestMapping(value = "/cart/{cartId}", method = RequestMethod.POST)
	public ModelAndView addToCart(@PathVariable("cartId") String cartId,
			@RequestParam("isbn") String isbn,
			@RequestParam("quantity") int quantity) {

		TntCart cart = getCart(cartId);

		cart.add(catalogOfBooks.bookByIsbn(isbn), quantity);

		return new ModelAndView().addObject("cartContent", buildCartContent(cart.getBooks()));
	}

	@RequestMapping(value = "/cart/{cartId}", method = RequestMethod.DELETE)
	public ModelAndView removeFromCart(@PathVariable("cartId") String cartId,
			@RequestParam("isbn") String isbn) {

		TntCart cart = getCart(cartId);

		cart.remove(catalogOfBooks.bookByIsbn(isbn));

		return new ModelAndView().addObject("cartContent", buildCartContent(cart.getBooks()));
	}
	
	@RequestMapping(value = "/purchases", method = RequestMethod.GET)
	public ModelAndView listPurchases(
			@RequestParam("clientId") String customerId) {

		return new ModelAndView().addObject("purchases", appFacade
				.purchases(customerId));
	}

	@RequestMapping(value = "/cart/{cartId}/checkout", method = RequestMethod.POST)
	public ModelAndView checkout(@PathVariable("cartId") String cartId,
			@RequestParam("clientId") String clientId) {

		TntCart cart = getCart(cartId);

		Ticket ticket = appFacade.purchase(clientId, cart);

		listOfCreatedCarts.remove(cartId);

		return new ModelAndView().addObject("transactionId", ticket
				.transactionId());
	}

	private TntCart getCart(String cartId) {
		TntCart cart = listOfCreatedCarts.get(cartId);
		if (cart == null) {
			throw new IllegalArgumentException("The cart Id does not exists");
		}
		return cart;
	}

	@ExceptionHandler(BusinessException.class)
	public ModelAndView handleBusinessException(BusinessException exception) {
		return errorModel(exception.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ModelAndView handleIllegalArgumentException(
			IllegalArgumentException exception) {
		return errorModel(exception.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception exception) {
		return errorModel("Sorry... something bad has ocurred.");
	}

	private ModelAndView errorModel(String message) {
		return new ModelAndView().addObject("error", message);
	}
}
