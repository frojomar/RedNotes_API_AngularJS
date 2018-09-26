package es.unex.giiis.pi.rednotes.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import es.unex.giiis.pi.rednotes.helper.DateTimeHelper;
import es.unex.giiis.pi.rednotes.helper.Encryptor;
import es.unex.giiis.pi.rednotes.model.Note;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.model.UserLogin;

public class JDBCUserDAOImpl implements UserDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCUserDAOImpl.class.getName());
	
	@Override
	public User get(long idu) {
		if (conn == null) return null;
		
		User user = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE idu ="+idu);			 
			if (!rs.next()) return null; 
			user  = new User();	 
			user.setIdu(rs.getInt("idu"));
			user.setUsername(rs.getString("username"));
			user.setEmail(rs.getString("email"));
			user.setImage(this.getImageURL(rs.getInt("image")));
			
			Date date=DateTimeHelper.StringToDate(rs.getString("dateCreation"));


			user.setDate(date);
			user.setName(rs.getString("name"));
			user.setCountry(rs.getString("country"));
			user.setCity(rs.getString("city"));
			user.setAge(rs.getInt("age"));
			user.setTelephone(rs.getString("telephone"));
			
			logger.info("fetching User by idu: "+idu+" -> "+user.getIdu()+" "+user.getUsername()+" "+user.getEmail());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	
	@Override
	public User get(String username) {
		if (conn == null) return null;
		
		User user = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE username ='"+username+"'");			 
			if (!rs.next()) return null; 
			user  = new User();	 
			user.setIdu(rs.getInt("idu"));
			user.setUsername(rs.getString("username"));
			user.setEmail(rs.getString("email"));
			user.setImage(this.getImageURL(rs.getInt("image")));
			
			Date date=DateTimeHelper.StringToDate(rs.getString("dateCreation"));


			user.setDate(date);
			user.setName(rs.getString("name"));
			user.setCountry(rs.getString("country"));
			user.setCity(rs.getString("city"));
			user.setAge(rs.getInt("age"));
			user.setTelephone(rs.getString("telephone"));
			logger.info("fetching User by name: "+ username + " -> "+ user.getIdu()+" "+user.getUsername()+" "+user.getEmail());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	
	
	public List<User> getAll() {

		if (conn == null) return null;
		
		ArrayList<User> users = new ArrayList<User>();
		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
			  stmt = conn.createStatement();
			  rs = stmt.executeQuery("SELECT * FROM User");
			}
			while ( rs.next() ) {
				User user = new User();
				user.setIdu(rs.getInt("idu"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setImage(this.getImageURL(rs.getInt("image")));
				
				Date date=DateTimeHelper.StringToDate(rs.getString("dateCreation"));


				user.setDate(date);
				user.setName(rs.getString("name"));
				user.setCountry(rs.getString("country"));
				user.setCity(rs.getString("city"));
				user.setAge(rs.getInt("age"));
				user.setTelephone(rs.getString("telephone"));
				
				users.add(user);
				logger.info("fetching users: "+user.getIdu()+" "+user.getUsername()+" "+user.getEmail());
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}
	

	@Override
	public long add(UserLogin user) {
		long idu=-1;
		long lastidu=-1;
		
		if (conn != null){
			logger.info("conectado");
			Statement stmt;
			
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_sequence WHERE name ='User'");			 
				if (!rs.next()) return -1; 
				lastidu=rs.getInt("seq");
								
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			try {
				logger.info("Insertando");
				
				String date= DateTimeHelper.DateToString(user.getDate());
				String passwordEncrypted= Encryptor.Encriptar(user.getPassword());

				
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO User (username,email,password,image,dateCreation,name,"
									+ "country,city,age,telephone) VALUES('"
									+user.getUsername()+"','"
									+user.getEmail()+"','"
									+passwordEncrypted+"','"
									+this.getImageID(user.getImage())+"','"
									+date+"','"
									+user.getName()+"','"
									+user.getCountry()+"','"
									+user.getCity()+"','"
									+user.getAge()+"','"
									+user.getTelephone()+"')");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_sequence WHERE name ='User'");			 
				if (!rs.next()) return -1; 
				idu=rs.getInt("seq");
				if (idu<=lastidu) return -1;
											
				logger.info("CREATING User("+idu+"): "+user.getUsername()+" "+user.getEmail()+" "+user.getPassword());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return idu;
	}



	@Override
	public boolean save(UserLogin user) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				
				String date= DateTimeHelper.DateToString(user.getDate());
				String passwordEncrypted= Encryptor.Encriptar(user.getPassword());
				
				stmt = conn.createStatement();
				stmt.executeUpdate("UPDATE User SET username='"+user.getUsername()
									+"', email='"+user.getEmail()
									+"', password='"+passwordEncrypted
									+"', image='"+this.getImageID(user.getImage())
									+"', dateCreation='"+date
									+"', name='"+user.getName()
									+"', country='"+user.getCountry()
									+"', city='"+user.getCity()
									+"', age='"+user.getAge()
									+"', telephone='"+user.getTelephone()
									+"' WHERE idu = "+user.getIdu());

				logger.info("updating User: "+user.getIdu()+" "+user.getUsername()+" "+user.getEmail()+" "+user.getPassword());
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return done;

	}

	@Override
	public boolean delete(long idu) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM User WHERE idu ="+idu);
				logger.info("deleting User: "+idu);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public void setConnection(Connection conn) {
		// TODO Auto-generated method stub
		this.conn = conn;
	}

	
	/*MY METHODS*/
	
	
	private String getImageURL(Integer id) {
		if (conn == null) return null;
		String image = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Image WHERE id ="+id);			 
			if (!rs.next()) return null; 
			image=rs.getString("url");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	
	private Integer getImageID(String image) {
		if (conn == null) return null;
		Integer id = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Image WHERE url='"+image+"'");			 
			if (!rs.next()) return null; 
			id=rs.getInt("id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	
	@Override
	public UserLogin getforLogin(String email){
		if (conn == null) return null;
		UserLogin user = null;		
		
		try {
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE email ='"+email+"'");			 
			if (!rs.next()) return null; 
			user  = new UserLogin();	 
			user.setIdu(rs.getInt("idu"));
			user.setUsername(rs.getString("username"));
			user.setEmail(rs.getString("email"));
			
			String passwordDecrypted= Encryptor.Desencriptar(rs.getString("password"));

			user.setPassword(passwordDecrypted);
			user.setImage(this.getImageURL(rs.getInt("image")));
			
			Date date=DateTimeHelper.StringToDate(rs.getString("dateCreation"));


			user.setDate(date);
			user.setName(rs.getString("name"));
			user.setCountry(rs.getString("country"));
			user.setCity(rs.getString("city"));
			user.setAge(rs.getInt("age"));
			user.setTelephone(rs.getString("telephone"));
			logger.info("fetching User by email: "+ email + " -> "+ user.getIdu()+" "+user.getUsername()+" "+user.getEmail()+" "+user.getPassword());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public UserLogin getforLoginByUsername(String username) {
		if (conn == null) return null;
		UserLogin user = null;		
		
		try {
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE username ='"+username+"'");			 
			if (!rs.next()) return null; 
			user  = new UserLogin();	 
			user.setIdu(rs.getInt("idu"));
			user.setUsername(rs.getString("username"));
			user.setEmail(rs.getString("email"));
			
			String passwordDecrypted= Encryptor.Desencriptar(rs.getString("password"));

			user.setPassword(passwordDecrypted);
			user.setImage(this.getImageURL(rs.getInt("image")));
			
			Date date=DateTimeHelper.StringToDate(rs.getString("dateCreation"));


			user.setDate(date);
			user.setName(rs.getString("name"));
			user.setCountry(rs.getString("country"));
			user.setCity(rs.getString("city"));
			user.setAge(rs.getInt("age"));
			user.setTelephone(rs.getString("telephone"));
			logger.info("fetching User by email: "+ user.getEmail() + " -> "+ user.getIdu()+" "+user.getUsername()+" "+user.getEmail()+" "+user.getPassword());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public boolean existsEmail(String email) {
		if (conn == null) return true;
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE email ='"+email+"'");			 
			if (!rs.next()) return false;
			else {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean existsUsername(String username) {
		if (conn == null) return true;
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE username ='"+username+"'");			 
			if (!rs.next()) return false;
			else {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean existsPassword(String password) {
		if (conn == null) return true;
		
		try {
			Statement stmt = conn.createStatement();
			
			String passwordEncrypted= Encryptor.Encriptar(password);

			ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE password ='"+passwordEncrypted+"'");			 
			if (!rs.next()) return false;
			else {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public String searchUsername(Integer idu) {
		String username=null;
		logger.info("Buscando username de user "+idu);
		if(conn!=null) {
			logger.info("Conectado para buscar usernames");
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT username FROM User WHERE idu ="+idu);			 
				if (rs.next()) {
					username=rs.getString("username");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return username;
		
	}
	
	@Override
	public String searchImage(Integer idu) {
		String image=null;
		if(conn!=null) {
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT image FROM User WHERE idu ="+idu);			 
				if (rs.next()) {
					image=this.getImageURL(rs.getInt("image"));
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return image;
		
	}

	@Override
	public List<String> getAllImages() {
		if (conn == null) return null;
		
		ArrayList<String> images = new ArrayList<String>();
		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
			  stmt = conn.createStatement();
			  rs = stmt.executeQuery("SELECT * FROM Image");
			}
			while ( rs.next() ) {
				images.add(rs.getString("url"));

				logger.info("fetching images: "+rs.getString("id")+" "+rs.getString("url"));
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return images;
	}

	@Override
	public List<User> getAllBy(String search) {
		if (conn == null) return null;
		
		ArrayList<User> users = new ArrayList<User>();
		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
			  stmt = conn.createStatement();
			  rs = stmt.executeQuery("SELECT * FROM User WHERE username LIKE '"+search+"' OR name LIKE '"+search+"'");
			}
			while ( rs.next() ) {
				User user = new User();
				user.setIdu(rs.getInt("idu"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setImage(this.getImageURL(rs.getInt("image")));
				
				Date date=DateTimeHelper.StringToDate(rs.getString("dateCreation"));


				user.setDate(date);
				user.setName(rs.getString("name"));
				user.setCountry(rs.getString("country"));
				user.setCity(rs.getString("city"));
				user.setAge(rs.getInt("age"));
				user.setTelephone(rs.getString("telephone"));
				
				users.add(user);
				logger.info("fetching users: "+user.getIdu()+" "+user.getUsername()+" "+user.getEmail());
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}

	@Override
	public List<User> getAllFiltered(String where) {
		if (conn == null) return null;
		
		ArrayList<User> users = new ArrayList<User>();
		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
			  stmt = conn.createStatement();
			  rs = stmt.executeQuery("SELECT * FROM User "+where);
			}
			while ( rs.next() ) {
				User user = new User();
				user.setIdu(rs.getInt("idu"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setImage(this.getImageURL(rs.getInt("image")));
				
				Date date=DateTimeHelper.StringToDate(rs.getString("dateCreation"));


				user.setDate(date);
				user.setName(rs.getString("name"));
				user.setCountry(rs.getString("country"));
				user.setCity(rs.getString("city"));
				user.setAge(rs.getInt("age"));
				user.setTelephone(rs.getString("telephone"));
				
				users.add(user);
				logger.info("fetching users: "+user.getIdu()+" "+user.getUsername()+" "+user.getEmail());
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}
}
