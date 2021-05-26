package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import deckGame.Game;
import deckGame.Island;
import deckGame.Item;
import deckGame.Player;
import deckGame.Store;

class StoreTest {
	private int expectedAdviceCount = 30;
	static ArrayList<Island> islands = new ArrayList<Island>();
	private Player player;
	private Store store;
	private final PrintStream systemOut = System.out;
	private ByteArrayOutputStream testOut;
	
	private void setStock() {
		store.addStock(Item.getCommonItems().get(0));
		store.addStock(Item.getUncommonItems().get(0));
		store.addStock(Item.getRareItems().get(0));
		store.addStock(Item.getLegendaryItems().get(0));
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Item.generateItems();
		islands.add(new Island("Home", 0, 0, 0));
		islands.add(new Island("Golgolles", -10, 5, 0));
		islands.add(new Island("Cansburg", 5, 5, 0));
		islands.add(new Island("Tisjour", -5, -5, 0));
		islands.add(new Island("Brighdown", 5, -5, 0));
	}

	@BeforeEach
	void setUp() throws Exception {
		testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
		player = new Player("Tester", "Testing ship", 100, 2, 4, 4, 3, 25, 10, islands.get(0), null);
		store = new Store(islands.get(0));
	}

	@Test
	void generateStockTest() {
		store.generateStock(player);
		for (Item item: store.getStock()) {
			assertTrue(Item.getItems().contains(item));
		}
	}
	
	@Test
	void addStockTest() {
		store.addStock(Item.getCommonItems().get(0));
		assertEquals(store.getStock().get(0).getName(), "Bread");
	}
	
	@Test
	void removeStockTest() {
		setStock();
		store.removeStock(Item.getRareItems().get(0));
	}
	
	@Test
	void printStockTest() {
		setStock();
		store.printStock();
		String expected = "This store has 4 items in stock:\r\n"
				+ "1: Bread\r\n"
				+ "2: Rainbow Bread\r\n"
				+ "3: Box of Horseshoes\r\n"
				+ "4: Box of Four-Leaved Clovers";
		assertTrue(testOut.toString().contains(expected));
	}
	
	@Test
	/**
	 * This throws an error if run with other tests as advice is created before it reaches this test
	 */
	void getAdviceTest() {
		if (Store.getAdvice().size() == expectedAdviceCount) {
			assert(true);
		} else {
			Store.readAdvice();
			assertEquals(expectedAdviceCount, Store.getAdvice().size());
		}
	}
	
	@Test
	void testInteractBuy() {
		String input = "1" + System.lineSeparator() 
		+ "5" + System.lineSeparator() 
		+ "3" + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		String expected = "Select an option to continue" + System.lineSeparator()
				+ "1: Buy" + System.lineSeparator()
				+ "2: Sell" + System.lineSeparator()
				+ "3: Exit shop";
		String expected2 = "This store has 4 items in stock:" + System.lineSeparator()
				+ "1: Bread" + System.lineSeparator()
				+ "2: Rainbow Bread" + System.lineSeparator()
				+ "3: Box of Horseshoes" + System.lineSeparator()
				+ "4: Box of Four-Leaved Clovers" + System.lineSeparator()
				+ "5: Return" + System.lineSeparator()
				+ "Select an item to view more information";
		
		setStock();
		
		store.interact(player, 0);
		assertTrue(testOut.toString().contains(expected));
		assertTrue(testOut.toString().contains(expected2));
	}
	
	@Test
	void testInteractSell() {
		String input = "2" + System.lineSeparator() 
		+ "1" + System.lineSeparator() 
		+ "3" + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		String expected = "Select an option to continue" + System.lineSeparator()
				+ "1: Buy" + System.lineSeparator()
				+ "2: Sell" + System.lineSeparator()
				+ "3: Exit shop";
		String expected2 = "Select an item to sell." + System.lineSeparator()
				+ "There are currently 0 items on the ship:" + System.lineSeparator()
				+ "There are currently 0 cards in your deck:" + System.lineSeparator()
				+ "1: Return" + System.lineSeparator()
				+ "Select an item or return to continue.";

		store.interact(player, 0);
		
		assertTrue(testOut.toString().contains(expected));
		assertTrue(testOut.toString().contains(expected2));
	}
	
	@Test
	void testBuy() {
		String input = "1" + System.lineSeparator() 
		+ "1" + System.lineSeparator() 
		+ "1" + System.lineSeparator()
		+ System.lineSeparator()
		+ "4" + System.lineSeparator()
		+ "3" + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		setStock();
		
		String expected = "Cargo: Bread\n"
				+ "Rarity: COMMON\n" 
				+ "Size: 1\n"
				+ "Description: It's bread. These loaves are wholegrain.\n"
				+ "This does not affect any of your stats." + System.lineSeparator()
				+ "You currently have $25" + System.lineSeparator()
				+ "1: Buy for $5" + System.lineSeparator() 
				+ "2: Return";
		store.interact(player, 0);
		
		assertTrue(testOut.toString().contains(expected));
		assertEquals("Bread", player.getInventory().get(0).getName());
		assertEquals(20, player.getGold());
	}
	
	@Test
	void testSell() {
		String input = "2" + System.lineSeparator() 
		+ "1" + System.lineSeparator() 
		+ "1" + System.lineSeparator()
		+ System.lineSeparator()
		+ "1" + System.lineSeparator()
		+ "3" + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		Item item =  Item.getItems().get(0);
		item.setLocationPurchased(player.getLocation());
		item.setPurchaseCost(5);
		player.addItem(item);
		
		String expected = "Cargo: Bread\n"
				+ "Rarity: COMMON\n" 
				+ "Size: 1\n"
				+ "Description: It's bread. These loaves are wholegrain.\n"
				+ "Purchase Cost: $5\n"
				+ "This does not affect any of your stats." + System.lineSeparator()
				+ "You currently have $25" + System.lineSeparator()
				+ "1: Sell for $4" + System.lineSeparator() 
				+ "2: Return";
		
		String expected2 = "Select an item to sell." + System.lineSeparator()
		+ "There are currently 0 items on the ship:" + System.lineSeparator()
		+ "There are currently 0 cards in your deck:" + System.lineSeparator()
		+ "1: Return" + System.lineSeparator()
		+ "Select an item or return to continue.";
		
		store.interact(player, 0);
		
		
		
		assertEquals(0, player.getInventory().size());
		assertEquals(29, player.getGold());
		assertTrue(testOut.toString().contains(expected));
		assertTrue(testOut.toString().contains(expected2));
	}
}
