package deckGame;

import java.util.ArrayList;

public class Logbook {
	
	private ArrayList<Entry> entries;

	public Logbook() {
		// TODO Auto-generated constructor stub
		entries = new ArrayList<Entry>();
	}

	
	public void addEntry(Entry entry) {
		entries.add(entry);
	}
	
	public ArrayList<Entry> getEntries(){
		return entries;
	}
	
	public void viewEntries() {
		for (Entry entry: entries) {
			System.out.println(entry);
		}
	}
}
