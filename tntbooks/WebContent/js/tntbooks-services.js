var tntBooks = angular.module('tntBooks', ['ngResource']);

var host = 'localhost';
var action = '/tntbooks/webapi';
var uri = 'http://' + host + ':port' + action;
var urls = {
    validateCustomer:	uri + '/customer/:customerName.json',
    listPurchases:		uri + '/purchases.json',
    listBooks:				uri + '/books.json',   
    createCart:			uri + '/cart.json',
    addToCart:				uri + '/cart/:cartId.json',
    removeFromCart:		uri + '/cart/:cartId.json',
    listCart:				uri + '/cart/:cartId.json',
    checkout:				uri + '/cart/:cartId/checkout'
};

/*
 * tntbooks service facade
 * */
tntBooks.service('tntbooksFacade', function ($resource) {
    /*
     * params: customerName: to validate
     * 			onError:   a call back function to be called on error
     * 			onSuccess: a call back function to be called on sucess
     * returns  {customerId:Id} or {error:description}
     * */
    this.validateCustomer = function (customerName, onError, onSuccess) {
        $resource(urls.validateCustomer,
            {port:':8080', customerName:customerName}).get(function(result) {
            		return callbackReturn(result, onError, onSuccess);
            });
    };
    
    /*
     * List purchases
     * params: clientId: List purchases from this client
     * 			onError:   a call back function to be called on error
     * 			onSuccess: a call back function to be called on sucess
     * returns  {purchases:[]} or {error:description}
     * */
    this.listPurchases = function (clientId, onError, onSuccess) {
        $resource(urls.listPurchases,
            {port:':8080', clientId:clientId}).get(function(result) {
            		return callbackReturn(result, onError, onSuccess);
            });
    };
    /*
     * List all books
     * params:	onError:   a call back function to be called on error
     * 			onSuccess: a call back function to be called on sucess
     * returns  {catalog:[]} or {error:description}
     * */
    this.listBooks = function (onError, onSuccess) {
        $resource(urls.listBooks, {port:':8080'}).get(function(result) {
     			return callbackReturn(result, onError, onSuccess);
        });
    };
    /*
     * Create a shop cart
     * params:	clientId: create a shop cart for this customer
     * 			onError:   a call back function to be called on error
     * 			onSuccess: a call back function to be called on sucess
     * returns  {cartId: id} or {error: description}
     * */
    this.createCart = function (clientId, onError, onSuccess) {
   	 $resource(urls.createCart, {port:':8080', clientId:clientId}).get(
   			 function(result) {
          			return callbackReturn(result, onError, onSuccess);
      		 });
    };
    /*
     * Add a book to the shop cart
     * params:	cartId: a shop cart id
     * 			isbn: the isbn of the book to add to the shop cart
     * 			onError:   a call back function to be called on error
     * 			onSuccess: a call back function to be called on sucess
     * returns  {cartContent:[]} or {error:description}
     * */
    this.addToCart = function (cartId, isbn, onError, onSuccess) {
        $resource(urls.addToCart,
            {port:':8080', cartId:cartId, isbn:isbn, quantity:'1'}).save(function(result) {
            	return callbackReturn(result, onError, onSuccess);
            });
    };
    /*
     * Remove a book from the shop cart
     * params:	cartId: a shop cart id
     * 			isbn: the isbn of the book to remove from the shop cart
     * 			onError:   a call back function to be called on error
     * 			onSuccess: a call back function to be called on sucess
     * returns  {cartContent:[]} or {error:description}
     * */
    this.removeFromCart = function (cartId, isbn, onError, onSuccess) {
        $resource(urls.addToCart,
            {port:':8080', cartId:cartId, isbn:isbn}).remove(function(result) {
            	return callbackReturn(result, onError, onSuccess);
            });
    };
 
    /*
     * List cart content
     * params:	cartId: Id of the cart to get the content from 
     * 			onError:   a call back function to be called on error
     * 			onSuccess: a call back function to be called on sucess
     * returns {cartContent:[]} or {error:description}
     * */
    this.listCart = function (cartId, onError, onSuccess) {
   	 $resource(urls.listCart, {port:':8080', cartId:cartId}).get(
   			 function(result) {
   				 return callbackReturn(result, onError, onSuccess);
   			 });
    };
    
    /*
     * Checkout
     * params:	cartId: Id of the cart to get the content from 
     * 			clientId: the customer
     * 			onError:   a call back function to be called on error
     * 			onSuccess: a call back function to be called on sucess
     * returns {transactionId:[]} or {error:description}
     * */
    this.checkout = function(cartId, clientId, onError, onSuccess) {
   	 $resource(urls.checkout, {port:':8080', cartId:cartId, clientId:clientId}).save(
   			 //on 200 OK	
   			 function(result) {
   				 return callbackReturn(result, onError, onSuccess);
             }, 
             //on STATUS != 200
             function(error) {
            	 return callbackReturn(error.data, onError, onSuccess);
             });
    };
    
    /*
     * This private method decides if returns onError or onSuccess
     */
    callbackReturn = function(result, onError, onSuccess) {
    	if (result.error) {
    		return onError(result.error);
    	} else {
    		return onSuccess(result);
    	}
    };
    
});
