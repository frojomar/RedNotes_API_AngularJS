angular.module('rednotesApp')
.controller('headerCtrl', ['usersFactory', 'notesFactory', '$location', '$routeParams', 
		function(usersFactory, notesFactory, $location, $routeParams){
    var headerViewModel = this;
    headerViewModel.user={};
    headerViewModel.search="";
    
    headerViewModel.functions = {
		readUser : function() {
			usersFactory.getUserLogin()
				.then(function(response){
					headerViewModel.user = response
					console.log("Getting userlogin. Response: ", response);
    			}, function(response){
    				console.log("Error reading user data");
    			})
		},
		searchNotes: function(){
			if(headerViewModel.search!=""){
				$location.path('/searchnotes/'+headerViewModel.search);
			}
		},
		searchFriends: function(){
			if(headerViewModel.search!=""){
				$location.path('/searchfriends/'+headerViewModel.search);
			}
		}
    }
	headerViewModel.functions.readUser();
}])