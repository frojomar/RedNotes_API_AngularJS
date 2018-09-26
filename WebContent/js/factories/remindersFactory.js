angular.module('rednotesApp')
.factory("remindersFactory", ['$http',function($http){
	var url = 'https://localhost:8443/RedNotes_API/rest/reminders/';
    
    var remindersInterface = {
        getReminders: function(idu){
			return $http.get(url)
				.then(function(response){
					console.log("Recuperados los recordatorios para "+idu +response.data);
					return response.data;
				});
		},
	    deleteReminder: function(idn, date){
			return $http.delete(url+idn+'/'+date)
				.then(function(response){
					console.log("Eliminado el recordatorio para la nota "+url+idn+'/'+date);
				});
	    },
	    newReminder: function(reminder){
			return $http.post(url, reminder)
			.then(function(response){
				console.log(response);
				return response.status;
			});
	    }
    }
    return remindersInterface;
}])