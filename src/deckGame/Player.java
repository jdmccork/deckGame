package deckGame;

import java.util.ArrayList;

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
	private int deckSize;
	
	/**
	 * The place where the player is.
	 */
	private Island location;
	
	/**
	 * The cards the player has
	 */
	private ArrayList<Card> cards;
	
	/**
	 * A list of cargo that is currently being transported
	 */
	private ArrayList<Cargo> inventory;
	
	/**
	 * The number of item space that can be used on the ship
	 */
	private int capacity;
	
	/**
	 * Alters the chance for item and card rarity 
	 */
	private int luck;
	
	private int cargoStored;
	
	private int crew;
	
	/**
	 * Creates a new player as an extension of the ship class.
	 * @param userName this player's name
	 * @param shipName the name of this player's ship
	 * @param health the amount of health this player's ship has
	 * @param speed the speed at which this player's ship travels
	 * @param capacity the amount of cargo this player's ship can hold
	 * @param power the amount of damage this player's ship can do
	 */
	public Player(String userName, String shipName, int health, int speed, int capacity, int power, int gold, int crew, Island location) {
		super(shipName, health, speed, power);

		this.capacity = capacity;
		this.location = location;
		this.gold = gold;
		this.userName = userName;
		luck = 0;
		inventory = new ArrayList<Cargo>();
		cards = new ArrayList<Card>();
		cargoStored = 0;
		
		this.crew = crew;
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
	 * Gets the capacity of this ship
	 * @return the capacity of the ship
	 */
	public int getCapacity() {
		return capacity;
	}
	
	public boolean modifyCapacity(int amount) {
		if (capacity + amount >= inventory.size()) {
			capacity += amount;
			return true;
		}
		return false;
	}
	
	/**
	 * Announces that this player's ship has been destroyed.
	 * <p>
	 * Says game over and sets the status of this player's ship to destroyed.
	 */
	public void getDestroyed() {
		System.out.println("The " + getShipName() + " has been destroyed. Game Over.");
		super.getDestroyed();
		throw new EndGameException();
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
	public int getLuck() {
		return luck;
	}
	
	public void modifyLuck(int amount) {
		luck += amount;
	}
	
	/**
	 * Prints out the number of items in this ship's inventory, 
	 * then prints each item in turn.
	 */
	public void printInventory() {
		if(inventory.size() == 1) {
			System.out.println("There is currently " + inventory.size() + " item on the ship:");
		} else {
			System.out.println("There are currently " + inventory.size() + " items on the ship:");
		}
		int i = 1;
		for (Cargo cargo: inventory) {
			System.out.println(i++ + ": " + cargo.getName());
		}
	}
	
	public void printCards() {
		if(cards.size() == 1) {
			System.out.println("There is currently " + cards.size() + " item on the ship:");
		} else {
			System.out.println("There are currently " + cards.size() + " items on the ship:");
		}
		int i = 1;
		for (Card card: cards) {
			System.out.println(i++ + ": " + card.getName());
		}
	}
	
	/**
	 * Adds the given cargo to this ship's inventory and checks
	 * if the capacity has not been exceeded.
	 * @param cargo the cargo to add
	 */
	public boolean addItem(Item item) {
		if (item instanceof Cargo) {
			Cargo cargo = (Cargo) item;
			if (cargoStored <= capacity) {
				if (cargo.alterStat(this)) {
					inventory.add(cargo);
					return true;
				}
			}
		} else if (item instanceof Card) {
			Card card = (Card) item;
			if (cards.size() < deckSize) {
				cards.add(card);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes the first instance of the given kind of cargo
	 * from this ship's inventory.
	 * @param cargo the cargo to remove
	 */
	public void removeItem(Item item) {
		if (item instanceof Cargo) {
			if (inventory.contains(item)) {
				inventory.remove(item);
				((Cargo) item).alterStat(this); //TODO Doesn't reduce
			}
		}else if (item instanceof Card) {
			cards.remove(item);
		}
	}
	
	public ArrayList<Card> getCards(){
		return cards;
	}
	
	
	public void viewInventory() {
		printInventory();
		System.out.println((inventory.size() + 1) + ": Return\n" + "Select an item or return to continue.");
		int selection = Game.getInt();
		if (selection == inventory.size() + 1) {
			return;
		}else if (selection <= inventory.size()) {
			inventory.get(selection - 1).viewItem();
		}else {
			System.out.println("Please enter a number between 1 and " + getInventory().size() + 1 + ".");
		}
	}
	
	public void viewCards() {
		printCards();
		System.out.println((cards.size() + 1) + ": Return\n" + "Select an item or return to continue.");
		int selection = Game.getInt();
		if (selection == cards.size() + 1) {
			return;
		}else if (selection <= cards.size()) {
			cards.get(selection - 1).viewItem();
		}else {
			System.out.println("Please enter a number between 1 and " + inventory.size() + 1 + ".");
		}
	}
	
	/**
	 * Gets the inventory of the ship
	 * @return the ArrayList of the items in the inventory
	 */
	public ArrayList<Cargo> getInventory() {
		return inventory;
	}
	
	public int getNumCrew() {
		return crew;
	}

}
