package es.unex.giiis.pi.rednotes.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.unex.giiis.pi.rednotes.model.UsersNotes;


public class JDBCUsersNotesDAOImpl implements UsersNotesDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCUsersNotesDAOImpl.class.getName());

	@Override
	public List<UsersNotes> getAll() {

		if (conn == null) return null;
						
		ArrayList<UsersNotes> usersNotesList = new ArrayList<UsersNotes>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UsersNotes");
						
			while ( rs.next() ) {
				UsersNotes usersNotes = new UsersNotes();
				usersNotes.setIdu(rs.getInt("idu"));
				usersNotes.setIdn(rs.getInt("idn"));
				usersNotes.setColor(rs.getString("color"));
				usersNotes.setArchived(rs.getInt("archived"));
				usersNotes.setPinned(rs.getInt("pinned"));
						
							
				usersNotesList.add(usersNotes);
				logger.info("fetching all usersNotes: "+usersNotes.getIdu()+" "+usersNotes.getIdn()+" "+usersNotes.getColor()
							+" "+usersNotes.getArchived()+" "+usersNotes.getPinned());
					
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usersNotesList;
	}

	@Override
	public List<UsersNotes> getAllByUser(long idu) {
		
		if (conn == null) return null;
						
		ArrayList<UsersNotes> usersNotesList = new ArrayList<UsersNotes>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UsersNotes WHERE idu="+idu);

			while ( rs.next() ) {
				UsersNotes usersNotes = new UsersNotes();
				usersNotes.setIdu(rs.getInt("idu"));
				usersNotes.setIdn(rs.getInt("idn"));
				usersNotes.setArchived(rs.getInt("archived"));
				usersNotes.setPinned(rs.getInt("pinned"));
				usersNotes.setColor(rs.getString("color"));
							
				usersNotesList.add(usersNotes);
				logger.info("fetching all usersNotes by idu: "+usersNotes.getIdu()+"->"+usersNotes.getIdn()+" "+usersNotes.getColor()
				+" "+usersNotes.getArchived()+" "+usersNotes.getPinned());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usersNotesList;
	}
	
	@Override
	public List<UsersNotes> getAllByNote(long idn) {
		
		if (conn == null) return null;
						
		ArrayList<UsersNotes> usersNotesList = new ArrayList<UsersNotes>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UsersNotes WHERE idn="+idn);

			while ( rs.next() ) {
				UsersNotes usersNotes = new UsersNotes();
				usersNotes.setIdu(rs.getInt("idu"));
				usersNotes.setIdn(rs.getInt("idn"));
				usersNotes.setArchived(rs.getInt("archived"));
				usersNotes.setPinned(rs.getInt("pinned"));
				usersNotes.setColor(rs.getString("color"));
							
				usersNotesList.add(usersNotes);
				logger.info("fetching all usersNotes by idn: "+usersNotes.getIdn()+"-> "+usersNotes.getIdu()+" "+usersNotes.getColor()
				+" "+usersNotes.getArchived()+" "+usersNotes.getPinned());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usersNotesList;
	}
	
	
	
	
	
	@Override
	public UsersNotes get(long idu,long idn) {
		if (conn == null) return null;
		
		UsersNotes usersNotes = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UsersNotes WHERE idu="+idu+" AND idn="+idn);			 
			if (!rs.next()) return null;
			usersNotes= new UsersNotes();
			usersNotes.setIdu(rs.getInt("idu"));
			usersNotes.setIdn(rs.getInt("idn"));
			usersNotes.setArchived(rs.getInt("archived"));
			usersNotes.setPinned(rs.getInt("pinned"));
			usersNotes.setColor(rs.getString("color"));
			
			logger.info("fetching usersNotes by idu: "+usersNotes.getIdu()+"  and idn: "+usersNotes.getIdn()+" "+usersNotes.getColor()
			+" "+usersNotes.getArchived()+" "+usersNotes.getPinned());
		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usersNotes;
	}
	
	

	@Override
	public boolean add(UsersNotes usersNotes) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO UsersNotes (idu,idn,color,archived,pinned) VALUES('"+
									usersNotes.getIdu()+"','"+
									usersNotes.getIdn()+"','"+
									usersNotes.getColor()+"','"+
									usersNotes.getArchived()+"','"+
									usersNotes.getPinned()+"')");
						
				logger.info("creating UsersNotes:("+usersNotes.getIdu()+" "+usersNotes.getIdn()+" "+usersNotes.getColor()+" "+usersNotes.getArchived()+" "+usersNotes.getPinned());
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean save(UsersNotes usersNotes) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
/*				stmt.executeUpdate("UPDATE UsersNotes SET owner='"+
									usersNotes.getOwner()+"' WHERE idu = "+usersNotes.getIdu()+" AND idn="+usersNotes.getIdn());
	*/			
				
				stmt.executeUpdate("UPDATE UsersNotes SET "
				+"archived='"+usersNotes.getArchived()
				+"', pinned='"+usersNotes.getPinned()
				+"', color='"+usersNotes.getColor()
				+"' WHERE idu = "+usersNotes.getIdu()+" AND idn="+usersNotes.getIdn());
				
				logger.info("updating UsersNotes:("+usersNotes.getIdu()+" "+usersNotes.getIdn()+" "+usersNotes.getArchived()+" "+usersNotes.getPinned());
				
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean delete(long idu, long idn) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM UsersNotes WHERE idu ="+idu+" AND idn="+idn);
				logger.info("deleting UsersNotes: "+idu+" , idn="+idn);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean deleteAllIdU(Integer idu) {
		boolean done = false;
		if (conn != null){

			List<UsersNotes> notes= this.getAllByUser(idu);
			
			for(int i=0; i<notes.size(); i++) {
				this.delete(idu, notes.get(i).getIdn());
			}
			
			done= true;
		}
		return done;
	}

	@Override
	public boolean deleteAllIdN(Integer idn) {
		boolean done = false;
		if (conn != null){

			List<UsersNotes> notes= this.getAllByNote(idn);
			
			for(int i=0; i<notes.size(); i++) {
				this.delete(notes.get(i).getIdu(), idn);
			}
			
			done= true;
		}
		return done;
	}
	
	@Override
	public void setConnection(Connection conn) {
		// TODO Auto-generated method stub
		this.conn = conn;
	}



	
	
		

	
}
