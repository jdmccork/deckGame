package deckGame;

import java.util.ArrayList;

public class Island {
	private String stores; // temp till the Store class is added
	private String islandName;
	private ArrayList<Route> routes;
	private int locationX;
	private int locationY;
	
	public Island(String name, int x, int y) {
		this.islandName = name;
		routes = new ArrayList<Route>();
		locationX = x;
		locationY = y;
	}
	
	public static double getDistance(Island source, Island destination) {
		int x = source.locationX - destination.locationX;
		int y = source.locationY - destination.locationY;
		return Math.sqrt(x*x+y*y);
	}
	
	public void generateRoutes(ArrayList<Island> islands) {
		for (Island destination : islands) {
			if (this != destination) {
				routes.add(new Route(this, destination));
			}
		}
	}
	
	public ArrayList<Route> getRoutes() {
		return routes;
	}
	
	public void displayRoutes() {
		System.out.println("The routes avalible for this island are:" + routes.size());
		for (Route route : routes) {
			System.out.println(route);
		}
	}
	
	public String getStore(){
		return stores;
	}
	
	public String toSting() {
		return ("Island: " + islandName + "\nLocation: (" + locationX + ", " + locationY + ")");
	}
	
	/*
	public void getStores{
		
	}
	*/
	
	public String getName(){
		return islandName;
	}
	/*
	public static void main(String[] args) {
		Island firstIsland = new Island("Home", 0, 0);
		Island secondIsland = new Island("Uni", 10, 15);
		Island thirdIsland = new Island("Shop", 5, -20);
		ArrayList<Island> islands = new ArrayList<Island>();
		islands.add(firstIsland);
		islands.add(secondIsland);
		islands.add(thirdIsland);
		for (Island island : islands) {
			island.generateRoutes(islands);
			island.displayRoutes();
		}
	}
	*/
	
}
