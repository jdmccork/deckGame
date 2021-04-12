package deckGame;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {
	
	private Scanner userInput;
	private Player player;
	private ArrayList<Island> islands;
	private int days;
	private int currentDay;
	
	public Game() {
		userInput = new Scanner(System.in);
		mainMenu();
		userInput.close();
	}
	
	public Player createPlayer() {
		String[] names = getNames();
		//select ship to insert into the final 4 values
		Player player = new Player(names[0], names[1], 100, 2, 4, 3);
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
		System.out.print("Enter your ship's name: The ");
		String shipName = userInput.nextLine();
		return new String[] {userName, shipName};
	}
	
	public boolean hasSpecial(String string) {
		//allowing spaces in people names
		Pattern my_pattern = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
		Matcher my_match = my_pattern.matcher(string);
	    if (my_match.find())
	    	return true;
	    else
	        return false;
	}
	
	public ArrayList<Island> generateIslands() {
		ArrayList<Island> islands = new ArrayList<Island>();
		islands.add(player.getLocation());
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
				+ "\nEarn as much gold as you can in the time you have left."
				+ "\nWelcome abord Captain " + player.getUserName() + ".");
	}
	
	public int getGameLength() {
		while (true) {
			System.out.println("Choose game length:");
			try {
				int days = userInput.nextInt();
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
		player = createPlayer();
		islands = generateIslands();
		generateRoutes(islands);
	}
	
	public void mainMenu(){
		int selection;
		while (true) {
			System.out.println("Select an option to continue");
			System.out.println("1: Play");
			System.out.println("2: Quit");
			try {
				selection = userInput.nextInt();
			}catch (java.util.InputMismatchException e) {
				System.out.println("Invalid character, please enter the number of the option you want.");
				continue;
			}
			userInput.nextLine();
			if (selection == 1) {
				gameSetup();
				welcome(player);
				days = getGameLength();
				play();
			} else if (selection == 2) {
				System.out.println("Goodbye");
				return;
			} else {
				System.out.println("Invalid option, please enter the number of the option you want.");
			}
		}
	}
	
	public void play() {
		int selection;
		while (currentDay <= days) {
			System.out.println("Current day: " + currentDay);
			System.out.println("Select an option to continue");
			System.out.println("1: Interact with shop");
			System.out.println("2: Set sail");
			System.out.println("3: See inventory");
			System.out.println("4: Return to main menu");
			try {
				selection = userInput.nextInt();
				userInput.nextLine();
			}catch (java.util.InputMismatchException e) {
				System.out.println("Invalid character, please enter the number of the option you want.");
				continue;
			}
			
		
			switch (selection) {
			case 1:
				//interact with shop
				shopInteraction((player.getLocation()).getStore());
				break;
			case 2:
				selectRoute();
				break;
			case 3:
				player.printInventory();
				pause();
				break;
			case 4:
				return;
			}
		}
	}
	
	
	public void shopInteraction(String shop) {
		int selection;
		while (true) {
			System.out.println("Select an option to continue");
			System.out.println("1: Buy");
			System.out.println("2: Sell");
			System.out.println("3: Exit shop");
			try {
				selection = userInput.nextInt();
				userInput.nextLine();
			}catch (java.util.InputMismatchException e) {
				System.out.println("Invalid character, please enter the number of the option you want.");
				continue;
			}
			
			switch (selection) {
			case 1:
				//see what's for sale
				break;
			case 2:
				//see the price that you can sell your items for
				break;
			case 3:
				return;
			default:
				System.out.println("Please enter a number between 1 and 3");
				break;
			}
		}
	}
	
	public void selectRoute() {
		int selection;
		ArrayList<Route> routes = player.getLocation().getRoutes();
		while (true) {
			int i = 1;
			System.out.println("Select an option to continue");
			for (Route route: routes) {
				System.out.println(i++ + ": The journey to " + route.getDestination().getName() 
						+ " will take " + route.getTime(player.getSpeed()) + " days to complete");
			}
			System.out.println(i++ + ": Return to island");
			try {
				selection = userInput.nextInt();
				userInput.nextLine();
			}catch (java.util.InputMismatchException e) {
				System.out.println("Invalid character, please enter the number of the option you want.");
				continue;
			}
			if (selection == i - 1) {
				return;
			}else if (selection > i - 1 | selection <= 0) {
				System.out.println("Please enter a number between 1 and " + i);
			}else {
				int time = routes.get(selection).getTime(player.getSpeed());
				player.sail(routes.get(selection));
				currentDay += time;
				//arrive(); generates shops when you arrive at the destination so that you can't enter and exit to regenerate the shops
				System.out.println("You travelled for " + time + " days and have arrived at " + player.getLocation());
				return;
			}
		}
		
	}
	
	public void pause() {
		System.out.println("Press enter to continue");
		userInput.nextLine();
	}
		
	public void blank() {
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
			
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game game = new Game();
	}

}
