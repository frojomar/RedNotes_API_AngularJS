package es.unex.giiis.pi.rednotes.model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**Class that represent the information of a user of the application.
 * We use this class to represent friends information, and ourselves information too.
 * 
 * @author javier3rm
 *
 */
@XmlRootElement
public class User {

	/**global definitions*/
	public static String name_regex="[A-Za-záéíóúñÁÉÍÓÚ]{2,}([\\s][A-Za-záéíóúñÁÉÍÓÚ]{2,})*";
	public static String city_regex="[A-Za-záéíóúñÁÉÍÓÚ]{2,}([\\s][A-Za-záéíóúñÁÉÍÓÚ]{2,})*";
	/*public static String telephone_regex="";*/
	public static String email_regex="[a-zA-Z0-9_\\.\\+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-\\.]+";
	public static String username_regex="[a-zA-Z][a-zA-Z0-9_-]*";
	
	/**Personal information*/
	
	/**Integer that store the id of the user*/
	protected Integer idu=null;
	/**String that store the name of the user*/
	protected String name="";
	/**String that store the country of the user*/
	protected String country="";
	/**String that store the city of the user*/
	protected String city="";
	/**Integer that store the age of the user*/
	protected Integer age=null;
	/**String that store the telephone of the user*/
	protected String telephone="";
	
	/**Other information*/
	/**String that store the e-mail of the user*/
	protected String email=null;
	/**String that store the username of the user*/
	protected String username=null;
	/**Date that store the date friendship of the user with user logged*/
	protected Date date=null;
	/**URL of the image that uses the user for his perfil*/
	protected String image="https://vignette.wikia.nocookie.net/doblaje/images/e/ea/BenderB.jpg/revision/latest?cb=20121030160916&path-prefix=es";
	

	
	public boolean validatePerfilDates(List<String> messages){
		   
		/*Validation of name*/
		if(!name.matches(name_regex) && !name.equals("")) {
		   messages.add("Invalid name: " + name.trim());
		}
		
		/*Validation of city*/
		if(!city.matches(city_regex) && !city.equals("")) {
		   messages.add("Invalid city: " + city.trim());
		}
	   
		/*Validation of age*/
		/*if(age!=null) {
			if(age<8 && age>90) {
				   messages.add("Invalid age: "+age);
			} 
		}*/
		
		/*Validation of telephone*/
		/*if(!telephone.trim().matches(telephone_regex)) {
		   messages.add("Invalid telephone: " + telephone.trim());
		}	*/
					
		/*Validation of email*/
		this.validateEmail(messages);
		
		/*Validation of username*/
		this.validateUsername(messages);

		
		if(messages.isEmpty()) {
		   return true; 
		}
		else {
		   return false;
		}
	}
	
	public boolean validateEmail(List<String> messages){
	
		/*Validation of email*/
		if (email == null || email.trim().isEmpty()) {
			messages.add("Fill in the email field.");
		} else if (email.length() > 70) {
			messages.add("Email length cannot be higher than 70 characters.");
		} else if (email.contains(" ")) {
			messages.add("Email cannot have blank spaces.");
		} else if (!email.matches(email_regex)) {
			messages.add("Invalid Email.");
		}
		
		if(messages.isEmpty()) {
			   return true; 
			}
			else {
			   return false;
			}
	}
	
	public boolean validateUsername(List<String> messages){
	
		/*Validation of username*/
		if (username == null || username.trim().isEmpty()) {
			messages.add("Fill in the Username field.");
		} else if (username.length() > 16) {
			messages.add("Username length cannot be higher than 16 characters.");
		} else if (username.length() < 3) {
			messages.add("Username length must be higher than 3 characters.");
		} else if (username.contains(" ")) {
			messages.add("Username cannot have blank spaces.");
		} else if (!username.matches(username_regex)) {
			messages.add("Invalid Username (Pattern allowed:[a-zA-Z][a-zA-Z0-9_-]*).");
		}	
		
		if(messages.isEmpty()) {
			   return true; 
			}
			else {
			   return false;
			}
	}
	
	
	public Integer getIdu() {
		return idu;
	}

	public void setIdu(Integer idu) {
		this.idu = idu;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
}
