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
		String output = "Item: " + name + "\nRarity: " + rarity
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
	
	public static void generateItems() {
		//TODO This can be changed to conform with the builder patterns we are currently doing in the lectures
		ArrayList<Item> common = new ArrayList<Item>();
		ArrayList<Item> uncommon = new ArrayList<Item>();
		ArrayList<Item> rare = new ArrayList<Item>();
		ArrayList<Item> legendary = new ArrayList<Item>();
		
		ArrayList<String> items = readItems();
		for(String item : items) {
			try {
				String[] temp = item.split(" #");
				ArrayList<String> parts = new ArrayList<String>();
				for(String part: temp) {
					String[] values = part.split(": ");
					parts.add(values[1]);
				}
				if(parts.get(0).equals("Cargo")) {
					Cargo cargo;
					String name = parts.get(1);
					String description = parts.get(2);
					int size = Integer.parseInt(parts.get(3));
					int basePrice = Integer.parseInt(parts.get(4));
					Rarity rarity = Rarity.valueOf(parts.get(5));
					if(parts.size() == 8) {
						Stats stat = Stats.valueOf(parts.get(6));
						int statAmount = Integer.parseInt(parts.get(7));
						cargo = new Cargo(name, description, size, basePrice, rarity, stat, statAmount);
					} else if (parts.size() == 9){
						//handling the special cases, note, all special will probably be percentages
						Stats stat = Stats.valueOf(parts.get(6));
						int statAmount = Integer.parseInt(parts.get(7));
						cargo = new Cargo(name, description, size, basePrice, rarity, stat, statAmount);
					} else {
						cargo = new Cargo(name, description, size, basePrice, rarity);
					}
					switch (cargo.getRarity()) {
					case COMMON:
						common.add(cargo);
						break;
					case UNCOMMON:
						uncommon.add(cargo);
						break;
					case RARE:					
						rare.add(cargo);
						break;
					case LEGENDARY:
						legendary.add(cargo);
						break;
					}
				}
			}catch(Exception e){
				;
			}
			/*else { //To be implemented once cards are implemented
			String name = parts.get(1);
			String description = parts.get(2);
			int size = Integer.parseInt(parts.get(3));
			int basePrice = Integer.parseInt(parts.get(4));
			Rarity rarity = Rarity.valueOf(parts.get(5));
			Stats stat = Stats.valueOf(parts.get(6));
			int statAmount = Integer.parseInt(parts.get(7));
			Card card = new Cargo(name, description, size, basePrice, rarity, stat, statAmount);
			}*/
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
	
	public int getPrice(double priceModifier/*double buySellModifier*/) {
		double timeModifier = (getDaysPassed(Game.getGame().getCurrentDay()) * 0.2) + 1;
		switch(getRarity()) {
		case COMMON:
			return (int) (getPrice() * priceModifier * timeModifier);
		case UNCOMMON:
			return (int) (getPrice() * priceModifier * 1.2 * timeModifier);
		case RARE:
			return (int) (getPrice() * priceModifier * 1.5 * timeModifier);
		case LEGENDARY:
			return (int) (getPrice() * priceModifier * 2 * timeModifier);
		default:
			return getPrice();
		}
	}

}
