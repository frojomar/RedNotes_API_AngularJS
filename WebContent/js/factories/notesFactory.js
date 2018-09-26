angular.module('rednotesApp')
.factory("notesFactory", ['$http','usersFactory',
			function($http,usersFactory){
	var url1 = 'https://localhost:8443/RedNotes_API/rest/notes/';
	var url2 = 'https://localhost:8443/RedNotes_API/rest/usersnotes/';
    
    var notesInterface = {
    	getNotes: function(){
	        
	        return $http.get(url2)
	        .then(function(response){
	        	var usersnotes= response.data;
		        var notesComplete=[];
                
		        var i=0;
		        angular.forEach(usersnotes, function(usersnote){
		        	$http.get(url1+usersnote.idn).then(function(response){
			        	var note=response.data;
		        		usersFactory.getUsername(note.ownerID).then(function(response){
		        			var username=response;
		        			usersFactory.getImage(note.ownerID).then(function(response){
		        				  var image= response;
		        				
			  		          	  console.log("Nota asignada a "+note.idn); 
							      console.log("ID solicitado:"+url1+usersnote.idn+" - ID DE LA NOTA:"+note.idn);
	
						          var noteComplete={
						            'id': note.idn,
						            'title' : note.title,
						            'content': note.content,
						            'ownerID': note.ownerID,
						            'owner': username,
						            'imageOwner': image,
						            'pinned': usersnote.pinned,
						            'archived': usersnote.archived,
						            'color': usersnote.color,
						            'creationDate': note.creationDate,
						            'modificationDate': note.modificationDate,
						            'type': note.type,
						            'dateVersion': ""
						          };
						          notesComplete[i]=noteComplete;
						          i++;
		        			});
		        		});


				     });
		          
		          });

		        return notesComplete;
	        	});
    		},
        	getNotesArchived: function(){
    	        
    	        return $http.get(url2)
    	        .then(function(response){
    	        	var usersnotes= response.data;
    		        var notesComplete=[];
                    
    		        var i=0;
    		        angular.forEach(usersnotes, function(usersnote){
    		        	if(usersnote.archived==1){
    		        		$http.get(url1+usersnote.idn).then(function(response){
	    			        	var note=response.data;
	    		        		usersFactory.getUsername(note.ownerID).then(function(response){
	    		        			var username=response;
	    		        			usersFactory.getImage(note.ownerID).then(function(response){
	    		        				  var image= response;
	    		        				
	    			  		          	  console.log("Nota asignada a "+note.idn); 
	    							      console.log("ID solicitado:"+url1+usersnote.idn+" - ID DE LA NOTA:"+note.idn);
	    	
	    						          var noteComplete={
	    						            'id': note.idn,
	    						            'title' : note.title,
	    						            'content': note.content,
	    						            'ownerID': note.ownerID,
	    						            'owner': username,
	    						            'imageOwner': image,
	    						            'pinned': usersnote.pinned,
	    						            'archived': usersnote.archived,
	    						            'color': usersnote.color,
	    						            'creationDate': note.creationDate,
	    						            'modificationDate': note.modificationDate,
	    						            'type': note.type,
	    						            'dateVersion': ""
	    						          };
	    						          notesComplete[i]=noteComplete;
	    						          i++;
	    		        			});
	    		        		});
	
	
	    				     });
    		        	}
    		          
    		          });

    		        return notesComplete;
    	        });

    		},
        	getNotesPinned: function(){
    	        
    	        return $http.get(url2)
    	        .then(function(response){
    	        	var usersnotes= response.data;
    		        var notesComplete=[];
                    
    		        var i=0;
    		        angular.forEach(usersnotes, function(usersnote){
    		        	if(usersnote.archived==0 && usersnote.pinned==1){
	    		        	$http.get(url1+usersnote.idn).then(function(response){
	    			        	var note=response.data;
	    		        		usersFactory.getUsername(note.ownerID).then(function(response){
	    		        			var username=response;
	    		        			usersFactory.getImage(note.ownerID).then(function(response){
	    		        				  var image= response;
	    		        				
	    			  		          	  console.log("Nota asignada a "+note.idn); 
	    							      console.log("ID solicitado:"+url1+usersnote.idn+" - ID DE LA NOTA:"+note.idn);
	    	
	    						          var noteComplete={
	    						            'id': note.idn,
	    						            'title' : note.title,
	    						            'content': note.content,
	    						            'ownerID': note.ownerID,
	    						            'owner': username,
	    						            'imageOwner': image,
	    						            'pinned': usersnote.pinned,
	    						            'archived': usersnote.archived,
	    						            'color': usersnote.color,
	    						            'creationDate': note.creationDate,
	    						            'modificationDate': note.modificationDate,
	    						            'type': note.type,
	    						            'dateVersion': ""
	    						          };
	    						          notesComplete[i]=noteComplete;
	    						          i++;
	    		        			});
	    		        		});
	
	
	    				     });
    		        	}
    		          
    		          });
     		       return notesComplete;

    		       });

    	       },
        	getNotesNotPinned: function(){
    	        
    	        return $http.get(url2)
    	        .then(function(response){
    	        	var usersnotes= response.data;
    		        var notesComplete=[];
                    
    		        var i=0;
    		        angular.forEach(usersnotes, function(usersnote){
    		        	if(usersnote.archived==0 && usersnote.pinned==0){
	    		        	$http.get(url1+usersnote.idn).then(function(response){
	    			        	var note=response.data;
	    		        		usersFactory.getUsername(note.ownerID).then(function(response){
	    		        			var username=response;
	    		        			usersFactory.getImage(note.ownerID).then(function(response){
	    		        				  var image= response;
	    		        				
	    			  		          	  console.log("Nota asignada a "+note.idn); 
	    							      console.log("ID solicitado:"+url1+usersnote.idn+" - ID DE LA NOTA:"+note.idn);
	    	
	    						          var noteComplete={
	    						            'id': note.idn,
	    						            'title' : note.title,
	    						            'content': note.content,
	    						            'ownerID': note.ownerID,
	    						            'owner': username,
	    						            'imageOwner': image,
	    						            'pinned': usersnote.pinned,
	    						            'archived': usersnote.archived,
	    						            'color': usersnote.color,
	    						            'creationDate': note.creationDate,
	    						            'modificationDate': note.modificationDate,
	    						            'type': note.type,
	    						            'dateVersion': ""
	    						          };
	    						          notesComplete[i]=noteComplete;
	    						          i++;
	    		        			});
	    		        		});
	
	
	    				     });
    		        	}
    		          
    		          });
    		       return notesComplete;
    	     });
        },
    	getNote: function(id){
	        
	        return $http.get(url2+id).then(function(response){
	        	var usersnote= response.data;
	        	return $http.get(url1+usersnote.idn).then(function(response){
		        	var note=response.data;
	        		return usersFactory.getUsername(note.ownerID).then(function(response){
	        			var username=response;
	        			return usersFactory.getImage(note.ownerID).then(function(response){
	        				  var image= response;
	        				
						      console.log("ID solicitado:"+url1+usersnote.idn+" - ID DE LA NOTA:"+note.idn);

					          var noteComplete={
					            'id': note.idn,
					            'title' : note.title,
					            'content': note.content,
					            'ownerID': note.ownerID,
					            'owner': username,
					            'imageOwner': image,
					            'pinned': usersnote.pinned,
					            'archived': usersnote.archived,
					            'color': usersnote.color,
					            'creationDate': note.creationDate,
					            'modificationDate': note.modificationDate,
					            'type': note.type,
					            'dateVersion': "",
					            'dateString': new Date(note.modificationDate)
					          };
					          return noteComplete;
	        			});
	        		});
	        	});
        	});
    	},
        updateUsersNote: function(note){
    		return usersFactory.getUserLogin().then(function(response){
    			user=response;
				var usersnote={
						"idn":note.id,
						"idu":user.idu,
						"archived":note.archived,
						"pinned":note.pinned,
						"color":note.color
					};
	        	return $http.put(url2+usersnote.idn, usersnote)
		        .then(function(response){
		        	console.log("Nota actualizada (UsersNote)");
		        });
    		});
        },
        updateNote: function(note){
			var noteInfo={
					"idn":note.id,
					"title":note.title,
					"content":note.content,
					"ownerID":note.ownerID,
					"creationDate":note.creationDate,
					"modificationDate": note.modificationDate,
					"type": note.type
			};
        	return $http.put(url1+noteInfo.idn, noteInfo)
        	.then(function(response){
        		console.log("Nota actualizada (Note)");
        	});
        },
        createUsersNote: function(note){
    		return usersFactory.getUserLogin().then(function(response){
    			user=response;
				var usersnote={
						"idn":note.id,
						"idu":user.idu,
						"archived":note.archived,
						"pinned":note.pinned,
						"color":note.color
					};
	        	return $http.post(url2, usersnote)
		        .then(function(response){
		        	console.log("Nota creada (UsersNote)");
	        		return response;

		        });
    		});
        },
        createNote: function(note){
			var noteInfo={
					"idn":note.id,
					"title":note.title,
					"content":note.content,
					"ownerID":note.ownerID,
					"creationDate":note.creationDate,
					"modificationDate": note.modificationDate,
					"type": note.type
			};
        	return $http.post(url1, noteInfo)
        	.then(function(response){
        		console.log("Nota creada (Note)");
        		return response;
        	});
        },
        deleteNote: function(id){
        	return $http.delete(url2+id)
        	.then(function(response){
        		console.log("Nota eliminada");
        	}).then(function(response){
        		console.log("No se ha podido eliminar la nota");
        	});
        },
        getSearchedNotes: function(search){
        	        	
        	return $http.get(url2)
	        .then(function(response){
	        	var usersnotes= response.data;
		        var notesComplete=[];
		        
		        var i=0;
		        angular.forEach(usersnotes, function(usersnote){
		        	$http.get(url1+usersnote.idn).then(function(response){
			        	var note=response.data;
			        	console.log("Buscando "+search+" en: "+note.title+" - "+note.content);
	        			if(note.title.toLowerCase().includes(search.toLowerCase()) || note.content.toLowerCase().includes(search.toLowerCase())){
	        				console.log("lo contiene");
			        		usersFactory.getUsername(note.ownerID).then(function(response){
			        			var username=response;
			        			usersFactory.getImage(note.ownerID).then(function(response){
			        				  var image= response;
			        				
				  		          	  console.log("Nota asignada a "+note.idn); 
								      console.log("ID solicitado:"+url1+usersnote.idn+" - ID DE LA NOTA:"+note.idn);
		
							          var noteComplete={
							            'id': note.idn,
							            'title' : note.title,
							            'content': note.content,
							            'ownerID': note.ownerID,
							            'owner': username,
							            'imageOwner': image,
							            'pinned': usersnote.pinned,
							            'archived': usersnote.archived,
							            'color': usersnote.color,
							            'creationDate': note.creationDate,
							            'modificationDate': note.modificationDate,
							            'type': note.type,
							            'dateVersion': ""
							          };
							          notesComplete[i]=noteComplete;
							          i++;
			        			});
			        		});

	        			}
				     });
		          
		          });

		        return notesComplete;
	        	});
        },
        readNotesFriend: function(idu){
        	
        	return $http.get(url2)
	        .then(function(response){
	        	var usersnotes= response.data;
		        var notesComplete=[];
		        
		        var i=0;
		        angular.forEach(usersnotes, function(usersnote){
		        	$http.get(url1+usersnote.idn).then(function(response){
			        	var note=response.data;
			        	console.log("Filtrando nota de "+note.ownerID+" por ownerID "+idu);
	        			if(note.ownerID==idu){
			        		usersFactory.getUsername(note.ownerID).then(function(response){
			        			var username=response;
			        			usersFactory.getImage(note.ownerID).then(function(response){
			        				  var image= response;
			        				
				  		          	  console.log("Nota asignada a "+note.idn); 
								      console.log("ID solicitado:"+url1+usersnote.idn+" - ID DE LA NOTA:"+note.idn);
		
							          var noteComplete={
							            'id': note.idn,
							            'title' : note.title,
							            'content': note.content,
							            'ownerID': note.ownerID,
							            'owner': username,
							            'imageOwner': image,
							            'pinned': usersnote.pinned,
							            'archived': usersnote.archived,
							            'color': usersnote.color,
							            'creationDate': note.creationDate,
							            'modificationDate': note.modificationDate,
							            'type': note.type,
							            'dateVersion': ""
							          };
							          notesComplete[i]=noteComplete;
							          i++;
			        			});
			        		});

	        			}
				     });
		          
		          });

		        return notesComplete;
	        	});
        },
        readSharedIDUsNote:function(idn){
        	return $http.get(url2+'sharedidus/'+idn)
	        .then(function(response){
	        	return response;
	        });
        },
        shareNote: function(note){
			var usersnote={
					"idn":note.id,
					"idu":note.idu,
					"archived":note.archived,
					"pinned":note.pinned,
					"color":note.color
				};
        	return $http.post(url2, usersnote)
	        .then(function(response){
	        	console.log("Nota creada (UsersNote)");
        		return response;

	        });
        }
        
    }
    return notesInterface;
}])
