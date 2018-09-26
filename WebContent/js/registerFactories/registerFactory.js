angular.module('registerApp')
.factory('registerFactory',['$http', function($http){
	var url = 'https://localhost:8443/RedNotes_API/rest/users/';
    var usersInterface = {
		addUser : function(user) {									
			return $http.post(url, user).then(function(response){
				return response.status;
			});
		}
    }
    return usersInterface;
}])

