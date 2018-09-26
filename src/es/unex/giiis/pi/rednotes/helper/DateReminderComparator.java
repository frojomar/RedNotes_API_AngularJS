package es.unex.giiis.pi.rednotes.helper;

import java.util.Comparator;

import es.unex.giiis.pi.rednotes.model.Reminder;

public class DateReminderComparator implements Comparator<Reminder> {

	@Override
	public int compare(Reminder r1, Reminder r2) {
        return r1.getDate().
                compareTo(r2.getDate());
	}
	
}