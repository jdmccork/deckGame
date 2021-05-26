package deckGame;

import java.util.ArrayList;

import enums.Actions;

public class Player extends Ship {
	
	/**
	 * The name of this player.
	 */
	private String userName;
	
	/**
	 * The amount of gold this player has.
	 */
	private int gold;
	
	/**
	 * The amount of cards the player can hold.
	 */
	private int deckSize;
	
	/**
	 * The place where the player is.
	 */
	private Island location;
	
	/**
	 * The cards the player has.
	 */
	private ArrayList<Card> cards;
	
	/**
	 * A list of cargo that is currently being transported.
	 */
	private ArrayList<Cargo> inventory;
	
	/**
	 * The number of item space that can be used on the ship.
	 */
	private int capacity;
	
	/**
	 * Alters the chance for item and card rarity .
	 */
	private int luck;
	
	/**
	 * The amount of cargo that is currently stored in the ship.
	 */
	private int cargoStored;
	
	/**
	 * The number of crew required to run the ship.
	 */
	private int crew;
	
	/**
	 * The ships logbook which holds entries of all the events that occur to the player.
	 */
	private Logbook logbook;
	
	/**
	 * The display that is used by the game.
	 */
	private Display display;
	
	/**
	 * Creates a new player as an extension of the ship class.
	 * @param userName this player's name
	 * @param shipName the name of this player's ship
	 * @param health the amount of health this player's ship has
	 * @param speed the speed at which this player's ship travels
	 * @param capacity the amount of cargo this player's ship can hold
	 * @param power the amount of damage this player's ship can do
	 */
	public Player(String userName, String shipName, int health, int speed, int capacity, int deckSize
			, int power, int gold, int crew, Island location, Display display) {
		super(shipName, health, speed, power);

		this.capacity = capacity;
		this.location = location;
		this.gold = gold;
		this.userName = userName;
		this.crew = crew;
		this.deckSize = deckSize;
		this.display = display;
		luck = 0;
		inventory = new ArrayList<Cargo>();
		cards = new ArrayList<Card>();
		cargoStored = 0;
		logbook = new Logbook();
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
	
	/**
	 * Changes the amount of cargo that the ship can carry. 
	 * @param amount
	 * @return false if the cargo would reduce the storage capacity below the current storage
	 */
	public boolean modifyCapacity(int amount) {
		if (capacity + amount >= cargoStored) {
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
	public String getDestroyed() {
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
	public boolean modifyGold(int amount) {
		if (gold + amount >= 0) {
			gold += amount;
			display.updateDialogue(String.valueOf(gold));
			return true;
		} else {
			return false;
		}
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
	
	/**
	 * Changes the luck value associated with the player. This has no restrictions.
	 * @param amount
	 */
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
	
	/**
	 * Prints out the number of cards in the players deck, 
	 * then prints each item in turn.
	 */
	public void printCards() {
		if(cards.size() == 1) {
			System.out.println("There is currently " + cards.size() + " card in your deck:");
		} else {
			System.out.println("There are currently " + cards.size() + " cards in your deck:");
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
			if (cargoStored + cargo.getSize() <= capacity) {
				if (cargo.alterStat(this, 1)) {
					inventory.add(cargo);
					cargoStored += item.getSize();
					return true;
				}
			}
		} else if (item instanceof Card) {
			Card card = (Card) item;
			if (cards.size() < deckSize) {
				for (int i = 0; i < cards.size(); i++) {
					if (card.getPriority() < cards.get(i).getPriority() | cards.size() == 0) {
						cards.add(i, card);
						return true;
					}
				}
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
				if (((Cargo) item).alterStat(this, -1)) {
					inventory.remove(item);
					cargoStored -= item.getSize();
					
				}
			}
		}else if (item instanceof Card) {
			cards.remove(item);
		}
		item.setPurchaseCost(-1);
	}
	
	/**
	 * Returns the cards that are currently in the players deck
	 * @return
	 */
	public ArrayList<Card> getCards(){
		return cards;
	}
	
	public int getDeckSize() {
		return deckSize;
	}
	
	/**
	 * Formats the inventory of the player and allows them to select cargo to view
	 */
	public void viewInventory() {
		while (true) {
			printInventory();
			if (inventory.size() == 0) {
				Game.pause();
				return;
			}
			System.out.println((inventory.size() + 1) + ": Return\n" + "Select an item or return to continue.");
			int selection = Game.getInt();
			if (selection == inventory.size() + 1) {
				return;
			}else if (selection <= inventory.size()) {
				inventory.get(selection - 1).viewItem();
			}else {
				System.out.println("Please enter a number between 1 and " + (getInventory().size() + 1) + ".");
				Game.pause();
			}
		}
	}
	
	/**
	 * Formats the deck of the player and allows them to select cards to view
	 */
	public void viewCards() {
		while(true) {
			printCards();
			if (cards.size() == 0) {
				Game.pause();
				return;
			}
			System.out.println((cards.size() + 1) + ": Return\n" + "Select an item or return to continue.");
			int selection = Game.getInt();
			if (selection == cards.size() + 1) {
				return;
			}else if (selection <= cards.size()) {
				cards.get(selection - 1).viewItem();
			}else {
				System.out.println("Please enter a number between 1 and " + (inventory.size() + 1) + ".");
				Game.pause();
			}
		}
	}
	
	/**
	 * Displays the players logbook
	 */
	public void viewLogbook() {
		logbook.viewEntries();
		Game.pause();
	}
	
	/**
	 * Gets the inventory of the ship
	 * @return the ArrayList of the items in the inventory
	 */
	public ArrayList<Cargo> getInventory() {
		return inventory;
	}
	
	/**
	 * 
	 * @return The number of crew on the ship
	 */
	public int getNumCrew() {
		return crew;
	}
	
	/**
	 * 
	 * @return The logbook of the player
	 */
	public Logbook getLogbook() {
		return logbook;
	}
	
	/**
	 * 
	 * @return The total space taken up by the cargo onboard
	 */
	public int getCargoStored() {
		return cargoStored;
	}
	
	/**
	 * [CMD]
	 * <p>
	 * Give the player the option to pay their crew
	 * @param time
	 * @return If the player payed their crew
	 */
	public boolean payCrewCMD(int time) {
		int cost = crew * time;

		while (true) {
			System.out.println("You must pay your crew $" + cost + " for this route.");
			System.out.println("You currently have $" + gold + ".");
			System.out.println("1: Pay\n2: Return");
			switch (Game.getInt()) {
			case 1:
				if (cost <= gold) {
					gold -= cost;
					return true;
				}else {
					System.out.println("You don't have enough money to pay your crew for this route. Get more money or select a different route.");
					Game.pause();
					return false;
				}
			case 2:
				return false;
			}
		}
	}
	
	/**
	 * [GUI]
	 * <p>
	 * Give the player the option to pay their crew
	 * @param time
	 */
	public void payCrew(int time) {
		int cost = crew * time;

		display.updateDialogue("You must pay your crew $" + cost + " for this route.\n"
				+ "You currently have $" + gold + ". Do you still wish to sail?");
		display.setGameState("Confirm");
		display.updateDisplayFunction(11, Actions.PAY);
		display.updateDisplayValue(11, cost);
		display.updateDisplayFunction(13, Actions.CHOOSE_ROUTE);
	}
	
	/**
	 * Removes an item from the players inventory and adds an entry into the logbook
	 * @param item
	 * @param currentDay
	 * @return if the item was dumped successfully
	 */
	public boolean dump(Item item, int currentDay) {
		if (inventory.contains(item)) {
			removeItem(item);
			System.out.println("Dump successful. " + item.getName() + " has been removed from your ship.");
			item.setLocationPurchased(null);
			Entry entry = new Entry(currentDay);
			entry.makeTransaction(item, "Dumped");;
			logbook.addEntry(entry);
			Game.pause();
			return true;
		} else {
			System.out.println("Something went wrong, you don't have this item.");
		}
		Game.pause();
		return false;
	}
}
