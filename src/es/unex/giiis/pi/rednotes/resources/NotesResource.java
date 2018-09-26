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
import es.unex.giiis.pi.rednotes.dao.JDBCNoteVersionsDAOImpl;
import es.unex.giiis.pi.rednotes.dao.JDBCUsersNotesDAOImpl;
import es.unex.giiis.pi.rednotes.dao.NoteDAO;
import es.unex.giiis.pi.rednotes.dao.NoteVersionsDAO;
import es.unex.giiis.pi.rednotes.dao.UsersNotesDAO;
import es.unex.giiis.pi.rednotes.helperdao.NotesComplexMethods;
import es.unex.giiis.pi.rednotes.model.Note;
import es.unex.giiis.pi.rednotes.model.NoteVersion;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomBadRequestException;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomNotFoundException;

@Path("/notes")
public class NotesResource {
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
	  @Context
	  ServletContext sc;
	  @Context
	  UriInfo uriInfo;
	  
	  
	  /**
	   * This method, it's called when we need the information of a specific note
	   * 
	   * @param idn id of the note we want to return in the JSON
	   * @param request
	   * @return
	   */
	  @GET
	  @Path("/{idn: [0-9]+}")	  
	  @Produces(MediaType.APPLICATION_JSON)
	  public Note getNoteJSON(@PathParam("idn") long idn,
			  					@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(conn);
		
		UsersNotesDAO usersnotesDAO = new JDBCUsersNotesDAOImpl();
		usersnotesDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
		
		if(userLogin != null) {
			
			if(usersnotesDAO.get(userLogin.getIdu(), (int)idn)!=null) {
				Note note= noteDAO.get(idn);
				
				if (note!=null) return note;
				else throw new CustomNotFoundException("This note doesn't exist");
			}
			else {
				throw new CustomNotFoundException("You don't have access to this note");
			}
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	  }
	  
	/**
	 * This method, it's called when the logged user want to created a new Note, where he is the owner. Also, create
	 *  the first version of note.
	 * 
	 * The ownerID must be the idu of the logged user
	 * 
	 * All the fields must be not empty, and type only can be 0 or 1
	 * 
	 * IDN can have any value, because this value will be changed by the DAO
	 * 
	 * You must call be POST of /usersnotes after, to have access to the note with the owner.
	 * 
	 * 
	 * @param newNote Note instance created with the JSON information
	 * @param request
	 * @return
	 * @throws Exception
	 */
	  @POST	  	  
	  @Consumes(MediaType.APPLICATION_JSON)
	  public Response post(Note newNote, @Context HttpServletRequest request) throws Exception {	
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(conn);
		
		NoteVersionsDAO versionDAO = new JDBCNoteVersionsDAOImpl();
		versionDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		
		User user= (User) session.getAttribute("userlogin");
		
		Response res;
				
		List<String> messages= new ArrayList<String>();
		
		if(user!=null) {
			if (!newNote.validate(messages))
				throw new CustomBadRequestException(messages.get(0));
			//save user in DB
			else {
				if(newNote.getOwnerID()!=user.getIdu()) {
					throw new CustomBadRequestException("You only can create notes where you are the owner");
				}
				else {
					long idn=noteDAO.add(newNote);
					
					if(idn>0) {
						NoteVersion nv= new NoteVersion();
						nv.setIdn((int)idn);
						nv.setIdu(newNote.getOwnerID());
						nv.setModificationDate(newNote.getModificationDate());
						nv.setTitle(newNote.getTitle());
						nv.setContent(newNote.getContent());
						
						versionDAO.add(nv);
						
						res = Response //return 201 and Location: /notes/idn
								   .created(
									uriInfo.getAbsolutePathBuilder().path(Long.toString(idn))
										   .build())
								   .contentLocation(
									uriInfo.getAbsolutePathBuilder().path(Long.toString(idn))
									       .build())
									.build();
						    return res;
					}
					else {
						throw new CustomBadRequestException("We cannot create the note. Retry in other moment");
					}
				}
			}	
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	  }
	  
	  
	/** This method, it's called when the logged user want to update a note and create a new version
	* 
	 * The ownerID must be the idu of the logged user
	 * 
	 * All the fields must be not empty, and type only can be 0 or 1
	 * 
	 * IDN of JSON and parameter must be the same value
	* 
	* @param noteUpdate Note instance created with the JSON information
	* @param idn id of the note to update. It must exist!
	* @param request
	* @return
	* @throws Exception
	*/
	@PUT
	@Path("/{idn: [0-9]+}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put(Note noteUpdate,
						@PathParam("idn") long idn,
						@Context HttpServletRequest request) throws Exception{
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(conn);
			  
		NoteVersionsDAO versionDAO = new JDBCNoteVersionsDAOImpl();
		versionDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("userlogin");
		
		List<String> messages= new ArrayList<String>();
		
		if (!noteUpdate.validate(messages))
			throw new CustomBadRequestException("Errors in data of JSON");
		//save user in DB
		else {
			Response response = null;
			if(idn==noteUpdate.getIdn()) {
				//We check that the order exists
				if(user != null) {
					Note note= noteDAO.get(idn);
					
					if (note!=null) {
						noteDAO.save(noteUpdate);
						
						NoteVersion nv= new NoteVersion();
						nv.setIdn((int)idn);
						nv.setIdu(user.getIdu());
						nv.setModificationDate(noteUpdate.getModificationDate());
						nv.setTitle(noteUpdate.getTitle());
						nv.setContent(noteUpdate.getContent());
						
						versionDAO.add(nv);
					}
					else throw new WebApplicationException(Response.Status.NOT_FOUND);			
				}	
				else {
					logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
					throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
				}
							  
				return response;
			}
			else {
				throw new CustomBadRequestException("Error in IDN. IDN if parameter must be the same of IDN  JSON");
			}
		}
	}
	  
	  
	  

	/**This method is called when the logger user want to delete a note.
	 * 
	 * The idn, must be of a note where logger user is the owner
	* 
	* @param ind id of the note to delete
	* @param request
	* @return
	*/
	@DELETE
	@Path("/{idn: [0-9]+}")
	public Response deleteNote(@PathParam("idn") long idn,
				  					  @Context HttpServletRequest request) {
			  
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(conn);
				
		NotesComplexMethods notesMethods= new NotesComplexMethods(conn);
		
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
				
		if(userLogin != null) {
			Note note= noteDAO.get(idn);
			
			if (note!=null) {
				if(note.getOwnerID()==userLogin.getIdu()) {
					notesMethods.dropNote((int)idn, userLogin.getIdu());
					return Response.noContent().build(); //204 no content 
				}
				else throw new CustomNotFoundException("You only can't delete definitively the notes where you are the owner");
			}
			else throw new CustomNotFoundException("This note doesn't exist");
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	}	
	
}
