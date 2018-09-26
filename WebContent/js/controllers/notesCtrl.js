angular.module('rednotesApp')
.controller('notesCtrl', ['notesFactory', 'usersFactory', 'filterNotesParametersFactory','$routeParams', '$location',
				function(notesFactory, usersFactory, filterNotesParametersFactory, $routeParams, $location){
    var nvm = this;
    nvm.notes=[];
    nvm.anchorednotes=[];
    nvm.archivednotes=[];
    nvm.notanchorednotes=[];
    
    nvm.orderby;
    nvm.show1;
    nvm.show2;
    nvm.colorfilter;
    
    nvm.link="#!/";
    
    nvm.user;
        
    nvm.functions = {
   		where : function(route){
   			return $location.path() == route;
   		},
    	readNotes : function() {
    /*		notesFactory.getNotes()
				.then(function(response){
	    			console.log("Reading all the notes: ", response);
	    			nvm.notes = response;
	    			
	    			if(nvm.notes.length==0){
	    				console.log("notas vacia");
	    			}
	    			
	    			
				    nvm.functions.filterNotes();
	        		

	    		}, function(response){
	    			console.log("Error reading notes");
	    		});*/
    		
	    		notesFactory.getNotesArchived().then(function(response){
	    			nvm.archivednotes=response;
	    		});
	    		notesFactory.getNotesPinned().then(function(response){
	    			nvm.anchorednotes=response;
	    		});    		
	    		notesFactory.getNotesNotPinned().then(function(response){
	    			nvm.notanchorednotes=response;
	    		});	    		
		},
		loadFilters: function(){
			console.log("Cargando los filtros");

			usersFactory.getUserLogin().then(function(response){
				nvm.user=response;
			});
			
			nvm.orderby=filterNotesParametersFactory.getOrderby();
		    nvm.show1=filterNotesParametersFactory.getShow1();
		    nvm.show2=filterNotesParametersFactory.getShow2();
		    nvm.colorfilter=filterNotesParametersFactory.getColorfilter();
		},
		saveFilters: function(){
			filterNotesParametersFactory.setOrderby(nvm.orderby);
		    filterNotesParametersFactory.setShow1(nvm.show1);
		    filterNotesParametersFactory.setShow2(nvm.show2);
		    filterNotesParametersFactory.setColorfilter(nvm.colorfilter);
		},
		archiveNote: function(note){
			note.archived=1;
			notesFactory.updateUsersNote(note).then(function(response){
				console.log("Nota archivada. Recargando notas.");
			    nvm.functions.readNotes();
			}).then(function(response){
				console.log("No se ha podido archivar la nota.")
			});
		},
		unarchiveNote: function(note){
			note.archived=0;
			notesFactory.updateUsersNote(note).then(function(response){
				console.log("Nota archivada. Recargando notas.");
			    nvm.functions.readNotes();
			}).then(function(response){
				console.log("No se ha podido archivar la nota.")
			});
		},
		pinnedNote: function(note){
			note.pinned=1;
			notesFactory.updateUsersNote(note).then(function(response){
				console.log("Nota archivada. Recargando notas.");
			    nvm.functions.readNotes();
			}).then(function(response){
				console.log("No se ha podido archivar la nota.")
			});
		},
		unpinnedNote: function(note){
			note.pinned=0;
			notesFactory.updateUsersNote(note).then(function(response){
				console.log("Nota archivada. Recargando notas.");
			    nvm.functions.readNotes();
			}).then(function(response){
				console.log("No se ha podido archivar la nota.")
			});
		},
		noteTypeLink: function(id, type){
			if(type==0){
				nvm.link='#!/'+id;
			}
			else{
				nvm.link='#!/list/'+id;
			}
		},
		searchNotes: function(search){
			console.log("Buscando notas por: "+search);
			
    		notesFactory.getSearchedNotes(search).then(function(response){
    			nvm.notes=response;
    		});
    		
		}
    }    
    if(nvm.functions.where('/searchnotes/'+$routeParams.SEARCH)){
    	nvm.functions.searchNotes($routeParams.SEARCH);
    }else{
        nvm.functions.loadFilters();
        nvm.functions.readNotes();
    }


}])
