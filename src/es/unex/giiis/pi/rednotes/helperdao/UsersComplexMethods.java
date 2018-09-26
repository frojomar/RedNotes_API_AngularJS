package es.unex.giiis.pi.rednotes.helperdao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;

import es.unex.giiis.pi.rednotes.dao.FriendDAO;
import es.unex.giiis.pi.rednotes.dao.JDBCFriendDAOImpl;
import es.unex.giiis.pi.rednotes.dao.JDBCNoteDAOImpl;
import es.unex.giiis.pi.rednotes.dao.JDBCNoteVersionsDAOImpl;
import es.unex.giiis.pi.rednotes.dao.JDBCReminderDAOImpl;
import es.unex.giiis.pi.rednotes.dao.JDBCUserDAOImpl;
import es.unex.giiis.pi.rednotes.dao.JDBCUsersNotesDAOImpl;
import es.unex.giiis.pi.rednotes.dao.NoteDAO;
import es.unex.giiis.pi.rednotes.dao.NoteVersionsDAO;
import es.unex.giiis.pi.rednotes.dao.ReminderDAO;
import es.unex.giiis.pi.rednotes.dao.UserDAO;
import es.unex.giiis.pi.rednotes.dao.UsersNotesDAO;
import es.unex.giiis.pi.rednotes.model.Note;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.model.UserLogin;

public class UsersComplexMethods {

	/**Attribute to set connection with the database. We need this to use DAO methods*/
	private Connection dbConnection=null;
	
	/**Attribute to show messages in the console of the server*/
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
	
	public UsersComplexMethods(Connection dbConnection) {
		this.dbConnection=dbConnection;
	}
	
	/**Method to set connection of the Database for DAO methods
	 * 
	 * @param connection of the database
	 */
	public void setConnection(Connection dbConnection) {
		this.dbConnection=dbConnection;
	}
	
	/**Method to get connection of the Database for DAO methods
	 * 
	 * @return connection of the database
	 */
	public Connection getConnection() {
		return dbConnection;
	}
	
	
	/**This method, find the user in the database. If found, return true and complete the information of user
	 * 
	 * @param user
	 * @param messages
	 * @return
	 */
	public boolean existsUser(UserLogin user,List<String> messages) {
		boolean emailExists=false;
		boolean exists=false;
		boolean passwordCorrect=false;
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);		
		
		UserLogin userOfDB=userDAO.getforLogin(user.getEmail());
		
		if(userOfDB!=null) {
			emailExists=true;
			if(userOfDB.getPassword().equals(user.getPassword())) {
				passwordCorrect=true;
				exists=true;
			}
		}
		
		/*Load the rest of data for the user instance*/
		if(exists) {
			/*Password and email is load yet*/
			user.setIdu(userOfDB.getIdu());
			user.setName(userOfDB.getName());
			user.setCountry(userOfDB.getCountry());
			user.setCity(userOfDB.getCity());
			user.setAge(userOfDB.getAge());
			user.setTelephone(userOfDB.getTelephone());
			user.setUsername(userOfDB.getUsername());
			user.setDate(userOfDB.getDate());
			user.setImage(userOfDB.getImage());
		}
		else {
			if(!emailExists) {
				exists=false;
				messages.add("This email is not register!!");
			}
			else {
				if(!passwordCorrect) {
					exists=false;
					messages.add("Password is not correct!!");
				}
			}

		}
		
