// Descomentar si se se va a usar un Listener para iniciar la conexi�n:
//package listener;
//Comentar si se va a usar un Listner para iniciar la conexi�n:
package es.unex.giiis.pi.rednotes.listener;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Logger;


// Descomentar si se se va a usar un Listener para iniciar la conexi�n:
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;


@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {

	
	private static final Logger logger = Logger.getLogger(ServletContextListener.class.getName());
	

	public void contextInitialized(ServletContextEvent event) {

		logger.info("Connecting DB");
		Connection conn = null;
		
		try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:file:"+System.getProperty("user.home")+"/sqlite_dbs/RedNotes.sqlite";
            conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected to the database");
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
                ServletContext sc = event.getServletContext();
                sc.setAttribute("dbConn", conn);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
		
		logger.info("DB connected");
		
	
		
		
	}
	

    public void contextDestroyed(ServletContextEvent arg0)  { 
 

		
		logger.info("Destroying DB");
		try {
			logger.info("DB shutdown start");
	   		ServletContext sc = arg0.getServletContext();
	   		Connection conn = (Connection) sc.getAttribute("dbConn");
			conn.close();
			Enumeration<Driver> drivers = DriverManager.getDrivers();
			while (drivers.hasMoreElements()) {
				logger.info("DB deregistering drivers");
				Driver driver = drivers.nextElement();
				try {
					DriverManager.deregisterDriver(driver);
					logger.info(String.format("deregistering jdbc driver: %s", driver));
				} catch (SQLException e) {
					logger.severe(String.format("Error deregistering driver %s %s", driver, e));
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("DB destroyed");
	}

	
   
	
	

}
