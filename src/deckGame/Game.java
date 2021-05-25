package deckGame;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import enums.Actions;
import enums.Statuses;


public class Game {
	private static Game currentGame;
	private static Scanner userInput;
	private Player player;
	private ArrayList<Island> islands;
	private int days;
	private int currentDay;
	private Display display;
	private Route chosenRoute;
	private boolean paused = false;
	
	public static void setTestInput() {
		userInput = new Scanner(System.in);
	}
	
	public Game() {
		gameSetup();
	}
	
	public void run() {
		display = new Display(this);
		boolean playing = true;
		while(playing == true) {
			playing = mainMenu();
		}
		userInput.close();
	}
	
	public ArrayList<Entry> getLogItems() {
		//TODO
		return player.getLogbook().getEntries();
	}

	public Player createPlayer(String userName, String shipName, String shipType) {
		//select ship to insert into the final 4 values
		int[] ship = getShip(shipType);
		Player player = new Player(userName, shipName, ship[0], ship[1], ship[2], ship[3], ship[4], ship[5], ship[5], islands.get(0));
		return player;
	}
	
	public int[] getShip(String shipType) {
		int[] output;
		switch (shipType) {
		//int health, int speed, int capacity, int deckSize, int power, int gold, int crew
		case "2":
			output = new int[] {100, 7, 6, 4, 3, 350, 10};
		case "3":
			output = new int[] {150, 7, 4, 4, 5, 250, 20};
		case "4":
			output = new int[] {75, 14, 3, 4, 2, 250, 15};
		default:
			output = new int[] {100, 10, 4, 4, 4, 250, 10};
		}
		return output;
	}
	
	public String getNames(String userName, String shipName) {
		if (userName != null) {
			if (hasSpecial(userName)) {
				return "Special";
			}else if (userName.length() < 3 | userName.length() > 15){
				return "Length";
			}
		}
		if (shipName.length() < 3 | shipName.length() > 15) {
			return "Length";
		}
		return "Good";
	}
	
	public boolean hasSpecial(String string) {
		//allowing spaces in people names
		Pattern my_pattern = Pattern.compile("[^(a-z)\s]", Pattern.CASE_INSENSITIVE);
		Matcher my_match = my_pattern.matcher(string);
	    if (my_match.find() | string.contains("\s\s"))
	    	return true;
	    else
	        return false;
	}
	
	public void generateIslands() {
		islands = new ArrayList<Island>();
		islands.add(new Island("Home", 0, 0, 7));
		islands.add(new Island("Golgolles", -10, 5, 0));
		islands.add(new Island("Cansburg", 5, 5, 3));
		islands.add(new Island("Tisjour", -5, -5, 11));
		islands.add(new Island("Brighdown", 5, -5, 13));
	}
	
	public void generateAllRoutes(ArrayList<Island> islands) {
		for (Island island : islands) {
			island.generateRoutes(islands);
		}
	}
	
	public void welcome(Player player) {
		System.out.println(player.getUserName() + " is the new captain of The " + player.getShipName() + "."
				+ "\nEarn as much gold as you can in the " + days + " days you have left."
				+ "\nWelcome abord Captain " + player.getUserName() + ".");
		pause();
	}
	
	public int getGameLength() {
		while (true) {
			System.out.println("Choose game length:");
			try {
				int days = getInt();
				if (days >= 20 & days <= 50) {
					return days;
				}else {
					System.out.println("Please enter an integer between 20 and 50.");
				}
			} catch (java.util.InputMismatchException e){
				System.out.println("Please enter a valid integer.");
				userInput.nextLine();
			}
		}
	}
	
	public void gameSetup() {
		userInput = new Scanner(System.in);
		currentGame = this;
		Item.generateItems();
		//Store.readAdvice();
		generateIslands();
		generateAllRoutes(islands);
	}
	
	public void sessionSetup(String userName, String shipName, int duration, String ship) {
		player = createPlayer(userName, shipName, ship);
		player.addItem(Item.getItem("Snake Eye Chef"));
		player.getLocation().getStore().generateStock(player);
		days = duration;
	}
	
	public boolean mainMenu(){
		int selection;
		try {
			while (true) {
				System.out.println("Select an option to continue");
				System.out.println("1: Play");
				System.out.println("2: Quit");
				selection = getInt();
				if (selection == 1) {
					play();
				} else if (selection == 2) {
					System.out.println("Thanks for playing. Goodbye.");
					return false;
				} else {
					System.out.println("Invalid option, please enter the number of the option you want.");
				}
			}
		}catch(EndGameException e){
			endGame();
			System.out.println("Would you like to return to the main menu?");
			System.out.println("1: Yes");
			System.out.println("2: No");
			switch (getInt()) {
			case 1:
				return true;
			case 2:
				System.out.println("Thanks for playing. Goodbye.");
				return false;
			}
		}
		return false;//should never be reached in reality
	}
	
	public void play() {
		while (currentDay <= days) {
			System.out.println("Current day: " + currentDay + "/" + days);
			System.out.println("Select an option to continue");
			System.out.println("1: Interact with store");
			System.out.println("2: Set sail");
			System.out.println("3: See inventory");
			System.out.println("4: See deck");
			System.out.println("5: Return to main menu");
			
			switch (getInt()) {
			case 1:
				//interact with shop
				player.getLocation().getStore().interact(player, currentDay);
				break;
			case 2:
				selectRoute();
				break;
			case 3:
				player.viewInventory();
				break;
			case 4:
				player.viewCards();
				break;
			case 5:
				return;
			default:
				System.out.println("Please enter a number between 1 and 5");
				break;
			}
		}
	}
	public static int getInt(){
		while(true) {
			try {
				int selection = userInput.nextInt();
				userInput.nextLine();
				return selection;
			}catch (java.util.InputMismatchException e) {
				System.out.println("Invalid character, please enter a valid option.");
				userInput.nextLine();
			}
		}
	}
	
