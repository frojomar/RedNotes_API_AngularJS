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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import es.unex.giiis.pi.rednotes.model.Friend;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.model.UserLogin;
import es.unex.giiis.pi.rednotes.dao.FriendDAO;
import es.unex.giiis.pi.rednotes.dao.JDBCFriendDAOImpl;
import es.unex.giiis.pi.rednotes.dao.JDBCUserDAOImpl;
import es.unex.giiis.pi.rednotes.dao.UserDAO;
import es.unex.giiis.pi.rednotes.helperdao.NotesComplexMethods;
import es.unex.giiis.pi.rednotes.helperdao.UsersComplexMethods;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomBadRequestException;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomNotFoundException;

@Path("/users")
public class UsersResource {

	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
	  @Context
	  ServletContext sc;
	  @Context
	  UriInfo uriInfo;
	  
	  
	  /**
	   * 
	   * This method, it's called when we need the information of all users of the database that have information
	   * like information pass in the query. This users, can be friends or not of the user yet (you can know if
	   * a user of returned users is a friend or not with GET resource of '/friends')
	   * 
	   * @param request
	   * @return
	   */
	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<User> getUsersForSearchJSON(@DefaultValue("") @QueryParam("name")String name,
			  								@DefaultValue("") @QueryParam("username") String username,
			  								@DefaultValue("") @QueryParam("city") String city,
			  								@DefaultValue("") @QueryParam("country") String country,
			  								@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UsersComplexMethods userMethods= new UsersComplexMethods(conn);
		
		
		List<User> users= userMethods.getUsersFiltered(name, username, city, country);
		
		if(users!=null) {
			//change private data for *******
			for(int i=0; i<users.size(); i++) {
				users.get(i).setAge(-1);
				users.get(i).setDate(null);
				users.get(i).setEmail("**********");
				users.get(i).setTelephone("*******");
			}
		}
		
	    return users; 
	  }
	  
	  
	  /**
	   * This method, it's called when we need the information of a specific user of the database. This user, must
	   * be our friend or you, or this return 404 error.
	   * 
	   * @param userid
	   * @param request
	   * @return
	   */
	  @GET
	  @Path("/{idu: [0-9]+}")	  
	  @Produces(MediaType.APPLICATION_JSON)
	  public User getUserJSON(@PathParam("idu") long userid,
			  					@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);
		
		FriendDAO friendDAO= new JDBCFriendDAOImpl();
		friendDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		UserLogin userLogin = (UserLogin) session.getAttribute("userlogin");
		  
		User user = userDAO.get(userid);

