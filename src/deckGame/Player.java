package deckGame;

public class Player extends Ship {
	
	/**
	 * The name of this player
	 */
	private String userName;
	
	/**
	 * The amount of gold this player has
	 */
	private int gold;
	
	/**
	 * The amount of cards the player can hold
	 */
	//private int deckSize;
	
	/**
	 * The place where the player is.
	 */
	private Island location;
	
	/**
	 * The cards the player has
	 */
	//private ArrayList<Card> cards;
	
	/**
	 * Alters the chance for item and card rarity 
	 */
	private double luck;
	
	/**
	 * Creates a new player as an extension of the ship class.
	 * @param userName this player's name
	 * @param shipName the name of this player's ship
	 * @param health the amount of health this player's ship has
	 * @param speed the speed at which this player's ship travels
	 * @param capacity the amount of cargo this player's ship can hold
	 * @param power the amount of damage this player's ship can do
	 */
	public Player(String userName, String shipName, int health, int speed, int capacity, int power, int gold) {
		super(shipName, health, speed, capacity, power);
		location = new Island("Home", 0, 0);
		this.gold = gold;
		this.userName = userName;
		luck = 1;
	}
	
	/**
	 * Creates a default test player as an extension of the ship class.
	 * @param name the name of this player's ship
	 * @param health the amount of health this player's ship has
	 * @param speed the speed at which this player's ship travels
	 * @param capacity the amount of cargo this player's ship can hold
	 * @param power the amount of damage this player's ship can do
	 * @param luck the luck of the player
	 */
	public Player(String name, int health, int speed, int capacity, int power, int luck) {
		super(name, health, speed, capacity, power);
		luck = 0;
	}
	
	/**
	 * Gets this player's name.
	 * @return this player's name
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Gets this player's location.
	 * @return the island where this player is
	 */
	public Island getLocation() {
		return location;
	}
	
	/**
	 * Announces that this player's ship has been destroyed.
	 * <p>
	 * Says game over and sets the status of this player's ship to destroyed.
	 */
	public void getDestroyed() {
		System.out.println("The " + getShipName() + " has been destroyed. Game Over.");
		super.getDestroyed();
	}
	
	/**
	 * Changes this ship's location from the source island 
	 * of the route to destination.
	 * @param route the route to travel
	 */
	public void sail(Route route) {
		location = route.getDestination();
	}
	
	/**
	 * Changes the amount of gold this player has
	 * @param amount the amount of gold to change by
	 */
	public void modifyGold(int amount) {
		gold += amount;
	}
	
	/**
	 * Gets the amount of gold that this player has
	 * @return the amount of gold
	 */
	public int getGold() {
		return gold;
	}
	
	/**
	 * Gets the value of the player's luck stat
	 * @return the value of the player's luck
	 */
	public double getLuck() {
		return luck;
	}
	
	/*
	public boolean addItem(Card card) {
		return false;
	}
	*/
	
	/**
	 * Adds a cargo item to this player's inventory using the ship method
	 */
	public boolean addItem(Cargo cargo) {
		return super.addItem(cargo);
	}

}
