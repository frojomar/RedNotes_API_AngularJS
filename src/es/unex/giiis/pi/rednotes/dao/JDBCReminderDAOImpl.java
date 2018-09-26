package es.unex.giiis.pi.rednotes.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.unex.giiis.pi.rednotes.helper.DateTimeHelper;
import es.unex.giiis.pi.rednotes.model.Friend;
import es.unex.giiis.pi.rednotes.model.Reminder;

public class JDBCReminderDAOImpl implements ReminderDAO{

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCUsersNotesDAOImpl.class.getName());
	
	@Override
	public void setConnection(Connection conn) {
		// TODO Auto-generated method stub
		this.conn = conn;	
	}

	@Override
	public List<Reminder> getAllBy(Integer idu) {
		
		if (conn == null) return null;
		
		ArrayList<Reminder> remindersList = new ArrayList<Reminder>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Reminder WHERE idu="+idu);

			while ( rs.next() ) {
				Reminder reminder = new Reminder();
				reminder.setIdu(rs.getInt("idu"));
				reminder.setIdn(rs.getInt("idn"));
				reminder.setDate(DateTimeHelper.StringToDate(rs.getString("date")));
				reminder.setDescription(rs.getString("description"));
				
				remindersList.add(reminder);
				logger.info("fetching all reminders by idu: "+reminder.getIdu()+"->"+reminder.getIdn()+" "+reminder.getDate()+" "+reminder.getDescription());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return remindersList;
	}

	@Override
	public boolean add(Reminder reminder) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				String date= DateTimeHelper.DateToString(reminder.getDate());
				
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO Reminder (idu,idn,date,description) VALUES('"+
						reminder.getIdu()+"','"+
						reminder.getIdn()+"','"+
						date+"','"+
						reminder.getDescription()+"')");
						
				logger.info("creating Reminder:("+reminder.getIdu()+" "+reminder.getIdn()+" "+reminder.getDate()+"-"+reminder.getDescription());
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean delete(Reminder reminder) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				String date= DateTimeHelper.DateToString(reminder.getDate());
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM Reminder WHERE idu ="+reminder.getIdu()+" AND idn="+reminder.getIdn()+" AND date='"+date+"'");
				logger.info("deleting Reminder: "+reminder.getIdu()+" , idn="+reminder.getIdn());
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean deleteAllOfUser(Integer idu) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM Reminder WHERE idu ="+idu);
				logger.info("deleting Reminder of user: "+idu);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean deleteAllOfNote(Integer idn) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM Reminder WHERE idn ="+idn);
				logger.info("deleting Reminder of note: "+idn);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean deleteAllOfUserAndNote(Integer idu, Integer idn) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM Reminder WHERE idu ="+idu+" AND idn="+idn);
				logger.info("deleting Reminder of  note:"+idn+" for user: "+idu);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

}
