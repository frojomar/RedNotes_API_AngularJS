angular.module('rednotesApp')
.controller('noteCtrl', ['notesFactory', 'usersFactory', 'versionsNotesFactory','$routeParams', '$location',
				function(notesFactory, usersFactory, versionsFactory, $routeParams, $location){
    var nvm = this;
    nvm.note={};
    nvm.listversions=[];
    
	nvm.oldcontent="";
	nvm.oldtitle="";
    
	nvm.user;
        
    nvm.functions = {
   		where : function(route){
   			return $location.path() == route;
   		},
    	readNote : function() {
    		notesFactory.getNote($routeParams.ID).then(function(response){
    			nvm.note=response;
        		nvm.oldcontent=nvm.note.content;
        		nvm.oldtitle=nvm.note.title;
    			console.log("Nota "+response.id+" leida");
    			versionsFactory.getVersions($routeParams.ID).then(function(response){
    				nvm.listversions=response;
    				console.log("Cargadas las versiones de la nota");
    			});
    			if(nvm.note.type==1  && !nvm.functions.where('/deletenote/'+$routeParams.ID)){
    				$location.path('/list/'+nvm.note.id);
    			}
    		}, function(response){
    			$location.path('/');
    			alert(response.data.userMessage);
    		});
		},
		updateColor: function(color){
			nvm.note.color=color;
			console.log("nuevo color"+ color);
			notesFactory.updateUsersNote(nvm.note).then(function(response){
				console.log("Color actualizado");
			});
		},	
		archiveNote: function(){
			nvm.note.archived=1;
			notesFactory.updateUsersNote(nvm.note).then(function(response){
				console.log("Nota archivada. Recargando notas.");
			});
		},
		unarchiveNote: function(){
			nvm.note.archived=0;
			notesFactory.updateUsersNote(nvm.note).then(function(response){
				console.log("Nota archivada. Recargando notas.");
			});
		},
		pinnedNote: function(){
			nvm.note.pinned=1;
			notesFactory.updateUsersNote(nvm.note).then(function(response){
				console.log("Nota archivada. Recargando notas.");
			});
		},
		unpinnedNote: function(){
			nvm.note.pinned=0;
			notesFactory.updateUsersNote(nvm.note).then(function(response){
				console.log("Nota archivada. Recargando notas.");
			});
		},
		loadVersion: function(idn, dateVersion){
			console.log("Intentando cargar la versión "+dateVersion+" de la nota "+idn);
			versionsFactory.getVersion(idn, dateVersion).then(function(response){
				console.log("Version cargada");
				nvm.note.content=response.content;
				nvm.note.title=response.title;
				nvm.note.modificationDate=response.modificationDate;
			});
		},
		setDefaultVersion: function(idn, dateVersion){
			
			angular.forEach(nvm.listversions, function(version){
				if(version.modificationDate>dateVersion){
					versionsFactory.deleteVersion(idn, version.modificationDate);
					console.log("Eliminando version"+dateVersion+" de la note"+idn);
				}
				else{
					if(version.modificationDate==dateVersion){
						nvm.note.content=version.content;
						nvm.note.title=version.title;
						nvm.note.modificationDate=version.modificationDate;
						notesFactory.updateNote(nvm.note).then(function(response){
							console.log("La nota "+idn+" ha vuelto a la version "+ dateVersion);
						})
					}
				}
			});
			/*for(var i=0; i<nvm.listversions.length; i++){
				if(nvm.listversions[i].modificationDate>dateVersion){
					versionsFactory.deleteVersion(idn, dateVersion);
					console.log("Eliminando version"+dateVersion+" de la note"+idn);
				}
				else{
					if(nvm.listversions[i].modificationDate==dateVersion){
						nvm.note.content=nvm.listversions[i].content;
						nvm.note.title=nvm.listversions[i].title;
						nvm.note.modificationDate=nvm.listversions[i].modificationDate;
						notesFactory.updateNote(nvm.note).then(function(response){
							console.log("La nota "+idn+" ha vuelto a la version "+ dateVersion);
						})
					}
				}
			}*/
			nvm.functions.readNote();
		},
		deleteVersion: function(idn, date){
			versionsFactory.deleteVersion(idn, date).then(function(response){
				console.log("Version eliminada. Recargando nota.");
				nvm.functions.readNote();
			});
		},
		updateNote: function(){
			if(nvm.note.title==""){
				alert("The title can not be empty");
			}
			else{
				if(nvm.note.title==nvm.oldtitle && nvm.note.content==nvm.oldcontent){
					console.log("No se ha modificado nada");
				}
				else{
					var date= new Date();
					nvm.note.modificationDate= date.getTime();
					console.log("Nota a escribir: "+nvm.note);
					if(nvm.functions.where('/addnote')){
						console.log("Creando la nota");
						notesFactory.createNote(nvm.note).then(function(response){
							console.log("Creada nota " + response);
							console.log(response);
							console.log(response.headers('Location'));
							
							var location=(response.headers('Location')).split('/');
							
							console.log(location);
							
							var idn = location[location.length-1]
							console.log("Nuevo idn es "+idn);
							nvm.note.id=idn;
							notesFactory.createUsersNote(nvm.note).then(function(response){
								console.log("Nota creada correctamente" + response);
								$location.path('/');
							}, function(response){
								alert(response.data.userMessage);
							});

						}, function(response){
							alert(response.data.userMessage);
						});
					}
					else{
						console.log("Actualizando la nota");
						notesFactory.updateNote(nvm.note).then(function(response){
							console.log("Actualizada la nota y creada nueva version");
						});
						$location.path('/'+nvm.note.id);
					}
				}
			}
		},
		deleteNote: function(bandera){
    		if(bandera==1){
    			console.log("Se ha decidido eliminar la nota");
	    		notesFactory.deleteNote(nvm.note.id)				
		    		.then(function(response){
						console.log("Delete note. Response:", response);
						$location.path('/');
					}, function(response){
						$location.path(history.back(0));
						alert("We can't delete the note in this moment. Try again later!");
					})
    		}
    		else{
    			console.log("Se ha decidido NO eliminar la nota");
				$location.path(history.back(0));
    		}
		},
		newNote: function(){
			
			console.log("Creando nueva nota");
			
    		usersFactory.getUserLogin().then(function(response){
    			nvm.user=response;
				nvm.note={
						'id': 100,
			            'title' : "",
			            'content': "",
			            'ownerID': nvm.user.idu,
			            'owner': nvm.user.username,
			            'imageOwner': nvm.user.image,
			            'pinned': 0,
			            'archived': 0,
			            'color':"yellow",
			            'creationDate': (new Date()).getTime(),
			            'modificationDate':(new Date()).getTime(),
			            'type': 0,
			            'dateVersion': ""
				}
	            nvm.note.dateString= new Date(nvm.note.modificationDate);

    		});
		}
    }    
    if(!nvm.functions.where('/addnote')){nvm.functions.readNote();}
    else{nvm.functions.newNote();}

}])
