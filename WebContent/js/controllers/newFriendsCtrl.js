angular.module('rednotesApp')
.controller('newFriendsCtrl', ['friendsFactory', 'usersFactory', '$location',
				function(friendsFactory, usersFactory, $location){
    var lvm = this;
   
    lvm.petitionsent=[];
    lvm.petitionreceived=[];
    
    lvm.userslist=[];
    lvm.user={};    

    lvm.name="";
    lvm.username="";
    lvm.country="";
    lvm.city="";
    
    
    lvm.functions = {
   		where : function(route){
   			return $location.path() == route;
   		},
   		readNotFriends: function(){
   			friendsFactory.readNotFriends(lvm.name, lvm.username, lvm.country, lvm.city).then(function(response){
   				lvm.userslist=response;
   				console.log("Usuarios no amigos cargados");
   			}, function(response){
   				console.log("Controller: No se ha podido cargar los usuarios no amigos");
   			});
   		},
   		loadUser: function(user){
   			if(lvm.user.idu==undefined){
   	   			lvm.user=user;
   			}
   			else{
   				if(lvm.user.idu!=user.idu){
   	   	   			lvm.user=user;
   				}
   				else{
   					lvm.user={};
   				}
   			}
   		},
   		sendPetition: function(){
   			if(lvm.user.idu==undefined){
   				alert("You must select the user previously");
   			}
   			else{
   				usersFactory.getUserLogin().then(function(response){
   	   				var myself=response;
   					var friend={
   	   						"idA": myself.idu,
   	   						"idB": lvm.user.idu,
   	   						"dateFriendship":(new Date()).getTime(),
   	   						"confirmed":0
   	   				};
   	   				friendsFactory.addPetition(friend).then(function(response){
   	   					console.log("Petición enviada");
   	   					alert("Petition sended");
   	   					$location.path('/petitions');
   	   				}, function(response){
   	   					alert("We can't send petition. Try later again. Sorry!!");
   	   				});			
   				});
   			}
   		},
   		loadPetitionsSent: function(){
   			friendsFactory.readPetitionsSent().then(function(response){
   				lvm.petitionsent=response;
   			},function(response){
	   				alert("We can't load petitions sent now. Sorry!!")
	   		});
   		},
   		loadPetitionsReceived: function(){
   			friendsFactory.readPetitionsReceived().then(function(response){
   				lvm.petitionreceived=response;
   			},function(response){
	   				alert("We can't load petitions received now. Sorry!!")
	   		});
   		},
   		deletePetition: function(petition){
   			
			usersFactory.getUserLogin().then(function(response){
				var user= response;
				
				var idu= petition.idA;
				if(petition.idA==user.idu){
					idu=petition.idB;
				}
				
	   			friendsFactory.deleteFriendship(idu).then(function(response){
	   					console.log("Peticion eliminada");
	   			    	lvm.functions.loadPetitionsSent();
	   			    	lvm.functions.loadPetitionsReceived();
	   			},function(response){
		   				alert("We can't drop petition now. Sorry!!")
		   		});
			}, function(response){
				console.log("NO se puede cargar el user login para eliminar peticion");
			});
			
   		},
   		confirmPetition: function(petition){
			usersFactory.getUserLogin().then(function(response){
				var user= response;
   			
				if(petition.idB!=user.idu){
					alert("YOu only can confirm petitions received");
				}
				else{
					var petitionNew={
			   				"idA":petition.idA,
			   				"idB":petition.idB,
			   				"dateFriendship":(new Date()).getTime(),
			   				"confirmed":1
			   			};
			   			friendsFactory.updatePetition(petition.idA,petitionNew).then(function(response){
			   				console.log("Peticion aceptada");
			   		    	lvm.functions.loadPetitionsSent();
			   		    	lvm.functions.loadPetitionsReceived();
			   			},function(response){
				   				console.log("We can't load petitions received now. Sorry!!")
				   		});
				}
	   			
			}, function(response){
				console.log("NO se puede cargar el user login para eliminar peticion");
			});
   		}
    
    
    }
    if(lvm.functions.where('/petitions')){
    	lvm.functions.loadPetitionsSent();
    	lvm.functions.loadPetitionsReceived();
    }
}])
