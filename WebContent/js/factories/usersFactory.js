angular.module('rednotesApp')
.factory('usersFactory',['$http', function($http){
	var url = 'https://localhost:8443/RedNotes_API/rest/users/';
    var usersInterface = {
    	getUserLogin : function(){
            return $http.get(url + 'myself')
              	.then(function(response){
              		console.log("Usuario recuperado");
        			 return response.data;
               	});
    	},
      getUsername: function(id){
        return $http.get(url + id)
            .then(function(response){
           return response.data.username;
         });
      },
      getImage: function(id){
        return $http.get(url + id)
            .then(function(response){
           return response.data.image;
         });
      },
      updateUserLogin: function(userUpdate){
  		var urlid = url+userUpdate.idu;
        return $http.put(urlid, userUpdate)
        	.then(function(response){
  				 return response.status;
				});
      },
      deleteAccount: function(id){
    		var urlid = url+id;
          return $http.delete(urlid)
          	.then(function(response){
    				 return response.status;
  				});
        }
    }
    return usersInterface;
}])

