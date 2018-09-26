angular.module('rednotesApp')
.controller('listCtrl', ['notesFactory', 'usersFactory', 'versionsNotesFactory','$routeParams', '$location',
				function(notesFactory, usersFactory, versionsFactory, $routeParams, $location){
    var lvm = this;
    lvm.note={};
    lvm.listversions=[];
    
	lvm.oldcontent="";
	lvm.oldtitle="";
    
	lvm.user;
    
	lvm.listelements=[];
	
    lvm.functions = {
   		where : function(route){
   			return $location.path() == route;
   		},
   		separateContent: function(){
   			var contentSeparate=lvm.note.content.split("/;/");
			if(contentSeparate.length%2!=1){  //1, aunque los elementos sean pares, por el último elemento vacío. 
										//Debido a formato 'valor/;/texto/;/valor/:/texto/;/'.
										//Se podría quitar /;/ del final, pero no se hace para permitir la 
										//compatibilidad de la misma base de datos para ambas versiones de RedNotes.
				alert("There are problem with this note. Sorry");
				$location.path('/');
			}
			else{
				lvm.listelements=[];
				var j=0;
				for(var i=0; i<contentSeparate.length-1; i=i+2){
					lvm.listelements[j]={};
					if(contentSeparate[i]==1){
						lvm.listelements[j].value=true;
					}
					else{
						lvm.listelements[j].value=false;
					}
					lvm.listelements[j].text=contentSeparate[i+1];
					lvm.listelements[j].delete=false;
					j++;
				}
			}
   		},
    	readNote : function() {
    		notesFactory.getNote($routeParams.ID).then(function(response){
    			lvm.note=response;
        		lvm.oldcontent=lvm.note.content;
        		lvm.oldtitle=lvm.note.title;
    			console.log("Nota "+response.id+" leida");
    			versionsFactory.getVersions($routeParams.ID).then(function(response){
    				lvm.listversions=response;
    				console.log("Cargadas las versiones de la nota");
    			});
    			lvm.functions.separateContent();
    			if(lvm.note.type==0){
    				$location.path('/'+lvm.note.id);
    			}
    		}, function(response){
    			$location.path('/');
    			alert(response.data.userMessage);

    		});
		},
		updateColor: function(color){
			lvm.note.color=color;
			console.log("nuevo color"+ color);
			notesFactory.updateUsersNote(lvm.note).then(function(response){
				console.log("Color actualizado");
			});
		},	
		archiveNote: function(){
			lvm.note.archived=1;
			notesFactory.updateUsersNote(lvm.note).then(function(response){
				console.log("Nota archivada. Recargando notas.");
			});
		},
		unarchiveNote: function(){
			lvm.note.archived=0;
			notesFactory.updateUsersNote(lvm.note).then(function(response){
				console.log("Nota archivada. Recargando notas.");
			});
		},
		pinnedNote: function(){
			lvm.note.pinned=1;
			notesFactory.updateUsersNote(lvm.note).then(function(response){
				console.log("Nota archivada. Recargando notas.");
			});
		},
		unpinnedNote: function(){
			lvm.note.pinned=0;
			notesFactory.updateUsersNote(lvm.note).then(function(response){
				console.log("Nota archivada. Recargando notas.");
			});
		},
		loadVersion: function(idn, dateVersion){
			console.log("Intentando cargar la versión "+dateVersion+" de la nota "+idn);
			versionsFactory.getVersion(idn, dateVersion).then(function(response){
				console.log("Version cargada");
				lvm.note.content=response.content;
				lvm.note.title=response.title;
				lvm.note.modificationDate=response.modificationDate;
				lvm.functions.separateContent();
			});
		},
		setDefaultVersion: function(idn, dateVersion){
			
			angular.forEach(lvm.listversions, function(version){
				if(version.modificationDate>dateVersion){
					versionsFactory.deleteVersion(idn, version.modificationDate);
					console.log("Eliminando version"+dateVersion+" de la note"+idn);
				}
				else{
					if(version.modificationDate==dateVersion){
						lvm.note.content=version.content;
						lvm.note.title=version.title;
						lvm.note.modificationDate=version.modificationDate;
						notesFactory.updateNote(lvm.note).then(function(response){
							console.log("La nota "+idn+" ha vuelto a la version "+ dateVersion);
						})
					}
				}
			});
			lvm.functions.readNote();
		},
		deleteVersion: function(idn, dateVersion){
			versionsFactory.deleteVersion(idn,dateVersion).then(function(response){
				console.log("Version eliminada. Recargando nota.");
				lvm.functions.readNote();
			});
		},
		updateNote: function(){
			if(lvm.note.title==""){
				alert("The title can not be empty");
			}
			else{
				var content="";
				for(var i=0; i<lvm.listelements.length; i++){
					if(lvm.listelements[i].value==false){
					content=content+'0/;/'+lvm.listelements[i].text+'/;/';
					}
					else{
						content=content+'1/;/'+lvm.listelements[i].text+'/;/';
					}
				}
				lvm.note.content=content;
				if(lvm.note.title==lvm.oldtitle && lvm.note.content==lvm.oldcontent){
					console.log("No se ha modificado nada");
				}
				else{
					var date= new Date();
					lvm.note.modificationDate= date.getTime();
					console.log("Nota a escribir: ");
					console.log(lvm.note);
					if(lvm.functions.where('/addlist')){
						console.log("Creando la nota");
						notesFactory.createNote(lvm.note).then(function(response){
							console.log("Creada nota " + response);
							console.log(response);
							console.log(response.headers('Location'));
							
							var location=(response.headers('Location')).split('/');
							
							console.log(location);
							
							var idn = location[location.length-1]
							console.log("Nuevo idn es "+idn);
							lvm.note.id=idn;
							notesFactory.createUsersNote(lvm.note).then(function(response){
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
						notesFactory.updateNote(lvm.note).then(function(response){
							console.log("Actualizada la nota y creada nueva version");
						});
						if(!lvm.functions.where('/list/'+$routeParams.ID)){
							$location.path('/list/'+lvm.note.id);
						}
						else{
							lvm.functions.readNote();
						}					}
				}
			}

		},
		deleteSelectedItems: function(){
			if(lvm.note.title==""){
				alert("The title can not be empty");
			}
			else{
				var content="";
				for(var i=0; i<lvm.listelements.length; i++){
					if(lvm.listelements[i].delete==false){
						if(lvm.listelements[i].value==false){
						content=content+'0/;/'+lvm.listelements[i].text+'/;/';
						}
						else{
							content=content+'1/;/'+lvm.listelements[i].text+'/;/';
						}
					}
				}
				lvm.note.content=content;
				if(lvm.note.title==lvm.oldtitle && lvm.note.content==lvm.oldcontent){
					console.log("No se ha modificado nada");
				}
				else{
					var date= new Date();
					lvm.note.modificationDate= date.getTime();
					console.log("Nota a escribir: "+lvm.note.modificationDate);
					notesFactory.updateNote(lvm.note).then(function(response){
						console.log("Actualizada la nota y creada nueva version");
					});
				}
			}
			lvm.functions.readNote();
		},
		addElement: function(){
			lvm.listelements[lvm.listelements.length]={
					"delete":false,
					"value":false,
					"text":""
			};
		},
		newNote: function(){
			
			console.log("Creando nueva nota");
			
    		usersFactory.getUserLogin().then(function(response){
    			lvm.user=response;
				lvm.note={
						'id': 100,
			            'title' : "",
			            'content': "",
			            'ownerID': lvm.user.idu,
			            'owner': lvm.user.username,
			            'imageOwner': lvm.user.image,
			            'pinned': 0,
			            'archived': 0,
			            'color':"yellow",
			            'creationDate': (new Date()).getTime(),
			            'modificationDate':(new Date()).getTime(),
			            'type': 1,
			            'dateVersion': ""
				}
	            lvm.note.dateString= new Date(lvm.note.modificationDate);

    		});
		}
    }    
    if(!lvm.functions.where('/addlist')){lvm.functions.readNote();}
    else{lvm.functions.newNote();}
    
}])
