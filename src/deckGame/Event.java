package deckGame;

import java.util.ArrayList;

import enums.Statuses;

public class Event {
	
	/*Plan for this, each event has a number associated with it, the higher the number the more dangerous/rewarding
	 * the event it. This allows for a modifier for each route which will multiply the random number, 
	 * more dangerous routes can have a number such as 2, while safe routes have a number 0.5	 
	 */
	
	private int numEvents = 3;
	
	public void selectEvent(Route route, Player player) {
		switch (Math.min((int) (Math.random() * numEvents), numEvents) + 1) { //For now it's just going to be random with no modifier
		case 1: //nothing happens
			System.out.println("The day passes uneventfully");
			break;
		case 2: //Fight
			fightLogic(player); // I want to have this called something else, just not sure what
			break;
		case 3: //Storm
			storm(player);
			player.damage(5);
			break;
		}
		Game.pause();
	}
	
	public void fightLogic(Player player){
		Ship enemy = new Ship("Enemy", 50, 4, 2);
		System.out.println("You are attacked by a ship full of pirates. Choose and option to continue");
		while (true) {
			System.out.println("1: Fight");
			System.out.println("2: Attempt to flee");
			System.out.println("3: View enemy");
			
			switch (Game.getInt()) {
			case 1:
				fight(enemy, player);
				System.out.println("The enemy has been sunk");
				Game.pause();
				reward(player, 0);
				return;
			case 2:
				if (flee(enemy, player)) {
					System.out.println("You escaped the pirates.");
				}else {
					System.out.println("While trying to escape the enemy attacks and harpons you.");
					ArrayList<Integer> enemyDice = roll(enemy);
					player.damage(enemyDice);
					Game.pause();
					
					fight(enemy, player);
				}
				return;
			case 3:
				System.out.println(enemy);
				Game.pause();
				break;
			}
		}
	}
	
	
	public void fight(Ship enemy, Ship player) {
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
		return (d20 + speedDifference > 10);
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
	
	public void storm(Player player){
		System.out.println("Storm");
		reward(player, -5);
	}

	//Item
	
	public void reward(Player player, int eventModifier) {
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
					System.out.println("Bring it aboard?\n1: Yes\n2: No");
					switch (Game.getInt()) {
					case 1:
						if (player.addItem(item)) {
							System.out.println("You aquired " + item.getName() + " and it has been added to your ship.");
						}else {
							System.out.println("You don't have enough space to take this item. Dump an item or leave it behind.");
							player.printInventory();
							continue;
							//need to implement dumping
						}
						return;
					case 2:
						return;
					default:
						System.out.println("Invalid option, please try again");
					}
				}
			}
		}
	}
}
