package deckGame;

import java.util.ArrayList;

public class Island {
	/**
	 * The cargo store that is on this island.
	 */
	private Store store;
	
	/**
	 * Location in the display grid the island will be visable
	 */
	private int displayLocation = 0;
	
	/**
	 * The name of this island.
	 */
	private String islandName;
	
	/**
	 * A list of the routes from this island to others.
	 */
	private ArrayList<Route> routes;
	
	/**
	 * The x co-ordinate of this island's location.
	 */
	private int locationX;
	
	/**
	 * The y co-ordinate of this island's location.
	 */
	private int locationY;
	
	/**
	 * Creates a new island with a given name and position.
	 * @param name the name of this island
	 * @param x the x co-ordinate of this island's position
	 * @param y the y co-ordinate of this island's position
	 */
	public Island(String name, int x, int y, int location) {
		this.islandName = name;
		routes = new ArrayList<Route>();
		locationX = x;
		locationY = y;
		setDisplay(location);
		store = new Store(this);
	}
	
	/**
	 * Sets the location that the island will be on the display grid
	 * @param location
	 */
	public void setDisplay(int location) {
		this.displayLocation = location;
	}
	
	/**
	 * @return location of the island on the display grid
	 */
	public int getDisplay() {
		return this.displayLocation;
	}
	
	/**
	 * Gets the distance between two islands.
	 * @param source the first island of the pair to find distance between
	 * @param destination the second island of the pair to find distance between
	 * @return the distance of the straight line between the two islands
	 */
	public static double getDistance(Island source, Island destination) {
		if (source == null | destination == null) {
			return 0;
		}
		int x = source.locationX - destination.locationX;
		int y = source.locationY - destination.locationY;
		return Math.sqrt(x*x+y*y);
	}
	
	/**
	 * Creates a new route between this island and every other island.
	 * @param islands the list of other islands.
	 */
	public void generateRoutes(ArrayList<Island> islands) {
		for (Island destination : islands) {
			if (this != destination) {
				routes.add(new Route(this, destination));
			}
		}
	}
	
	/**
	 * Gets the routes from this island to other islands.
	 * @return the routes available from this island.
	 */
	public ArrayList<Route> getRoutes() {
		return routes;
	}
	
	/**
	 * Displays the routes available from this island.
	 */
	public void displayRoutes() {
		System.out.println("The routes available for this island are:" + routes.size());
		for (Route route : routes) {
			System.out.println(route);
		}
	}
	
	/**
	 * Gets the store(s) at this island.
	 * @return This island's store(s)
	 */
	public Store getStore(){
		return store;
	}
	
	/**
	 * Gets a string representation of this island.
	 */
	public String toString() {
		return ("Island: " + islandName + "\nLocation: (" + locationX + ", " + locationY + ")");
	}
	
	/**
	 * Gets the name of this island.
	 * @return this island's name
	 */
	public String getName(){
		return islandName;
	}
}
