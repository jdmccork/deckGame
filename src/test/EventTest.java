package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import deckGame.Display;
import deckGame.Event;
import deckGame.Game;
import deckGame.Island;
import deckGame.Item;
import deckGame.Player;
import deckGame.Ship;

class EventTest {
	private Event event;
	private ArrayList<Island> islands;
	private Player player;
	private Ship enemy;
	private final PrintStream systemOut = System.out;
	private ByteArrayOutputStream testOut;
	private int testNum = 100;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Game game = new Game();
		Item.generateItems();
	}

	@BeforeEach
	void setUp() throws Exception {
		testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
		islands = new ArrayList<Island>();
		int [] eventArray = new int[] {4, 3, 2, 1};
		event = new Event(eventArray);
		Island island1 = new Island("island1", 0, 0, 0);
		Island island2 = new Island("Island2", 5, 5, 0);
		player = new Player("Test", "Tester", 100, 1, 1, 5, 4, 1, 1, island1, new Display());
		enemy = new Ship("Jolly Rodgers", 1, 10, 3);
		islands.add(island1);
		islands.add(island2);
	}
	
	@Test
	void testEventCreation() {
		ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 2, 2, 2, 3, 3, 4));
		
		int [] eventArray = new int[] {4, 3, 2, 1};
		event = new Event(eventArray);
		
		assertEquals(event.getEventChance(), expected);
	}
	
	@Test
	void testNothing() {
		event.setChance(new ArrayList<>(Arrays.asList(1)));
		String input = System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		String expected = "The day passes uneventfully.";
		
		event.selectEvent(player, 0, null);
		assertTrue(testOut.toString().contains(expected));
	}
	
	@Test
	void testStormSelection() {
		String input = System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
	
		event.setChance(new ArrayList<>(Arrays.asList(1)));
		player.modifyLuck(-50);
		event.selectEvent(player, 0, null);
	}
	
	@Test
	void testRescueSelection() {
		String input = System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
	
		event.setChance(new ArrayList<>(Arrays.asList(1)));
		player.modifyLuck(-50);
		event.selectEvent(player, 0, null);
	}

	@Test
	void testFight() {
		String input = "1" + System.lineSeparator();
	}
	
	@Test
	void testFleeSuccess() {
		for (int i = 0; i < testNum; i++) {
			player = new Player("Test", "Tester", 100, 25, 1, 4, 0, 1, 1, islands.get(0), null);
			enemy = new Ship("Jolly Rodgers", 1, 0, 3);
			
			assertTrue(event.flee(enemy, player));
		}
	}
	
	@Test
	void testFleeFail() {
		String input = "";
		for (int i = 0; i < testNum; i++) {
			input += System.lineSeparator() + System.lineSeparator() + System.lineSeparator();
		}
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		for (int i = 0; i < testNum; i++) {
			player = new Player("Test", "Tester", 100, 0, 1, 0, 4, 1, 1, islands.get(0), null);
			enemy = new Ship("Jolly Rodgers", 1, 25, 3);
			
			assertFalse(event.flee(enemy, player));
		}
	}

	@Test
	void testStorm() {
		String input = System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		player.modifyLuck(-50);
		event.storm(player, 1);
		assertTrue(player.getHealth() < player.getMaxHealth());
	}

	@Test
	void testRewardSuccess() {
		String input = "";
		for (int i = 0; i < testNum; i++) {
			input += "2" + System.lineSeparator();
		}
		
		for (int i = 0; i < testNum; i++) {
			input += System.lineSeparator() + System.lineSeparator();
		}
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		for (int i = 0; i < testNum; i++) {
			player = new Player("Test", "Tester", 100, 0, 10, 4, 0, 1, 1, islands.get(0), null);
			player.modifyLuck(20);
			
			assertTrue(event.reward(player, 0, 1));
		}
	}
	
	@Test
	void testRewardFail() {
		String input = "";
		for (int i = 0; i < testNum; i++) {
			input += "2" + System.lineSeparator();
		}
		
		for (int i = 0; i < testNum; i++) {
			input += System.lineSeparator() + System.lineSeparator();
		}
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		for (int i = 0; i < testNum; i++) {
			player = new Player("Test", "Tester", 100, 0, 10, 0, 4, 1, 1, islands.get(0), null);
			player.modifyLuck(-20);
			
			assertFalse(event.reward(player, 0, 1));
		}
	}
	
	@Test
	void testRescue() {
		player = new Player("Test", "Tester", 100, 0, 10, 0, 4, 100, 1, islands.get(0), null);
		event.rescue(player, 1);
		String expected = "1: Rescued sailors reciving $";
		
		assertTrue(player.getGold() > 100);
		player.getLogbook().viewEntries();
		
		assertTrue(testOut.toString().contains(expected));
		}
	
	@Test
	void testSurrenderWItems() {
		String input = "3" + System.lineSeparator() 
		+ "1" + System.lineSeparator() 
		+ System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		player.addItem(Item.getItems().get(0));
		
		String expected = "Are you sure you want to surrender a random piece of cargo?" + System.lineSeparator()
				+ "1: Yes" + System.lineSeparator()
				+ "2: No" + System.lineSeparator()
				+ "The pirates took your Bread and allowed you to escape with your lives." + System.lineSeparator()
				+ "Press enter to continue";
		
		event.setChance(new ArrayList<>(Arrays.asList(2)));
		event.selectEvent(player, 0, null);
		
		assertEquals(0, player.getInventory().size());
		assertTrue(testOut.toString().contains(expected));
	}
	
	@Test
	void testSurrenderWNoItems() {
		String input = "3" + System.lineSeparator() 
		+ System.lineSeparator()
		+ "2" + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		player.modifySpeed(500);
		
		String expected = "You have no cargo? Then pay with your life!" + System.lineSeparator()
				+ "Press enter to continue" + System.lineSeparator()
				+ "1: Fight";
		
		event.setChance(new ArrayList<>(Arrays.asList(2)));
		event.selectEvent(player, 0, null);
		
		assertTrue(testOut.toString().contains(expected));
	}

	@Test
	void testAttack() {
		player.setMaxHealth(500);
		enemy.setMaxHealth(500);
		player.repair();
		enemy.repair();
		
		event.attack(enemy, player);

		assertTrue(enemy.getHealth() < enemy.getMaxHealth());
		assertTrue(player.getHealth() < player.getMaxHealth());
	}
	
	@Test
	void testDumping() {
		
	}
	
	@Test
	void testRewardAccept() {
		String input = "1" + System.lineSeparator() 
		+ System.lineSeparator()
		+ "2" + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		player = new Player("Test", "Tester", 100, 0, 500, 4, 0, 1, 1, islands.get(0), null);
		player.modifyLuck(20);
		
		event.reward(player, 0, 1);
		assertEquals(1, player.getInventory().size());
	}
}
