package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import deckGame.Cargo;
import deckGame.Item;
import enums.Rarity;
import enums.Stats;

class ItemTest {
	static ArrayList<Item> commonItems;
	static ArrayList<Item> uncommonItems;
	static ArrayList<Item> rareItems;
	static ArrayList<Item> legendaryItems;
	static ArrayList<Item> allItems;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Item.generateItems();
		commonItems = Item.getCommonItems();
		uncommonItems = Item.getUncommonItems();
		rareItems = Item.getRareItems();
		legendaryItems = Item.getLegendaryItems();
		allItems = Item.getItems();
	}

	@BeforeEach
	void setUp() throws Exception {
		;
	}
	
	@Test
	void testGetItems() {
		int commonCount = 0;
		int uncommonCount = 0;
		int rareCount = 0;
		int legendaryCount = 0;
		for (Item item : commonItems) {
			assertTrue(allItems.contains(item));
		}
		for (Item item : uncommonItems) {
			assertTrue(allItems.contains(item));
		}
		for (Item item : rareItems) {
			assertTrue(allItems.contains(item));
		}
		for (Item item : legendaryItems) {
			assertTrue(allItems.contains(item));
		}
	}

	@Test
	void testGetCommonItems() {
		for (Item item: Item.getCommonItems()) {
			assertEquals(Rarity.COMMON, item.getRarity());
		}
	}

	@Test
	void testGetUncommonItems() {
		for (Item item: Item.getUncommonItems()) {
			assertEquals(Rarity.UNCOMMON, item.getRarity());
		}
	}

	@Test
	void testGetRareItems() {
		for (Item item: Item.getRareItems()) {
			assertEquals(Rarity.RARE, item.getRarity());
		}
	}

	@Test
	void testGetLegendaryItems() {
		for (Item item: Item.getLegendaryItems()) {
			assertEquals(item.getRarity(), Rarity.LEGENDARY);
		}
	}

	@Test
	void testGetRandomItem() {
		int commonCount = 0;
		int uncommonCount = 0;
		int rareCount = 0;
		int legendaryCount = 0;
		for (int i = 0; i<1000; i++) {
			Item item = Item.getRandomItem();
			if (item instanceof Cargo){
				assertTrue(allItems.contains(item));
			} else {
				fail("Tests for other item types have not been implemented");
			}
			switch(item.getRarity()) {
			case COMMON:
				commonCount++;
				break;
			case UNCOMMON:
				uncommonCount++;
				break;
			case RARE:
				rareCount++;
				break;
			case LEGENDARY:
				legendaryCount++;
				break;
			default:
				fail("Item not found");
			}
		}
		
		System.out.println(commonCount + " " + uncommonCount + " " + rareCount + " " + legendaryCount);
	}

}