	public void chargeRepair() {
		int healthLost = player.getMaxHealth()-player.getHealth();
		int cost = (int) (healthLost / 5) + 1;
		if (player.getGold() < cost & player.getInventory().size() == 0 & player.getCards().size() == 0) {
			this.display.updateDialogue("You don't have enough money and no items to sell for the repairs.");
			throw new EndGameException();
		}
		this.display.updateDialogue(
				"Your ship has sustained " + healthLost + " damage. This will cost $" + cost + " to repair.");
		this.display.setGameState("Repairs");
	}
	
	public boolean executeRepair(int selection) {
		int healthLost = player.getMaxHealth()-player.getHealth();
		int cost = (int) (healthLost / 5) + 1;
		if(selection == 1) {
			if (player.getGold() >= cost) {
				player.repair();
				player.modifyGold(-cost);
				Entry entry = new Entry(currentDay);
					entry.addDamage(-healthLost);
					entry.makeEvent("Repaired ship");
					entry.addCost(cost);
					player.getLogbook().addEntry(entry);
				return true;
			} else {
				this.display.updateDialogue(
						"You don't have enough money to repair your ship. Sell an item to make some more cash.");
				return false;
			}
		}else {
			return false;
		}
	}
	
	public void payCrew(int time) {
		int cost = player.getNumCrew() * time;

		display.updateDialogue("You must pay your crew $" + cost + " for this route.\n"
				+ "You currently have $" + player.getGold() + ". Do you still wish to sail?");
		display.setGameState("Confirm");
		display.updateDisplayFunction(11, Actions.PAY);
		display.updateDisplayValue(11, cost);
		display.updateDisplayFunction(13, Actions.CHOOSE_ROUTE);
	}
	
	public void executePay(int cost) {
		if (cost <= player.getGold()) {
			player.modifyGold(-cost);
			Entry entry = new Entry(currentDay);
			entry.makeEvent("Payed crew");
			entry.addCost(cost);
			player.getLogbook().addEntry(entry);
			executeSail();
		}else {
			display.updateDialogue("You don't have enough money to pay your crew for this route. Get more money or select a different route.");
		}
	}
	
	public void selectRoute() {
		if (player.getStatus() == Statuses.DAMAGED) {
			chargeRepair();
		} else {
			ArrayList<Route> routes = player.getLocation().getRoutes();
			for (Route route: routes) {
				display.updateDisplayToolTip(route.getDestination().getDisplay(), "The journey to " 
						+ route.getDestination().getName() + " will take " + route.getTime(player.getSpeed())
						+ " days to complete.");
			}
		}
	}
	
	public void executeRoute(int selection) {
		ArrayList<Route> routes = player.getLocation().getRoutes();
		Route route = routes.get(selection);
		int time = route.getTime(player.getSpeed());
		if (currentDay + time >= days) {
			display.updateDialogue("Note, this trip will exceed your remaining time, all items will"
					+ " be sold on day " + days + ".\nContinue?");
			display.setGameState("Confirm");
			display.updateDisplayFunction(11, Actions.GO_TO_ISLAND);
			display.updateDisplayFunction(13, Actions.CLOSE_STORE);
		}
		payCrew(time);
		this.chosenRoute = route;
	}
	
	public void executeSail() {
		int time = chosenRoute.getTime(player.getSpeed());
		player.sail(chosenRoute);
		for (int i = 0; i != time & currentDay < days; i++) {
			while (paused) {
				//An infinite loop that relies on external change of the paused variable.
			}
			currentDay += 1;
			display.updateDay(String.valueOf(currentDay));
			Event event = new Event();
			display.setGameState(event.eventForGUI());
		}
		if (currentDay < days) {
			player.getLocation().getStore().generateStock(player); //generates shops when you arrive at the destination so that you can't enter and exit to regenerate the shops
			display.updateDialogue("You travelled for " + time + " days and have arrived at " + player.getLocation());
			display.setGameState("Island");
		}else {
			throw new EndGameException();
		}
	}
	
	public void setPause(boolean setting) {
		this.paused = setting;
	}
	
	public static void pause() {
		System.out.println("Press enter to continue");
		userInput.nextLine();
	}
	
	public void endGame() {
		int gold = getTotalWorth();
		printResults(gold);
		//extendGame(); question with y/n answer
		
	}
	
	public int getTotalWorth() {
		int totalGold = 0;
		totalGold += player.getGold();
		for (Item item: player.getInventory()) {
			totalGold += item.getPrice(1, player.getLocation());
		}
		return totalGold;
	}
	
	public void printResults(int gold) {
		System.out.println("Total gold earned: " + gold);
	}
	
	public int getCurrentDay() {
		return currentDay;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public static Game getGame() {
		return currentGame;
	}
	
	public ArrayList<Island> getIslands(){
		return islands;
	}
			
	public static void main(String[] args) {
		Game game = new Game();
		game.run();
	}
}
