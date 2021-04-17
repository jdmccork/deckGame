package deckGame;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Game {
	public void print(String string){
		System.out.println(string);
	}

	private Scanner userInput;
	private Player player;
	private ArrayList<Island> islands;
	private int days;
	private int currentDay;
	private double priceModifier; //We can add a difficulty setting that will increase this making it harder
	
	public Game() {
		userInput = new Scanner(System.in);
		mainMenu();
		userInput.close();
	}
	public Game(int testNum) {
		userInput = new Scanner(System.in);
		player = new Player("Tester", "The void", 100, 2, 4, 3, 25);
		priceModifier = 1;
		islands = generateIslands();
		generateRoutes(islands);
		generateStores();
		days = 25;
		play();
		userInput.close();	
	}
	
	public Player createPlayer() {
		String[] names = getNames();
		//select ship to insert into the final 4 values
		Player player = new Player(names[0], names[1], 100, 2, 4, 3, 25);
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
		
		//temporary
		priceModifier = 1;
		
		
		player = createPlayer();
		islands = generateIslands();
		generateRoutes(islands);
		generateStores();
	}
	
	public void mainMenu(){
		int selection;
		while (true) {
			System.out.println("Select an option to continue");
			System.out.println("1: Play");
			System.out.println("2: Quit");
			selection = getInt();
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
		while (currentDay <= days) {
			System.out.println("Current day: " + currentDay);
			System.out.println("Select an option to continue");
			System.out.println("1: Interact with cargo store");
			System.out.println("2: Interact with card store");
			System.out.println("3: Set sail");
			System.out.println("4: See inventory");
			System.out.println("5: Return to main menu");
			
			switch (getInt()) {
			case 1:
				//interact with shop
				shopInteraction((player.getLocation()).getCargoStore());
				break;
			case 2:
				//shopInteraction((player.getLocation()).getCardStore());
			case 3:
				selectRoute();
				break;
			case 4:
				player.printInventory();
				pause();
				break;
			case 5:
				return;
			}
		}
	}
	
	
	public void shopInteraction(Store store) {
		while (true) {
			System.out.println("Select an option to continue");
			System.out.println("1: Buy");
			System.out.println("2: Sell");
			System.out.println("3: Exit shop");
			
			switch (getInt()) {
			case 1:
				buyStock(store);
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
	
	public void buyStock(Store store) {
		store.printStock();
		if (store.getCargo().size() == 0) {
			pause();
			return;
		}
		System.out.println("Select an item to view more options or return with 0.");
		int selection = getInt();
		if (selection == 0) {
			return;
		}else if (selection <= store.getCargo().size()) {
			viewCargoItem(store.getCargo().get(selection - 1));
		}else {
			System.out.println("Please enter a number between 0 and " + store.getCargo().size() + ".");
		}
	}
	
	public void viewCargoItem(Cargo cargo) {
		System.out.println(cargo);
		System.out.println("The " + cargo.getName() + " will cost $" + getPrice(cargo));
		System.out.println("You currently have $" + player.getGold());
		System.out.println("Select an option to continue");
		System.out.println("1: Buy");
		System.out.println("2: Return to shop");
		switch (getInt()) {
		case 1:
			purchaseItem(cargo);
			break;
		case 2:
			return;
		default:
			System.out.println("Please enter a number between 1 and 2");
			break;
		}
	}
	
	public void purchaseItem(Cargo cargo) {
		int price = getPrice(cargo);
		if (player.getGold() >= price & player.getInventory().size() < player.getCapacity()) {
			player.addCargo(cargo);
			player.modifyGold(-price);
			player.getLocation().getCargoStore().removeStock(cargo);
			System.out.println("Purchase successful. " + cargo.getName() + " has been added to your ship");
		}else if (player.getGold() < price){
			System.out.println("Your don't have enough money to buy this item.");
		}else {
			System.out.println("Your ship currently has " + player.getInventory().size() + "/" + player.getCapacity()   
					+ " items. Upgrade your ship or sell an item to purchace this item.");
		}
		pause();
	}
	
	public int getPrice(Cargo cargo) {
		switch(cargo.getRarity()) {
		case COMMON:
			return (int) (cargo.getBasePrice() * priceModifier * 1);
		case UNCOMMON:
			return (int) (cargo.getBasePrice() * priceModifier * 1.2);
		case RARE:
			return (int) (cargo.getBasePrice() * priceModifier * 1.5);
		case LEGENDARY:
			return (int) (cargo.getBasePrice() * priceModifier * 2);
		default:
			return cargo.getBasePrice();
		}
	}
	
	public int getInt(){
		while(true) {
			try {
				int selection = userInput.nextInt();
				userInput.nextLine();
				return selection;
			}catch (java.util.InputMismatchException e) {
				System.out.println("Invalid character, please enter the number of the option you want.");
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
				selection = userInput.nextInt() - 1;
				userInput.nextLine();
			}catch (java.util.InputMismatchException e) {
				System.out.println("Invalid character, please enter the number of the option you want.");
				userInput.nextLine();
				continue;
			}
			if (selection == i) {
				return;
			}else if (selection > i | selection <= 0) {
				System.out.println("Please enter a number between 1 and " + i);
			}else {
				int time = routes.get(selection).getTime(player.getSpeed());
				player.sail(routes.get(selection));
				currentDay += time;
				generateStores(); //generates shops when you arrive at the destination so that you can't enter and exit to regenerate the shops
				System.out.println("You travelled for " + time + " days and have arrived at " + player.getLocation());
				return;
			}
		}
		
	}
	
	public void pause() {
		System.out.println("Press enter to continue");
		userInput.nextLine();
	}
	
	public void generateStores() {
		player.getLocation().getCargoStore().generateCargoStock();
		//player.getLocation().getCardStore().generateCardStock();
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
			
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game game = new Game(1);
	}

}
