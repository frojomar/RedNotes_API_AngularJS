package es.unex.giiis.pi.rednotes.helper;

import java.util.Comparator;

import es.unex.giiis.pi.rednotes.model.User;

public class UsernameComparator implements Comparator<User> {

	@Override
	public int compare(User user1, User user2) {
        return user1.getUsername().
                compareTo(user2.getUsername());
	}
}