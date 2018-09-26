package es.unex.giiis.pi.rednotes.helper;

import java.util.Comparator;

import es.unex.giiis.pi.rednotes.model.NoteVersion;

public class VersionComparatorDESC  implements Comparator<NoteVersion> {

	@Override
	public int compare(NoteVersion version1, NoteVersion version2) {
        return version1.getModificationDate().
                compareTo(version2.getModificationDate());
	}
}