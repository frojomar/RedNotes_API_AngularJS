angular.module('rednotesApp')
.factory('filterNotesParametersFactory', ['$http',function($http){
	  var orderby="creationDate";
	  var show1="notesandlists";
	  var show2="all";
	  var colorfilter="allcolors";
	  
	  var filternotesParametersInterface = {
	    		getOrderby: function(){
	        		return orderby;
	        	},
	        	getShow1: function(){
	        		return show1;
	        	},
	        	getShow2: function(){
	        		return show2;
	        	},
	        	getColorfilter: function(param){
	        		return colorfilter;
	        	},
	    		setOrderby: function(param){
	        		orderby=param;
	        	},
	        	setShow1: function(param){
	        		show1=param;
	        	},
	        	setShow2: function(param){
	        		show2=param;
	        	},
	        	setColorfilter: function(param){
	        		colorfilter=param;
	        	}
	    }
	    return filternotesParametersInterface;
}])