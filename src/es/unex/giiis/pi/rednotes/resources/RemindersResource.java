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

import es.unex.giiis.pi.rednotes.dao.JDBCReminderDAOImpl;
import es.unex.giiis.pi.rednotes.dao.ReminderDAO;
import es.unex.giiis.pi.rednotes.model.Reminder;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomBadRequestException;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomNotFoundException;

@Path("/reminders")
public class RemindersResource {

	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
	  @Context
	  ServletContext sc;
	  @Context
	  UriInfo uriInfo;
	  
	  
	  
	  /**
	   * 
	   * This method, it's called when we need the information of all reminders of logged user
	   * 
	   * @param request
	   * @return List of Reminders of a user
	   */
	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<Reminder> getRemindersUserJSON(@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		ReminderDAO reminderDAO= new JDBCReminderDAOImpl();
		reminderDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
		List<Reminder> reminders;
		
		reminders= new ArrayList<Reminder>();
		if(userLogin!=null) {
			reminders = reminderDAO.getAllBy(userLogin.getIdu());
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
		
	    return reminders; 
	  }	  
	  
	  
	/**
	 * This method, it's called when the logged user want to created a new reminder to a note, in a specific date.
	 * 
	 * Can not there are two reminders for the same note in the same date
	 * 
	 * The 'idu' must be the idu of the logged user
	 * 
	 * IDN, IDU and DATE can not be empty.
	 * 
	 * @param newReminder Reminder instance created with the JSON information
	 * @param request
	 * @return
	 * @throws Exception
	 */
	  @POST	  	  
	  @Consumes(MediaType.APPLICATION_JSON)
	  public Response post(Reminder newReminder, @Context HttpServletRequest request) throws Exception {	
		Connection conn = (Connection) sc.getAttribute("dbConn");
		ReminderDAO reminderDAO= new JDBCReminderDAOImpl();
		reminderDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		
		User user= (User) session.getAttribute("userlogin");
		
		Response res;
				
		List<String> messages= new ArrayList<String>();
		
		if(user!=null) {
			if (!newReminder.validate(messages))
				throw new CustomBadRequestException("Errors in data of JSON");
			//save user in DB
			else {
				if(newReminder.getIdu()!=user.getIdu()) {
					throw new CustomBadRequestException("You must put your id in the idu of JSON");
				}
				else {
					boolean ok=reminderDAO.add(newReminder);
					
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
						throw new CustomBadRequestException("There are a reminder of this note in this date yet. We cannot create twice");	
					}
				}
			}	
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	  }
	  

	/**This method is called when the logged user want to delete ALL reminders of a note.
	 * 
	* 
	* @param idn id of the note to delete reminders
	* @param request
	* @return
	*/
	@DELETE
	@Path("/{idn: [0-9]+}")
	public Response deleteNoteReminders(@PathParam("idn") long idn,
				  					  @Context HttpServletRequest request) {
			  
		Connection conn = (Connection) sc.getAttribute("dbConn");
		ReminderDAO reminderDAO= new JDBCReminderDAOImpl();
		reminderDAO.setConnection(conn);
		
								  
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
				
		if(userLogin != null) {
			if (reminderDAO.deleteAllOfNote((int)idn)) {
				return Response.noContent().build(); //204 no content 
			}
			else throw new CustomNotFoundException("There are not reminders for note idn");
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	}	
	
	/**This method is called when the logger user want to delete ALL OR HIS reminders of a note.
	 * 
	* 
	* @param idn id of the note to delete reminders
	* @param request
	* @return
	*/
	@DELETE
	@Path("/loggeduser/{idn: [0-9]+}")
	public Response deleteUserNoteReminders(@PathParam("idn") long idn,
				  					  @Context HttpServletRequest request) {
			  
		Connection conn = (Connection) sc.getAttribute("dbConn");
		ReminderDAO reminderDAO= new JDBCReminderDAOImpl();
		reminderDAO.setConnection(conn);
		
								  
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
				
		if(userLogin != null) {
			if (reminderDAO.deleteAllOfUserAndNote(userLogin.getIdu(),(int)idn)) {
				return Response.noContent().build(); //204 no content 
			}
			else throw new CustomNotFoundException("There are not reminders for note idn in logged user");
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	}	
	
	/**This method is called when the logger user want to delete a specific reminder.
	 * 
	* @param idn id of the note to delete all versions
	* @param modDate long to convert in Date. It's the date of the reminder of the note 'idn'
	* @param request
	* @return
	*/
	@DELETE
	@Path("/{idn: [0-9]+}/{modDate: [0-9]+}")
	public Response deleteReminder(@PathParam("idn") long idn,@PathParam("modDate") long modDateInt,
				  					  @Context HttpServletRequest request) {
			  
		Connection conn = (Connection) sc.getAttribute("dbConn");
		ReminderDAO reminderDAO= new JDBCReminderDAOImpl();
		reminderDAO.setConnection(conn);
					
		Date modDate= new Date(modDateInt);

		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
				
		if(userLogin != null) {
			Reminder rm= new Reminder();
			rm.setIdu(userLogin.getIdu());
			rm.setIdn((int)idn);
			rm.setDate(modDate);
			if (reminderDAO.delete(rm)) {
				return Response.noContent().build(); //204 no content 
			}
			else throw new CustomNotFoundException("There are not reminders for note idn in logged user");
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	}
	
	
	/**This method is called when the logger user want to delete ALL his reminders.
	 * 
	* @param request
	* @return
	*/
	@DELETE
	public Response deleteReminders(@Context HttpServletRequest request) {
			  
		Connection conn = (Connection) sc.getAttribute("dbConn");
		ReminderDAO reminderDAO= new JDBCReminderDAOImpl();
		reminderDAO.setConnection(conn);
										  
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
				
		if(userLogin != null) {
			List<Reminder> reminders= reminderDAO.getAllBy(userLogin.getIdu());
			if (reminders!=null && reminders.size()>0) {
				reminderDAO.deleteAllOfUser(userLogin.getIdu());
				return Response.noContent().build(); //204 no content 
			}
			else throw new CustomNotFoundException("There are not reminders for the logged user");
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	}

}

