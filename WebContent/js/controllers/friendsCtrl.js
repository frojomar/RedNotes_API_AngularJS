angular.module('rednotesApp')
.controller('friendsCtrl', ['friendsFactory', 'usersFactory', 'notesFactory', '$routeParams', '$location',
				function(friendsFactory, usersFactory, notesFactory, $routeParams, $location){
    var lvm = this;
   
    lvm.friendslist=[];
    lvm.friend={};
    lvm.notes=[];
    
    lvm.orderby='username';
    

    lvm.link="#!/";
        
    lvm.functions = {
   		where : function(route){
   			return $location.path() == route;
   		},
   		readFriends: function(){
   			friendsFactory.readFriends().then(function(response){
   				lvm.friendslist=response;
   				console.log("Amigos cargados");
   			}, function(response){
   				console.log("No se ha podido cargar los amigos");
   			});
   		},
   		loadFriend: function(idu){
   			if(lvm.friend.idu==idu){
   				lvm.friend={};
   			}
   			else{
	   			friendsFactory.readFriend(idu).then(function(response){
	   				lvm.friend=response;
	   				console.log("Amigo cargado -> "+lvm.friend.username);
	   				if(lvm.friend.confirmed==0){
	   					alert("This friendship is not confirmed yet!!");
	   				}
	   				else{
		   				notesFactory.readNotesFriend(idu).then(function(response){
		   					lvm.notes=response;
		   					console.log("Cargadas las notas compartidas por el amigo "+ idu);
		   				}, function(response){
		   	   				console.log("No se ha podido cargar las notas del amigo con idu "+idu);
		   	   			});
	   				}
	   			}, function(response){
	   				console.log("No se ha podido cargar el amigo con idu "+idu);
	   				alert("You are not friend of this user!!");
	   			});
   			}
   		},
   		searchFriend: function(search){
   			friendsFactory.readFriend(idu).then(function(response){
   				lvm.friend=response;
   				console.log("Amigos encontrados -> ")
   				console.log(lvm.friendslist);
   			}, function(response){
   				console.log("No se ha podido buscar los amigos");
   			});
   		},
		noteTypeLink: function(id, type){
			if(type==0){
				lvm.link='#!/'+id;
			}
			else{
				lvm.link='#!/list/'+id;
			}
		},
		readFriendsNotShared: function(idn){
			/*notesFactory.getNote(idn).then(function(response){
				var note=response;
				usersFactory.getUserLogin().then(function(response){
					var user=response;
					if(note.ownerID!=user.idu){
						alert("You only can share your notes!! Sorry");
						$location.path('/');
					}
					else{
						friendsFactory.readFriends().then(function(response){
			   				var friends=response;
			   				console.log("Amigos cargados");
			   				notesFactory.readSharedIDUsNote(idn).then(function(response){
			   					var sharedwith=response.data;
			   					
			   					angular.forEach(friends, function(friend){
			   						var mostrar=true;
			   						
			   						for(var i=0; i<sharedwith.length; i++){
			   							if(friend.idu==sharedwith[i]){
			   								mostrar=false;
			   							}
			   						}
			   						if(mostrar){
			   							lvm.friendslist.push(friend);
			   						}
			   					});
			   				}, function(response){
			   					alert(response.data.userMessage);
			   				})
			   			}, function(response){
			   				console.log("No se ha podido cargar los amigos");
			   			});
					}
				}, function(response){
					console.log("Problemas con el usuario logueado")
				})
			}, function(response){
				alert("We don't have this note in your list of notes")
			})*/
   			friendsFactory.notSharedFriends(idn).then(function(response){
   				lvm.friendslist=response;
   			}, function(response){
   				alert("We can't load friends!! Sorry");
   				$location.path('/');
   			});
   		},
		shareNote: function(friend){
			var usersnote={
					"id":$routeParams.ID,
					"idu":friend.idu,
					"archived":0,
					"pinned":0,
					"color":"yellow"
				};
			notesFactory.shareNote(usersnote).then(function(response){
				console.log("Nota compartida correctamente" + response);
				$location.path('/');
			}, function(response){
				alert(response.data.userMessage);
				$location.path('/');
			});
		},
    	deleteFriendAction: function(bandera){
    		
    		if(bandera==1){
    			console.log("Se ha decidido eliminar el amigo");
	    		friendsFactory.deleteFriendship($routeParams.ID)				
		    		.then(function(response){
						console.log("Delete friendship. Response:", response);
						$location.path('/friends');
					}, function(response){
						console.log("Error deleting friendship");
						alert("Error deleting friendship");
						$location.path('/friends');
					})
    		}
    		else{
    			console.log("Se ha decidido NO terminar la amistad");
				$location.path('/friends');
    		}
    	},
    	searchFriends: function(search){
    		lvm.friendslist=[];
			console.log("Buscando amigos por: "+search);
			
			friendsFactory.getSearchedFriends(search).then(function(response){
				lvm.friendslist=response;
			});
		
    	}
    }
    if(!lvm.functions.where('/share/'+$routeParams.ID)){
	    if(lvm.functions.where('/searchfriends/'+$routeParams.SEARCH)){
	    	lvm.functions.searchFriends($routeParams.SEARCH);
	    }else{
		    lvm.functions.readFriends();
	        if(lvm.functions.where('/friends/'+$routeParams.ID) || 
	        		lvm.functions.where('/deletefriend/'+$routeParams.ID)){
	        	lvm.functions.loadFriend($routeParams.ID);
	        }
	    }
    }
    else{
    	lvm.functions.readFriendsNotShared($routeParams.ID);
    }



}])
