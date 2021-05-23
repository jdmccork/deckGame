package deckGame;

public class Entry extends Logbook {
	private int day;
	private Item item = null;
	private String transactionType = null;
	private String eventName = null;
	private int damage = 0;
	private int cost = 0;
	private Island location = null;

	public Entry(int day) {
		this.day = day;
	}
	
	public void makeTransaction(Item item, String type) {
		this.item = item;
		transactionType = type;
	}
	
	public void makeEvent(String type) {
		eventName = type;
	}
	
	public void addDamage(int amount) {
		damage = amount;
	}
	
	public void addCost(int amount) {
		cost = amount;
	}
	
	public String toString() {
		String output = "Day " + day + ": ";
		if (transactionType != null) {
			output += item.getName() + " " + transactionType;
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
			output += "at " + location.getName();
		}
		output += ".";
		return output;
	}

}
