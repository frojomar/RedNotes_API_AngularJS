angular.module('registerApp', ['ngRoute'])
.config(function($routeProvider){
	$routeProvider
    	.when("/", {
    		controller: "registerCtrl",
    		controllerAs: "registerVM",
    		templateUrl: "Register.html",
    		resolve: {
    			// produce 500 miliseconds (0,5 seconds) of delay that should be enough to allow the server
    			//does any requested update before reading the orders.
    			// Extracted from script.js used as example on https://docs.angularjs.org/api/ngRoute/service/$route
    			delay: function($q, $timeout) {
    			var delay = $q.defer();
    			$timeout(delay.resolve, 500);
    			return delay.promise;
    			}
    		}
    	})
    	.when("/registercomplete", {
			controller: "registerCtrl",
			controllerAs: "registerVM",
			templateUrl: "RegisterComplete.html"
		})
		;
	
})
