package deckGame;

import java.util.ArrayList;

import enums.Statuses;

public class Event {
	
	/*Plan for this, each event has a number associated with it, the higher the number the more dangerous/rewarding
	 * the event it. This allows for a modifier for each route which will multiply the random number, 
	 * more dangerous routes can have a number such as 2, while safe routes have a number 0.5	 
	 */
	
	private int numEvents = 4;
	
	public void selectEvent(Route route, Player player, int currentDay) {
		switch (Math.min((int) (Math.random() * numEvents), numEvents) + 1) {
		case 1: //nothing happens
			System.out.println("The day passes uneventfully");
			break;
		case 2: //Fight
			fight(player, currentDay);
			break;
		case 3: //Storm
			storm(player, currentDay);
			reward(player, -5, currentDay);
			break;
		case 4: //rescue sailors
			rescue(player, currentDay);
			break;
		}
		Game.pause();
	}
	
	public void fight(Player player, int currentDay){
		Ship enemy = new Ship("Enemy", 50, 4, 2);
		System.out.println("You are attacked by a ship full of pirates. Choose and option to continue");
		while (true) {
			System.out.println("1: Fight");
			System.out.println("2: Attempt to flee");
			System.out.println("3: View enemy");
			
			switch (Game.getInt()) {
			case 1:
				attack(enemy, player);
				System.out.println("The enemy has been sunk");
				Game.pause();
				reward(player, 0, currentDay);
				return;
			case 2:
				flee(enemy, player);
				return;
			case 3:
				System.out.println(enemy);
				Game.pause();
				break;
			}
		}
	}
	
	
	public void attack(Ship enemy, Ship player) {
		while (enemy.getStatus() != Statuses.DESTROYED) {
			ArrayList<Integer> playerDice = roll(player);
			enemy.damage(playerDice);
			
			if (enemy.getStatus() == Statuses.DESTROYED) {
				return;
			}
			
			ArrayList<Integer> enemyDice = roll(enemy);
			player.damage(enemyDice);
			Game.pause();
		}
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
		System.out.println("Storm");
		int damage = ((int) (Math.random() * 20));
		player.damage(damage);
		Entry entry = new Entry(currentDay);
		entry.addDamage(damage);
		entry.makeEvent("Encountered a storm");
		player.getLogbook().addEntry(entry);
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
			if (!player.getInventory().contains(item)) {
				while (true) {
					System.out.println("You found " + item.getName() + " among the wreakage.");
					System.out.println("Bring it aboard?\n1: Yes\n2: No\n3: View inventory");
					switch (Game.getInt()) {
					case 1:
						item.setLocationPurchased(player.getLocation());
						if (player.addItem(item)) {
							System.out.println("You aquired " + item.getName() + " and it has been added to your ship.");
							Entry entry = new Entry(currentDay);
							entry.makeTransaction(item, "Aquired ");
							player.getLogbook().addEntry(entry);
						}else {
							System.out.println("You don't have enough space to take this item. Dump an item or leave it behind.");
							continue;
						}
						return true;
					case 2:
						Entry entry = new Entry(currentDay);
						entry.makeTransaction(item, "Found ");
						player.getLogbook().addEntry(entry);
						return true;
					case 3:
						dumpOptions(player, currentDay);
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
				complete = dump(item, player, currentDay);
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
	
	public boolean dump(Item item, Player player, int currentDay) {
		if (player.getInventory().contains(item)) {
			player.removeItem(item);
			System.out.println("Dump successful. " + item.getName() + " has been removed from your ship.");
			item.setLocationPurchased(null);
			Entry entry = new Entry(currentDay);
			entry.makeTransaction(item, "Dumped ");;
			player.getLogbook().addEntry(entry);
			Game.pause();
			return true;
		} else {
			System.out.println("Something went wrong, you don't have this item.");
		}
		Game.pause();
		return false;
	}
	
	public void rescue(Player player, int currentDay) {
		int amount = Math.max((int) (Math.random() * 25), 10);
		System.out.println("You come across a shipwreck and help the survivors onboard.\nThey reward you with $" + amount + ".");
		player.modifyGold(amount);
		Entry entry = new Entry(currentDay);
		entry.makeEvent("Rescued sailors");
		entry.addCost(-amount);
		player.getLogbook().addEntry(entry);
	}
}
