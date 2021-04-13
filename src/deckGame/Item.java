package deckGame;

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
	private String rarity;
	
	/**
	 * Creates a new instance of item from the given data.
	 * @param tempName the name of the item
	 * @param tempDescription a description of the item
	 * @param tempSize the space the item requires to store
	 * @param tempBasePrice the base price of this item
	 * @param tempRarity the rarity of this item
	 */
	public Item(String tempName, String tempDescription, int tempSize, int tempBasePrice, String tempRarity) {
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
	
	/**
	 * Gets the base price of this item.
	 * @return the base price of this item
	 */
	public int GetPrice() {
		return basePrice;
	}
}
