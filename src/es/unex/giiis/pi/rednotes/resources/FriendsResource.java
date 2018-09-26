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

import es.unex.giiis.pi.rednotes.dao.FriendDAO;
import es.unex.giiis.pi.rednotes.dao.JDBCFriendDAOImpl;
import es.unex.giiis.pi.rednotes.helperdao.FriendComplexMethods;
import es.unex.giiis.pi.rednotes.model.Friend;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomBadRequestException;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomNotFoundException;

@Path("/friends")
public class FriendsResource {
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
	  @Context
	  ServletContext sc;
	  @Context
	  UriInfo uriInfo;
	  
	  
	  /**
	   * 
	   * This method, it's called when we need the information of all friendships of logged user
	   * 
	   * @param request
	   * @return
	   */
	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<Friend> getFriendsJSON(@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		FriendDAO friendDAO = new JDBCFriendDAOImpl();
		friendDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
		List<Friend> friends;
		
		friends= new ArrayList<Friend>();
		if(userLogin!=null) {
			friends = friendDAO.getAllBy(userLogin.getIdu());
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
		}
		
	    return friends; 
	  }
	  
	  
	  /**
	   * This method, it's called when we need the information of a friendship specific of the logged user
	   * 
	   * @param userid
	   * @param request
	   * @return
	   */
	  @GET
	  @Path("/{idu: [0-9]+}")	  
	  @Produces(MediaType.APPLICATION_JSON)
	  public Friend getFriendJSON(@PathParam("idu") long idu,
			  					@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		FriendDAO friendDAO = new JDBCFriendDAOImpl();
		friendDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
		
		if(userLogin != null) {
			Friend friend= friendDAO.get(userLogin.getIdu(), (int) idu);
			
			if (friend!=null) return friend;
			else throw new CustomNotFoundException("User ("+ idu + ") doesn't exist or he's not your friend");
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	  }
	  
	/**
	 * This method, it's called when the logged user want to create a new friendship with another user.
	 * 
	 * The confirmed value pass in the JSON should be 0, to guarantee a good working
	 * 
	 * @param newFriendship Friend instance created with the JSON information
	 * @param request
	 * @return
	 * @throws Exception
	 */
	  @POST	  	  
	  @Consumes(MediaType.APPLICATION_JSON)
	  public Response post(Friend newFriendship, @Context HttpServletRequest request) throws Exception {	
		Connection conn = (Connection) sc.getAttribute("dbConn");
		FriendDAO friendDAO = new JDBCFriendDAOImpl();
		friendDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		
		User user= (User) session.getAttribute("userlogin");
		
		Response res;
				
		if (newFriendship.getIdA()<0 || newFriendship.getIdB()<0)
			throw new CustomBadRequestException("Errors in idu of users");
		//save user in DB
		else {
			if(newFriendship.getIdA()!=user.getIdu()) {
				throw new CustomBadRequestException("You only can do friendships for you, and you must be idA");
			}
			else {
				if(newFriendship.getConfirmed()!=0 && newFriendship.getConfirmed()!=1) {
					throw new CustomBadRequestException("Confirmed value must be 0 or 1");
				}
				else {
					if(friendDAO.get(newFriendship.getIdA(), newFriendship.getIdB())==null) {
						friendDAO.add(newFriendship);
						long idu;
						if(newFriendship.getIdA()==user.getIdu()) {
							idu=newFriendship.getIdB();
						}
						else {
							idu=newFriendship.getIdA();
						}
						res = Response //return 201 and Location: /friends/idu
								   .created(
									uriInfo.getAbsolutePathBuilder().path(Long.toString(idu))
										   .build())
								   .contentLocation(
									uriInfo.getAbsolutePathBuilder().path(Long.toString(idu))
									       .build())
									.build();
						    return res;
					}
					else {
						throw new CustomBadRequestException("This friendship exists yet");
					}
				}
			}
		}	
	  }
	  
	  
	/** This method, it's called when the logged user want to update a friendship with other user
	* 
	* The confirmed value pass in the JSON should be 1, to guarantee a good working
	* 
	* @param friendUpdate Friend instance created with the JSON information
	* @param idu id of the other user
	* @param request
	* @return
	* @throws Exception
	*/
	@PUT
	@Path("/{idu: [0-9]+}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put(Friend friendUpdate,
						@PathParam("idu") long idu,
						@Context HttpServletRequest request) throws Exception{
		Connection conn = (Connection) sc.getAttribute("dbConn");
		FriendDAO friendDAO = new JDBCFriendDAOImpl();
		friendDAO.setConnection(conn);
			  
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("userlogin");
				
		Response response = null;
						
		//We check that the order exists
		if(user != null) {
			Friend friend= friendDAO.get(user.getIdu(), (int) idu);
			
			if (friend!=null) {
				if (friendUpdate.getIdA()<0 || friendUpdate.getIdB()<0)
					throw new CustomBadRequestException("Errors in idu of users");
				//save user in DB
				else {
					if(friendUpdate.getIdB()!=user.getIdu()) {
						throw new CustomBadRequestException("You only can modify your friendships, and you must be idB");
					}
					else {
						if(friendUpdate.getConfirmed()!=0 && friendUpdate.getConfirmed()!=1) {
							throw new CustomBadRequestException("Confirmed value must be 0 or 1");
						}
						else {
							logger.info("Modificando la info de la amistad");
							if(friendDAO.get(friendUpdate.getIdA(), friendUpdate.getIdB())!=null) {
								friendDAO.save(friendUpdate);
							}
							else {
								throw new CustomBadRequestException("Doesn't exists friendship of "+
										friendUpdate.getIdA()+" and "+friendUpdate.getIdB());
							}
						}
					}
				}
			}
			else throw new WebApplicationException(Response.Status.NOT_FOUND);			
		}	
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
					  
		return response;
	}
	  
	  
	  

	/**This method is called when the user login want to delete a friendship with the user idu
	* 
	* @param idu id of the other user to delete the Friendship
	* @param request
	* @return
	*/
	@DELETE
	@Path("/{idu: [0-9]+}")
	public Response deleteFriendship(@PathParam("idu") long idu,
				  					  @Context HttpServletRequest request) {
			  
		Connection conn = (Connection) sc.getAttribute("dbConn");
		FriendDAO friendDAO = new JDBCFriendDAOImpl();
		friendDAO.setConnection(conn);
					
		FriendComplexMethods friendMethods= new FriendComplexMethods(conn);
		
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
				
		if(userLogin != null) {
			Friend friend= friendDAO.get(userLogin.getIdu(), (int) idu);
			
			if (friend!=null) {
				friendMethods.deleteSharedNotesWith(userLogin.getIdu(), (int)idu);
				friendDAO.delete(userLogin.getIdu(), (int) idu);
				return Response.noContent().build(); //204 no content 
			}
			else throw new CustomNotFoundException("User ("+ idu + ") doesn't exist or he's not your friend");
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
			throw new CustomNotFoundException("We can not verify your identity. Login yourself previously");
		}
	}	
	
}
