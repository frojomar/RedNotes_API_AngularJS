package es.unex.giiis.pi.rednotes.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;

public class IntegerStringPair {

	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
	
	private Integer value;
	private String text;
	
	public IntegerStringPair() {
		this.value=0;
		this.text="";
	}
	public IntegerStringPair(Integer value, String text) {
		this.value=value;
		this.text=text;
	}
	
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	/**Method that parse a String to a list of IntegerStringPair. Each element must separate with /;/
	 * Example:0/;/hello world/;/4/;/second element/;/2/;/always the same...
	 * 
	 * NOTE: it's very important doesn't put /;/ in the beggining
	 * 
	 * @param content String that has the format explained before
	 * @return List of IntegerStringPair
	 */
	public static List<IntegerStringPair> parseToListOfElements(String content){
		List<IntegerStringPair> result= new ArrayList<IntegerStringPair>();
		
		String[] parts = content.split("/;/");
		
		if(parts.length%2!=0) {
			logger.info("List of elements it's not valid to parse");
			return null;
		}
		
		try {
			for(int i=0; i<parts.length;i++) {
				IntegerStringPair element= new IntegerStringPair();
				element.setValue(Integer.parseInt(parts[i]));
				i++;
				element.setText(parts[i]);
				
				result.add(element);
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		return result;
	}
	
}
