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
	
	public int getDistance(){
		return distance;
	}
	
	public Island getDestination() {
		return destination;
	}
	
	public int getTime(int speed) {
		return distance/speed;
	}
	
	public String toString() {
		return ("The journey from " + source.getName() + " to " + destination.getName() + " is " + getDistance() + " units.");
	}
	
}
