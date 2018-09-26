package es.unex.giiis.pi.rednotes.dao;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import es.unex.giiis.pi.rednotes.model.Friend;
import es.unex.giiis.pi.rednotes.model.Reminder;

public interface ReminderDAO {
	/**
	 * Sets the database connection in this DAO.
	 * 
	 * @param conn
	 *            database connection.
	 */
	public void setConnection(Connection conn);

	/**
	 * Gets all the reminders of a user from the database.
	 * 
	 * @return List of all the reminders of "idu" from the database.
	 */
	public List<Reminder> getAllBy(Integer idu);

	/**Add a new reminder to the database
	 * 
	 * @param idu
	 * @return
	 */
	public boolean add(Reminder reminder);
	

	/**Delete a reminder of the database.
	 * 
	 * @param idu
	 * @return
	 */
	public boolean delete(Reminder reminder);


	/**Delete all reminders of a user "idu"
	 * 
	 * @param idu
	 * @return
	 */
	public boolean deleteAllOfUser(Integer idu);
	

	/**Delete all reminders of a note "idn"
	 * 
	 * @param idu
	 * @return
	 */
	public boolean deleteAllOfNote(Integer idn);
	

	/**Delete all reminders of a note "idn" for a user "idu"
	 * 
	 * @param idu
	 * @return
	 */
	public boolean deleteAllOfUserAndNote(Integer idu, Integer idn);
	
	
	
}
