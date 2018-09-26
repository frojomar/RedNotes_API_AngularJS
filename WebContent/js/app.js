angular.module('rednotesApp', ['ngRoute', 'angularCSS'])
.config(function($routeProvider){
	$routeProvider
    	.when("/", {
    		controller: "notesCtrl",
    		controllerAs: "notesVM",
    		templateUrl: "Notes.html",
    		css:'../css/notes.css',
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
    	.when("/reminders", {
			controller: "remindersCtrl",
			controllerAs: "remindersVM",
			templateUrl: "Reminders.html",
    		css:'../css/friends.css'
		})
		.when("/perfil", {
			controller: "perfilCtrl",
			controllerAs: "perfilVM",
			templateUrl: "Perfil.html",
    		css:'../css/perfil.css'
		})
		.when("/editperfil", {
			controller: "perfilCtrl",
			controllerAs: "perfilVM",
			templateUrl: "EditPerfil.html",
    		css:'../css/perfil.css'
		})		
		.when("/editimage", {
			controller: "perfilCtrl",
			controllerAs: "perfilVM",
			templateUrl: "SelectImagePerfil.html",
    		css:'../css/perfil.css'
		})
		.when("/deleteAccount", {
			controller: "perfilCtrl",
			controllerAs: "perfilVM",
			templateUrl: "DeleteAccount.html",
    		css:['../css/reglogin.css', '../css/delete.css']
		})
		.when("/editnote/:ID", {
			controller: "noteCtrl",
			controllerAs: "noteVM",
			templateUrl: "EditNote.html",
    		css:'../css/note.css'
		})
		.when("/deletenote/:ID", {
			controller: "noteCtrl",
			controllerAs: "noteVM",
			templateUrl: "DeleteNote.html",
    		css:['../css/reglogin.css', '../css/delete.css']
		})
		.when("/addnote", {
			controller: "noteCtrl",
			controllerAs: "noteVM",
			templateUrl: "EditNote.html",
    		css:'../css/note.css'
		})
		.when("/list/:ID", {
			controller: "listCtrl",
			controllerAs: "listVM",
			templateUrl: "List.html",
    		css:'../css/note.css'
		})
		.when("/editlist/:ID", {
			controller: "listCtrl",
			controllerAs: "listVM",
			templateUrl: "EditList.html",
    		css:'../css/note.css'
		})
		.when("/deletelist/:ID", {
			controller: "noteCtrl",
			controllerAs: "noteVM",
			templateUrl: "DeleteNote.html",
    		css:['../css/reglogin.css', '../css/delete.css']
		})
		.when("/addlist", {
			controller: "listCtrl",
			controllerAs: "listVM",
			templateUrl: "EditList.html",
    		css:'../css/note.css'
		})
		.when("/addreminder", {
			controller: "remindersCtrl",
			controllerAs: "remindersVM",
			templateUrl: "AddReminder.html",
    		css:'../css/friends.css'
		})
		.when("/searchnotes/:SEARCH", {
			controller: "notesCtrl",
			controllerAs: "notesVM",
			templateUrl: "SearchNote.html",
    		css:'../css/notes.css'
		})
		.when("/searchfriends/:SEARCH", {
			controller: "friendsCtrl",
			controllerAs: "friendsVM",
			templateUrl: "Friends.html",
    		css:'../css/friends.css'
		})
		.when("/friends", {
			controller: "friendsCtrl",
			controllerAs: "friendsVM",
			templateUrl: "Friends.html",
    		css:'../css/friends.css'
		})
		.when("/friends/:ID", {
			controller: "friendsCtrl",
			controllerAs: "friendsVM",
			templateUrl: "Friends.html",
    		css:'../css/friends.css'
		})
		.when("/addfriend", {
			controller: "newFriendsCtrl",
			controllerAs: "newFriendsVM",
			templateUrl: "SearchFriends.html",
    		css:'../css/friends.css'
		})
		.when("/deletefriend/:ID", {
			controller: "friendsCtrl",
			controllerAs: "friendsVM",
			templateUrl: "DeleteFriend.html",
    		css:['../css/reglogin.css', '../css/delete.css']
		})
		.when("/share/:ID", {
			controller: "friendsCtrl",
			controllerAs: "friendsVM",
			templateUrl: "ShareNote.html",
    		css:'../css/friends.css'
		})
		.when("/petitions", {
			controller: "newFriendsCtrl",
			controllerAs: "newFriendsVM",
			templateUrl: "Petitions.html",
    		css:'../css/friends.css'
		})
		.when("/:ID", {
			controller: "noteCtrl",
			controllerAs: "noteVM",
			templateUrl: "Note.html",
    		css:'../css/note.css'
		})
		;
	
})
