angular.module('rednotesApp')
.filter('filterReminders', [
		function(){
	
    
	return function(input) {
		
    	var date= (new Date()).getTime();
    	
	    
	    var out=[];
	   
	    angular.forEach(input, function(reminder) {
	    	
	    	if(reminder.date>=date){
	    		out.push(reminder);
	    	}
	    })


	    return out;
	}
}]);