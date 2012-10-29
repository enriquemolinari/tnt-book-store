//TODO: 
//commentar todo
//clean up? msgService?
angular.module('tnt', ['tntBooks']).config(['$routeProvider', function($routeProvider) {
			$routeProvider
			.when('/login', {templateUrl : 'login.html', controller : TntLoginCtrl})
			.when('/home/:customerName/:customerId', {templateUrl : 'home.html', controller : TntHomeCtrl})
			.when('/books', {templateUrl : 'books.html', controller : TntBooksCtrl})
			.when('/books/:cartId', {templateUrl : 'books.html', controller : TntBooksWithCartCtrl})
			.when('/purchases/:cartId', {templateUrl : 'purchases.html', controller : TntPurchasesCtrl})
			.otherwise({redirectTo : '/login'});}]);

function TntLoginCtrl($scope, tntbooksFacade, $location) {
	$scope.spinner = false;
	//validate userName
	$scope.validate = function(customerName) {
		$scope.spinner = true;
		tntbooksFacade.validateCustomer(customerName, 
			//onError
			function(error) {
				$scope.spinner = false;
				$scope.error = error;
			},
			//onSuccess
			function(result) {
				localStorage.setItem('customerId', result.customerId);
				$location.path('/home/' + $scope.customerName + '/' + result.customerId);
			});
	};
}

function TntHomeCtrl($scope, $routeParams) {
	$scope.customerId = $routeParams.customerId;
	$scope.customerName = $routeParams.customerName;
}

function TntBooksCtrl($scope, tntbooksFacade, $location) {	
	$scope.customerId = localStorage.getItem('customerId');

	turnOnSpinner($scope);
	
	tntbooksFacade.listBooks(
		//onError
		function(error) {
			displayError($scope, error);
		}, 
		//onSuccess
		function(result) {
			turnOffSpinner($scope);
			$scope.books = result;
		}
	);
		
	$scope.createCart = function() {
		tntbooksFacade.createCart($scope.customerId, 
			//onError
			function(error) {
				displayError($scope, error);
			}, 
			//onSuccess
			function(result) {
				$location.path('/books/' + result.cartId);
			});
	};
}

function TntBooksWithCartCtrl($scope, $location, tntbooksFacade, $routeParams) {	
	$scope.customerId = localStorage.getItem('customerId');
	$scope.cartId = $routeParams.cartId;

	//show books
	tntbooksFacade.listBooks(
		//onError
		function(error) {
			displayError($scope, error);
		}, 
		//onSuccess
		function(result) {
			$scope.books = result;
		}
	);
	
	$scope.addToCart = function(isbn) {
		tntbooksFacade.addToCart($scope.cartId, isbn, 
		//onError
		function(error) {
			displayError($scope, error);
		},
		//onSuccess
		function(result) {
			showCartContent($scope, result);
		});
	};

	$scope.removeFromCart = function(isbn) {
		tntbooksFacade.removeFromCart($scope.cartId, isbn, 
		  //onError
		  function(error) {
			 displayError($scope, error);
		  },
		  //onSuccess
		  function(result) {
			 showCartContent($scope, result);
		});
	};
	
	$scope.listCart = function() {
		tntbooksFacade.listCart($scope.cartId, 
			//onError
			function(error) {
				displayError($scope, error);
			},
			//onSuccess
			function(result) {
				showCartContent($scope, result);
		});
	};

	$scope.checkout = function() {
		tntbooksFacade.checkout($scope.cartId, $scope.customerId, 
			//onError
			function(error) {
				displayError($scope, error);
			},
			//onSuccess
			function(result) {
				alert("The checkout was successfull");
				$scope.cartId = null;
				$location.path('/purchases/');
			});
	};
	
	//list cart content
	$scope.listCart();

}

function TntPurchasesCtrl($scope, tntbooksFacade, $routeParams) {
	turnOnSpinner($scope);
	$scope.cartId = $routeParams.cartId;
	$scope.customerId = localStorage.getItem('customerId');
	
	tntbooksFacade.listPurchases($scope.customerId,
		//onError
		function(error) {
			displayError($scope, error);
		}, 
		//onSuccess
		function(result) {
			$scope.purchases = result;
			turnOffSpinner($scope);
		}
	);
}

function showCartContent($scope, result) {
	$scope.cartContent = result.cartContent;
	$scope.total = 0;
	for (var i = 0; i < result.cartContent.length; i++) {
		$scope.total += result.cartContent[i].book.price * result.cartContent[i].quantity;
	}
}

function displayError($scope, error) {
	turnOffSpinner($scope);
	$scope.error = error;
}
function turnOnSpinner($scope) {
	$scope.spinner = true;
}
function turnOffSpinner($scope) {
	$scope.spinner = false;
}
