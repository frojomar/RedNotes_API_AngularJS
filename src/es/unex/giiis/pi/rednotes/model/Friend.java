
package es.unex.giiis.pi.rednotes.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Friend {

	private Integer idA;
	private Integer idB;
	private Date dateFriendship;
	private Integer confirmed; //0 is not, 1 is yes
	public Integer getIdA() {
		return idA;
	}
	public void setIdA(Integer idA) {
		this.idA = idA;
	}
	public Integer getIdB() {
		return idB;
	}
	public void setIdB(Integer idB) {
		this.idB = idB;
	}
	public Date getDateFriendship() {
		return dateFriendship;
	}
	public void setDateFriendship(Date dateFriendship) {
		this.dateFriendship = dateFriendship;
	}
	public Integer getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(Integer confirmed) {
		this.confirmed = confirmed;
	}
	
	
}
