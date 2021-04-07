package deckGame;

public class Route {
	private Island source;
	private Island destination;
	private int distance;
	//private ArrayList<Event> dangers;
	
	public Route(Island source, Island destination) {
		this.source = source;
		this.destination = destination;
		distance = (int) Math.ceil(Island.getDistance(source, destination));
	}
	
	
}
