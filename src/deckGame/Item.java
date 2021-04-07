package deckGame;

public abstract class Item {
	private String name;
	private String description;
	private int size;
	private int basePrice;
	private String rarity;
	
	public Item(String tempName, String tempDescription, int tempSize, int tempBasePrice, String tempRarity) {
		name = tempName;
		description = tempDescription;
		size = tempSize;
		basePrice = tempBasePrice;
		rarity = tempRarity;
	}
	
	public String ToString() {
		String output = "Item: "+name+"\nRarity: "+rarity+"\nSize: "+size+"\nDescription: "+description;
		return output;
	}
	
	public int GetPrice() {
		return basePrice;
	}
}
