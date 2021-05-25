package deckGame;

import java.util.ArrayList;

import enums.ItemType;
import enums.Statuses;

public class Event {
	
	/*Plan for this, each event has a number associated with it, the higher the number the more dangerous/rewarding
	 * the event it. This allows for a modifier for each route which will multiply the random number, 
	 * more dangerous routes can have a number such as 2, while safe routes have a number 0.5	 
	 */
	
	private int numEvents = 4;
	private Display display;
	private ArrayList<Integer> eventChance = new ArrayList<Integer>();
	
	public Event(int[] eventChance) {
		int eventCounter = 1;
		for (int event: eventChance) {
			for (int i = 0; i < event; i++) {
				this.eventChance.add(eventCounter);
			}
			eventCounter++;
		}
	}
	
	public void selectEvent(Player player, int currentDay, Display display) {
		this.display = display;
		switch (eventChance.get((int) (Math.random() * eventChance.size()))) {
		case 1: //nothing happens
			if (display != null) {
				display.updateDialogue("The day passes uneventfully.");
			} else {
				System.out.println("The day passes uneventfully.");
			}
			break;
		case 2: //Fight
			fight(player, currentDay);
			break;
		case 3: //Storm
			storm(player, currentDay);
			break;
		case 4: //rescue sailors
			rescue(player, currentDay);
			break;
		}
	}
	
	public void fight(Player player, int currentDay){
		boolean fleeAttempt = false;
		Ship enemy = new Ship("Enemy", 50, 4, 2);
		int startHealth = player.getHealth();
		if (display != null) {
			display.updateDialogue("You are attacked by a ship full of pirates. Choose an option to continue");
			display.setGameState("Pirates");
		} else {
			System.out.println("You are attacked by a ship full of pirates. Choose an option to continue");
		}
		while (enemy.getStatus() != Statuses.DESTROYED) {
			int selection;
			if (display != null) {
				selection = -1;
			} else {
				System.out.println("1: Fight");
				System.out.println("2: Attempt to flee");
				System.out.println("3: Surrender cargo");
				System.out.println("4: View enemy");
				selection = Game.getInt();
			}
			
			switch (selection) {
			case 1:
				attack(enemy, player);
				break;
			case 2:
				if (!fleeAttempt) {
					fleeAttempt = true;
					if (flee(enemy, player)) {
						Entry entry;
						entry = new Entry(currentDay);
						entry.makeEvent("Fled from pirates");
						player.getLogbook().addEntry(entry);
						return;
					}
				} else {
					System.out.println("The pirates have harpooned you. You can no longer flee.");
				}
				break;
			case 3:
				if (surrenderItems(player, currentDay)) {
					return;
				}else {
					break;
				}
			case 4:
				System.out.println(enemy);
				Game.pause();
				break;
			}
		}
		Entry entry;
		entry = new Entry(currentDay);
		entry.makeEvent("Attacked by pirates");
		entry.addDamage(startHealth - player.getHealth());
		player.getLogbook().addEntry(entry);
		reward(player, 0, currentDay);
	}
	
	public boolean surrenderItems(Player player, int currentDay) {
		if (player.getInventory().size() == 0) {
			System.out.println("You have no cargo to surrender.");
			return false;
		}
		while (true) {
			System.out.println("Are you sure you want to surrender a random piece of cargo?");
			System.out.println("1: Yes");
			System.out.println("2: No");
			int selection = Game.getInt();
			
			switch(selection) {
			case 1:
				int itemIndex = (int) (Math.random() * player.getInventory().size());
				
				Item item = player.getInventory().get(itemIndex);
				Entry entry = new Entry(currentDay);
				entry.makeTransaction(item, "Surrendered ");
				player.getLogbook().addEntry(entry);
				break;
			case 2:
				return false;
			default:
				break;
			}
		}
	}
	
	public void attack(Ship enemy, Ship player) {
		ArrayList<Integer> playerDice = roll(player);
		enemy.damage(playerDice);
		if (enemy.getStatus() == Statuses.DESTROYED) {
			return;
		}
		
		ArrayList<Integer> enemyDice = roll(enemy);
		player.damage(enemyDice);
	}
	
