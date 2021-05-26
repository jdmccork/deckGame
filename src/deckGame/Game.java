package deckGame;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import enums.Actions;
import enums.Statuses;

//TODO Closing store doesn't make ship stats reappear
//TODO Buying/selling items doesn't update the ship stats
//TODO Rescuing sailors doesn't have an event or update gold
//TODO Selling at less cost, add location purchased

//TODO Going to island requires number of days 
//TODO Index error with cards and cargo
public class Game {
	
	/**
	 * The player input used in the command line application
	 */
	private static Scanner userInput;
	
	/**
	 * The instance of player associated with the instance of Game
	 */
	private Player player;
	
	/**
	 * The list of islands associated with the instance of Game
	 */
	private ArrayList<Island> islands;
	
	/**
	 * The number of days that the game will last for
	 */
	private int days;
	
	/**
	 * The number of days that have passed since the start of the game
	 */
	private int currentDay;
	
	/**
	 * The display used for the GUI application
	 */
	private Display display;
	
	/**
	 * The route that is being traveled, used for the GUI
	 */
	private Route chosenRoute;

	/**
	 * The number of days that sailing has occurred for, used by GUI
	 */
	private int daysSailed = 0;
	
	/**
	 * Provides an output variable to store any output that occurs to the command line during a GUI run
	 */
	private ByteArrayOutputStream GUIOut;

	
	/**
	 * Used for testing to allow the input to be changed to a byteArray string
	 */
	public static void setTestInput() {
		userInput = new Scanner(System.in);
	}
	
	/**
	 * Creates a new instance of game and sets it up
	 */
	public Game() {
		gameSetup();
	}
	
	/**
	 * Creates a new display for the game to run on and sets the out to GUIOut to prevent 
	 * any text getting posted on the command line
	 */
	public void runGUI() {
		display = new Display();
		display.run(this);
		
		//GUIOut = new ByteArrayOutputStream();
		//System.setOut(new PrintStream(GUIOut));
	}
	
	public void runCMD() {
		userInput = new Scanner(System.in);
		boolean playing = true;
		while(playing == true) {
			playing = mainMenu();
		}
		userInput.close();
	}
	
	/**
	 * Returns the route which the player is currently on
	 * @return the route the player most recently chose
	 */
	public Route getChosenRoute() {
		return chosenRoute;
	}

	/**
	 * Returns the entries in the players logbook 
	 * @return
	 */
	public ArrayList<Entry> getLogItems() {
		return player.getLogbook().getEntries();
	}

	/**
	 * Creates a new player for the instance of game
	 * @param userName
	 * @param shipName
	 * @param shipType
	 * @return The player created
	 */
	public Player createPlayer(String userName, String shipName, String shipType) {
		int[] ship = getShip(shipType);
		player = new Player(userName, shipName, ship[0], ship[1], ship[2], ship[3], ship[4], ship[5], ship[6], islands.get(0), display);
		return player;
	}
	
	/**
	 * Gets the players selection of ship and passes it as an int list to create the player
	 * @param shipType
	 * @return A list of ints corresponding to the ships;
	 * health, speed, capacity, deck size, power, gold, and crew
	 */
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
	
	/**
	 * Gets the players selection of ship and passes it as an int list to create the player
	 * @return The integer selection of the ship
	 */
	public String getShipCMD() {
		while (true) {
			System.out.println("Select a class:");
			System.out.println("1: None. A ship with 10 crew which has a balance of all stats.");
			System.out.println("2: Merchant. A slow moving ship with 10 crew that has more cargo space.");
			System.out.println("3: Warrior.  A slow moving ship with 20 crew that has high health and damage.");
			System.out.println("4: Rouge. A fast ship with 15 crew that has lower heath and strength.");
			switch (getInt()) {
			//int health, int speed, int capacity, int power, int gold, int crew
			case 1:
				return "1";
			case 2:
				return "2";
			case 3:
				return "3";
			case 4:
				return "4";
			default:
				System.out.println("Please select a number between 1 and 4.");
			}
		}
	}
	
	/**
	 * Gets the names of the player and their ship from the player
	 * @param userName
	 * @param shipName
	 * @return A string that informs if the name is too short/long, has special characters or is accepted
	 */
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
	
