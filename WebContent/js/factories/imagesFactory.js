angular.module('rednotesApp')
.factory('imagesFactory',['$http', function($http){
	var url = 'https://localhost:8443/RedNotes_API/rest/images/';
    var imagesInterface = {
    	getImages : function(){
            return $http.get(url)
              	.then(function(response){
        			 return response.data;
               	});
    	}
    }
    return imagesInterface;
}])