		if(user!=null) {
			
			Friend friend= friendDAO.get(userLogin.getIdu(), (int)userid);
			
			if (friend!=null || userid==userLogin.getIdu()) return user;
			else throw new CustomNotFoundException("User ("+ userid + ") is not found in your friends list");
			
		}
		else throw new CustomNotFoundException("User ("+ userid + ") is not found in database");		   
	  }
	  
	  /**
	   * This method, it's called when we need your information. This method is call when the logged user
	   *  need the information about himself.
	   * 
	   * @param request
	   * @return
	   */
	  @GET
	  @Path("/myself")	  
	  @Produces(MediaType.APPLICATION_JSON)
	  public User getUserLoginJSON(@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);
		
		FriendDAO friendDAO= new JDBCFriendDAOImpl();
		friendDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		UserLogin userLogin = (UserLogin) session.getAttribute("userlogin");
		  
		User user = userDAO.get(userLogin.getIdu());

		if(user!=null) {
			return user;
		}
		else throw new CustomNotFoundException("User is not found in database");		   
	  }
	  
	/**
	 * This method, it's called when a new user register himself.
	 * 
	 * The only obligatory properties that must be defined, must be: date, email, username and password. Other 
	 * properties, can be put in this call.	
	 * 
	 * @param newUser User instance, with the information necessary to create a new user.
	 * @param request
	 * @return response, with the URI of the new user, if we can create.
	 * @throws Exception
	 */
	  @POST	  	  
	  @Consumes(MediaType.APPLICATION_JSON)
	  public Response post(UserLogin newUser, @Context HttpServletRequest request) throws Exception {	
		Connection conn = (Connection) sc.getAttribute("dbConn");  	 
		
		UsersComplexMethods userMethods= new UsersComplexMethods(conn);
				
		Response res;
		
		List<String> messages = new ArrayList<String>();

		if (!newUser.validateEmail(messages) || !newUser.validatePassword(messages) ||
				!newUser.validateUsername(messages) || !userMethods.dataRegisterValid(newUser, messages)) {
			for(int i=0; i<messages.size(); i++) {
				logger.info(messages.get(i));
			}
			throw new CustomBadRequestException(messages.get(0));
		}
		//save user in DB
		else {
			long id=userMethods.addNewUser(newUser);
			res = Response //return 201 and Location: /users/newid
					   .created(
						uriInfo.getAbsolutePathBuilder()
							   .path(Long.toString(id))
							   .build())
					   .contentLocation(
						uriInfo.getAbsolutePathBuilder()
						       .path(Long.toString(id))
						       .build())
						.build();
			    return res;
		}	
	  }
	  
	  
	  /**This method, it's called when we want to update the information of our account
	   * 
	   * The userUpdate, must content: date, idu, email and username. Else, this call doesn't work goodly.
	   * 
	   * If you don't sent other property, like name, this property of user will have "" value.
	   * 
	   * @param userUpdate
	   * @param idu
	   * @param request
	   * @return
	   * @throws Exception
	   */
	  @PUT
	  @Path("/{idu: [0-9]+}")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response put(UserLogin userUpdate,
							@PathParam("idu") long idu,
							@Context HttpServletRequest request) throws Exception{
		  Connection conn = (Connection)sc.getAttribute("dbConn");
		  UserDAO userDAO = new JDBCUserDAOImpl();
		  userDAO.setConnection(conn);
		  
		  UsersComplexMethods userMethods= new UsersComplexMethods(conn);
		  
		  HttpSession session = request.getSession();
		  UserLogin userLogin = (UserLogin) session.getAttribute("userlogin");
			
		  Response response = null;
					
		  //We check that the order exists
		  User u= userDAO.get(idu);
		  if(u!=null) {
			  UserLogin user=userDAO.getforLoginByUsername(u.getUsername());
			  
			  List<String> messages= new ArrayList<String>();
			  
			  logger.info("UserUpdate: "+userUpdate.getIdu() + " - idu:"+idu+" user:"+user.getIdu());
			  
			  if (user != null){//if exists the user...
				  if(userLogin.getIdu()==idu){ //and the user idu is the user logger	
					  
					if(userUpdate.getPassword()==null || userUpdate.getPassword().equals("")) {
					  userUpdate.setPassword(user.getPassword());
					}
					else {
						if(!userMethods.validateNewPassword(userUpdate,user.getPassword())) {
							throw new CustomBadRequestException("Password format is <oldpas###newpas>");	
						}
					}
					  
					if(!userUpdate.validatePerfilDates(messages)) {
						for(int i=0; i<messages.size(); i++) {
							logger.info(messages.get(i));
						}
						throw new CustomBadRequestException("Data of perfil is not valid. "+messages.get(0));	
					}
					else {
						if(!userDAO.save(userUpdate)) {
							throw new CustomBadRequestException("This email or username is used by other user");
						}
					}
				  }
				  else throw new CustomBadRequestException("Errors in parameters");						
			  }
			  else throw new WebApplicationException(Response.Status.NOT_FOUND);			
		  }
		  else throw new WebApplicationException(Response.Status.NOT_FOUND);			
		  
		  return response;
		}
	  
	  
	  

	  /**This method is called when the user login want to delete his account
	   * 
	   * @param idu
	   * @param request
	   * @return
	   */
	  @DELETE
	  @Path("/{idu: [0-9]+}")
	  public Response deleteUser(@PathParam("idu") long idu,
			  					  @Context HttpServletRequest request) {
		  
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);
		
		UsersComplexMethods userMethods= new UsersComplexMethods(conn);
		
		HttpSession session = request.getSession();
		UserLogin userLogin = (UserLogin) session.getAttribute("userlogin");
		
		User user= userDAO.get(idu);
		if ( user!= null && userLogin!=null) {
			if(userLogin.getIdu()==idu) {
				userMethods.deleteAccount(userDAO.getforLoginByUsername(user.getUsername()));
				session.invalidate();
				return Response.noContent().build(); //204 no content 
			}
			else throw new CustomBadRequestException("Error in idu of user. This isn't your user");		
		}
		else throw new CustomBadRequestException("Error in idu of user.");		
	  }	
	
}
