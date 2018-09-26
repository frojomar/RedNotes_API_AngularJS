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
import es.unex.giiis.pi.rednotes.dao.JDBCUserDAOImpl;
import es.unex.giiis.pi.rednotes.dao.UserDAO;
import es.unex.giiis.pi.rednotes.helperdao.FriendComplexMethods;
import es.unex.giiis.pi.rednotes.model.Friend;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomBadRequestException;
import es.unex.giiis.pi.rednotes.resources.exceptions.CustomNotFoundException;

@Path("/images")
public class ImagesResource {

	private static final Logger logger = 
				Logger.getLogger(HttpServlet.class.getName());
		
	  @Context
	  ServletContext sc;
	  @Context
	  UriInfo uriInfo;
	  
	  
	  /**
	   * 
	   * This method, returns all the URLs of the images of the database, in a list of strings.
	   * 
	   * @param request
	   * @return
	   */
	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<String> getImagesJSON(@Context HttpServletRequest request) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);
		
		HttpSession session = request.getSession();
		User userLogin = (User) session.getAttribute("userlogin");
		List<String> images;
		
		images= new ArrayList<String>();
		if(userLogin!=null) {
			images = userDAO.getAllImages();
		}
		else {
			logger.info("ERROR: No se han encontrado los datos del usuario en la sesion");
		}
		
	    return images; 
	  }
	  
}

