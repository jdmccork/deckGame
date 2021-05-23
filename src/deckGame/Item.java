package deckGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import enums.ItemType;
import enums.Rarity;
import enums.Stats;

public abstract class Item {
	
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
	
	private Island locationPurchased;
	
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
		String output;
		if (this instanceof Cargo) {
			output = "Cargo: ";
		} else {
			output = "Card: ";
		}
		 output += name + "\nRarity: " + rarity
				+ "\nSize: " + size + "\nDescription: " + description + "\n";
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
	public int getPrice() {
		return basePrice;
	}
	
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
	
	public static void generateItems() {
		//TODO This can be changed to conform with the builder patterns we are currently doing in the lectures
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
					item = new Cargo(name, description, size, basePrice, rarity);
					while (index < parts.size()-1) {
						if (parts.get(index).equals("Stat")) {
							((Cargo) item).changeModifier(Stats.valueOf(parts.get(index + 1)), Integer.parseInt(parts.get(index + 3)));
							index += 4;
						} else {
							throw new Exception("Variable doesn't exist");
						}
					}
				}
				else if(parts.get(1).equals("Card")){ 
					item = new Card(name, description, size, basePrice, rarity);
					while (index < parts.size()-1) {
						if (parts.get(index + 1).equals("multi-transform")) {
							((Card) item).makeMultiTransform(Integer.parseInt(parts.get(index + 3)),
									Integer.parseInt(parts.get(index + 5)), Integer.parseInt(parts.get(index + 7)));
							index += 8;
						} else if (parts.get(index + 1).equals("moreDice")) {
							((Card) item).makeDiceAdder(Integer.parseInt(parts.get(index + 3)),
									Integer.parseInt(parts.get(index + 5)), Integer.parseInt(parts.get(index + 7)));
							index += 8;
						}
					}
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
	private static ArrayList<String> readItems() {
		ArrayList<String> items = new ArrayList<String>();
		try {
			//Defines the items file as a new file to read
			File myObj = new File("src/resources/Items");
			//Creates a scanner object to read the items file
		    Scanner myReader = new Scanner(myObj);
		    //While the scanner finds new lines, keep adding them to the list
		    while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        items.add(data);
		    }
		    myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
		return items;
	}
	
	public static ArrayList<Item> getCommonItems() {
		return allItems.get(0);
	}
	
	public static ArrayList<Item> getUncommonItems() {
		return allItems.get(1);
	}
	
	public static ArrayList<Item> getRareItems() {
		return allItems.get(2);
	}
	
	public static ArrayList<Item> getLegendaryItems() {
		return allItems.get(3);
	}
	
	public static Item getRandomItem() {
		return getRandomItem(0);
	}
	
	public static Item getRandomItem(int luck) {
		ArrayList<Item> itemList = getRandomItems(luck);
		return itemList.get((int) (Math.random() * itemList.size()));
	}
	
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
	
	public static ArrayList<Item> getItems() {
		ArrayList<Item> items = new ArrayList<Item>();
		items.addAll(getCommonItems());
		items.addAll(getUncommonItems());
		items.addAll(getRareItems());
		items.addAll(getLegendaryItems());
		return items;
	}
	
	public void viewItem() {
		System.out.println(this);
		Game.pause();
	}
	
	public int getPrice(double priceModifier, Island currentLocation/*double buySellModifier*/) {
		double distanceModifier = (Island.getDistance(locationPurchased, currentLocation) * 0.2) + 1;
		switch(getRarity()) {
		case COMMON:
			return (int) (getPrice() * priceModifier * distanceModifier);
		case UNCOMMON:
			return (int) (getPrice() * priceModifier * 1.2 * distanceModifier);
		case RARE:
			return (int) (getPrice() * priceModifier * 1.5 * distanceModifier);
		case LEGENDARY:
			return (int) (getPrice() * priceModifier * 2 * distanceModifier);
		default:
			return getPrice();
		}
	}
	
	public static Item getItem(String itemName) {
		for (Item item: getItems()) {
			if (item.getName() == itemName) {
				return item;
			}
		}
		return null;
	}

}
