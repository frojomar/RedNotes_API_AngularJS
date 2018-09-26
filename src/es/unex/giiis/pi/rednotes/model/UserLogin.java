package es.unex.giiis.pi.rednotes.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**Class that extends User, and represent the information of the user login actually on the application.
 * We use this class to extend the information about our session.
 * 
 * 
 * @author javier3rm
 *
 */

@XmlRootElement
public class UserLogin extends User{
	
	/**global definitions*/
	public static String password_regex="(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]*";
	
	/**
	 * The attribute 'date' is the date of creation of account, in this case; and not the date of friendship.
	 */
	
	/**String that store the 'password' of the logged user*/
	private String password=null;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * We complete the validatePerfilDates of User, adding password validation
	 */
	public boolean validatePerfilDates(List<String> messages){
		
		super.validatePerfilDates(messages);
		
		/*Validation of password*/
		this.validatePassword(messages);


		if(messages.isEmpty()) {
			   return true; 
		}
		else {
			   return false;
		}
	}
	

	/**Method that we use to validate the password during the registration process
	 * 
	 * @param messages Map that return the errors detected in the password.
	 * @return boolean that is true if password is correct, and false it's not.
	 */
	public boolean validatePassword(List<String> messages){
	
		if (password == null || password.trim().isEmpty()) {
			messages.add("Fill in the Password field.");
		} else if (password.length() > 40) {
			messages.add("Password length cannot be higher than 40 characters.");
		} else if (password.length() < 6) {
			messages.add("Password length must be higher than 6 characters.");
		} else if (password.contains(" ")) {
			messages.add("Password cannot have blank spaces.");
		} else if (!password.matches(password_regex)) {
			messages.add("Invalid password. It must contain at least 1 upper case and 1 number.");
		}
		
		if(messages.isEmpty()) {
			   return true; 
			}
			else {
			   return false;
			}
	}

	
}
