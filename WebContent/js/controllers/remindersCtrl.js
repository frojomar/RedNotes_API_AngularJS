angular.module('rednotesApp')
.controller('remindersCtrl', ['notesFactory', 'usersFactory', 'remindersFactory', '$location',
				function(notesFactory, usersFactory, remindersFactory, $location){
    var rvm = this;
    
    rvm.reminders=[];
             
    rvm.note={};
    rvm.notes=[];
    rvm.reminder={};
    
    rvm.functions = {
   		where : function(route){
   			return $location.path() == route;
   		},
    	readReminders : function() {
    		
    		usersFactory.getUserLogin().then(function(response){
    			var user=response;
    			remindersFactory.getReminders(user.idu).then(function(response){
    				
    				var reminderslist=response;
    				
    				rvm.reminders=[];
    				var j=0;
    				angular.forEach(reminderslist, function(reminder){
    					notesFactory.getNote(reminder.idn).then(function(response){
        					reminder.dateString= new Date(reminder.date);
        					console.log(reminder.dateString);	
        					reminder.title= response.title;
        					reminder.imageOwner= response.imageOwner;
    					});
    					rvm.reminders[j]=reminder;
    					j++;
    				});
    				
    			});
    		});    		
		},
		deleteReminder: function(idn, date){
			remindersFactory.deleteReminder(idn, date).then(function(response){
				console.log("Recordatorio eliminado");
			}, function(response){
				alert("We can't delete the reminder. Try later!");
			});
			rvm.functions.readReminders();
		},
		readNotes: function(){
    		notesFactory.getNotes().then(function(response){
    			rvm.notes=response;
    		});
		},
		selectNote: function(selectedNote){
    		usersFactory.getUserLogin().then(function(response){
    			var user=response;
				rvm.note=selectedNote;
				rvm.reminder={
						"idn":selectedNote.id,
						"idu":user.idu,
						"description":""
				};
    		});
		},
		addReminder: function(){
			rvm.reminder.date=rvm.reminder.dateString.getTime();
			remindersFactory.newReminder(rvm.reminder).then(function(response){
				console.log("Añadido nuevo reminder");
				$location.path('/reminders');
			}, function(response){
				alert("We can't create the reminder in this moment. Maybe you have a reminder for this note for the same date");
			});
		}
    }    
    if(rvm.functions.where('/reminders')){rvm.functions.readReminders();}
    else{rvm.functions.readNotes();}

}])
