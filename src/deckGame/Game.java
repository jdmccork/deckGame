package deckGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import enums.ItemType;
import enums.Rarity;
import enums.Stats;


public class Game {
	private static Game currentGame;
	private static Scanner userInput = new Scanner(System.in);
	private Player player;
	private ArrayList<Island> islands;
	private int days;
	private int currentDay;
	private double priceModifier; //We can add a difficulty setting that will increase this making it harder
	//private ArrayList<Cards> allCards;
	
	public void setTestInput() {
		userInput = new Scanner(System.in);
	}
	
	public Game() {
		gameSetup();
	}
	
	public void run() {
		Display display = new Display();
		boolean playing = true;
		while(playing == true) {
			playing = mainMenu();
		}
		userInput.close();
	}

	public Player createPlayer() {
		String[] names = getNames();
		//select ship to insert into the final 4 values
		Player player = new Player(names[0], names[1], 100, 2, 4, 3, 100, islands.get(0));
		return player;
	}
	
	public String[] getNames() {
		String userName = null;
		do {
			if (userName != null) {
				if (hasSpecial(userName)) {
					System.out.println("Username must not contain digits or special characters.");
				}else {
					System.out.println("Length of username must be between 3 and 15 characters.");
				}
			}
			System.out.print("Enter username: Captain ");
			userName = userInput.nextLine();
		}while (userName.length() < 3 | userName.length() > 15 | hasSpecial(userName));
		System.out.print("Enter your ship's name: ");
		String shipName = userInput.nextLine();
		return new String[] {userName, shipName};
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
	
	public ArrayList<Island> generateIslands() {
		ArrayList<Island> islands = new ArrayList<Island>();
		islands.add(new Island("Home", 0, 0));
		islands.add(new Island("Golgolles", -10, 5));
		islands.add(new Island("Cansburg", 5, 5));
		islands.add(new Island("Tisjour", -5, -5));
		islands.add(new Island("Brighdown", 5, -5));
		return islands;
	}
	
	public void generateRoutes(ArrayList<Island> islands) {
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
		currentGame = this;
		Item.generateItems();
		Store.readAdvice();
		islands = generateIslands();
		generateRoutes(islands);
	}
	
	public void sessionSetup() {
		//temporary
		priceModifier = 1; // difficulty
		player = createPlayer();
		generateStore(player.getLocation());
		days = getGameLength();
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
					sessionSetup();
					welcome(player);
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
			System.out.println("4: Return to main menu");
			
			switch (getInt()) {
			case 1:
				//interact with shop
				player.getLocation().getStore().interact(player);
				break;
			case 2:
				selectRoute();
				break;
			case 3:
				viewInventory();
				break;
			case 4:
				return;
			}
		}
	}
/*
	public void viewItem(Item item, double priceModifier, String option) {
		System.out.println(item);
		if (option == "Buy") {
			System.out.println("The " + item.getName() + " will cost $" + item.getPrice(priceModifier));
		} else {
			System.out.println("You will recive $" + item.getPrice(priceModifier) + " for the " + item.getName() + ".");
		}
		System.out.println("You currently have $" + player.getGold());
		System.out.println("Select an option to continue");
		System.out.println("1: " + option);
		System.out.println("2: Return to shop");
		while (true) {
			switch (getInt()) {
			case 1:
				if (option == "Buy") {
					purchaseItem(item, priceModifier);
				}else {
					sellItem(item, priceModifier);
				}
				return;
			case 2:
				return;
			default:
				System.out.println("Please enter a number between 1 and 2");
				break;
			}
		}
	}
*/
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
	
	public void selectRoute() {
		
		/* 
		 * Might change this to be similar to the store, you select where you want to go then select the route.
		 * Should also probably separate it more as it is one of the longest methods
		 */
		
		int selection;
		ArrayList<Route> routes = player.getLocation().getRoutes();
		while (true) {
			int index = 1;
			System.out.println("Select an option to continue");
			for (Route route: routes) {
				System.out.println(index++ + ": The journey to " + route.getDestination().getName() 
						+ " will take " + route.getTime(player.getSpeed()) + " days to complete");
			}
			System.out.println(index + ": Return to island");
			try {
				selection = getInt();
			}catch (java.util.InputMismatchException e) {
				System.out.println("Invalid character, please enter the number of the option you want.");
				userInput.nextLine();
				continue;
			}
			if (selection == index) {
				return;
			}else if (selection > index | selection < 0) {
				System.out.println("Please enter a number between 1 and " + index);
			}else {
				selection--; // accounts for the index's starting at 0 and not 1
				Route route = routes.get(selection);
				int time = route.getTime(player.getSpeed());
				if (currentDay + time >= days) {
					System.out.println("Note, this trip will exceed your remaining time, all items will"
							+ " be sold on day " + days + ".\nContinue?");
					System.out.println("1: Yes");
					System.out.println("2: No");
					switch (getInt()) {
					case 1:
						break;
					case 2:
						return;
					default:
						System.out.println("Please enter the number 1 or 2.");
					}
				}
				player.sail(route);
				for (int i = 0; i != time & currentDay < days; i++) {
					currentDay += 1;
					Event event = new Event();
					event.selectEvent(route, player);
				}
				if (currentDay < days) {
					System.out.println("----------------");
					generateStore(player.getLocation()); //generates shops when you arrive at the destination so that you can't enter and exit to regenerate the shops
					System.out.println("+++++++++++++++++++++++++++");
					System.out.println("You travelled for " + time + " days and have arrived at " + player.getLocation());
					pause();
				}else {
					throw new EndGameException();
				}
				return;
			}
		}
	}
	
	public void viewInventory() {
		player.printInventory();
		System.out.println((player.getInventory().size() + 1) + ": Return\n" + "Select an item or return to continue.");
		int selection = getInt();
		if (selection == player.getInventory().size() + 1) {
			return;
		}else if (selection <= player.getInventory().size()) {
			player.getInventory().get(selection - 1).viewItem();
		}else {
			System.out.println("Please enter a number between 1 and " + player.getInventory().size() + 1 + ".");
		}
	}
	
	public static void pause() {
		System.out.println("Press enter to continue");
		userInput.nextLine();
	}
	
	public void generateStore(Island island) {
		island.getStore().generateStock(player);
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
			totalGold += item.getPrice(1);
		}
		return totalGold;
	}
	
	public void printResults(int gold) {
		System.out.println("Total gold earned: " + gold);
	}
		
	public void template() {
		int selection;
		while (currentDay <= days) {
			System.out.println("Select an option to continue");
			System.out.println("1: ");
			try {
				selection = userInput.nextInt();
				userInput.nextLine();
			}catch (java.util.InputMismatchException e) {
				System.out.println("Invalid character, please enter the number of the option you want.");
				continue;
			}
			
			switch (selection) {
			case 1:
				break;
			}
		}
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
			
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game game = new Game();
		game.run();
	}
}
