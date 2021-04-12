package deckGame;

public class Player extends Ship {
	
	private String userName;
	private int gold;
	private int deckSize;
	private Island location;
	//private ArrayList<Card> cards;
	
	/**
	 * Alters the chance for item and card rarity 
	 */
	private int luck;

	public Player(String userName, String shipName, int health, int speed, int capacity, int power) {
		super(shipName, health, speed, capacity, power);
		// TODO Auto-generated constructor stub
		location = new Island("Home", 0, 0);
		this.userName = userName;
		luck = 0;
	}
	
	public Player(String name, int health, int speed, int capacity, int power, int luck) {
		super(name, health, speed, capacity, power);
		// TODO Auto-generated constructor stub
		luck = 0;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public Island getLocation() {
		return location;
	}
	
	public void getDestroyed() {
		System.out.println("The " + getShipName() + " has been destroyed. Game Over.");
		super.getDestroyed();
	}

}
