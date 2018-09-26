package es.unex.giiis.pi.rednotes.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.unex.giiis.pi.rednotes.helper.DateTimeHelper;
import es.unex.giiis.pi.rednotes.helperdao.UsersComplexMethods;
import es.unex.giiis.pi.rednotes.model.UserLogin;

/**
 * Servlet implementation class Login
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("The request was made using GET");
		HttpSession session = request.getSession();
		
		
		if(request.getParameter("disconnect")!=null) {
			if(((String)request.getParameter("disconnect")).equals("true")) {
	
				session = request.getSession();
	
				session.invalidate();
				
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
				view.forward(request,response);	
			}
		}
		session = request.getSession();

		
		if(session.isNew() || session.getAttribute("login")==null) {
			session.setAttribute("login", false);
		}
		
		logger.info("Session id: "+session.getId());
		logger.info("Session new? "+session.isNew());
		logger.info("Session creation time: "+DateTimeHelper.time2Date(session.getCreationTime()));
		logger.info("Session last accessed time: "+DateTimeHelper.time2Date(session.getLastAccessedTime()));
		logger.info("Session max inactive time: "+DateTimeHelper.time2Date(session.getMaxInactiveInterval()));
		
		if((boolean)session.getAttribute("login")==false || session.getAttribute("login")==null ) {
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
			logger.info("Redirigiendo al Login Servlet desde login");
			view.forward(request,response);	
		}
		else {
			response.sendRedirect("pages/index.html");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UsersComplexMethods userMethods= new UsersComplexMethods(conn);
		
		request.setCharacterEncoding("UTF-8");
		
		logger.info("The request was made using POST");
		
		List<String> messages = new ArrayList<String>();

		UserLogin user = new UserLogin();
		
		String password=request.getParameter("password");

		String email=request.getParameter("email");
		
		if(email==null || email=="") {
			messages.add(new String("you must put your email or username"));
			request.setAttribute("messages",messages);
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
			view.forward(request,response);
		}
		else {
			if(password==null || password=="") {
				messages.add(new String("you must put your password"));
				request.setAttribute("messages",messages);
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
				view.forward(request,response);
			}
			else {
				Integer index=email.indexOf("@");
				
				if(index<0) {//the user put the username
					user.setUsername(email);
					user.setPassword(password);
					logger.info("User username: "+user.getUsername());
					
					
					/*We do not valid the password here because, when the user registered, the 
					 * system had to validate it, and see that it was correct.
					 * The email is validated because, if it is invalid, we will remove the work to the 
					 * database from the search of the user.
					 * */
					if (user.validateUsername(messages) && userMethods.existsUserByUsername(user,messages)) {
						session.setAttribute("userlogin",user);
						logger.info("Registrado 'userlogin' con id"+user.getIdu());
						session.setAttribute("login", true);
						response.sendRedirect("pages/index.html");
					} 
					else {
						request.setAttribute("messages",messages);
						RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
						view.forward(request,response);
					}	
				}
				else {//the user put the email
					user.setEmail(email);
					user.setPassword(password);
					logger.info("User email: "+user.getEmail());
					
					
					/*We do not valid the password here because, when the user registered, the 
					 * system had to validate it, and see that it was correct.
					 * The email is validated because, if it is invalid, we will remove the work to the 
					 * database from the search of the user.
					 * */
					if (user.validateEmail(messages) && userMethods.existsUser(user,messages)) {
						session.setAttribute("userlogin",user);
						logger.info("Registrado 'userlogin' con id"+user.getIdu());
						session.setAttribute("login", true);
						response.sendRedirect("pages/index.html");
					} 
					else {
						request.setAttribute("messages",messages);
						RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
						view.forward(request,response);
					}	
				}
			}
		}
		

		
	}

}
