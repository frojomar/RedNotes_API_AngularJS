package es.unex.giiis.pi.rednotes.model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Reminder {

	private Date date;
	private Integer idn;
	private Integer idu;
	private String description;
	private Note n;
	private String dateString;
	
	
	public Reminder() {
		
	}

	public boolean validate(List<String> messages) {
		
		if(idn==null || idn<0) {
			messages.add(new String("ERROR: idn no es valido"));
		}
		
		if(idu==null || idu<0) {
			messages.add(new String("ERROR: idu no es valido"));
		}
		
		if(date==null) {
			messages.add(new String("ERROR: Date no es valido"));
		}
		
		if(description==null) {
			description="";
		}
		
		if(messages.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}

	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Integer getIdn() {
		return idn;
	}


	public void setIdn(Integer idn) {
		this.idn = idn;
	}


	public Note getN() {
		return n;
	}


	public void setN(Note n) {
		this.n = n;
	}


	public Integer getIdu() {
		return idu;
	}


	public void setIdu(Integer idu) {
		this.idu = idu;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getDateString() {
		return dateString;
	}


	public void setDateString(String dateString) {
		this.dateString = dateString;
	}



	
	
}
