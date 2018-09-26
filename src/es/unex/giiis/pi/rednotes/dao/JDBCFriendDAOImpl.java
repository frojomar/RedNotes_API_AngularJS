package es.unex.giiis.pi.rednotes.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import es.unex.giiis.pi.rednotes.helper.DateTimeHelper;
import es.unex.giiis.pi.rednotes.model.Friend;
import es.unex.giiis.pi.rednotes.model.User;

public class JDBCFriendDAOImpl implements FriendDAO{

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCUserDAOImpl.class.getName());
	
	@Override
	public void setConnection(Connection conn) {
		// TODO Auto-generated method stub
		this.conn = conn;
	}

	@Override
	public List<Friend> getAllBy(Integer idu) {
		if (conn == null) return null;
		
		ArrayList<Friend> friends = new ArrayList<Friend>();
		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
			  stmt = conn.createStatement();
			  rs = stmt.executeQuery("SELECT * FROM Friend WHERE idA="+idu+" OR idB="+idu);
			}
			while ( rs.next() ) {
				Friend friend= new Friend();
				friend.setIdA(rs.getInt("idA"));
				friend.setIdB(rs.getInt("idB"));
				friend.setConfirmed(rs.getInt("confirmed"));
				
				Date date=DateTimeHelper.StringToDate(rs.getString("dateFriendship"));


				friend.setDateFriendship(date);
				
				friends.add(friend);
				logger.info("fetching friends: "+friend.getIdA()+" "+friend.getIdB()+" "+friend.getDateFriendship()+" "+friend.getConfirmed());
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return friends;
	}
	
	

	@Override
	public boolean add(Friend friend) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				String date= DateTimeHelper.DateToString(friend.getDateFriendship());
				
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO Friend (idA,idB,dateFriendship,confirmed) VALUES('"+
									friend.getIdA()+"','"+
									friend.getIdB()+"','"+
									date+"','"+
									friend.getConfirmed()+"')");
						
				logger.info("creating Friend:("+friend.getIdA()+" "+friend.getIdB()+" "+friend.getConfirmed()+" "+friend.getDateFriendship());
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;		
	}

	@Override
	public boolean save(Friend friend) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				if(friend.getConfirmed()==1) {
					stmt = conn.createStatement();
					stmt.executeUpdate("UPDATE Friend SET confirmed="+friend.getConfirmed()
										+" WHERE idA = "+friend.getIdA()+" AND idB= "+friend.getIdB());
	
							
					logger.info("confirmed Friend:("+friend.getIdA()+" "+friend.getIdB()+" "+friend.getConfirmed()+" "+friend.getDateFriendship());
					done= true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;	
		
	}

	@Override
	public boolean confirm(Integer idu1, Integer idu2, Date dateFriendship) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				String date= DateTimeHelper.DateToString(dateFriendship);
				
				stmt = conn.createStatement();
				stmt.executeUpdate("UPDATE Friend SET confirmed=1, dateFriendship='"+date+"'"
									+" WHERE idA = "+idu1+" AND idB= "+idu2);

						
				logger.info("confirmed Friend:("+idu1+" "+idu2+" "+dateFriendship);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;	
		
	}

	@Override
	public boolean delete(Integer idu, Integer idu2) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM Friend WHERE (idA="+idu+" AND idB="+idu2+") OR (idA="+idu2+" AND idB="+idu+")");
				logger.info("deleting Friend: "+idu+"-"+idu2);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;		
	}

	@Override
	public Friend get(Integer idu1, Integer idu2) {
		if (conn == null) return null;
		Friend friend=null;
		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
			  stmt = conn.createStatement();
			  rs = stmt.executeQuery("SELECT * FROM Friend WHERE (idA="+idu1+" AND idB="+idu2+") OR (idA="+idu2+" AND idB="+idu1+")");
			}
			if ( rs.next() ) {
				friend= new Friend();
				friend.setIdA(rs.getInt("idA"));
				friend.setIdB(rs.getInt("idB"));
				friend.setConfirmed(rs.getInt("confirmed"));
				
				Date date=DateTimeHelper.StringToDate(rs.getString("dateFriendship"));


				friend.setDateFriendship(date);
				
				logger.info("fetching friends: "+friend.getIdA()+" "+friend.getIdB()+" "+friend.getDateFriendship()+" "+friend.getConfirmed());
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return friend;
	}

	@Override
	public List<Friend> getAllNotConfirmedBy(Integer idu) {
		if (conn == null) return null;
		
		ArrayList<Friend> friends = new ArrayList<Friend>();
		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
			  stmt = conn.createStatement();
			  rs = stmt.executeQuery("SELECT * FROM Friend WHERE idA="+idu+" AND confirmed=0");
			}
			while ( rs.next() ) {
				Friend friend= new Friend();
				friend.setIdA(rs.getInt("idA"));
				friend.setIdB(rs.getInt("idB"));
				friend.setConfirmed(rs.getInt("confirmed"));
				
				Date date=DateTimeHelper.StringToDate(rs.getString("dateFriendship"));


				friend.setDateFriendship(date);
				
				friends.add(friend);
				logger.info("fetching friends: "+friend.getIdA()+" "+friend.getIdB()+" "+friend.getDateFriendship()+" "+friend.getConfirmed());
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return friends;
	}

	@Override
	public List<Friend> getAllNotConfirmedTo(Integer idu) {
		if (conn == null) return null;
		
		ArrayList<Friend> friends = new ArrayList<Friend>();
		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
			  stmt = conn.createStatement();
			  rs = stmt.executeQuery("SELECT * FROM Friend WHERE idB="+idu+" AND confirmed=0");
			}
			while ( rs.next() ) {
				Friend friend= new Friend();
				friend.setIdA(rs.getInt("idA"));
				friend.setIdB(rs.getInt("idB"));
				friend.setConfirmed(rs.getInt("confirmed"));
				
				Date date=DateTimeHelper.StringToDate(rs.getString("dateFriendship"));


				friend.setDateFriendship(date);
				
				friends.add(friend);
				logger.info("fetching friends: "+friend.getIdA()+" "+friend.getIdB()+" "+friend.getDateFriendship()+" "+friend.getConfirmed());
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return friends;
	}

	@Override
	public boolean deleteAllOf(Integer idu) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM Friend WHERE idA="+idu+" OR idB="+idu);
				logger.info("deleting all Friends of: "+idu);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;		
	}

}
