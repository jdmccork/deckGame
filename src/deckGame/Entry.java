package deckGame;

public class Entry extends Logbook {
	private Item item = null;
	private String transactionType = null;
	private String eventName = null;
	private int damage = 0;
	private int cost = 0;
	private Island location = null;

	public Entry() {
		
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
		String output = "";
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
			}
			output += damage + " damage";
		}
		if (cost != 0) {
			if (cost > 0) {
				output += " costing $";
			} else {
				output += " reciving $";
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
