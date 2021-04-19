package deckGame;

import java.util.ArrayList;

import enums.ItemType;

public class Store {
	
	private ArrayList<Item> stock = new ArrayList<Item>();

	/**
	 * The name of this store.
	 */
	//i don't think the store needs a name
	private String name;
	
	private double buyModifier = 1; //put in to allow for changes later
	
	private double sellModifier = 0.8; //should be less than 1 to prevent the ability to buy and sell instantly
	
	/**
	 * Creates a new store with the given name.
	 * @param islandName the name of the island
	 */
	public Store(String islandName){
		name = islandName + " General Store";
		generateStock();
	}
	
	public ArrayList<Item> getStock(){
		return stock;
	}
	
	public double getBuyModifier() {
		return buyModifier;
	}
	
	public double getSellModifier() {
		return sellModifier;
	}
	
	/**
	 * Gets the items for sale.
	 */
	public void generateStock() {
		Cargo bread = new Cargo("Bread", "It's bread", 1, 1, Rarity.COMMON, ItemType.CARGO);
		stock.add(bread);
	}
	
	/**
	 * Prints the items for sale.
	 */
	public void printStock() {
		System.out.print(name + " has " + stock.size());
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
	
	public void addStock() {
		
	}
	
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
}
