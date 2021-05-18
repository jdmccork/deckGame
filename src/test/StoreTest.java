package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
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
	private int expectedAdviceCount = 5;
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
		islands.add(new Island("Home", 0, 0));
		islands.add(new Island("Golgolles", -10, 5));
		islands.add(new Island("Cansburg", 5, 5));
		islands.add(new Island("Tisjour", -5, -5));
		islands.add(new Island("Brighdown", 5, -5));
	}

	@BeforeEach
	void setUp() throws Exception {
		testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
		player = new Player("Tester", "Testing ship", 100, 2, 4, 3, 25, islands.get(0));
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
	void getAdviceTest() {
		Store.readAdvice();
		assertEquals(Store.getAdvice().size(), expectedAdviceCount);
	}
}