		return exists;
	}

	public boolean existsUserByUsername(UserLogin user, List<String> messages) {
		boolean usernameExists=false;
		boolean exists=false;
		boolean passwordCorrect=false;
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);		
		
		UserLogin userOfDB=userDAO.getforLoginByUsername(user.getUsername());
		
		if(userOfDB!=null) {
			usernameExists=true;
			if(userOfDB.getPassword().equals(user.getPassword())) {
				passwordCorrect=true;
				exists=true;
			}
		}
		
		/*Load the rest of data for the user instance*/
		if(exists) {
			/*Password and email is load yet*/
			user.setIdu(userOfDB.getIdu());
			user.setName(userOfDB.getName());
			user.setCountry(userOfDB.getCountry());
			user.setCity(userOfDB.getCity());
			user.setAge(userOfDB.getAge());
			user.setTelephone(userOfDB.getTelephone());
			user.setEmail(userOfDB.getEmail());
			user.setDate(userOfDB.getDate());
			user.setImage(userOfDB.getImage());
		}
		else {
			if(!usernameExists) {
				exists=false;
				messages.add("This username is not register!!");
			}
			else {
				if(!passwordCorrect) {
					exists=false;
					messages.add("Password is not correct!!");
				}
			}

		}
		
		return exists;
	}
	
	/**Comprobation of username, email and password are not yet in the database*/
	public boolean dataRegisterValid(UserLogin user, List<String> messages) {
		boolean valid=true;
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		if(userDAO.existsEmail(user.getEmail())) {
			messages.add("Email already register");
			valid=false;
		}
		
		if(userDAO.existsPassword(user.getPassword())) {
			messages.add("Password is already being used");
			valid=false;
		}
		
		if(userDAO.existsUsername(user.getUsername())) {
			messages.add("Username already register");
			valid=false;
		}
		
		return valid;
	}
	
	/**Method that add a new register user to the database*/
	public long addNewUser(UserLogin user) {
		
		logger.info("DBManager: Adding user to db");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		return userDAO.add(user);
	}

	
	
	public void deleteAccount(UserLogin user) {
		boolean wellDone;
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		
		FriendDAO friendDAO= new JDBCFriendDAOImpl();
		friendDAO.setConnection(this.dbConnection);
		
		ReminderDAO remindersDAO= new JDBCReminderDAOImpl();
		remindersDAO.setConnection(this.dbConnection);
		
		NoteVersionsDAO versionDAO = new JDBCNoteVersionsDAOImpl();
		versionDAO.setConnection(this.dbConnection);
		
		/*borrar todos los recordatorios del usuario*/
		wellDone=remindersDAO.deleteAllOfUser(user.getIdu());
		
		if(wellDone)
			logger.info("ALL REMINDERS DELETED");
		
		
		/*borrar todas las notas de UserNotes asociadas al idu del user*/
		wellDone=usersnotesDAO.deleteAllIdU(user.getIdu());
		
		if(wellDone)
			logger.info("ALL NOTES SHARED DELETED");

		/*para cada nota de Notes donde el ownerID es idu..*/
		List<Note> notes= noteDAO.getAll();
		
		for(int i=0; i<notes.size(); i++) {
			Note n= notes.get(i);
			if(n.getOwnerID()==user.getIdu()) {
				/*eliminamos las apariciones de esa nota en UsersNotes*/
				usersnotesDAO.deleteAllIdN(n.getIdn());
				/*elimiamos los recordatorios de notas cuyo propietario era idu*/
				remindersDAO.deleteAllOfNote(n.getIdn());
				/*eliminamos todas las versiones de la nota*/
				versionDAO.deleteAllOf(n.getIdn());
			}
		}
		
		/*borrar todas las notas de Notes donde el owner sea el idu del user*/
		wellDone=noteDAO.deleteAllIdU(user.getIdu());
		
		if(wellDone)
			logger.info("ALL NOTES OF USER DELETED");

		/*borrar todas las amistades (confirmadas y sin confirmar) en las que aparezca el usuario*/
		wellDone=friendDAO.deleteAllOf(user.getIdu());
		
		if(wellDone)
			logger.info("ALL FRIENDSHIP RELATIONS DELETED");
		
		/*borrar el user de la tabla Users*/
		userDAO.delete(user.getIdu());
		
		logger.info("USER ACCOUNT DELETED");
	}

	public boolean validateNewPassword(UserLogin userUpdate, String oldPas) {
		
		String[] parts = userUpdate.getPassword().split("###");
		
		if(!parts[0].equals(oldPas)) {
			return false;
		}
		userUpdate.setPassword(parts[1]);
		return true;
	}
	
	public List<User> getUsersFiltered(String name, String username, String city, String country) {
		
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(this.dbConnection);
		logger.info(">>Buscando usuarios con filtro");
		String where= "WHERE";
		
		if(!name.trim().equals("") || !username.trim().equals("") || !city.trim().equals("") || !country.trim().equals("")) {
			if(name!="") {
				where= where.concat(" name LIKE '%"+name+"%'");
			}
			if( name!="" &&(username!="" || city!="" || country!="")) {
				where= where.concat(" AND ");
	
			}
			if(username!="") {
				where= where.concat(" username LIKE '%"+username+"%'");
			}
			if(username!="" &&(city!="" || country!="")) {
				where= where.concat(" AND ");
	
			}
			if(city!="") {
				where= where.concat(" city LIKE '%"+city+"%'");
			}
			if(city!="" && country!="") {
				where= where.concat(" AND ");
	
			}
			if(country!="") {
				where= where.concat(" country LIKE '%"+country+"%'");
			}
			
			
			logger.info("WHERE para buscar: "+where);
			List<User> users= userDAO.getAllFiltered(where);
			return users;
		}
		else {
			return new ArrayList<User>();
		}
	}
}
