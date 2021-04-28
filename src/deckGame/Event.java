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
		double chance = route.getEventChance();
		switch (Math.min((int) (Math.random() * numEvents), numEvents) + 1) { //For now it's just going to be random with no modifier
		case 1: //nothing happens
			System.out.println("The day passes uneventfully");
			route.modifyEventChance(0.5);
			break;
		case 2: //Fight
			fightLogic(player); // I want to have this called something else, just not sure what
			route.modifyEventChance(-0.3);
			break;
		case 3: //Storm
			storm();
			route.modifyEventChance(-0.5);
			break;
		}
		Game.pause();
	}
	
	public void fightLogic(Player player){
		Enemy enemy = new Enemy("Enemy", 50, 4, 2);
		System.out.println("You are attacked by a ship full of pirates. Choose and option to continue");
		while (true) {
			System.out.println("1: Fight");
			System.out.println("2: Attempt to flee");
			System.out.println("3: View enemy");
			
			switch (Game.getInt()) {
			case 1:
				fight(enemy, player);
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
	
	
	public void fight(Enemy enemy, Player player) {
		while (enemy.getStatus() != Statuses.DESTROYED) {
			ArrayList<Integer> playerDice = roll(player);
			enemy.damage(playerDice);
			
			if (enemy.getStatus() == Statuses.DESTROYED) {
				//Gain reward
				return;
			}
			
			ArrayList<Integer> enemyDice = roll(enemy);
			player.damage(enemyDice);
			Game.pause();
		}
	}
	
	public boolean flee(Enemy enemy, Player player) {
		int d20 = (int)(Math.random() * 20) + 1;
		int speedDifference = player.getSpeed() - enemy.getSpeed();
		System.out.println("----------------" + d20 + speedDifference);
		return (d20 + speedDifference > 10);
	}
	
	
	private ArrayList<Integer> roll(Ship attacker){
		ArrayList<Integer> dice = new ArrayList<>();
		do {
			dice.add((int) (Math.random() * 6) + 1);
		} while (dice.size() < attacker.getStrength());
		//Do the special abilities crew mates will have (reroll 1 etc)
		System.out.println(dice);
		return dice;
		
	}
	
	public void storm(){
		System.out.println("Storm");
	}

	//Item
	
	
}
