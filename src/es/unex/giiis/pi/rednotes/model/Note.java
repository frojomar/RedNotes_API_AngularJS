package es.unex.giiis.pi.rednotes.model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Note {
	private int idn;
	private String title;
	private String content;
	private int ownerID; 
	private Date creationDate;
	private Date modificationDate;
	private int type;
	
	
	
	public boolean validate(List<String> validationMessages) {
	if (title == null || title.trim().isEmpty() || title.length() < 4) {
		validationMessages.add("The title must be higher than 3 characters.");
	} else if (title.length() > 50) {
		validationMessages.add("The title cannot be higher than 50 characters.");
	}

	if(creationDate ==null) {
		validationMessages.add("The creationDate can not be null.");

	}
	
	if(modificationDate==null) {
		validationMessages.add("The modificationDate can note be null");
	}
	
	if(ownerID<0) {
		validationMessages.add("Owner ID is not valid");
	}
	
	if(type !=0 && type!=1) {
		validationMessages.add("Type is not valid");
	}
	
	if (validationMessages.isEmpty())
		return true;
	else
		return false;
	}
	
	public int getIdn() {
		return idn;
	}
	public void setIdn(int idn) {
		this.idn = idn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String newTitle) {
		this.title = newTitle;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String newContent) {
		this.content = newContent;
	}
	public int getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(int propietary) {
		this.ownerID = propietary;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	


}
