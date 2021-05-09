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
	
	public static void generateItems() {
		ArrayList<Item> common = new ArrayList<Item>();
		ArrayList<Item> uncommon = new ArrayList<Item>();
		ArrayList<Item> rare = new ArrayList<Item>();
		ArrayList<Item> legendary = new ArrayList<Item>();
		
		ArrayList<String> items = readItems();
		for(String item : items) {
			String[] temp = item.split(" #");
			ArrayList<String> parts = new ArrayList<String>();
			for(String part: temp) {
				String[] values = part.split(": ");
				parts.add(values[1]);
			}
			if(parts.get(0).equals("Cargo")) {
				Cargo cargo;//what's this needed for?
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
			allItems.add(common);
			allItems.add(uncommon);
			allItems.add(rare);
			allItems.add(legendary);
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
		return allItems.get(0);
	}
	
	public static ArrayList<Item> getRareItems() {
		return allItems.get(0);
	}
	
	public static ArrayList<Item> getLegendaryItems() {
		return allItems.get(0);
	}
	
	public static ArrayList<Item> getRandomItems(int luck){
		int chance = (int) (Math.random() * 20) + luck + 1;
		System.out.println(chance);
		int possible;
		System.out.println(chance > Rarity.LEGENDARY.getChanceModifier());
		if (chance > Rarity.LEGENDARY.getChanceModifier()) {
			possible = 4;
		} else if(chance > Rarity.RARE.getChanceModifier()){
			possible = 3;
		} else if(chance > Rarity.UNCOMMON.getChanceModifier()){
			possible = 2;
		} else {
			possible = 1;
		}
		return allItems.get((int) (possible * Math.random()));
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
		System.out.println("Select an item to see to continue");
		System.out.println("1: Return");
		switch (Game.getInt()) {
		case 1:
			return;
		default:
			System.out.println("Please enter a number between 1 and 2");
			break;
		}
	}
}
