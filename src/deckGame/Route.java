package deckGame;

public class Route {
	/**
	 * The island from which this route starts.
	 */
	private Island source;
	
	/**
	 * The island to which this route goes.
	 */
	private Island destination;
	
	/**
	 * The distance between this route's source and destination.
	 */
	private int distance;
	//private ArrayList<Event> dangers;
	
	/**
	 * The likelihood of a dangerous event happening;
	 */
	private double eventChance;
	
	
	private int routeSafety;
	
	/**
	 * Creates a new route from the source island to the destination island.
	 * @param source the island from which this route starts
	 * @param destination the island to which this route goes
	 */
	public Route(Island source, Island destination) {
		this.source = source;
		this.destination = destination;
		distance = (int) Math.ceil(Island.getDistance(source, destination));
		eventChance = Math.random()*2;
		routeSafety = 1;
	}
	
	/**
	 * Gets the distance of this route.
	 * @return the distance between this route's source and destination
	 */
	public int getDistance(){
		return distance;
	}
	
	/**
	 * Gets the destination island of this route.
	 * @return the island that this route goes to
	 */
	public Island getDestination() {
		return destination;
	}
	
	/**
	 * Gets the time taken to traverse the route at the given speed.
	 * @param speed the speed used to calculate the time to travel
	 * @return the time to sail the entire route
	 */
	public int getTime(int speed) {
		return distance/speed;
	}
	
	public double getEventChance() {
		return eventChance;
	}
	
	public void modifyEventChance(double value) {
		if (eventChance + (value * routeSafety) > 0) {
			eventChance += value * routeSafety;
		}else {
			eventChance = 0;
		}
	}
	
	/**
	 * Represents the route as a string.
	 */
	public String toString() {
		return ("The journey from " + source.getName() + " to " + destination.getName() + " is " + getDistance() + " units.");
	}
	
}
