package es.unex.giiis.pi.rednotes.helperdao;

import java.sql.Connection;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;

import es.unex.giiis.pi.rednotes.dao.JDBCNoteDAOImpl;
import es.unex.giiis.pi.rednotes.dao.JDBCNoteVersionsDAOImpl;
import es.unex.giiis.pi.rednotes.dao.JDBCReminderDAOImpl;
import es.unex.giiis.pi.rednotes.dao.JDBCUsersNotesDAOImpl;
import es.unex.giiis.pi.rednotes.dao.NoteDAO;
import es.unex.giiis.pi.rednotes.dao.NoteVersionsDAO;
import es.unex.giiis.pi.rednotes.dao.ReminderDAO;
import es.unex.giiis.pi.rednotes.dao.UsersNotesDAO;
import es.unex.giiis.pi.rednotes.model.Note;

public class NotesComplexMethods {
	
	
	/**Attribute to set connection with the database. We need this to use DAO methods*/
	private Connection dbConnection=null;
	
	/**Attribute to show messages in the console of the server*/
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
	
	
	public NotesComplexMethods(Connection dbConnection) {
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
	
	
	public void dropNote(Integer idn, Integer idu) {
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		ReminderDAO remindersDAO= new JDBCReminderDAOImpl();
		remindersDAO.setConnection(this.dbConnection);
		
		NoteVersionsDAO versionDAO = new JDBCNoteVersionsDAOImpl();
		versionDAO.setConnection(this.dbConnection);
		
		usersnotesDAO.delete(idu, idn);
		remindersDAO.deleteAllOfUserAndNote(idu, idn);
		
		Note n=noteDAO.get(idn);
		
		if(n.getOwnerID()==idu) {
			remindersDAO.deleteAllOfNote(idn);
			usersnotesDAO.deleteAllIdN(idn);
			versionDAO.deleteAllOf(idn);
			noteDAO.delete(idn);
			logger.info("Eliminando nota "+idn+" definitivamente");
		}
		
		logger.info("Nota "+idn+" eliminada");
	}
}
