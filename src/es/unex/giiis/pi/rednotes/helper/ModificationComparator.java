package es.unex.giiis.pi.rednotes.helper;

import java.util.Comparator;

import es.unex.giiis.pi.rednotes.model.Note;

public class ModificationComparator implements Comparator<Note> {

	@Override
	public int compare(Note nota1, Note nota2) {
        return nota1.getModificationDate().
                compareTo(nota2.getModificationDate());
	}
}