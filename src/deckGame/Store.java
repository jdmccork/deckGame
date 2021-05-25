package deckGame;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//import enums.ItemType;

public class Store {
	
	/**
	 * The stock which the store has
	 */
	private ArrayList<Item> stock = new ArrayList<Item>();

	/**
	 * The counter ensuring that advice cycles, even between stores.
	 */
	private static int adviceCount;
	
	/**
	 * A list of the possible advice available from shopkeepers.
	 */
	private static ArrayList<String> adviceList = new ArrayList<String>();
	
	/**
	 * The modifier used when this store sells an item
	 */
	private double buyModifier = 1; //put in to allow for changes later
	
	/**
	 * The modifier used when this store buys an item
	 */
	private double sellModifier = 0.8; //should be less than 1 to prevent the ability to buy and sell instantly
	
	
	/**
	 * The luck modifier used when generating items
	 */
	private int storeModifier = 0;
	
	private Island location;
	
	/**
	 * Creates a new store and generates its stock.
	 */
	public Store(Island island){
		location = island;
	}
	
	/**
	 * Gets the stock of this store
	 * @return the stock
	 */
	public ArrayList<Item> getStock(){
		return stock;
	}
	
	/**
	 * Gets the buy modifier of items
	 * @return the buy modifier of items
	 */
	public double getBuyModifier() {
		return buyModifier;
	}
	
	/**
	 * Gets the sell modifier of items
	 * @return the sell modifier of items
	 */
	public double getSellModifier() {
		return sellModifier;
	}
	
	/**
	 * Creates the stock which the store will sell.
	 * @param items the items in play
	 * @param player the player
	 */
	public void generateStock(Player player) {
		int randomNum;
		stock = new ArrayList<Item>();
		while (stock.size() < 5) {
			ArrayList<Item> items = Item.getRandomItems(storeModifier);
			randomNum = (int) (Math.random() * items.size());
			Item item = items.get(randomNum);
			if (!stock.contains(item) & !player.getInventory().contains(item) & !player.getCards().contains(item)) {
				addStock(item);
			}
		}
	}
	
	/**
	 * Prints out the items in the stock of this store
	 */
	public void printStock() {
		System.out.print("This store has " + stock.size());
		if (stock.size() == 0) {
			System.out.println(" items in stock. Please try again later.");
			return;
		}else if (stock.size() == 1) {
			System.out.println(" item in stock:");
		}else {
			System.out.println(" items in stock:");
		}
		int i = 1;
		for (Item item: stock) {
			System.out.println(i++ + ": " + item.getName());
		}
	}
	
	/**
	 * TBD
	 */
	public void addStock(Item item) {
		stock.add(item);
	}
	
	/**
	 * Removes an item from stock.
	 * @param item the item to be removed.
	 */
	public void removeStock(Item item) {
		stock.remove(item);
	}

