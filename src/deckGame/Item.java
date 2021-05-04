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
		String output = "Item: " + name + "\nRarity: " + rarity + "\nSize: " + size;
		output += "\nDescription: " + description + "\nType: " + itemType;
		return output;
	}
	
	/**
	 * Gets the space this item takes up
	 * @return the size of this item
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Sets the day which this item was purchased
	 * @param day the day this item was purchased
	 */
	public void setDayPurchased(int day) {
		dayPurchased = day;
	}
	
	/**
	 * Gets the day this item was purchased
	 * @return the day this was purchased
	 */
	public int getDayPurchased() {
		return dayPurchased;
	}
	
	/**
	 * Gets the number of days that have passed since this was purchased
	 * @param currentDay the current day
	 * @return the days between the purchase day and the current day
	 */
	public int getDaysPassed(int currentDay) {
		if (dayPurchased == -1) {
			return 0;
		}else {
			return currentDay - dayPurchased;
		} 
	}
	
	/**
	 * Gets the name of this item
	 * @return the name of the product
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the base price of this item.
	 * @return the base price of this item
	 */
	public int getPrice() {
		return basePrice;
	}
	
	/**
	 * Gets the description of this item
	 * @return this item's description
	 */
	public String getDesription() {
		return description;
	}
	
	/**
	 * Gets the rarity of this item
	 * @return this item's rarity
	 */
	public Rarity getRarity() {
		return rarity;
	}
}
