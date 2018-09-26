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
import es.unex.giiis.pi.rednotes.model.NoteVersion;

public class JDBCNoteVersionsDAOImpl implements NoteVersionsDAO{

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCUserDAOImpl.class.getName());
	
	@Override
	public void setConnection(Connection conn) {
		// TODO Auto-generated method stub
		this.conn = conn;
	}

	@Override
	public List<NoteVersion> getAllBy(Integer idn) {
		if (conn == null) return null;
		
		ArrayList<NoteVersion> versions = new ArrayList<NoteVersion>();
		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
			  stmt = conn.createStatement();
			  rs = stmt.executeQuery("SELECT * FROM NoteVersions WHERE idn="+idn);
			}
			while ( rs.next() ) {
				NoteVersion version= new NoteVersion();
				version.setIdn(rs.getInt("idn"));
				
				Date date=DateTimeHelper.StringToDate(rs.getString("modificationDate"));

				version.setModificationDate(date);
				version.setIdu(rs.getInt("idu"));
				version.setTitle(rs.getString("title"));
				version.setContent(rs.getString("content"));
				
				versions.add(version);
				logger.info("fetching versions: "+version.getIdn()+" - "+version.getModificationDate()+" - "+version.getIdu()+" - "+version.getTitle()+" - "+version.getContent());
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return versions;
	}

	@Override
	public NoteVersion get(Integer idn, Date modificationDate) {
		if (conn == null) return null;
		NoteVersion version=null;
		try {
			Statement stmt;
			ResultSet rs;
			synchronized(conn){
			  stmt = conn.createStatement();
			  String date= DateTimeHelper.DateToString(modificationDate);
			  rs = stmt.executeQuery("SELECT * FROM NoteVersions WHERE idn="+idn+" AND modificationDate='"+date+"'");
			}
			if ( rs.next() ) {
				version=new NoteVersion();
				
				version.setIdn(rs.getInt("idn"));
				
				Date date=DateTimeHelper.StringToDate(rs.getString("modificationDate"));

				version.setModificationDate(date);
				version.setIdu(rs.getInt("idu"));
				version.setTitle(rs.getString("title"));
				version.setContent(rs.getString("content"));
				
				logger.info("fetching version: "+version.getIdn()+" - "+version.getModificationDate()+" - "+version.getIdu()+" - "+version.getTitle()+" - "+version.getContent());
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return version;
	}

	@Override
	public boolean delete(Integer idn, Date modificationDate) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				
				String date= DateTimeHelper.DateToString(modificationDate);

				stmt.executeUpdate("DELETE FROM NoteVersions WHERE idn="+idn+" AND modificationDate='"+date+"'");
				logger.info("deleting NoteVersion: "+idn+" - "+modificationDate);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;	
	}

	@Override
	public boolean deleteAllOf(Integer idn) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM NoteVersions WHERE idn="+idn);
				logger.info("deleting all NoteVersions of: "+idn);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;	
	}

	@Override
	public boolean add(NoteVersion nv) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				String date= DateTimeHelper.DateToString(nv.getModificationDate());
				
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO NoteVersions (idn,modificationDate,idu,title, content) VALUES('"+
									nv.getIdn()+"','"+
									date+"','"+
									nv.getIdu()+"','"+
									nv.getTitle()+"','"+
									nv.getContent()+"')");
						
				logger.info("creating NoteVersion:("+nv.getIdn()+" "+nv.getModificationDate()+" "+nv.getIdu()+" "+nv.getTitle()+" - "+nv.getContent());
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;		
	}
}
