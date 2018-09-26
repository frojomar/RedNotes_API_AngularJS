package es.unex.giiis.pi.rednotes.model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NoteVersion {

		private Integer idn;
		private Date modificationDate;
		private Integer idu;
		private String title;
		private String content;
		
		
		public boolean validate(List<String> messages) {
			
			if(idn==null || idn<0) {
				messages.add(new String("ERROR: idn no es valido"));
			}
			
			if(idu==null || idu<0) {
				messages.add(new String("ERROR: idu no es valido"));
			}
			
			if(modificationDate==null) {
				messages.add(new String("ERROR: modificationDate no es valido"));
			}
			
			if(title==null || title.equals("")) {
				messages.add(new String("ERROR: title no es valido"));
			}
			
			if(content==null || content.equals("")) {
				messages.add(new String("ERROR: content no es valido"));
			}
			
			
			if(messages.isEmpty()) {
				return true;
			}
			else {
				return false;
			}
		}
		
		
		public Integer getIdn() {
			return idn;
		}
		public void setIdn(Integer idn) {
			this.idn = idn;
		}
		public Date getModificationDate() {
			return modificationDate;
		}
		public void setModificationDate(Date modificationDate) {
			this.modificationDate = modificationDate;
		}
		public Integer getIdu() {
			return idu;
		}
		public void setIdu(Integer idu) {
			this.idu = idu;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}

		
		
		
}
