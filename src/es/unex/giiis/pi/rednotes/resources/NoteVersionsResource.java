package es.unex.giiis.pi.rednotes.resources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import es.unex.giiis.pi.rednotes.dao.JDBCNoteVersionsDAOImpl;
import es.unex.giiis.pi.rednotes.dao.NoteVersionsDAO;
import es.unex.giiis.pi.rednotes.model.NoteVersion;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomBadRequestException;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomNotFoundException;

@Path("/notes/{idn: [0-9]+}/versions")
public class NoteVersionsResource {

	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
	  @Context
	  ServletContext sc;
	  @Context
	  UriInfo uriInfo;
	  
	  
	  
	  /**
	   * 
	   * This method, it's called when we need the information of all versions of a note 'idn'.
	   * 
	   * @param idn ID of the note, to get all versions of that note.
	   * @param request
	   * @return List of NoteVersion, with all the versions of a Note 'idn'
	   */
	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<NoteVersion> getVersionNoteJSON(@PathParam("idn") long idn, @Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteVersionsDAO noteversionsDAO = new JDBCNoteVersionsDAOImpl();
		noteversionsDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
		List<NoteVersion> noteversions;
		
		noteversions= new ArrayList<NoteVersion>();
		if(userLogin!=null) {
			noteversions = noteversionsDAO.getAllBy((int)idn);
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
		}
		
	    return noteversions; 
	  }
	  
	  
	  
	  /**
	   * This method, it's called when we need the information of a specific version of a note
	   * 
	   * @param idn id of the note we want to return in the JSON
	   * @param modDate int for convert to Date (Java.util.Date) 
	   * 	that identify the version of the note idn that we want to return
	   * @param request
	   * @return NoteVersion of the Note 'idn' with modification Date like 'modDate' 
	   */
	  @GET
	  @Path("/{modDate: [0-9]+}")	  
	  @Produces(MediaType.APPLICATION_JSON)
	  public NoteVersion getVersionNoteJSON(@PathParam("idn") long idn, @PathParam("modDate") long modDateInt,
			  					@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteVersionsDAO noteversionsDAO = new JDBCNoteVersionsDAOImpl();
		noteversionsDAO.setConnection(conn);
		
		Date modDate= new Date(modDateInt);
		
		logger.info("Fecha: "+modDate+" - entero:"+modDateInt);
		
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
		
		if(userLogin != null) {
			NoteVersion noteversion= noteversionsDAO.get((int)idn,modDate);
			
			if (noteversion!=null) return noteversion;
			else throw new CustomNotFoundException("This version of this note doesn't exist");
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	  }
	  
	/**
	 * This method, it's called when the logged user want to created a new version of a Note. This note must
	 * be of himself, or it must be shared with him.
	 * 
	 * The 'idu' must be the idu of the logged user
	 * 
	 * All the fields must be not empty.	 * 
	 * 
	 * @param newNoteVersion NoteVersion instance created with the JSON information
	 * @param request
	 * @return
	 * @throws Exception
	 */
	  @POST	  	  
	  @Consumes(MediaType.APPLICATION_JSON)
	  public Response post(NoteVersion newNoteVersion, @Context HttpServletRequest request) throws Exception {	
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteVersionsDAO noteversionsDAO = new JDBCNoteVersionsDAOImpl();
		noteversionsDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		
		User user= (User) session.getAttribute("userlogin");
		
		Response res;
				
		List<String> messages= new ArrayList<String>();
		
		if(user!=null) {
			if (!newNoteVersion.validate(messages))
				throw new CustomBadRequestException("Errors in data of JSON");
			//save user in DB
			else {
				if(newNoteVersion.getIdu()!=user.getIdu()) {
					throw new CustomBadRequestException("You must put your id in the idu of JSON");
				}
				else {
					
					if(noteversionsDAO.get(newNoteVersion.getIdn(), newNoteVersion.getModificationDate())==null) {
						boolean ok=noteversionsDAO.add(newNoteVersion);
						
						if(ok) {
						res = Response //return 201 and Location: /notes/idn
								   .created(
									uriInfo.getAbsolutePathBuilder()//.path(Long.toString(Long.parseLong(newNoteVersion.getModificationDate()))
										   .build())
								   .contentLocation(
									uriInfo.getAbsolutePathBuilder()
									       .build())
									.build();
						    return res;
						}
						else {
							throw new CustomBadRequestException("We cannot create the version of note. Retry in other moment");
						}
					}
					else {
						throw new CustomBadRequestException("This version of this note exists yet. We cannot create twice");	
					}
				}
			}	
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	  }
	  

	/**This method is called when the logger user want to delete a version of a note.
	 * 
	* 
	* @param idn id of the note to delete
	* @param modDateInt int to convert to Date. This is the modificationDate of the version to delete
	* @param request
	* @return
	*/
	@DELETE
	@Path("/{modDate: [0-9]+}")
	public Response deleteNote(@PathParam("idn") long idn, @PathParam("modDate") long modDateInt,
				  					  @Context HttpServletRequest request) {
			  
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteVersionsDAO noteversionsDAO = new JDBCNoteVersionsDAOImpl();
		noteversionsDAO.setConnection(conn);
		
		Date modDate= new Date(modDateInt);
								  
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
				
		if(userLogin != null) {
			NoteVersion noteversion= noteversionsDAO.get((int)idn, modDate);
			
			if (noteversion!=null) {
				noteversionsDAO.delete((int)idn, modDate);
				return Response.noContent().build(); //204 no content 
			}
			else throw new CustomNotFoundException("This version of note doesn't exist");
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	}	
	
	/**This method is called when the logger user want to delete ALL version of a note.
	 * 
	* 
	* @param idn id of the note to delete all versions
	* @param request
	* @return
	*/
	@DELETE
	public Response deleteNote(@PathParam("idn") long idn,
				  					  @Context HttpServletRequest request) {
			  
		Connection conn = (Connection) sc.getAttribute("dbConn");
		NoteVersionsDAO noteversionsDAO = new JDBCNoteVersionsDAOImpl();
		noteversionsDAO.setConnection(conn);
										  
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
				
		if(userLogin != null) {
			List<NoteVersion> noteversions= noteversionsDAO.getAllBy((int)idn);
			
			if (noteversions!=null) {
				noteversionsDAO.deleteAllOf((int) idn);
				return Response.noContent().build(); //204 no content 
			}
			else throw new CustomNotFoundException("There are not any version for the note"+ idn);
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	}

}
