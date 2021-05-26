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
	
	/**
	 * An instance of event which contains the chance of each event happening
	 */
	private Event event;
	
	/**
	 * Creates a new route from the source island to the destination island.
	 * @param source the island from which this route starts
	 * @param destination the island to which this route goes
	 */
	public Route(Island source, Island destination) {
		this.source = source;
		this.destination = destination;
		distance = (int) Math.ceil(Island.getDistance(source, destination));
		int nothingChance = Math.min(1,(int) (Math.random() * 25));
		int pirateChance = (int) (Math.random() * 10);
		int stormChance = (int) (Math.random() * 15);
		int rescueChance = (int) (Math.random() * 5);
		int[] eventChances = new int[] {nothingChance, pirateChance, stormChance, rescueChance};
		event = new Event(eventChances);
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
		int time = (distance*5)/speed;
		if (time <= 0) {
			return 1;
		} else {
			return time;
		}
	}
	
	/**
	 * Represents the route as a string.
	 */
	public String toString() {
		return ("The journey from " + source.getName() + " to " + destination.getName() + " is " + getDistance() + " units.");
	}
	
	/**
	 * 
	 * @return The instance of Event stored in the route
	 */
	public Event getEvent() {
		return event;
	}
	
	/**
	 * Prints the chance of any event happening on a route
	 */
	public void viewEvents() {
		int totalProbability = event.getEventChance().size();
		int count = 0;
		String output = "There is a: ";
		for (int nothingOccurance: event.getEventChance()) {
			if (nothingOccurance == 1) {
				count += 1;
			}
		}
		if (count >= 1) {
			output += "\n" + count/totalProbability + " chance for nothing to happen";
			}
		
		count = 0;
		for (int pirateOccurance: event.getEventChance()) {
			if (pirateOccurance == 2) {
				count += 1;
			}
		}
		if (count >= 1) {
			output += "\n" + count/totalProbability + " chance to be attacked by pirates";
		}
		
		count = 0;
		for (int stormOccurance: event.getEventChance()) {
			if (stormOccurance == 3) {
				count += 1;
			}
		}
		if (count >= 1) {
			output += "\n" + count/totalProbability + " chance to encounter a storm";
		}
		
		count = 0;
		for (int rescueOccurance: event.getEventChance()) {
			if (rescueOccurance == 4) {
				count += 1;
			}
		}
		if (count >= 1) {
			output += "\n" + count/totalProbability + " chance to find sailors in need of rescue";
		}
	}
}
