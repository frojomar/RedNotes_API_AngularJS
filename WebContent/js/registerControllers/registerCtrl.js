angular.module('registerApp')
.controller('registerCtrl', ['registerFactory', '$location', function(registerFactory, $location){
	var vm = this;
	vm.user = {};
	vm.countries = [];
	vm.repeatedPassword;
	vm.exchangeTypeFirst;
	vm.exchangeTypeSecond;
	vm.birthDateFormated;
	vm.validationError;
	
	
	vm.message="";
	
	vm.username="";
	vm.email="";
	vm.password="";
	vm.passwordRepeat="";
	
	vm.functions = {		
		registerNewUser : function(){
			vm.message="";
			
			if(vm.username==""){
				alert("Username can't be empty");
			}
			else{
				if(vm.email==""){
					alert("Email can't be empty");
				}
				else{
					if(vm.password == vm.passwordRepeat){
					var user={
							"date": (new Date()).getTime(),
							"email": vm.email,
							"username": vm.username,
							"password": vm.password	
					}
					registerFactory.addUser(user)
						.then(function(response){
							console.log("Usuario registrado con exito", response.status);
							$location.path('/registercomplete');
						}, function(response){
							console.log("error registrando el user", response);
							vm.message = response.data.userMessage;
						})
					}
					else{
						vm.message = "Passwords do not match";
					}
				}
			}
			vm.password="";
			vm.passwordRepeat="";
		}
	}
}])
	