package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import deckGame.Island;
import deckGame.Item;
import deckGame.Player;
import deckGame.Card;
import deckGame.Cargo;
import deckGame.Display;
import deckGame.EndGameException;
import deckGame.Game;
import enums.Rarity;
import enums.Stats;
import enums.Statuses;

class PlayerTest {
	private Island island;
	private Player player;
	private Game game;
	private final PrintStream systemOut = System.out;
	private ByteArrayOutputStream testOut;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Item.generateItems();
	}

	@BeforeEach
	void setUp() throws Exception {
		testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
		
		island = new Island("Island1", 5, 5, 0);
		//userName, shipName, health, speed, capacity, power, gold, crew, location
		player = new Player("Tester", "Test ship", 100, 10, 4, 4, 3, 75, 10, island, null);
	}

	@Test
	void testGetDestroyed() {
		try {
			player.damage(100);
			fail("Should end the game");
		}catch (EndGameException e){
		}
	}

	@Test
	void testAddItemCargo() {
		Cargo testCargo = new Cargo("Test cargo", "Cargo to be used for testing", 1, 50, Rarity.COMMON);
		player.addItem(testCargo);
		assertEquals(1, player.getCargoStored());
		assertTrue(player.getInventory().contains(testCargo));
	}
	
	@Test
	void testAddItemCargoModifier() {
		Cargo testCargo = new Cargo("Test item", "Item to be used for testing", 1, 50, Rarity.COMMON);
		testCargo.changeModifier(Stats.SPEED, 5);
		player.addItem(testCargo);
		assertEquals(1, player.getCargoStored());
		assertTrue(player.getInventory().contains(testCargo));
		assertEquals(15, player.getSpeed());
	}

	@Test
	void testAddItemCard() {
		Card testCard = new Card("Test card", "Card to be used for testing", 1, 50, Rarity.COMMON);
		player.addItem(testCard);
		assertTrue(player.getCards().contains(testCard));
	}
	
	@Test
	void testAddItemCardOrder() {
		Card testCardTransform = new Card("Test card", "Card to be used for testing", 1, 50, Rarity.COMMON);
		testCardTransform.makeTransform(1, 5);
		player.addItem(testCardTransform);
		
		Card testCardMultiply = new Card("Test card", "Card to be used for testing", 1, 50, Rarity.COMMON);
		testCardMultiply.makeDamageMultiplier(2, 1.3, 1);
		player.addItem(testCardMultiply);
		
		Card testCardDiceAdd = new Card("Test card", "Card to be used for testing", 1, 50, Rarity.COMMON);
		testCardDiceAdd.makeDiceAdder(1, 1, 1);
		player.addItem(testCardDiceAdd);
		
		assertTrue(player.getCards().contains(testCardTransform));
		assertTrue(player.getCards().contains(testCardMultiply));
		assertTrue(player.getCards().contains(testCardDiceAdd));
		
		assertEquals(player.getCards().get(0), testCardDiceAdd);
		assertEquals(player.getCards().get(1), testCardTransform);
		assertEquals(player.getCards().get(2), testCardMultiply);
	}
	
	@Test
	void testAddItemCargoOverStorage() {
		Cargo testCargo = new Cargo("Test cargo", "Cargo to be used for testing", 5, 50, Rarity.COMMON);
		player.addItem(testCargo);
		assertEquals(0, player.getCargoStored());
		assertFalse(player.getInventory().contains(testCargo));
	}
	
	@Test
	void testAddItemCargoExact() {
		Cargo testCargo = new Cargo("Test cargo", "Cargo to be used for testing", 4, 50, Rarity.COMMON);
		player.addItem(testCargo);
		assertEquals(4, player.getCargoStored());
		assertTrue(player.getInventory().contains(testCargo));
	}
	
	@Test
	void testremoveItemCargo() {
		Cargo testCargo = new Cargo("Test cargo", "Cargo to be used for testing", 4, 50, Rarity.COMMON);
		
		player.addItem(testCargo);
		player.removeItem(testCargo);
		
		
		assertEquals(0, player.getCargoStored());
		assertFalse(player.getInventory().contains(testCargo));
	}
	
	@Test
	void testremoveItemNonexistantCargo() {
		Cargo testCargo = new Cargo("Test cargo", "Cargo to be used for testing", 4, 50, Rarity.COMMON);

		player.removeItem(testCargo);
		
		assertEquals(0, player.getCargoStored());
		assertFalse(player.getInventory().contains(testCargo));
	}
	
	@Test
	void testremoveItemCargomodifier() {
		Cargo testCargo = new Cargo("Test cargo", "Cargo to be used for testing", 4, 50, Rarity.COMMON);
		
		testCargo.changeModifier(Stats.SPEED, 5);
		player.addItem(testCargo);
		player.removeItem(testCargo);
		
		assertEquals(0, player.getCargoStored());
		assertFalse(player.getInventory().contains(testCargo));
		assertEquals(10, player.getSpeed());
	}
	
	@Test
	void testViewInventory() {
		String input = "2" + System.lineSeparator() + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		String expected = "There is currently 1 item on the ship:" + System.lineSeparator()
		+ "1: Test cargo" + System.lineSeparator()
		+ "2: Return";
		Cargo testCargo = new Cargo("Test cargo", "Cargo to be used for testing", 1, 50, Rarity.COMMON);
		player.addItem(testCargo);
		
		player.viewInventory();
		assertTrue(testOut.toString().contains(expected));
	}
	
	@Test
	void testViewNoInventory() {
		String input = "1" + System.lineSeparator() + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		String expected = "There are currently 0 items on the ship:" + System.lineSeparator()
		+ "Press enter to continue";
		
		player.viewInventory();
		assertTrue(testOut.toString().contains(expected));
	}
	
	@Test
	void testViewInvalidInventory() {
		String input = "3" + System.lineSeparator() + System.lineSeparator() + "2" + System.lineSeparator()
		+ System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		String expected = "There is currently 1 item on the ship:" + System.lineSeparator()
		+ "1: Test cargo" + System.lineSeparator()
		+ "2: Return";
		Cargo testCargo = new Cargo("Test cargo", "Cargo to be used for testing", 1, 50, Rarity.COMMON);
		player.addItem(testCargo);
		
		String expected2 = "Please enter a number between 1 and 2." + System.lineSeparator();

		
		player.viewInventory();
		assertTrue(testOut.toString().contains(expected));
		assertTrue(testOut.toString().contains(expected2));
	}
	
	@Test
	void testViewItemInventory() {
		String input = "1" + System.lineSeparator() + System.lineSeparator() + "2" + System.lineSeparator()
		+ System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		String expected = "There is currently 1 item on the ship:" + System.lineSeparator()
		+ "1: Test cargo" + System.lineSeparator()
		+ "2: Return";
		Cargo testCargo = new Cargo("Test cargo", "Cargo to be used for testing", 1, 50, Rarity.COMMON);
		player.addItem(testCargo);
		
		String expected2 = "Cargo: Test cargo\nRarity: COMMON\nSize: 1";

		
		player.viewInventory();
		assertTrue(testOut.toString().contains(expected));
		assertTrue(testOut.toString().contains(expected2));
	}
	
	@Test
	void testViewCards() {
		String input = "2" + System.lineSeparator() + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		String expected = "There is currently 1 card in your deck:" + System.lineSeparator()
		+ "1: Test card" + System.lineSeparator()
		+ "2: Return";
		Card testCard = new Card("Test card", "Card to be used for testing", 1, 50, Rarity.COMMON);
		player.addItem(testCard);
		
		player.viewCards();
		System.out.println(testOut);
		assertTrue(testOut.toString().contains(expected));
	}
	
	@Test
	void testViewNoCards() {
		String input = "1" + System.lineSeparator() + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		String expected = "There are currently 0 cards in your deck:" + System.lineSeparator()
		+ "Press enter to continue";
		
		
		player.viewCards();
		System.out.println(testOut);
		assertTrue(testOut.toString().contains(expected));
	}
	
	@Test
	void testPayCrewCMD() {
		String input = "1" + System.lineSeparator() + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		player.payCrewCMD(5);
		
		assertEquals(25, player.getGold());
	}
	
	@Test
	void testPayCrewCMDPoor() {
		String input = "1" + System.lineSeparator() + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		player.modifyGold(-player.getGold());
		
		assertFalse(player.payCrewCMD(5));
	}
	
	@Test
	void testDump() {
		String input = System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		Item item = Item.getItems().get(0);
		player.modifyCapacity(10);
		player.addItem(item);
		player.dump(item, 5);
		
		String expected = "Dump successful. Bread has been removed from your ship.";
		
		assertEquals(0, player.getCargoStored());
		assertTrue(testOut.toString().contains(expected));
	}
}
