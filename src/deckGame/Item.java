package deckGame;

import enums.ItemType;
import enums.Rarity;

public abstract class Item {
	/**
	 * The name of this item.
	 */
	private String name;
	
	/**
	 * The description of this item.
	 */
	private String description;
	
	/**
	 * The amount of space this item requires to store.
	 */
	private int size;
	
	/**
	 * The base value of this item.
	 */
	private int basePrice;
	
	/**
	 * The rarity of this item.
	 */
	private Rarity rarity;
	
	private int dayPurchased = -1; //Thinking it should be distance so that being faster doesn't negatively effect the profit margin
		
	/**
	 * Creates a new instance of item from the given data.
	 * @param tempName the name of the item
	 * @param tempDescription a description of the item
	 * @param tempSize the space the item requires to store
	 * @param tempBasePrice the base price of this item
	 * @param tempRarity the rarity of this item
	 */
	public Item(String tempName, String tempDescription, int tempSize, int tempBasePrice, Rarity tempRarity) {
		name = tempName;
		description = tempDescription;
		size = tempSize;
		basePrice = tempBasePrice;
		rarity = tempRarity;
	}
	
	/**
	 * Gets a string representation of this item.
	 * @return the string representing this item
	 */
	public String toString() {
		String output = "Item: "+name+"\nRarity: "+rarity+"\nSize: "+size+"\nDescription: "+description;
		return output;
	}
	
	public void setDayPurchased(int day) {
		dayPurchased = day;
	}
	
	public int getDayPurchased() {
		return dayPurchased;
	}
	
	public int getDaysPassed(int currentDay) {
		if (dayPurchased == -1) {
			return 0;
		}else {
			return currentDay - dayPurchased;
		} 
	}
	
	/**
	 * @return the name of the product
	 */
	public String getName() {
		return name;
	}
	
	public int getBasePrice() {
		return basePrice;
	}
	
	/**
	 * Gets the base price of this item.
	 * @return the base price of this item
	 */
	public int GetPrice() {
		return basePrice;
	}
	
	public String getDesription() {
		return description;
	}
	
	public Rarity getRarity() {
		return rarity;
	}
}
