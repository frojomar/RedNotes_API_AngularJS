package es.unex.giiis.pi.rednotes.resources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import es.unex.giiis.pi.rednotes.dao.JDBCNoteDAOImpl;
import es.unex.giiis.pi.rednotes.dao.JDBCReminderDAOImpl;
import es.unex.giiis.pi.rednotes.dao.JDBCUsersNotesDAOImpl;
import es.unex.giiis.pi.rednotes.dao.NoteDAO;
import es.unex.giiis.pi.rednotes.dao.ReminderDAO;
import es.unex.giiis.pi.rednotes.dao.UsersNotesDAO;
import es.unex.giiis.pi.rednotes.model.Note;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.model.UsersNotes;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomBadRequestException;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomNotFoundException;


@Path("/usersnotes")
public class UsersNotesResource {
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
	  @Context
	  ServletContext sc;
	  @Context
	  UriInfo uriInfo;
	  

	  /**
	   * This method, it's called when logged user needs the information that depends on him of ALL HIS NOTES. 
	   * 
	   * In summary, this method returns part of the information (the one that is stored in 
	   * UsersNotes, for each note and user) of ALL the notes that the user has (BE IT HIS OR SHARE WITH HIM).
	   * 
	   * @param request
	   * @return List of UsersNote instances, with all the notes of the logged user
	   */
	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<UsersNotes> getUsersNotesJSON(@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UsersNotesDAO usersnotesDAO = new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
		List<UsersNotes> usersnotes;
		
		usersnotes= new ArrayList<UsersNotes>();
		if(userLogin!=null) {
			usersnotes = usersnotesDAO.getAllByUser(userLogin.getIdu());
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
		}
		
	    return usersnotes; 
	  }
	  
	  /**
	   * This method, it's called when logged user needs the information that depends on him of ALL HIS NOTES. 
	   * 
	   * In summary, this method returns part of the information (the one that is stored in 
	   * UsersNotes, for each note and user) of ALL the notes that the user has (BE IT HIS OR SHARE WITH HIM).
	   * 
	   * @param request
	   * @return List of UsersNote instances, with all the notes of the logged user
	   */
	  @GET
	  @Path("/sharedidus/{idn: [0-9]+}")
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<Integer> getUsersShared(@PathParam("idn") long idn, @Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");

		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(conn);
		
		UsersNotesDAO usersnotesDAO = new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(conn);
		
		
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
		List<UsersNotes> usersnotes;
		
		usersnotes= new ArrayList<UsersNotes>();
		
		List<Integer> ids= new ArrayList<Integer>();
		
		if(userLogin!=null) {
			if(noteDAO.get(idn).getOwnerID()==userLogin.getIdu()) {
				usersnotes = usersnotesDAO.getAllByNote(idn);
				for(int i=0; i<usersnotes.size(); i++) {
					ids.add(usersnotes.get(i).getIdu());
				}
			}
			else {
				throw new CustomBadRequestException("You are not the owner of this note");
			}
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			 throw new CustomNotFoundException("We don't have information of the logged user");
		}
		
	    return ids; 
	  }
	  
