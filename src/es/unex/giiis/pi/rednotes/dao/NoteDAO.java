package es.unex.giiis.pi.rednotes.dao;

import java.sql.Connection;
import java.util.List;

import es.unex.giiis.pi.rednotes.model.Note;

public interface NoteDAO {

	/**
	 * set the database connection in this DAO.
	 * 
	 * @param conn
	 *            database connection.
	 */
	public void setConnection(Connection conn);
	
	/**
	 * Gets a note from the DB using idn.
	 * 
	 * @param idn
	 *            Note Identifier.
	 * 
	 * @return Note object with that idn.
	 */
	public Note get(long idn);

	/**
	 * Gets all the notes from the database.
	 * 
	 * @return List of all the notes from the database.
	 */
	public List<Note> getAll();
	
	/**
	 * Gets all the notes from the database that contain a text in the title.
	 * 
	 * @param search
	 *            Search string .
	 * 
	 * @return List of all the notes from the database that contain a text either in the title.
	 */	
	public List<Note> getAllBySearchTitle(String search);
	/**
	 * Gets all the notes from the database that contain a text in the content.
	 * 
	 * @param search
	 *            Search string .
	 * 
	 * @return List of all the notes from the database that contain a text either in the content.
	 */	
	public List<Note> getAllBySearchContent(String search);
	/**
	 * Gets all the notes from the database that contain a text either in the title or in the content.
	 * 
	 * @param search
	 *            Search string .
	 * 
	 * @return List of all the notes from the database that contain a text either in the title or in the content.
	 */	
	public List<Note> getAllBySearchAll(String search);

	/**
	 * Adds a note to the database.
	 * 
	 * @param note
	 *            Note object with the note details.
	 * 
	 * @return Note identifier or -1 in case the operation failed.
	 */
	
	public Integer add(Note note);

	/**
	 * Updates an existing note in the database.
	 * 
	 * @param note
	 *            Note object with the new details of the existing note.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	
	
	public boolean save(Note note);

	/**
	 * Deletes a note from the database.
	 * 
	 * @param idn
	 *            Note identifier.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	
	public boolean delete(long idn);

	/**
	 * Delete all notes where ownerID is idu
	 * 
	 * @param idu id of the owner of the note
	 */
	public boolean deleteAllIdU(Integer idu);
}
