package deckGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioSystem;

import enums.ItemType;
import enums.Rarity;
import enums.Stats;

public abstract class Item {
	
	/**
	 * All of the items that are used in the game sorted into rarity classes.
	 */
	private static ArrayList<ArrayList<Item>> allItems = new ArrayList<ArrayList<Item>>();

	/**
	 * The name of this item.
	 */
	private String name;
	
	/**
	 * The description of this item.
	 */
	private String description;
	
	/**
	 * The amount of space this item requires to store. NOTE: all cards will be regarded as having a size of
	 * 1 in the current implementation.
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
	
	/**
	 * The location that the item was purchased.
	 */
	private Island locationPurchased;
	
	/**
	 * The amount of money paid for the item.
	 */
	private int purchaseCost;
	
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
		purchaseCost = -1;
	}
	
	/**
	 * Gets a string representation of this item.
	 * @return the string representing this item
	 */
	public String toString() {
		String output;
		if (this instanceof Cargo) {
			output = "Cargo: ";
		} else {
			output = "Card: ";
		}
		 output += name + "\nRarity: " + rarity
				+ "\nSize: " + size + "\nDescription: " + description;
		if (purchaseCost != -1) {
			output += "\n" + "Purchase Cost: $" + purchaseCost;
		}
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
	public void setLocationPurchased(Island island) {
		locationPurchased = island;
	}
	
	/**
	 * Gets the day this item was purchased
	 * @return the day this was purchased
	 */
	public Island getLocationPurchased() {
		return locationPurchased;
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
	public int getBasePrice() {
		return basePrice;
	}
	
	/**
	 * 
	 * @return The type of item that the child is.
	 */
	public abstract ItemType getType();
	
	/**
	 * Gets the description of this item
	 * @return this item's description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gets the rarity of this item
	 * @return this item's rarity
	 */
	public Rarity getRarity() {
		return rarity;
	}
	
	/**
	 * Creates the items from a text file and stores them into the allItems ArrayList
	 */
	public static void generateItems() {
		ArrayList<Item> common = new ArrayList<Item>();
		ArrayList<Item> uncommon = new ArrayList<Item>();
		ArrayList<Item> rare = new ArrayList<Item>();
		ArrayList<Item> legendary = new ArrayList<Item>();

		ArrayList<String> lines = readItems();
		Item item;
		for(String line : lines) {
			item = null;
			try {
				String[] temp = line.split(" #");
				ArrayList<String> parts = new ArrayList<String>();
				for(String part: temp) {
					String[] values = part.split(": ");
					parts.add(values[0]);
					parts.add(values[1]);
				}
				String name = parts.get(3);
				String description = parts.get(5);
				int size = Integer.parseInt(parts.get(7));
				int basePrice = Integer.parseInt(parts.get(9));
				Rarity rarity = Rarity.valueOf(parts.get(11));
				int index = 12;
				if(parts.get(1).equals("Cargo")) {
					item = generateCargo(index, parts, name, description, size, basePrice, rarity);
				} else if(parts.get(1).equals("Card")) {
					item = generateCard(index, parts, name, description, size, basePrice, rarity);
				}else {
					throw new Exception("Item Type doesn't exist");
				}
				switch (item.getRarity()) {
				case COMMON:
					common.add(item);
					break;
				case UNCOMMON:
					uncommon.add(item);
					break;
				case RARE:					
					rare.add(item);
					break;
				case LEGENDARY:
					legendary.add(item);
					break;
				}
			}catch(Exception e){
				System.out.println(item);
				System.out.println(e);
			}
		}
		allItems.add(common);
		allItems.add(uncommon);
		allItems.add(rare);
		allItems.add(legendary);
	}
	
	/**
	 * Creates an instance of Cargo and applies the builder to add the stats it changes
	 * <p>
	 * This was originally planned to have more that one possible outcome but time constraints prevented this
	 * @param index
	 * @param parts
	 * @param name
	 * @param description
	 * @param size
	 * @param basePrice
	 * @param rarity
	 * @return
	 * @throws Exception
	 */
	private static Item generateCargo(int index, ArrayList<String> parts,
			String name, String description, int size, int basePrice, Rarity rarity) throws Exception {
		Item item = new Cargo(name, description, size, basePrice, rarity);
		while (index < parts.size()-1) {
			if (parts.get(index).equals("Stat")) {
				((Cargo) item).changeModifier(Stats.valueOf(parts.get(index + 1)),
						Integer.parseInt(parts.get(index + 3)));
				index += 4;
			} else {
				throw new Exception("Variable doesn't exist");
			}
		}
		return item;
	}
	
	/**
	 * Creates an instance of Card and applies the builder to add the stats it changes
	 * <p>
	 * This was originally planned to have more that one possible outcome but time constraints prevented this
	 * @param index
	 * @param parts
	 * @param name
	 * @param description
	 * @param size
	 * @param basePrice
	 * @param rarity
	 * @return
	 * @throws Exception
	 */
	private static Item generateCard(int index, ArrayList<String> parts,
			String name, String description, int size, int basePrice, Rarity rarity) throws Exception {
		Item item = new Card(name, description, size, basePrice, rarity);
		while (index < parts.size()-1) {
			if (parts.get(index + 1).equals("multi-transform")) {
				((Card) item).makeMultiTransform(Integer.parseInt(parts.get(index + 3)),
						Integer.parseInt(parts.get(index + 5)), Integer.parseInt(parts.get(index + 7)));
				index += 8;
			} else if (parts.get(index + 1).equals("moreDice")) {
				((Card) item).makeDiceAdder(Integer.parseInt(parts.get(index + 3)),
						Integer.parseInt(parts.get(index + 5)), Integer.parseInt(parts.get(index + 7)));
				index += 8;
			}else {
				throw new Exception("Variable doesn't exist");
			}
		}
		return item;
	}
		
	/**
	 * Gets all the items from the text file to be generated using the generate items function
	 * @return
	 */
	private static ArrayList<String> readItems() {
		ArrayList<String> items = new ArrayList<String>();
		try {
			//Defines the items file as a new file to read
			//new File(Item.class.getResource("Items.txt"));
			InputStream myObj = Item.class.getClassLoader().getResourceAsStream("resources/Items.txt");
			//Creates a scanner object to read the items file
		    Scanner myReader = new Scanner(myObj);
		    //While the scanner finds new lines, keep adding them to the list
		    while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        items.add(data);
		    }
		    myReader.close();
		} catch (Exception e) {
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
		return items;
	}
	
	/**
	 * @return All common items that have been generated
	 */
	public static ArrayList<Item> getCommonItems() {
		return allItems.get(0);
	}
	
	/**
	 * @return All uncommon items that have been generated
	 */
	public static ArrayList<Item> getUncommonItems() {
		return allItems.get(1);
	}
	
	/**
	 * @return All rare items that have been generated
	 */
	public static ArrayList<Item> getRareItems() {
		return allItems.get(2);
	}
	
	/**
	 * @return All legendary items that have been generated
	 */
	public static ArrayList<Item> getLegendaryItems() {
		return allItems.get(3);
	}
	
	/**
	 * 
	 * @return A completely random item from the items that have been generated
	 */
	public static Item getRandomItem() {
		return getRandomItem(0);
	}
	
	/**
	 * Finds a rarity based on luck and then returns an item within that rarity 
	 * @param luck
	 * @return Item
	 */
	public static Item getRandomItem(int luck) {
		ArrayList<Item> itemList = getRandomItems(luck);
		return itemList.get((int) (Math.random() * itemList.size()));
	}
	
	/**
	 * Finds the rarity of item to be returned based on the luck value that is passed in
	 * @param luck
	 * @return An ArrayList of items with a certain rarity
	 */
	public static ArrayList<Item> getRandomItems(int luck){
		int chance = (int) (Math.random() * 20) + luck + 1;
		if (chance >= Rarity.LEGENDARY.getChanceModifier()) {
			return getLegendaryItems();
		} else if(chance >= Rarity.RARE.getChanceModifier()){
			return getRareItems();
		} else if(chance >= Rarity.UNCOMMON.getChanceModifier()){
			return getUncommonItems();
		} else {
			return getCommonItems();
		}
	}
	
	/**
	 * Combines all the items into one ArrayList
	 * @return ArrayList of all items
	 */
	public static ArrayList<Item> getItems() {
		ArrayList<Item> items = new ArrayList<Item>();
		items.addAll(getCommonItems());
		items.addAll(getUncommonItems());
		items.addAll(getRareItems());
		items.addAll(getLegendaryItems());
		return items;
	}
	
	/**
	 * Prints the items and waits for the player to continue
	 */
	public void viewItem() {
		System.out.println(this);
		Game.pause();
	}
	
	/**
	 * Gets the price of an item when buying or selling depending on where the item 
	 * was bought and the stores personal modifier
	 * @param priceModifier
	 * @param currentLocation
	 * @return The integer price of an item
	 */
	public int getPrice(double priceModifier, Island currentLocation) {
		double distanceModifier = (Island.getDistance(locationPurchased, currentLocation) * 0.2) + 1;
		switch(getRarity()) {
		case COMMON:
			return (int) (getBasePrice() * priceModifier * distanceModifier);
		case UNCOMMON:
			return (int) (getBasePrice() * priceModifier * 1.2 * distanceModifier);
		case RARE:
			return (int) (getBasePrice() * priceModifier * 1.5 * distanceModifier);
		case LEGENDARY:
			return (int) (getBasePrice() * priceModifier * 2 * distanceModifier);
		default:
			return getBasePrice();
		}
	}
	
	/**
	 * Saves the amount that you bought the item for.
	 * @param cost
	 */
	public void setPurchaseCost(int cost) {
		purchaseCost = cost;
	}
	
	/**
	 * @return The amount of money that the item cost
	 */
	public int getPurchaseCost() {
		return purchaseCost;
	}

}