	  /**
	   * This method, it's called when logged user needs the information that depends on him of ONE OF HIS NOTES. 
	   * 
	   * In summary, this method returns part of the information (the one that is stored in 
	   * UsersNotes, for each note and user) of A NOTE that the user has (BE IT HIS OR SHARE WITH HIM).
	   * 
	   * @param idn id of the UsersNote we want to return in the JSON
	   * @param request
	   * @return usersNote instance, with the information of the note 'idn' for logged user
	   */
	  @GET
	  @Path("/{idn: [0-9]+}")	  
	  @Produces(MediaType.APPLICATION_JSON)
	  public UsersNotes getUsersNoteJSON(@PathParam("idn") long idn,
			  					@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UsersNotesDAO usersnotesDAO = new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
		
		if(userLogin != null) {
			UsersNotes usersnote= usersnotesDAO.get(userLogin.getIdu(), idn);
			
			if (usersnote!=null) return usersnote;
			else throw new CustomNotFoundException("This note doesn't exist to the logged user");
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	  }
	  
	/**
	 * This method, it's called when the logged user want to created a new UsersNote.
	 * 
	 * This method, it's useful when you share a note with a new user, or when you create a new Note (because,
	 * with the creation of a new entry for Note, you need to add a entry in UsersNotes, were the idn was the
	 * id of this new note, and the idu the id of the owner of the Note, the person that is creating this Note).
	 * 
	 * The idu (in JSON) must be the id of the new user that have access to this note.
	 * 
	 * The idn (in JSON) must be the id of the Note associted with this entry.
	 * 
	 * Idu and Idn must exists previously in their DAOs, if you want a good working of the API.
	 * 
	 * All the fields must be not empty, and archived and pinned only can be 0 or 1
	 * 
	 * @param newUsersNote UsersNote instance created with the JSON information
	 * @param request
	 * @return
	 * @throws Exception
	 */
	  @POST	  	  
	  @Consumes(MediaType.APPLICATION_JSON)
	  public Response post(UsersNotes newUsersNote, @Context HttpServletRequest request) throws Exception {	
		Connection conn = (Connection) sc.getAttribute("dbConn");
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(conn);
		
		UsersNotesDAO usersnotesDAO = new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		
		User user= (User) session.getAttribute("userlogin");
		
		Response res;
				
		List<String> messages= new ArrayList<String>();
		
		if(user!=null) {
			if (!newUsersNote.validate(messages))
				throw new CustomBadRequestException("Errors in data of JSON");
			//save user in DB
			else {
				Note n= noteDAO.get(newUsersNote.getIdn());
				logger.info("ID de la nota "+newUsersNote.getIdn());
				logger.info("ID del usuario "+user.getIdu());
				logger.info("ID de del dueño de la nota "+n.getOwnerID());
				if(n.getOwnerID()!=user.getIdu()) {
					throw new CustomBadRequestException("You are not the owner of the note");
				}
				else {				
					boolean ok=usersnotesDAO.add(newUsersNote);
						
					if(ok) {
					res = Response //return 201 and Location: /notes/idn
						   .created(
							uriInfo.getAbsolutePathBuilder().path(Long.toString(newUsersNote.getIdn()))
								   .build())
						   .contentLocation(
							uriInfo.getAbsolutePathBuilder().path(Long.toString(newUsersNote.getIdn()))
							       .build())
							.build();
				    return res;
					}
					else {
						throw new CustomBadRequestException("We cannot create the entry of this note for this user. Retry in other moment");
					}
				}

			}	
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	  }
	  
	  
	/** This method, it's called when the logged user want to update a UserNotes. With this action, logged user
	 * can change information of the note that only affect to him (color, archived, pinned). If the logged user
	 * want to change information like content or title (that affect to all users that have access to this Note), he
	 * must use /notes/idn recourse (with the HTTP verb PUT).
	 * 
	 * The idu must be the idu of the logged user (you only can modify info for you)
	 *
	 * The idn must be the id of the Note that we want to modify (in part). The idn of parameter and the idn of
	 * JSON, must be the same value.
	 * 
	 * All the fields must be not empty, and archived and pinned only can be 0 or 1
	 * 
	 * 
	 * @param usersNoteUpdate UsersNote instance created with the JSON information
	 * @param idn id of the UsersNote to update. It must exist!
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PUT
	@Path("/{idn: [0-9]+}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put(UsersNotes usersNoteUpdate,
						@PathParam("idn") long idn,
						@Context HttpServletRequest request) throws Exception{
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UsersNotesDAO usersnotesDAO = new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(conn);
			  
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("userlogin");
		
		List<String> messages= new ArrayList<String>();
		
		if (!usersNoteUpdate.validate(messages))
			throw new CustomBadRequestException("Errors in data of JSON");
		//save user in DB
		else {
			Response response = null;
			
			if(idn==usersNoteUpdate.getIdn()) {
				//We check that the order exists
				if(user != null) {
					if(user.getIdu()==usersNoteUpdate.getIdu()) {
						UsersNotes usersnote= usersnotesDAO.get(user.getIdu(), idn);
						
						if (usersnote!=null) {
							usersnotesDAO.save(usersNoteUpdate);
						}
						else throw new WebApplicationException(Response.Status.NOT_FOUND);
					}
					else {
						throw new CustomNotFoundException("Idu of the JSON must be your idu (modify UserNote for you)");
					}
				}	
				else {
					logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
					throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
				}
			}
			else {
				throw new CustomNotFoundException("Idn of parameter and JSON must be the same value");
			}
						  
			return response;
		}
	}
	  
	  
	  

	/**This method is called when the logger user want to delete a note that the user has (BE IT HIS OR 
	 *  SHARE WITH HIM) and the reminders associated (you don't need to call the DELETE of /reminders)
	 * 
	 * If logged user delete a note where he is the owner, the correct working implies that all UsersNotes where
	 * idn it's like idn of the UsersNote to delete must be delete.
	 * 
	 * In summary, if idu of UsersNote is the ownerID of the Note, we delete all UsersNote entries of this Note. If
	 * idu of UsersNote is not the ownerId of the Note, we only delete the UsersNote entry idn-idu.
	 * 
	 * 
	 * The idn must be the id of the Note that we want to modify (in part). The idn of parameter and the idn of
	 * JSON, must be the same value.
	 * 
	 * ADVICE: To a good working, if you are the owner of the note, you must call following of this 
	 * method to the DELETE method of notes/idn (because this Note entry, if you don't delete, will remain
	 *  as residual waste, as there is no entry in UsersNotes to access it)
	 * 
	 * @param ind id of the note. We use this, with idu of logged user, to delete entries in that method.
	 * @param request
	 * @return
	 */
	@DELETE
	@Path("/{idn: [0-9]+}")
	public Response deleteNote(@PathParam("idn") long idn,
				  					  @Context HttpServletRequest request) {
			  
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UsersNotesDAO usersnotesDAO = new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(conn);
						
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(conn);
		
		ReminderDAO reminderDAO= new JDBCReminderDAOImpl();
		reminderDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
				
		if(userLogin != null) {
			
			UsersNotes usersnote = usersnotesDAO.get(userLogin.getIdu(), idn);
			
			if (usersnote!=null) {
				Note note= noteDAO.get(idn);
				
				if(note!=null) {
					if(note.getOwnerID()==userLogin.getIdu()) {//logged user is the owner of the Note
						usersnotesDAO.deleteAllIdN((int)idn);
						reminderDAO.deleteAllOfNote((int)idn);
						noteDAO.delete((int)idn);
					}
					else { //logged user is not the owner of the Note associate with this UsersNote
						usersnotesDAO.delete(userLogin.getIdu(), idn);
						reminderDAO.deleteAllOfUserAndNote(userLogin.getIdu(),(int) idn);
					}
					return Response.noContent().build(); //204 no content 
				}
				else throw new CustomNotFoundException("This note doesn't exist");
			}
			else throw new CustomNotFoundException("This note doesn't exist for the logged user");
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	}	
	
}
