angular.module('rednotesApp')
.factory("friendsFactory", ['$http', 'notesFactory', 'usersFactory',
			function($http, notesFactory, usersFactory){
	var url1 = 'https://localhost:8443/RedNotes_API/rest/friends/';
	var url2 = 'https://localhost:8443/RedNotes_API/rest/users/';
    
    var friendsInterface = {
    	readFriends: function(){
	      
        	return $http.get(url1)
        	.then(function(response){
        		var friendlist=response.data;
        		var userfriends=[];
        		
        		console.log("Cargada informacion de amigos");
        		console.log(friendlist);
	        	return $http.get(url2+'myself')
	        	.then(function(response){
	        		var userlogin=response.data;
	        		var j=0;
	        		angular.forEach(friendlist, function(friend){
	        			console.log(friend);
	        			var idu= friend.idA;
	        			if(friend.idA==userlogin.idu){
	        				idu=friend.idB;
	        			}
	        			else{
	        				idu= friend.idA;
	        			}
	        			console.log("Idu del otro "+idu)
	        			if (friend.confirmed==1){
				        	$http.get(url2+idu)
				        	.then(function(response){
				        		var user=response.data;
				        		console.log("Cargada informacion de usuario "+idu);
				        		
				        		user.date=friend.dateFriendship;
				        		
				        		user.confirmed=friend.confirmed;
				        		
				        		userfriends[j]=user;
				        		j++;
				        	});	
	        			}
	        		});
	        		
	        		return userfriends;
	        	});
        	});
		},
		readFriend: function(idu){
			return $http.get(url1+idu)
        	.then(function(response){
        		var friend=response.data;
        		console.log("Cargada informacion de amigo");
        		var userfriends=[];
    			if (friend.confirmed==1){
    				return $http.get(url2+idu)
		        	.then(function(response){
		        		var user=response.data;
		        		console.log("Cargada informacion de usuario "+idu);
		        		
		        		user.date=friend.dateFriendship;
		        		
		        		user.dateString=(new Date(friend.dateFriendship));
		        		
		        		user.confirmed=friend.confirmed;

		        		console.log(user);
		        		return user;
		        	});	
    			}
        	});
		},
		notSharedFriends: function(idn){
			return notesFactory.getNote(idn).then(function(response){
				var note=response;
				return usersFactory.getUserLogin().then(function(response){
					var user=response;
					if(note.ownerID!=user.idu){
						alert("You only can share your notes!! Sorry");
						$location.path('/');
					}
					else{
						return $http.get(url1)
			        	.then(function(response){
			        		var friendlist=response.data;
			        		var userfriends=[];
			        		
			        		console.log("Cargada informacion de amigos");
			        		console.log(friendlist);
				        	return $http.get(url2+'myself')
				        	.then(function(response){
				        		var userlogin=response.data;
				        		var j=0;
				        		angular.forEach(friendlist, function(friend){
				        			console.log(friend);
				        			var idu= friend.idA;
				        			if(friend.idA==userlogin.idu){
				        				idu=friend.idB;
				        			}
				        			else{
				        				idu= friend.idA;
				        			}
				        			console.log("Idu del otro "+idu)
				        			if (friend.confirmed==1){
							        	$http.get(url2+idu)
							        	.then(function(response){
							        		var user=response.data;
							        		console.log("Cargada informacion de usuario "+idu);
							        		
							   				notesFactory.readSharedIDUsNote(idn).then(function(response){
							   					var sharedwith=response.data;
							   					
						   						var mostrar=true;
							   					angular.forEach(sharedwith, function(sharedfriend){
							   						if(user.idu==sharedfriend){
							   							mostrar=false;
							   						}
							   					});

						   						if(mostrar){
									        		user.date=friend.dateFriendship;
									        		
									        		user.confirmed=friend.confirmed;
									        		
									        		userfriends[j]=user;
									        		j++;	
									        	}
							   				});

							        	});	
				        			}
				        		});
				        		return userfriends;
					        		
				        	});
			        	});
					}
				});
        	});

		},
		deleteFriendship: function(idu){
    		var urlid = url1+idu;
            return $http.delete(urlid)
            	.then(function(response){
      				 return response.status;
    				});
		},
		getSearchedFriends: function(search){

        	return $http.get(url1)
        	.then(function(response){
        		var friendlist=response.data;
        		var userfriends=[];
        		
        		console.log("Cargada informacion de amigos");
        		console.log(friendlist);
	        	return $http.get(url2+'myself')
	        	.then(function(response){
	        		var userlogin=response.data;
	        		var j=0;
	        		angular.forEach(friendlist, function(friend){
	        			console.log(friend);
	        			var idu= friend.idA;
	        			if(friend.idA==userlogin.idu){
	        				idu=friend.idB;
	        			}
	        			else{
	        				idu= friend.idA;
	        			}
	        			console.log("Idu del otro "+idu)
	        			if (friend.confirmed==1){
				        	$http.get(url2+idu)
				        	.then(function(response){
				        		var user=response.data;
				        		console.log("Cargada informacion de usuario "+idu);
				        		
				        		if(user.username.toLowerCase().includes(search.toLowerCase()) ||
				        				(user.name!=null && user.name.toLowerCase().includes(search.toLowerCase()))){
					        		console.log("Usuario "+idu+" lo contiene");
				        			user.date=friend.dateFriendship;
					        		
					        		user.confirmed=friend.confirmed;
					        		
					        		userfriends[j]=user;
					        		j++;
				        		}
				        	});	
	        			}
	        		});
	        		
	        		return userfriends;
	        	});
        	});
		},
    	readNotFriends: function(name,username,country,city){
  	      
    		console.log("Mandando peticion GET: ("+url2+"?name="+name+"&username="+username+"&country="+country+"&city="+city+")");
    		
        	return $http.get(url2+"?name="+name+"&username="+username+"&country="+country+"&city="+city)
        	.then(function(response){
        		var userslist=response.data;
        		
        		console.log("readNotFriends: Cargada informacion de usuarios coincidentes");
        		console.log(userslist);
	        	return $http.get(url2+'myself')
	        	.then(function(response){
	        		var userlogin=response.data;
	        		console.log("readNotFriends: Cargada mi informacion")
	        		console.log("readNotFriends: Procedemos a comparar cada usuario con los amigos");
	        		return $http.get(url1)
	        		 .then(function(response){
	        			var friendslist= response.data;
	            		var usernotfriends=[];

	        			var j=0;
	        			angular.forEach(userslist, function(user){
	        				var mostrar=true;
	        				for(var i=0; i<friendslist.length; i++){
	        					if(user.idu==friendslist[i].idA || user.idu==friendslist[i].idB){
	        						console.log("El usuario "+user.username+" ya es un amigo");
	        						mostrar=false;
	        					}
	        				}
	        				if(mostrar==true){
	        					usernotfriends[j]=user;
	        					console.log("Añadiendo el usuario "+user.username+" a la lista de usuarios no amigos");
	        					j++;
	        				}
	        			});
	        			return usernotfriends;
	        		});	        		
	        	});
        	});
		},
		addPetition: function(friend){
            return $http.post(url1, friend)
            	.then(function(response){
      				 return response.status;
    				});
		},
		readPetitionsSent: function(){
			return $http.get(url1)
        	.then(function(response){
        		var friendlist=response.data;
        		var userfriends=[];
        		
        		console.log("Cargada informacion de amigos");
        		console.log(friendlist);
	        	return $http.get(url2+'myself')
	        	.then(function(response){
	        		var userlogin=response.data;
	        		var j=0;
	        		angular.forEach(friendlist, function(friend){
	        			console.log(friend);
	        			if(friend.idA==userlogin.idu && friend.confirmed==0){
		        			var idu= friend.idB;
		        			console.log("Idu del otro "+idu)
				        	$http.get(url2+idu)
				        	.then(function(response){
				        		var user=response.data;
				        		console.log("Cargada informacion de usuario "+idu);
				        		
				        		user.date=friend.dateFriendship;
				        		user.dateFriendship=friend.dateFriendship;
				        		
				        		user.confirmed=friend.confirmed;
				        		
				        		user.idA=userlogin.idu;
				        		user.idB=user.idu;
				        		
				        		userfriends[j]=user;
				        		j++;
				        	});
	        			}
	        		});
	        		
	        		return userfriends;
	        	});
        	});
		},
		readPetitionsReceived: function(){
			return $http.get(url1)
        	.then(function(response){
        		var friendlist=response.data;
        		var userfriends=[];
        		
        		console.log("Cargada informacion de amigos");
        		console.log(friendlist);
	        	return $http.get(url2+'myself')
	        	.then(function(response){
	        		var userlogin=response.data;
	        		var j=0;
	        		angular.forEach(friendlist, function(friend){
	        			console.log(friend);
	        			if(friend.idB==userlogin.idu && friend.confirmed==0){
		        			var idu= friend.idA;
		        			console.log("Idu del otro "+idu)
				        	$http.get(url2+idu)
				        	.then(function(response){
				        		var user=response.data;
				        		console.log("Cargada informacion de usuario "+idu);
				        		
				        		user.date=friend.dateFriendship;
				        		user.dateFriendship=friend.dateFriendship;
				        		
				        		user.confirmed=friend.confirmed;
				        		
				        		user.idB=userlogin.idu;
				        		user.idA=user.idu;
				        		
				        		userfriends[j]=user;
				        		j++;
				        	});
	        			}
	        		});
	        		
	        		return userfriends;
	        	});
        	});
		},
		updatePetition: function(idu, petitionNew){
            return $http.put(url1+idu, petitionNew)
        	.then(function(response){
  				 return response.status;
				});
		}
    }
    return friendsInterface;
}])
