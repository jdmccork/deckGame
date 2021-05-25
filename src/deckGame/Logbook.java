package deckGame;

import java.util.ArrayList;

public class Logbook {
	
	/**
	 * Where all the entries of the logbook are stored.
	 */
	private ArrayList<Entry> entries;

	/**
	 * Creates a new instance of a logbook.
	 */
	public Logbook() {
		// TODO Auto-generated constructor stub
		entries = new ArrayList<Entry>();
	}

	/**
	 * Adds an entry into the entries ArrayList.
	 * @param entry
	 */
	public void addEntry(Entry entry) {
		entries.add(entry);
	}
	
	/**
	 * Returns all the entries in the logbook.
	 * @return
	 */
	public ArrayList<Entry> getEntries(){
		return entries;
	}
	
	/**
	 * Prints all the entries in the logbook.
	 */
	public void viewEntries() {
		for (Entry entry: entries) {
			System.out.println(entry);
		}
	}
}
