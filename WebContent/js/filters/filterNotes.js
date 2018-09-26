angular.module('rednotesApp')
.filter('filterNotes', ['filterNotesParametersFactory', 'usersFactory', 
		function(filterNotesParametersFactory, usersFactory){
	
    var user;
    usersFactory.getUserLogin().then(function(response){
    	user=response;
    })
    
	return function(input) {
		
		var orderby=filterNotesParametersFactory.getOrderby();
	    var show1=filterNotesParametersFactory.getShow1();
	    var show2=filterNotesParametersFactory.getShow2();
	    var colorfilter=filterNotesParametersFactory.getColorfilter();

	    
	    var out=[];
	    
	    angular.forEach(input, function(note) {
	    	
	    	var mostrar=true;
	    	
		    if(show1=="notesandlists" && show2=="all" && colorfilter=="allcolors"){
	          mostrar=true;
	        }
		    else{
		    	switch (show1){
			    case "notesandlists":
			    	break;
			    case "lists":
			    	if(note.type!=1){
			    		mostrar=false;
			    	}
			    	break;
			    case "notes":
			    	if(note.type!=0){
			    		mostrar=false;
			    	}
			    	break;
			    	
			    }
			    
			    switch (show2){
			    case "all":
			    	break;
			    case "my":
			    	if(user.idu!=note.ownerID){
			    		mostrar=false;
			    	}
			    	break;
			    case "friends":
			    	if(user.idu==note.ownerID){
			    		mostrar=false;
			    	}
			    	break;
			    	
			    }
			    
			    switch (colorfilter){
			    case "allcolors":
			    	break;
			    default:
			    	if(colorfilter!=note.color){
			    		mostrar=false;
			    	}
			    	break;
			    }
		    }
		    
		    
		    if(mostrar==true){
		    	out.push(note);
		    }	

	    })


	    return out;
	}
}]);