	public boolean flee(Ship enemy, Ship player) {
		int d20 = (int)(Math.random() * 20) + 1;
		int speedDifference = player.getSpeed() - enemy.getSpeed();
		if (d20 + speedDifference > 10) {
			System.out.println("You escaped the pirates.");
			return true;
		}else {
			System.out.println("While trying to escape the enemy attacks and harpons you.");
			ArrayList<Integer> enemyDice = roll(enemy);
			player.damage(enemyDice);
			Game.pause();
			
			attack(enemy, player);
			return false;
		}
	}
	
	
	private ArrayList<Integer> roll(Ship attacker){
		ArrayList<Integer> dice = new ArrayList<>();
		do {
			dice.add((int) (Math.random() * 6) + 1);
		} while (dice.size() < attacker.getStrength());
		if (attacker instanceof Player) {
			for (Card card: ((Player) attacker).getCards()) {
				dice = card.doSpecial(dice);
			}
		}
		System.out.println(dice);
		return dice;
		
	}
	
	public void storm(Player player, int currentDay){
		System.out.println("You encounter a storm.");
		int damage = ((int) (Math.random() * 20) + 1);
		player.damage(damage);
		Entry entry = new Entry(currentDay);
		entry.addDamage(damage);
		entry.makeEvent("Encountered a storm");
		player.getLogbook().addEntry(entry);
		reward(player, -5, currentDay);
	}

	//Item
	
	public boolean reward(Player player, int eventModifier, int currentDay) {
		int randomNum;
		int chance;
		chance = (int) (Math.random() * 20) + player.getLuck() + eventModifier + 1;
		ArrayList<Item> items = Item.getRandomItems(player.getLuck());
		while (chance > 10) {
			randomNum = (int) (Math.random() * items.size());
			Item item = items.get(randomNum);
			if (!player.getInventory().contains(item) & item.getType() == ItemType.CARGO) {
				while (true) {
					System.out.println("You found " + item.getName() + " among the wreckage.");
					System.out.println("Bring it aboard?\n1: Yes\n2: No\n3: View inventory\n4: View item");
					switch (Game.getInt()) {
					case 1:
						item.setLocationPurchased(player.getLocation());
						if (player.addItem(item)) {
							item.setPurchaseCost(0);
							System.out.println("You aquired " + item.getName() + " and it has been added to your ship.");
							Entry entry = new Entry(currentDay);
							entry.makeTransaction(item, "Aquired");
							player.getLogbook().addEntry(entry);
						}else {
							System.out.println("You don't have enough space to take this item. Dump an item or leave it behind.");
							continue;
						}
						return true;
					case 2:
						System.out.println("You decided to leave the " + item + " behind.");
						Entry entry = new Entry(currentDay);
						entry.makeTransaction(item, "Found");
						player.getLogbook().addEntry(entry);
						return true;
					case 3:
						dumpOptions(player, currentDay);
						break;
					case 4:
						System.out.println(item);
						Game.pause();
						break;
					default:
						System.out.println("Invalid option, please try again");
					}
				}
			}
		}
		return false;
	}
	
	public void dumpOptions(Player player, int currentDay) {
		System.out.println("Select an item to dump.");
		player.printInventory();
		System.out.println((player.getInventory().size() + 1) + ": Return\n" + "Select an item or return to continue.");
		int selection = Game.getInt();
		if (selection == player.getInventory().size() + 1) {
			return;
		}else if (selection <= player.getInventory().size()) {
			confirmDump(player.getInventory().get(selection - 1), player, currentDay);
		}else {
			System.out.println("Please enter a number between 1 and " + player.getInventory().size() + 1 + ".");
		}
	}
	
	public void confirmDump(Item item, Player player, int currentDay){
		boolean complete = false;
		while (complete == false) {
			System.out.println(item);
			System.out.println("1: Dump");
			System.out.println("2: Return");
			switch (Game.getInt()) {
			case 1:
				complete = player.dump(item, currentDay);
				break;
			case 2:
				complete = true;
				break;
			default:
				System.out.println("Please enter 1 or 2.");
				Game.pause();
				break;
			}
		}
	}
	
	public void rescue(Player player, int currentDay) {
		int amount = Math.max((int) (Math.random() * 25) + 1, 10);
		System.out.println("You come across a shipwreck and help the survivors onboard.\nThey reward you with $" + amount + ".");
		player.modifyGold(amount);
		Entry entry = new Entry(currentDay);
		entry.makeEvent("Rescued sailors");
		entry.addCost(-amount);
		player.getLogbook().addEntry(entry);
		Game.pause();
	}
	
	public ArrayList<Integer> getEventChance() {
		return eventChance;
	}
}
