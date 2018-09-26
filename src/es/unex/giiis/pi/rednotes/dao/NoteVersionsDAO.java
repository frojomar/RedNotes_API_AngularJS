package es.unex.giiis.pi.rednotes.dao;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import es.unex.giiis.pi.rednotes.model.Friend;
import es.unex.giiis.pi.rednotes.model.NoteVersion;

public interface NoteVersionsDAO {
	/**
	 * Sets the database connection in this DAO.
	 * 
	 * @param conn
	 *            database connection.
	 */
	public void setConnection(Connection conn);
	
	/**
	 * Gets all versions of a note
	 * 
	 * @return List of all versions of a note
	 */
	public List<NoteVersion> getAllBy(Integer idn);
	
	/**
	 * Gets data of a version of a note
	 * 
	 * @return version of a note
	 */
	public NoteVersion get(Integer idn, Date modificationDate);
	
	/** Delete the tuple of the version "idn, modificationDate"
	 * 
	 * @param search
	 * @return
	 */
	public boolean delete(Integer idn, Date modificationDate);
	
	
	/** Delete all the tuples of version of a note
	 * 
	 * @param search
	 * @return
	 */
	public boolean deleteAllOf(Integer idn);

	/**
	 * Add a new version of a note
	 * 
	 * @param nv
	 */
	public boolean add(NoteVersion nv);
}
