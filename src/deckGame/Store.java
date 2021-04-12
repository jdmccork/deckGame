package deckGame;

import java.util.ArrayList;

public class Store {
	/**
	 * The items the store has available.
	 */
	private ArrayList<Item> stock = new ArrayList<Item>();
	
	/**
	 * The items the store will purchase
	 */
	private ArrayList<Item> quota = new ArrayList<Item>();
	
	/**
	 * The name of this store.
	 */
	private String name;
	
	/**
	 * Creates a new store with the given name.
	 * @param islandName the name of the island
	 */
	Store(String islandName){
		name = islandName + " General Store";
	}
	
	/**
	 * Gets the items for sale.
	 * @return the items for sale
	 */
	public ArrayList<Item> getStock() {
		return stock;
	}
	
	/**
	 * Prints the items for sale.
	 */
	public void printStock() {
		String output = name+" has "+stock.size();
		if (stock.size() == 1) {
			output += " item ";
		}else {
			output += " items ";
		}
		output += "in stock:";
		for (Item item: stock) {
			output += "\n"+item.toString()+"\n";
		}
		System.out.println(output);
	}
	
	/**
	 * Gets the items this store is willing to buy.
	 * @return the items this store will buy
	 */
	public ArrayList<Item> getQuota() {
		return quota;
	}
	
	/**
	 * Prints the items this store is willing to buy.
	 */
	public void printQuota() {
		String output = name+" wishes to purchase "+stock.size();
		if (stock.size() == 1) {
			output += " item:";
		}else {
			output += " items:";
		}
		for (Item item: quota) {
			output += "\n"+item.toString()+"\n";
		}
		System.out.println(output);
	}
	
	/**
	 * This store buys an item.
	 * @param newStock the item this store tries to buy
	 * @return true if the item is in this store's quota,
	 * 		   false if not.
	 */
	public boolean buy(Item newStock) {
		//This may have to be re-engineered with a for loop to check equality.
		if (quota.contains(newStock)) {
			stock.add(newStock);
			quota.remove(newStock);
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * This store sells an item.
	 * @param sale the item this store sells.
	 */
	public void sell(Item sale) {
		stock.remove(sale);
	}
}
