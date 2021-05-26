package deckGame;

public class Entry extends Logbook {
	/**
	 * The day that the entry was created
	 */
	private int day;
	
	/**
	 * The item that is involved in the event if any
	 */
	private Item item = null;
	
	/**
	 * The transaction that is involved in the event if any
	 */
	private String transactionType = null;
	
	/**
	 * The name of the event that is involved in the event if any
	 */
	private String eventName = null;
	
	/**
	 * The damage that occurred to the ship during the event if any
	 */
	private int damage = 0;
	
	/**
	 * The cost of the event if any
	 */
	private int cost = 0;
	
	/**
	 * The island the event happened if any
	 */
	private Island location = null;

	/**
	 * Creates a new instance of Entry
	 * @param day
	 */
	public Entry(int day) {
		this.day = day;
	}
	
	/**
	 * Sets the parameters to allow for correct formating
	 * @param item
	 * @param type
	 */
	public void makeTransaction(Item item, String type) {
		this.item = item;
		transactionType = type;
	}
	
	public void addLocation(Island island) {
		location = island;
	}
	
	/**
	 * Sets the parameters to allow for correct formating
	 * @param item
	 * @param type
	 */
	public void makeEvent(String type) {
		eventName = type;
	}
	
	/**
	 * Sets the parameters to allow for correct formating
	 * @param item
	 * @param type
	 */
	public void addDamage(int amount) {
		damage = amount;
	}
	
	/**
	 * Sets the parameters to allow for correct formating
	 * @param item
	 * @param type
	 */
	public void addCost(int amount) {
		cost = amount;
	}
	
	/**
	 * Formats the details of the entry in string form
	 */
	public String toString() {
		String output = "Day " + day + ": ";
		if (transactionType != null) {
			output += transactionType + " " + item.getName();
		}
		if (eventName != null) {
			output += eventName;
		}
		if (damage != 0) {
			if (damage > 0) {
				output += " taking ";
			}else {
				output += " repairing ";
				damage *= -1;
			}
			output += damage + " damage";
		}
		if (cost != 0) {
			if (cost > 0) {
				output += " costing $";
			} else {
				output += " reciving $";
				cost *= -1;
			}
			output += cost;
		}
		if (location != null) {
			output += " at " + location.getName();
		}
		output += ".";
		return output;
	}

}
