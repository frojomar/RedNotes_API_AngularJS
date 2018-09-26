package es.unex.giiis.pi.rednotes.dao;

import java.sql.Connection;
import java.util.List;

import es.unex.giiis.pi.rednotes.model.User;
import es.unex.giiis.pi.rednotes.model.UserLogin;

public interface UserDAO {

	/**
	 * Sets the database connection in this DAO.
	 * 
	 * @param conn
	 *            database connection.
	 */
	public void setConnection(Connection conn);

	/**
	 * Gets an user from the DB using idu.
	 * 
	 * @param idu
	 *            User Identifier.
	 * 
	 * @return User object with that idu.
	 */
	public User get(long idu);

	/**
	 * Gets an user from the DB using username.
	 * 
	 * @param username
	 *            Username of the user.
	 * 
	 * @return User object with that username.
	 */
	public User get(String username);

	/**
	 * Gets all the users from the database.
	 * 
	 * @return List of all the users from the database.
	 */
	public List<User> getAll();

	/**
	 * Adds an user to the database.
	 * 
	 * @param user
	 *            User object with the user details.
	 * 
	 * @return User identifier or -1 in case the operation failed.
	 */
	public long add(UserLogin user);

	/**
	 * Updates an existing user in the database.
	 * 
	 * @param user
	 *            User object with the new details of the existing user.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	public boolean save(UserLogin user);

	/**
	 * Deletes an user from the database.
	 * 
	 * @param idu
	 *            User identifier.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	public boolean delete(long idu);
	
	/**
	 * Search a user in the database using his email. If exists, return all the information of user.
	 * 
	 * @param email String that content the email of searched user
	 * @return UserLogin with all information of searched user. If user not exists, return null.
	 */
	public UserLogin getforLogin(String email);

	/**
	 * Search a user in the database using his username. If exists, return all the information of user.
	 * 
	 * @param email String that content the email of searched user
	 * @return UserLogin with all information of searched user. If user not exists, return null.
	 */
	public UserLogin getforLoginByUsername(String username);
	
	/**
	 * check if the email exists in the database
	 * 
	 * @param email
	 * @return
	 */
	public boolean existsEmail(String email);
	
	/**
	 * check if the username exists in the database
	 * 
	 * @param email
	 * @return
	 */
	public boolean existsUsername(String username);
	
	/**
	 * check if the password exists in the database
	 * 
	 * @param email
	 * @return
	 */
	public boolean existsPassword(String password);

	/**
	 * Search username of a idu, to complete information of a note
	 * @param username
	 * @param image
	 */
	public String searchUsername(Integer idu);
	
	/**
	 * Search image of a idu, to complete information of a note
	 * @param username
	 * @param image
	 */
	public String searchImage(Integer idu);

	/**
	 * Return all the URL of images registered in the database
	 * */
	public List<String> getAllImages();

	/**Return user that have a name or username like search 
	 * 
	 * @param search
	 * @return
	 */
	public List<User> getAllBy(String search);

	/**
	 * 
	 * @return
	 */
	public List<User> getAllFiltered(String where);

	/**Return all the images URLs avalaibles for perfil photo of the database, in a list of strings.
	 * 
	 * @return
	 */



}
