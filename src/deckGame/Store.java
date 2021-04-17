package deckGame;

import java.util.ArrayList;


// I think this might be an abstract class and will need to be refactored as such

public class Store {
	
	private ArrayList<Cargo> cargoStock = new ArrayList<Cargo>();

	
	//private ArrayList<Card> cardStock = new ArrayList<Card>();
	
	/**
	 * The items the store will purchase
	 */
	private ArrayList<Cargo> quota = new ArrayList<Cargo>();
	
	//private ArrayList<Card> quota = new ArrayList<Card>();
	
	/**
	 * The name of this store.
	 */
	//i don't think the store needs a name
	private String name;
	
	/**
	 * Creates a new store with the given name.
	 * @param islandName the name of the island
	 */
	public Store(String islandName){
		name = islandName + " General Store";
	}
	
	public ArrayList<Cargo> getCargo(){
		return cargoStock;
	}
	
	
	/**
	 * Gets the items for sale.
	 */
	public void generateCargoStock() {
		Cargo bread = new Cargo("Bread", "It's bread", 1, 1, Rarity.COMMON);
		cargoStock.add(bread);
	}
	
	/**
	 * Prints the items for sale.
	 */
	public void printStock() {
		System.out.print(name + " has " + cargoStock.size());
		if (cargoStock.size() == 0) {
			System.out.println(" Items in stock. Please try again later.");
			return;
		}else if (cargoStock.size() == 1) {
			System.out.println(" cargo item in stock:");
		}else {
			System.out.println(" cargo items in stock:");
		}
		int i = 1;
		for (Cargo cargo: cargoStock) {
			System.out.println(i++ + ": " + cargo.getName());
		}
	}
	
	/**
	 * Gets the items this store is willing to buy.
	 * @return the items this store will buy
	 */
	public ArrayList<Cargo> getQuota() {
		return quota;
	}
	
	/**
	 * Prints the items this store is willing to buy.
	 */
	public void printQuota() {
		String output = name + " wishes to purchase " + cargoStock.size();
		if (cargoStock.size() == 1) {
			output += " item:";
		}else {
			output += " items:";
		}
		for (Item item: quota) {
			output += "\n" + item.toString() + "\n";
		}
		System.out.println(output);
	}
	
	public void addStock() {
		
	}
	
	public void removeStock(Cargo cargo) {
		cargoStock.remove(cargo);
	}
	
	/**
	 * This store buys an item.
	 * @param newStock the item this store tries to buy
	 * @return true if the item is in this store's quota,
	 * 		   false if not.
	 */
	public boolean buy(Item stock) {
		//This may have to be re-engineered with a for loop to check equality.
		if (quota.contains(stock)) {
			quota.remove(stock);
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
		cargoStock.remove(sale);
	}
}
