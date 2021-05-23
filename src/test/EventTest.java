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
	/*
	String input = "Test" + System.lineSeparator() + "Tester";
	InputStream in = new ByteArrayInputStream(input.getBytes());
	System.setIn(in);
	game.setTestInput();
	*/

	@BeforeEach
	void setUp() throws Exception {
		testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
		islands = new ArrayList<Island>();
		event = new Event();
		Island island1 = new Island("island1", 0, 0, 0);
		Island island2 = new Island("Island2", 5, 5, 0);
		player = new Player("Test", "Tester", 100, 1, 1, 5, 4, 1, 1, island1);
		enemy = new Ship("Jolly Rodgers", 1, 10, 3);
		islands.add(island1);
		islands.add(island2);
	}

	@Test
	void testFightLogic() {
		fail("Not yet implemented");
	}

	@Test
	void testFight() {
		fail("Not yet implemented");
	}
	
	@Test
	void testFleeSuccess() {
		for (int i = 0; i < testNum; i++) {
			player = new Player("Test", "Tester", 100, 25, 1, 4, 0, 1, 1, islands.get(0));
			enemy = new Ship("Jolly Rodgers", 1, 0, 3);
			
			assertTrue(event.flee(enemy, player));
		}
	}
	
	@Test
	void testFleeFail() {
		String input = "";
		for (int i = 0; i < testNum; i++) {
			input += System.lineSeparator() + System.lineSeparator();
		}
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		for (int i = 0; i < testNum; i++) {
			player = new Player("Test", "Tester", 100, 0, 1, 0, 4, 1, 1, islands.get(0));
			enemy = new Ship("Jolly Rodgers", 1, 25, 3);
			
			assertFalse(event.flee(enemy, player));
		}
	}

	@Test
	void testStorm() {
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
			player = new Player("Test", "Tester", 100, 0, 10, 4, 0, 1, 1, islands.get(0));
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
			player = new Player("Test", "Tester", 100, 0, 10, 0, 4, 1, 1, islands.get(0));
			player.modifyLuck(-20);
			
			assertFalse(event.reward(player, 0, 1));
		}
	}

}
