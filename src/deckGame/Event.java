package deckGame;

import java.util.ArrayList;

import enums.Actions;
import enums.ItemType;
import enums.Statuses;

public class Event {
	private Display display;
	private Item reward;
	private ArrayList<Integer> eventChance = new ArrayList<Integer>();
	/**
	 * Takes a list of 4 integers (more or less will not break it) where each entry corresponds the the number
	 * of chances that the event has of happening.
	 * @param eventChance
	 */
	public Event(int[] eventChance) {
		int eventCounter = 1;
		for (int event: eventChance) {
			for (int i = 0; i < event; i++) {
				this.eventChance.add(eventCounter);
			}
			eventCounter++;
		}
	}
	
	/**
	 * Allows the manual setting of the event chance if an item were 
	 * to increase/decrease the chance of something.
	 * happening or for testing
	 * @param eventChance
	 */
	public void setChance(ArrayList<Integer> eventChance) {
		this.eventChance = eventChance;
	}
	
	/**
	 * Selects a random integer that is in the eventChance list and runs the logic behind that event .
	 * @param player
	 * @param currentDay
	 * @param display
	 * @return The type of event that occurs for GUI
	 */
	public String selectEvent(Player player, int currentDay, Display display) {
		this.display = display;
		switch (eventChance.get((int) (Math.random() * eventChance.size()))) {
		case 1: //nothing happens
			if (display == null) {
				System.out.println("The day passes uneventfully.");
			}
			return "Sea";
		case 2: //Fight
			if (display == null) {
				fight(player, currentDay);
			}
			return "Pirates";
		case 3: //Storm
			if (display == null) {
				storm(player, currentDay);
			}
			return "Storm";
		case 4: //rescue sailors
			if (display == null) {
				rescue(player, currentDay);
			}
			return "Rescue";
		default:
			return "Error";
		}
			
	}
	