	/**
	 * Gets the names of the player and their ship from the player
	 * @return A list of strings corresponding to the player's name and the ship's name
	 */
	public String[] getNamesCMD() {
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
	
	/**
	 * @param string
	 * @return true if the string has any special characters
	 */
	public boolean hasSpecial(String string) {
		Pattern my_pattern = Pattern.compile("[^(a-z)\s]", Pattern.CASE_INSENSITIVE);
		Matcher my_match = my_pattern.matcher(string);
	    if (my_match.find() | string.contains("\s\s"))
	    	return true;
	    else
	        return false;
	}
	
	/**
	 * Generates the islands to be used in the game and stores them in the ArrayList islands
	 */
	public void generateIslands() {
		islands = new ArrayList<Island>();
		islands.add(new Island("Home", 0, 0, 7));
		islands.add(new Island("Golgolles", -13, 4, 0));
		islands.add(new Island("Cansburg", 7, 3, 3));
		islands.add(new Island("Tisjour", -5, -10, 11));
		islands.add(new Island("Brighdown", 6, -8, 13));
	}
	
	/**
	 * iterates through the islands and generates the routes between them
	 * @param islands
	 */
	public void generateAllRoutes(ArrayList<Island> islands) {
		for (Island island : islands) {
			island.generateRoutes(islands);
		}
	}
	
	/**
	 * Prints a welcome message to the player
	 * @param player
	 */
	public void welcome(Player player) {
		System.out.println(player.getUserName() + " is the new captain of The " + player.getShipName() + "."
				+ "\nEarn as much gold as you can in the " + days + " days you have left."
				+ "\nWelcome abord Captain " + player.getUserName() + ".");
		pause();
	}
	
	/**
	 * Gets the number of days that the player want to play for
	 * @return the number of days the player has selected 
	 */
	public int getGameLength() {
		while (true) {
			System.out.println("Choose game length in days (between 20 and 50):");
			int days = getInt();
			if (days >= 20 & days <= 50) {
				return days;
			}else {
				System.out.println("Please enter an integer between 20 and 50.");
			}
		}
	}
	
	/**
	 * Sets up the game state by generating things that are maintained between players
	 */
	public void gameSetup() {
		Item.generateItems();
		Store.readAdvice();
		generateIslands();
		generateAllRoutes(islands);
	}
	
	/**
	 * Creates the classes and information required for a players session
	 * @param userName
	 * @param shipName
	 * @param duration
	 * @param ship
	 */
	public void sessionSetup(String userName, String shipName, int duration, String ship) {
		createPlayer(userName, shipName, ship);
		player.getLocation().getStore().generateStock(player);
		days = duration;
	}
	
	/**
	 * The first menu options the player experiences allowing access to the rest of the game
	 * @return
	 */
	public boolean mainMenu(){
		int selection;
		try {
			while (true) {
				System.out.println("Select an option to continue");
				System.out.println("1: Play");
				System.out.println("2: Quit");
				selection = getInt();
				if (selection == 1) {
					gameSetup();
					String[] names = getNamesCMD();
					sessionSetup(names[0], names[1], getGameLength(), getShipCMD());
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
	
	/**
	 * Puts the player onto their current island and allows them to select what they want to 
	 * do from the options.
	 */
	public void play() {
		while (currentDay <= days) {
			System.out.println("Current day: " + currentDay + "/" + days);
			System.out.println("Select an option to continue");
			System.out.println("1: Interact with store");
			System.out.println("2: Set sail");
			System.out.println("3: See inventory");
			System.out.println("4: See deck");
			System.out.println("5: View Captain's log");
			System.out.println("6: Return to main menu");
			
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
				player.viewLogbook();
				break;
			case 6:
				if (menuConfirm() == true){
					return;
				}
				break;
			default:
				System.out.println("Please enter a number between 1 and 5");
				break;
			}
		}
	}
	
	/**
	 * Confirms if the player want to return to the main menu. This was added to prevent players losing their 
	 * current session
	 * @return
	 */
	public boolean menuConfirm() {
		while (true) {
			System.out.println("Are you sure you want to return to the main menu?");
			System.out.println("1: Yes");
			System.out.println("2: No");
			int selection = Game.getInt();
			
			switch(selection) {
			case 1:
				return true;
			case 2:
				return false;
			default:
				break;
			}
		}
	}
	
	/**
	 * Gets an integer value from the player.
	 * @return An integer from the player
	 */
	public static int getInt(){
		while(true) {
			try {
				int selection = userInput.nextInt();
				userInput.nextLine();
				return selection;
			}catch (java.util.InputMismatchException e) {
				System.out.println("Invalid character, please enter a valid integer.");
				userInput.nextLine();
			}
		}
	}
	
	/**
	 * Charges the player repair costs depending on how much damage they have taken. Ends the game if they
	 * don't have enough money and no items.
	 * @return
	 */
	public boolean chargeRepair() {
		if (player.getStatus() == Statuses.REPAIRED) {
			return true;
		}
		int healthLost = player.getMaxHealth()-player.getHealth();
		int cost = Math.max((int) (healthLost / 5), 1);
		if (player.getGold() < cost & player.getInventory().size() == 0 & player.getCards().size() == 0) {
			if (display != null) {
				this.display.updateDialogue("You don't have enough money and no items to sell for the repairs.");
			} else {
				System.out.println("You don't have enough money and no items to sell for the repairs.");
			}
			throw new EndGameException();
		}
		if (display != null) {
			this.display.updateDialogue(
					"Your ship has sustained " + healthLost + " damage. This will cost $" + cost + " to repair.");
			this.display.setGameState("Repair");
			return false;
		}else {
			return chargeRepairCMD(cost, healthLost);
		}
	}
	
	/**
	 * Charges the player repair costs depending on how much damage they have taken. Ends the game if they
	 * don't have enough money and no items.
	 * @param cost
	 * @param healthLost
	 * @return
	 */
	public boolean chargeRepairCMD(int cost, int healthLost) {
		while (true) {
			System.out.println(
					"Your ship has sustained " + healthLost + " damage. This will cost $" + cost + " to repair.");
			System.out.println("1: Pay\n2: Return");
			switch (Game.getInt()) {
			case 1:
				if (player.modifyGold(-cost)) {
					player.repair();
					return true;
				} else {
					System.out.println(
							"You don't have enough money to repair your ship. Sell an item to make some more cash.");
					return false;
				}
			case 2:
				return false;
			}
		}
	}
	
	/**
	 * Repairs the players ship, removes the amount it costs from the
	 * players gold and adds an entry to the logbook
	 * @param selection
	 * @return
	 */
	public boolean executeRepair(int selection) {
		int healthLost = player.getMaxHealth()-player.getHealth();
		int cost = (int) (healthLost / 5) + 1;
		if(selection == 1) {
			if (player.modifyGold(-cost)) {
				player.repair();
				display.updateGold(String.valueOf(player.getGold()));
				Entry entry = new Entry(currentDay);
					entry.addDamage(-healthLost);
					entry.makeEvent("Repaired ship");
					entry.addCost(cost);
					player.getLogbook().addEntry(entry);
				display.updateDialogue("Your ship has been repaired.");
				display.setGameState("Sea");
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
	
	/**
	 * Pays the crew the correct amount, removes the money from the players gold and adds
	 * an entry into the logbook.
	 * @param cost
	 */
	public void executePay(int cost) {
		if (player.modifyGold(-cost)) {
			display.updateGold(String.valueOf(player.getGold()));
			Entry entry = new Entry(currentDay);
			entry.makeEvent("Paid crew");
			entry.addCost(cost);
			player.getLogbook().addEntry(entry);
			display.updateDialogue("You paid your crew $" + cost + " to sail.");
			executeSail(chosenRoute);
		}else {
			display.updateDialogue("You don't have enough money to pay your crew for this route. Get more money or select a different route.");
		}
	}
	
	/**
	 * Determines how to the player will select the route and charges for repairs
	 */
	public void selectRoute() {
		if (chargeRepair()) {
			if (display != null) {
				ArrayList<Route> routes = player.getLocation().getRoutes();
				for (Route route: routes) {
					display.updateDisplayToolTip(route.getDestination().getDisplay(), "The journey to " 
							+ route.getDestination().getName() + " will take " + route.getTime(player.getSpeed())
							+ " days to complete.");
				}
			}else {
				selectRouteCMD();
			}
		}
	}
	
	/**
	 * The command line variation to select a route. Gets a player input and
	 * send them back or onto confirm route
	 */
	public void selectRouteCMD() {
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
				chosenRoute = routes.get(selection-1);
				int time = chosenRoute.getTime(player.getSpeed());
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
				}else if(!confirmRoute(chosenRoute)) {
					return;
				}
				if (!player.payCrewCMD(time)) {
					return;
				}
				executeSail(chosenRoute);
				return;
			}
		}
	}
	
	/**
	 * Confirms if the player want to travel the route they selected and prints the information about the route.
	 * @param route
	 * @return
	 */
	public boolean confirmRoute(Route route) {
		while (true) {
			System.out.println("Are you sure you want to travel to " + route.getDestination() + "?");
			System.out.println("This will take " + route.getTime(player.getSpeed()) 
			+ " days arriving on day " + currentDay + route.getTime(player.getSpeed()) + ".");
			System.out.println(route.viewEvents());
			System.out.println("1: Yes");
			System.out.println("2: No");
			switch (Game.getInt()) {
			case 1:
				return true;
			case 2:
				return false;					
			default:
				System.out.println("Please enter a number between 1 and 2");
				Game.pause();
				break;
			}
		}
	}
	
	/**
	 * Selects the route the player selected and pays the crew
	 * @param selection
	 */
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
		player.payCrew(time);
		this.chosenRoute = route;
	}
	
	/**
	 * Executes the sail which causes the events to occur for the number of days that the route takes and
	 * sets the players location to the destination
	 * @param chosenRoute
	 */
	public void executeSail(Route chosenRoute) {
		int time = chosenRoute.getTime(player.getSpeed());
		if(display != null) {
			this.daysSailed += 1;
			currentDay += 1;
			display.updateDay(String.valueOf(currentDay));
			display.setGameState(chosenRoute.getEvent().selectEvent(player, currentDay, display));
			if(daysSailed >= time) {
				this.daysSailed = 0;
				if (currentDay < days) {
					player.sail(chosenRoute);
					player.getLocation().getStore().generateStock(player);
					display.updateDialogue("You travelled for " + time + " days and have arrived at " + player.getLocation());
					display.setGameState("Island");
				} else {
					throw new EndGameException();
				}
			}
		} else {
			player.sail(chosenRoute);
			for (int i = 0; i != time & currentDay < days; i++) {
				currentDay += 1;
				chosenRoute.getEvent().selectEvent(player, currentDay, display);
			}
			if (currentDay < days) {
				player.getLocation().getStore().generateStock(player); //generates shops when you arrive at the destination so that you can't enter and exit to regenerate the shops
				System.out.println("You travelled for " + time + " days and have arrived at " + player.getLocation());
				pause();
			} else {
				throw new EndGameException();
			}
		}
	}
	
	
	/**
	 * Put the input in a state where the player has to input something for the game state to continue
	 */
	public static void pause() {
		System.out.println("Press enter to continue");
		userInput.nextLine();
	}
	
	/**
	 * Ends the game and gets the players score
	 */
	public void endGame() {
		int gold = getTotalWorth();
		printResults(gold);
	}
	
	/**
	 * Sells all the players items and adds it to the players gold to get the total value
	 * in gold the player has
	 * @return Players total gold
	 */
	public int getTotalWorth() {
		int totalGold = 0;
		totalGold += player.getGold();
		for (Item item: player.getInventory()) {
			totalGold += item.getPrice(1, player.getLocation());
		}
		return totalGold;
	}
	
	/**
	 * Calculates the players score and displays the number of days the player survived
	 * @param gold
	 */
	public void printResults(int gold) {
		if (display == null) {
			System.out.println("Total gold earned: " + gold);
			System.out.println("Days survived: " + currentDay + "/" + days);
			System.out.println("Total score: " + (int) (gold * 2 * ((double) currentDay/days)));
		} else {
			display.updateMainDisplay(6, "<html>" + display.wrapButtonText("Total gold earned: " + gold) + "</html>", true, true);
			display.updateMainDisplay(7, "<html>" + display.wrapButtonText("Days survived: " + currentDay + "/" + days) + "</html>", true, true);
			display.updateMainDisplay(8, "<html>" + display.wrapButtonText("Total score: " + (int) (gold * 2 * ((double) currentDay/days))) + "</html>", true, true);
			display.updateMainDisplay(12, "Return to Main Menu", true, true);
			display.updateDisplayFunction(12, Actions.MAIN_MENU);
		}
	}
	
	/** 
	 * @return The games current day
	 */
	public int getCurrentDay() {
		return currentDay;
	}
	
	/**
	 * @return The instance of player associated with the instance of game
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * @return An ArrayList of the islands associated with the instance of game
	 */
	public ArrayList<Island> getIslands(){
		return islands;
	}
	
	/**
	 * Sets the current day
	 * @param day
	 */
	public void setCurrentDay(int day) {
		currentDay = day;
	}
	
	public void setDayLimit(int day) {
		days = day;
	}
	
	/**
	 * Runs the program
	 * @param args
	 */
	public static void main(String[] args) {
		Game game = new Game();
		game.runGUI();
	}
}
