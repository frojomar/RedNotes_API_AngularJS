package es.unex.giiis.pi.rednotes.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UsersNotes {
	private int idu;
	private int idn;
	private int archived;//0=false,1=true
	private int pinned;//0=false,1=true
	private String color;

	public boolean validate(List<String> validationMessages) {
		if (archived!=0 && archived!=1) {
			validationMessages.add("Fill in the Archived field with a correct value (0=false,1=true).");
		} 

		if (pinned!=0 && pinned!=1) {
			validationMessages.add("Fill in the Pinned field with a correct value (0=false,1=true).");
		} 

		if(idu<0) {
			validationMessages.add("The idu must be a valid int (>=0)");

		}
		
		if(idn<0) {
			validationMessages.add("The idn must be a valid int (>=0)");

		}
		
		if(!color.equals("yellow") && !color.equals("red") && !color.equals("blue") &&
				!color.equals("green") && !color.equals("orange")) {
			validationMessages.add("This color is not a color valid: yellow - red - blue - green - orange");
		}
		
		if (validationMessages.isEmpty())
			return true;
		else
			return false;
	}
	
	public int getIdu() {
		return idu;
	}
	public void setIdu(int idu) {
		this.idu = idu;
	}
	
	public int getIdn() {
		return idn;
	}
	public void setIdn(int idn) {
		this.idn = idn;
	}

	public int getArchived() {
		return archived;
	}
	public void setArchived(int stored) {
		this.archived = stored;
	}
	public int getPinned() {
		return pinned;
	}
	public void setPinned(int marked) {
		this.pinned = marked;
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	


}
