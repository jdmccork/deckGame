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
	 * A variable that stores whether the advice file has been successfully read
	 */
	private static boolean adviceRead = false;
	
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
	
	/**
	 * Creates a new store and generates its stock.
	 */
	public Store(String islandName, Player player){
		generateStock(player);
		readAdvice();
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
		while (stock.size() < 4) {
			System.out.println(stock.size());
			ArrayList<Item> items = Item.getRandomItems(storeModifier);
			randomNum = (int) (Math.random() * items.size());
			Item item = items.get(randomNum);
			if (!stock.contains(item) & !player.getInventory().contains(item)) {
				stock.add(item);			
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
	public void addStock() {
		
	}
	
	/**
	 * Removes an item from stock.
	 * @param item the item to be removed.
	 */
	public void removeStock(Item item) {
		stock.remove(item);
	}
	
	/**
	 * This store buys an item.
	 * @param newStock the item this store tries to buy
	 * @return true if the item is in this store's quota,
	 * 		   false if not.
	 */
	public boolean sell(Item stock) {
		//This may have to be re-engineered with a for loop to check equality.
		return true;
	}
	
	/**
	 * This store sells an item.
	 * @param sale the item this store sells.
	 */
	public void buy(Item sale) {
		stock.remove(sale);
	}
	
	/**
	 * Attempts to read the advice file in the resources package.
	 * If successful, adds each line to the list of possible advice.
	 */
	private void readAdvice() {
		if (!adviceRead) {
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
			    adviceRead = true;
			} catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
			    e.printStackTrace();
			}
		}
	}
	
	/**
	 * Prints a piece of advice and increments an advice counter.
	 */
	public void talkToShopKeep() {
		if (adviceRead) {
			System.out.println(adviceList.get(adviceCount));
		    adviceCount += 1;
		    if(adviceCount == adviceList.size()) {
		    	adviceCount = 0;
		    }
		} else {
			System.out.println("*The shopkeeper has forgotten their advice*");
		}
	}
}
