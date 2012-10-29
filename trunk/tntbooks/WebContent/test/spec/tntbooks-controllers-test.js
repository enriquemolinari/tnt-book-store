describe("tnt book test", function() {

	var facade, $httpBackend;

	//init the thtBooks module
	beforeEach(module('tntBooks'));

	//injecting $httpBackend mock and my tntBooksFacade service
	beforeEach(angular.mock.inject(function(_$httpBackend_, tntbooksFacade) {
		facade = tntbooksFacade;
		$httpBackend = _$httpBackend_;
	}));
	
	it('I have entered a valid user, then I get redirected to the home page',	function() {
		//For any *.json request, just repond with ...
		mocking_get_json_call_respond_sucess();
		
		//my fake scope
		var scope = {};
		//my fake $location
		var location = {
			path : function(urlPath) {
				// do nothing
			}
		};
		spyOn(location, 'path');
		//Initialize the controller call TntLoginCtrl#validate
		new TntLoginCtrl(scope, facade, location);
		//call the valida that triggers the call to the service
		scope.validate('Angus');
		//this makes the fake ajax call to run
		$httpBackend.flush();
		//if the $location.path was called, then it was a sucess validation
		expect(location.path).toHaveBeenCalled();
	});
	
	it('I have entered a non valid user, then I stay on the same page with an error msg',	function() {
		//For any *.json request, just repond with ...
		mocking_get_json_call_respond_error();

		//my fake scope
		var scope = {};
		//my fake $location
		var location = {
			path : function(urlPath) {
				// do nothing
			}
		};
		spyOn(location, 'path');
		//Initialize the controller call TntLoginCtrl#validate
		new TntLoginCtrl(scope, facade, location);
		//call the valida that triggers the call to the service
		scope.validate('aNonValidUser');
		//This makes the fake ajax call to run
		$httpBackend.flush();
		//I should have $scope.error defined
		expect(scope.error).toBeDefined();
		//the $location.path should not be called
		expect(location.path).not.toHaveBeenCalled();
	});

	it('I have arrived to the home page, then it contains in the scope the params from the query string', function() {
		//my fake scope
		var scope = {};
		//my fake $routeParams
		var routeParams = {
			customerId : 'theId',
			customerName: 'theName'
		};
		
		new TntHomeCtrl(scope, routeParams);
		
		//route params should be on the scope
		expect(scope.customerId).toEqual('theId');
		expect(scope.customerName).toEqual('theName');
	});
	
	it('I go to the books catalog, then I get listed all the books from the store', function() {
		//For any books.json request, just repond with ...
		mocking_get_booksJson_call_respond_with_fake_books_catalog();

		//my fake scope
		var scope = {};
		//my fake $location
		var location = {
			path : function(urlPath) {
				// do nothing
			}
		};
		new TntBooksCtrl(scope, facade, location);
		//after calling the Ctrl, the spineer must be true
		expect(scope.spinner).toBeTruthy();
		//here the books are not yet defined
		expect(scope.books).toBeUndefined();
		//This makes the fake ajax call to run
		$httpBackend.flush();
		//here I have the catalog
		expect(scope.books.catalog).toBeDefined();
		expect(scope.books.catalog[0].isbn).toEqual('AR123456');
		//and the spineer must be false
		expect(scope.spinner).toBeFalsy();		
	});	
	
	it('I am on the books catalog, then I can create a shopping cart', function() {

		mocking_get_booksJson_call_respond_with_fake_books_catalog();

		//my fake scope
		var scope = {};
		//my fake $location
		var location = {
			path : function(urlPath) {
				// do nothing
			}
		};
		//Inside this Ctrl, there is call using the $http service
		new TntBooksCtrl(scope, facade, location);
		//The call to the 2 flush pending request...
		$httpBackend.flush();
		//For any *cart.json request, just repond with ...
		$httpBackend.expectGET(new RegExp('cart.json', 'g')).respond({
			cartId : 'aCartId'
		});		
		//make the call to create a cart
		scope.createCart();
		//spying on location.path
		spyOn(location, 'path');
		//flush pending request...
		$httpBackend.flush();
		//the $location.path must be called
		expect(location.path).toHaveBeenCalled();
	});	

	it('I have two books on the cart, then the total amount is correct', function() {
		//For any *books.json request, just repond with ...
		mocking_get_booksJson_call_respond_with_fake_books_catalog();
		
		//my fake scope
		var scope = {};
		//my fake $location
		var location = {
			path : function(urlPath) {
				// do nothing
			}
		};
		//my fake routeParams
		var routeParams = {
				cartId: 'anId'
		};
		//when requesting for the content of the cart, respond with...
		mocking_get_cartContentJson_call_respond_with_fake_cartContent();

		new TntBooksWithCartCtrl(scope, location, facade, routeParams);
		//flush pending request...
		$httpBackend.flush();
		//verify
		expect(scope.cartContent).toBeDefined();
		expect(scope.total).toEqual(126);
		expect(scope.cartContent[0].book.isbn).toEqual('AR1');
		
	});	
	it('I add a book to the cart, then, the total amount is calculated again', function() {
		//For any *books.json request, just repond with ...
		mocking_get_booksJson_call_respond_with_fake_books_catalog();
		
		//my fake scope
		var scope = {};
		//my fake $location
		var location = {
			path : function(urlPath) {
				// do nothing
			}
		};
		//my fake routeParams
		var routeParams = {
				cartId: 'anId'
		};

		//when requesting for the content of the cart, respond with...
		mocking_get_cartContentJson_call_respond_with_fake_cartContent();

		new TntBooksWithCartCtrl(scope, location, facade, routeParams);
		//flush pending request...
		$httpBackend.flush();
		//when requesting for add a book to the cart, respond with...
		$httpBackend.expectPOST(new RegExp('anId.json', 'g')).respond(
			{cartContent:[{book:{isbn:'AR1',price:'62.5',title:'AC/DC at River Plate: chat with a fan'},quantity:1},
			              {book:{isbn:'AR2',price:'63.5',title:'Let there be Rock'},quantity:1},
			              {book:{isbn:'AR3',price:'70',title:'The History of AC/DC'},quantity:1}]});				
		//add a book to the cart
		scope.addToCart('AR3');
		//flush pending request...
		$httpBackend.flush();
		//verify
		expect(scope.cartContent).toBeDefined();
		expect(scope.total).toEqual(196);
		expect(scope.cartContent[0].book.isbn).toEqual('AR1');
	});	
	it('I remove a book from the cart, then, the total amount is calculated again', function() {
		//For any *books.json request, just repond with ...
		mocking_get_booksJson_call_respond_with_fake_books_catalog();
		//my fake scope
		var scope = {};
		//my fake $location
		var location = {
			path : function(urlPath) {
				// do nothing
			}
		};
		//my fake routeParams
		var routeParams = {
				cartId: 'anId'
		};

		//when requesting for the content of the cart, respond with...
		mocking_get_cartContentJson_call_respond_with_fake_cartContent();

		new TntBooksWithCartCtrl(scope, location, facade, routeParams);
		//flush pending request...
		$httpBackend.flush();
		//when requesting for add a book to the cart, respond with...
		$httpBackend.expectDELETE(new RegExp('anId.json', 'g')).respond(
			{cartContent:[{book:{isbn:'AR2',price:'63.5',title:'Let there be Rock'},quantity:1},
			              {book:{isbn:'AR3',price:'70',title:'The History of AC/DC'},quantity:1}]});				
		//add a book to the cart
		scope.removeFromCart('AR1');
		//flush pending request...
		$httpBackend.flush();
		//verify
		expect(scope.cartContent).toBeDefined();
		expect(scope.total).toEqual(133.5);
	});	

	it('I checkout successfully, they I get redirected to my purchases', function() {
		//For any *books.json request, just repond with ...
		mocking_get_booksJson_call_respond_with_fake_books_catalog();
		
		//my fake scope
		var scope = {
				customerId: 'aCustomerId'
		};
		//my fake $location
		var location = {
			path : function(urlPath) {
				// do nothing
			}
		};
		//my fake routeParams
		var routeParams = {
				cartId: 'anId'
		};
		//when requesting for the content of the cart, respond with...
		mocking_get_cartContentJson_call_respond_with_fake_cartContent();

		new TntBooksWithCartCtrl(scope, location, facade, routeParams);
		//flush pending request...
		$httpBackend.flush();
		//when requesting for checkout, respond with...
		$httpBackend.expectPOST(new RegExp('checkout', 'g')).respond({transactionId:'andId'});		
		//spy on the alert to see if the successfull message is displayed
		spyOn(window,"alert");
		spyOn(location,"path");
		//do the checkout ...
		scope.checkout();
		//flush pending request...
		$httpBackend.flush();
		
		expect(location.path).toHaveBeenCalled();
		expect(window.alert).toHaveBeenCalled();
		expect(scope.cartId).toEqual(null);
	});	

	it('I go the my purchases, then I get purchases populated', function() {
		//my fake scope
		var scope = {
				customerId: 'aCustomerId'
		};
		//my fake routeParams
		var routeParams = {
				cartId: 'anId'
		};
		//when requesting for purchases, respond with...
		$httpBackend.expectGET(new RegExp('purchases.json', 'g')).respond(
				{purchases:[{ticketItems:
							[{isbn:'AR123456', price:'65.5', quantity:1, title:'AC/DC at River Plate: chat with a fan'}]}], 
							salesDate:'2012-10-28T17:35:36.297-03:00', totalAmount:'65.5'}
		);		

		new TntPurchasesCtrl(scope, facade, routeParams);
		//verify		
		expect(scope.spinner).toBeTruthy();
		//flush pending request...
		$httpBackend.flush();
		//verify
		expect(scope.spinner).toBeFalsy();
		expect(scope.purchases).toBeDefined();
		expect(scope.purchases.purchases[0].ticketItems[0].isbn).toEqual('AR123456');
		expect(scope.purchases.totalAmount).toEqual('65.5');
	});	

	function mocking_get_json_call_respond_sucess() {
		$httpBackend.expectGET(new RegExp('.json', 'g')).respond({
			customerId : 'thisIsAndId'
		});
	}
	
	function mocking_get_json_call_respond_error() {
		$httpBackend.expectGET(new RegExp('.json', 'g')).respond({
			error : 'you have entered an invalid user...'
		});
	}
	
	function mocking_get_booksJson_call_respond_with_fake_books_catalog() {
		$httpBackend.expectGET(new RegExp('books.json', 'g')).respond({
			catalog : [{isbn:'AR123456',price:'65.5',title:'AC/DC at River Plate: chat with a fan'}]
		});
	}

	function mocking_get_cartContentJson_call_respond_with_fake_cartContent() {
		$httpBackend.expectGET(new RegExp('anId.json', 'g')).respond(
			{cartContent:[{book:{isbn:'AR1',price:'62.5',title:'AC/DC at River Plate: chat with a fan'},quantity:1},
			              {book:{isbn:'AR2',price:'63.5',title:'Let there be Rock'},quantity:1}]});		
	}
	
});