	/**
	 * Attempts to read the advice file in the resources package.
	 * If successful, adds each line to the list of possible advice.
	 */
	public static void readAdvice() {
		try {
			//Defines the advice file as a new file
			File myObj = new File("src/resources/Advice");
			//Creates a scanner object to read the advice file
		    Scanner myReader = new Scanner(myObj);
		    //While the scanner finds new lines, keep adding them to the list
		    while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        adviceList.add(data);
		    }
		    myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getAdvice(){
		return adviceList;
	}
	
	public void interact(Player player, int currentDay) {
		while (true) {
			//talkToShopKeep();
			System.out.println("Select an option to continue");
			System.out.println("1: Buy");
			System.out.println("2: Sell");
			System.out.println("3: Exit shop");
			
			switch (Game.getInt()) {
			case 1:
				buyOptions(player, currentDay);
				//see what's for sale
				break;
			case 2:
				sellOptions(player, currentDay);
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
	
	public void buyOptions(Player player, int currentDay) {
		while (true) {
			printStock();
			if (stock.size() == 0) {
				Game.pause();
				return;
			}
			System.out.println((getStock().size() + 1) + ": Return");
			System.out.println("Select an item to view more information");
			int selection = Game.getInt();
			if (selection == stock.size() + 1) {
				return;
			}else if (selection <= getStock().size() & selection > 0) {
				Item item = stock.get(selection - 1);
				boolean complete = false;
				while (complete == false) {
					System.out.println(item);
					System.out.println("You currently have $" + player.getGold());
					System.out.println("1: Buy for $" + item.getPrice(buyModifier, player.getLocation()));
					System.out.println("2: Return");
					switch (Game.getInt()) {
					case 1:
						complete = buyItem(item, player, currentDay);
						break;
					case 2:
						complete = true;
						break;						
					default:
						System.out.println("Please enter a number between 1 and 2");
						Game.pause();
						break;
					}
				}
			}else {
				System.out.println("Please enter a number between 1 and " + (stock.size() + 1) + ".");
			}
		}
	}
	
	public boolean buyItem(Item item, Player player, int currentDay) {
		int price = item.getPrice(buyModifier, player.getLocation());
		if (player.getGold() >= price) {
			if (!player.addItem(item)) {
				System.out.println("Your ship currently has " + player.getCargoStored() + "/" + player.getCapacity()   
				+ " items. Upgrade your ship or sell an item to purchace this item.");
				Game.pause();
				return false;
			}
			player.modifyGold(-price);
			item.setPurchaseCost(price);
			removeStock(item);
			item.setLocationPurchased(location);
			System.out.println("Purchase successful. " + item.getName() + " has been added to your ship");
			Entry entry = new Entry(currentDay);
			entry.makeTransaction(item, "Bought ");
			entry.addCost(price);
			player.getLogbook().addEntry(entry);
			Game.pause();
			return true;
		}else if (player.getGold() < price){
			System.out.println("Your don't have enough money to buy this item.");
			Game.pause();
			return false;
		}else {
			
			Game.pause();
			return false;
		}
	}
	
	public void sellOptions(Player player, int currentDay) {
		System.out.println("Select an item to sell.");
		player.printInventory();
		player.printCards();
		System.out.println((player.getInventory().size() + player.getCards().size() + 1) + ": Return\n" + "Select an item or return to continue.");
		int selection = Game.getInt();
		if (selection == player.getInventory().size() + player.getCards().size() + 1) {
			return;
		}else if (selection <= player.getInventory().size()) {
			sellConfirm(player.getInventory().get(selection - 1), player, currentDay);
		}else if (selection <= player.getInventory().size() + player.getCards().size() ) {
			sellConfirm(player.getCards().get(selection - player.getInventory().size() - 1), player, currentDay);
		} else {
			System.out.println("Please enter a number between 1 and " + player.getInventory().size() + 1 + ".");
		}
	}
	
	public void sellConfirm(Item item, Player player, int currentDay) {
		boolean complete = false;
		while (complete == false) {
			System.out.println(item);
			System.out.println("1: Sell for $" + item.getPrice(sellModifier, player.getLocation()));
			System.out.println("2: Return");
			switch (Game.getInt()) {
			case 1:
				complete = sellItem(item, player, currentDay);
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
	
	public boolean sellItem(Item item, Player player, int currentDay) {
		int price = item.getPrice(sellModifier, player.getLocation());
		if (player.getInventory().contains(item)) {
			player.removeItem(item);
			player.modifyGold(price);
			System.out.println("Sale successful. " + item.getName() + " has been removed from your ship and $" + price + " has been added to your account.");
			item.setLocationPurchased(null);
			Entry entry = new Entry(currentDay);
			entry.makeTransaction(item, "Sold ");
			entry.addCost(-price);
			player.getLogbook().addEntry(entry);
			Game.pause();
			return true;
		} else {
			System.out.println("Something went wrong, you don't have this item.");
		}
		Game.pause();
		return false;
	}
	
	/**
	 * Prints a piece of advice and increments an advice counter.
	 */
	public String talkToShopKeep() {
		String output;
		try {
			output = adviceList.get(adviceCount);
		    adviceCount += 1;
		    if(adviceCount == adviceList.size()) {
		    	adviceCount = 0;
		    }
		} catch (ArrayIndexOutOfBoundsException e) {
			output = "*The shopkeeper seems like he's had a bit too much to drink to give advice*";
		}
		return output;
	}
}
