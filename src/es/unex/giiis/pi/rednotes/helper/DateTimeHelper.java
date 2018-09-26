package es.unex.giiis.pi.rednotes.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateTimeHelper {
	
	public static String time2Date(long time){
    	Date date01 = new Date();
		date01.setTime(time);
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
		String dateFormatted = formatter.format(date01);
		return dateFormatted;
    }
	
	public static String DateToString(Date date) {
		DateFormat formatter = new SimpleDateFormat("d-MMM-yyyy,HH:mm:ss aaa");
		String s = formatter.format(date);
		return s;
	}
	
	public static Date StringToDate(String s) {
		DateFormat formatter = new SimpleDateFormat("d-MMM-yyyy,HH:mm:ss aaa");
		Date date=null;
		try {
			date = formatter.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public static String DateToString2(Date date) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
		String s = formatter.format(date);
		return s;
	}
	
	public static Date StringToDate2(String s) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
		Date parsed=null;
		try {
			parsed = format.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parsed;
	}
	
}
