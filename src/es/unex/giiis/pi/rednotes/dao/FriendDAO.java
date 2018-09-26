package es.unex.giiis.pi.rednotes.dao;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import es.unex.giiis.pi.rednotes.model.Friend;
import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.model.UserLogin;

public interface FriendDAO {
	/**
	 * Sets the database connection in this DAO.
	 * 
	 * @param conn
	 *            database connection.
	 */
	public void setConnection(Connection conn);

	/**
	 * Gets all the friends of a user from the database.
	 * 
	 * @return List of all the users from the database.
	 */
	public List<Friend> getAllBy(Integer idu);

	/**Gets the petitions not confirmed sent BY "idu"
	 * 
	 * @param idu
	 * @return
	 */
	public List<Friend> getAllNotConfirmedBy(Integer idu);
	

	/**Gets the petitions not confirmed sent TO "idu"
	 * 
	 * @param idu
	 * @return
	 */
	public List<Friend> getAllNotConfirmedTo(Integer idu);
	
	
	/**
	 * Gets data of a friendship from the database.
	 * 
	 * @return List of all the users from the database.
	 */
	public Friend get(Integer idu1, Integer idu2);

	
	/**Add a entry, of a petition of friendship
	 * 
	 * @param idu
	 * @param idu2
	 */
	public boolean add(Friend friend);
	
	
	/**Confirm a entry, of a petition of friendship
	 * 
	 * @param idu
	 * @param idu2
	 */
	public boolean save(Friend friend);
	
	/**Confirm a entry, of a petition of friendship
	 * 
	 * @param idu
	 * @param idu2
	 */
	public boolean confirm(Integer idu1, Integer idu2, Date dateFriendship);
	
	/** Delete the tuple of Friend of Idu1-Idu2 key
	 * 
	 * @param search
	 * @return
	 */
	public boolean delete(Integer idu, Integer idu2);

	/**Delete all tuples where idu1 or idu2 is like idu
	 * 
	 * @param idu
	 * @return
	 */
	public boolean deleteAllOf(Integer idu);

}
