angular.module('rednotesApp')
.controller('perfilCtrl', ['usersFactory', 'imagesFactory', '$location', '$window', 
				function(usersFactory, imagesFactory, $location, $window){
    var pvm = this;
    
    pvm.userlogin={};
    pvm.images=[];
    pvm.oldpas1="";
    pvm.oldpas2="";
    pvm.newpas="";
    pvm.dateCreation;
    
    pvm.functions = {
    	getUserLogin : function() {
	    		usersFactory.getUserLogin().then(function(response){
	    			pvm.userlogin=response; 
	    			pvm.dateCreation=new Date(pvm.userlogin.date);
	    		});
		},
    	loadImages: function(){
    		imagesFactory.getImages().then(function(response){
    			pvm.images=response;
    		});
    	},
    	editPerfil: function(){
    		console.log("Image: "+pvm.userlogin.image);
    		console.log("User: "+pvm.userlogin);
    		if(pvm.oldpas1=="" && pvm.oldpas2=="" && pvm.newpas==""){
    			console.log("No queremos cambiar contraseña");
    		}
    		else{
    			console.log("Intentando cambiar la contraseña antigua");
    			if(pvm.oldpas1!=pvm.oldpas2){
    				console.log("Las dos repeticiones de la contraseña antigua no coinciden");
    			}
    			else{
    				pvm.userlogin.password=pvm.oldpas1+'###'+pvm.newpas;
    				console.log("Modificando la contraseña. Enviando:"+pvm.userlogin.password);
    			}
    		}
    		usersFactory.updateUserLogin(pvm.userlogin)				
	    		.then(function(response){
					console.log("Update user. Response:", response);
					$location.path('/perfil');
				}, function(response){
					console.log("Error updating user");
					alert(response.data.userMessage);
				});

    	},
    	deleteAccountAction: function(bandera){
    		
    		if(bandera==1){
    			console.log("Se ha decidido eliminar la cuenta");
	    		usersFactory.deleteAccount(pvm.userlogin.idu)				
		    		.then(function(response){
						console.log("Delete user. Response:", response);
						$window.location.href = "../LoginServlet?disconnect=true";
					}, function(response){
						console.log("Error deleting user");
						$location.path('/perfil');
					})
    		}
    		else{
    			console.log("Se ha decidido NO eliminar la cuenta");
				$location.path('/perfil');
    		}
    	}
    }    
    pvm.functions.getUserLogin();
    pvm.functions.loadImages();
}])
