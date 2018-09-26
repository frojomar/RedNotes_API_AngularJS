package es.unex.giiis.pi.rednotes.helperdao;

import java.sql.Connection;
import java.util.List;
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
import es.unex.giiis.pi.rednotes.model.UsersNotes;

public class FriendComplexMethods {
	
	
	/**Attribute to set connection with the database. We need this to use DAO methods*/
	private Connection dbConnection=null;
	
	/**Attribute to show messages in the console of the server*/
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
	
	public FriendComplexMethods(Connection dbConnection) {
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
	


	
	/**Method that delete the notes that "id" shared with "idu" of "idu" notes collection. Also, delete the notes
	 * that "idu" shared with "id" of "id" notes collection.
	 * 
	 * @param idu
	 * @param id
	 */
	public void deleteSharedNotesWith(Integer idu, Integer id) {
		
		UsersNotesDAO usersnotesDAO= new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(this.dbConnection);
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(this.dbConnection);
		
		NotesComplexMethods notesMethods= new NotesComplexMethods(this.dbConnection);
		
		/*cogemos la coleccion de notas de idu*/
		List<UsersNotes> notes= usersnotesDAO.getAllByUser(idu);
		
		Note n;
		/*para cada nota de la lista...*/
		for(int i=0; i<notes.size(); i++) {
			n=noteDAO.get(notes.get(i).getIdn());
			/*si esta nota es de idu, la eliminamos en id (si es que existe)*/
			if(n.getOwnerID()==idu) {
				usersnotesDAO.delete(id, n.getIdn());
				notesMethods.dropNote(n.getIdn(), id);
			}
			/*si esta nota es de id, la eliminamos en idu*/
			if(n.getOwnerID()==id) {
				usersnotesDAO.delete(idu, n.getIdn());
				notesMethods.dropNote(n.getIdn(), idu);
			}
		}

		logger.info("ELIMINADAS TODAS LAS NOTAS COMPARTIDAS ENTRE "+idu+" Y "+id);
	}
}
