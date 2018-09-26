angular.module('rednotesApp')
.factory("versionsNotesFactory", ['$http','usersFactory',
			function($http,usersFactory){
	var urlBase = 'https://localhost:8443/RedNotes_API/rest/notes/';
    
    var versionsInterface = {
    	getVersions: function(id){
	        var url= urlBase+id+'/versions';
	        
	        return $http.get(url)
	        .then(function(response){
	        	var listversions=response.data;
	        	var listversionsComplete=[];
	        	
	        	var i=0;
		        angular.forEach(listversions, function(version){
	        		usersFactory.getUsername(version.idu).then(function(response){
	        			var username=response;
	        			usersFactory.getImage(version.idu).then(function(response){
	        				  var image= response;

					          var versionComplete=version;
					          versionComplete.imageOwner=image;
					          versionComplete.owner=username;
					          versionComplete.dateString= new Date(version.modificationDate);
					          
					          listversionsComplete[i]=versionComplete;
					          i++;
	        			});
	        		});
		          
		          });
		       return listversionsComplete; 
	        });
    	},
    	getVersion: function(id, dateVersion){
	        var url= urlBase+id+'/versions/'+dateVersion;
	        
	        return $http.get(url)
	        .then(function(response){
	        	return response.data;
		       });
    	},
    	deleteVersion: function(id, dateVersion){
	        var url= urlBase+id+'/versions/'+dateVersion;
	        
	        return $http.delete(url)
	         	.then(function(response){
	         		return response.status;
	  		});
    	}
    }
    return versionsInterface;
}])