	/**
	 * Creates an enemy pirate and allows the user to select an option to continue the fight and
	 * select the correct logic to continue.
	 * @param player
	 * @param currentDay
	 */
	public void fight(Player player, int currentDay){
		boolean fleeAttempt = false;
		Ship enemy = getEnemy();
		int startHealth = player.getHealth();
		if (display != null) {
			display.updateDialogue("You are attacked by a ship full of pirates. Choose an option to continue");
			display.setGameState("Pirates");
			return;
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
	
	public Ship getEnemy() {
		Ship enemy = new Ship("Enemy", 
				(Math.max(50, (int) Math.random() * 100)), 
				Math.max(5, (int) (Math.random() * 15)), 
				Math.max(1, (int) (Math.random() * 5)));
		return enemy;
	}
	
	/**
	 * Allows the player to leave the fight by surrendering one of their items to the pirates.
	 * @param player
	 * @param currentDay
	 * @return if an item was surrendered
	 */
	public boolean surrenderItems(Player player, int currentDay) {
		if (player.getInventory().size() == 0) {
			System.out.println("You have no cargo? Then pay with your life!");
			Game.pause();
			return false;
		}
		while (true) {
			System.out.println("Are you sure you want to surrender a random piece of cargo?");
			System.out.println("1: Yes");
			System.out.println("2: No");
			int selection = Game.getInt();
			switch(selection) {
			case 1:
				chooseSurrender(player, currentDay);
				return true;
			case 2:
				return false;
			default:
				break;
			}
		}
	}
	
	public void chooseSurrender(Player player, int currentDay) {
		int itemIndex = (int) (Math.random() * player.getInventory().size());
		
		Item item = player.getInventory().get(itemIndex);
		player.removeItem(item);
		
		Entry entry = new Entry(currentDay);
		entry.makeTransaction(item, "Surrendered ");
		player.getLogbook().addEntry(entry);
		
		if(display == null) {
			System.out.println("The pirates took your " + item.getName() + " and allowed you to escape with your lives.");
			Game.pause();
		} else {
			display.updateDialogue("The pirates took your " + item.getName() + " and allowed you to escape with your lives.");
		}
	}
	
	/**
	 * Simulates a turn by rolling dice and deals damage to both the player and the pirate.
	 * @param enemy
	 * @param player
	 */
	public String attack(Ship enemy, Ship player) {
		ArrayList<Integer> playerDice = roll(player);
		String damageDealt = enemy.damage(playerDice);
		if (enemy.getStatus() == Statuses.DESTROYED) {
			return "The enemy has been sunk";
		}
		
		ArrayList<Integer> enemyDice = roll(enemy);
		String damageTaken = player.damage(enemyDice);
		if(display != null) {
			return damageDealt + "#" + damageTaken;
		}
		return damageTaken;

	}
	
	/**
	 * Determines if the player can escape from the pirates.
	 * The ship with more speed has a greater chance of success.
	 * @param enemy
	 * @param player
	 * @return if the player was successful
	 */
	public boolean flee(Ship enemy, Ship player) {
		int d20 = (int)(Math.random() * 20) + 1;
		int speedDifference = player.getSpeed() - enemy.getSpeed();
		if (d20 + speedDifference > 10) {
			System.out.println("You escaped the pirates.");
			if (display != null) {
				display.updateDialogue("You escaped the pirates.");
			}
			return true;
		}else {
			System.out.println("While trying to escape the enemy attacks and harpons you.");
			ArrayList<Integer> enemyDice = roll(enemy);
			String lostHealth = player.damage(enemyDice);
			if (display == null) {
				attack(enemy, player);
			} else {
				display.updateDialogue("While trying to escape the enemy attacks and harpoons you. " + lostHealth);
			}
			return false;
		}
	}
	
	/**
	 * Simulates a dice roll using the attackers strength and cards to determine how many dice to roll
	 * and the results of the dice.
	 * @param attacker
	 * @return the dice that were rolled
	 */
	private ArrayList<Integer> roll(Ship attacker){
		ArrayList<Integer> dice = new ArrayList<>();
		do {
			dice.add((int) (Math.random() * 6) + 1);
		} while (dice.size() < attacker.getStrength());		
		System.out.println(attacker.getShipName() + " rolled " + dice);
		if (attacker instanceof Player) {
			for (Card card: ((Player) attacker).getCards()) {
				dice = card.doSpecial(dice);
			}
			if (((Player) attacker).getCards().size() > 0){
				System.out.println("Your " + ((Player) attacker).getCards().size() + 
						" cards modified the dice to produce " + dice);
			}
		}
		return dice;
	}
	
	/**
	 * Simulates a storm damaging the player and rewarding the player if they are lucky
	 * @param player
	 * @param currentDay
	 * @return A String of the damage that occurred
	 */
	public String storm(Player player, int currentDay){
		System.out.println("You encounter a storm.");
		int damage = ((int) (Math.random() * 15) + 1);
		
		Entry entry = new Entry(currentDay);
		entry.addDamage(damage);
		entry.makeEvent("Encountered a storm");
		player.getLogbook().addEntry(entry);
		reward(player, -5, currentDay);
		
		return player.damage(damage);
	}
	
	/**
	 * Gets a random item if the dice roll + players luck + the modifier is over 15 and allows inventory
	 * management to allow them to pick it up if they have too much stock
	 * @param player
	 * @param eventModifier
	 * @param currentDay
	 * @return if the player is rewarded for testing purposes
	 */
	public boolean reward(Player player, int eventModifier, int currentDay) {
		int randomNum;
		int chance;
		chance = (int) (Math.random() * 20) + player.getLuck() + eventModifier + 1;
		ArrayList<Item> items = Item.getRandomItems(player.getLuck());
		while (chance > 14) {
			randomNum = (int) (Math.random() * items.size());
			Item item = items.get(randomNum);
			if (!player.getInventory().contains(item) & item.getType() == ItemType.CARGO) {
				if (display == null) {
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
				} else {
					this.reward = item;
				}
			}
		}
		return false;
	}
	
	public Item getReward() {
		return this.reward;
	}
	
	/**
	 * Allows the player to select an item to dump
	 * @param player
	 * @param currentDay
	 */
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
	
	/**
	 * Confirms with the player if they want to dump an item
	 * @param item
	 * @param player
	 * @param currentDay
	 */
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
	
	/**
	 * Simulates the player rescuing some sailor getting some money
	 * @param player
	 * @param currentDay
	 */
	public void rescue(Player player, int currentDay) {
		int amount = Math.max((int) (Math.random() * 25) + 1, 10);
		player.modifyGold(amount);
		Entry entry = new Entry(currentDay);
		entry.makeEvent("Rescued sailors");
		entry.addCost(-amount);
		player.getLogbook().addEntry(entry);
		if (display != null) {
			display.updateDialogue("You come across a shipwreck and help the survivors onboard.\nThey reward you with $" + amount + ".");
			display.updateMainDisplay(12, "Continue", true, true);
			display.updateDisplayFunction(12, Actions.CONTINUE);
		} else {
			System.out.println("You come across a shipwreck and help the survivors onboard.\nThey reward you with $" + amount + ".");
			Game.pause();
		}
		
	}
	
	/**
	 * Gets the event chance to allow the player to know what events each route will have
	 * @return
	 */
	public ArrayList<Integer> getEventChance() {
		return eventChance;
	}
}
