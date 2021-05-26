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
import deckGame.EndGameException;
import deckGame.Game;
import deckGame.Island;
import deckGame.Item;
import deckGame.Player;
import deckGame.Route;

class GameTest {
	Game game;
	private ByteArrayOutputStream testOut;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
		game = new Game();
	}
	
	@Test
	void testGameSetup() {
		assertTrue(Item.getItems().size() > 0);
	}
	
	@Test
	void testGetNameNormal() {
		String input = "Test" + System.lineSeparator() + "Tester";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		String[] names = game.getNamesCMD();
		assertEquals("Test", names[0]);
		assertEquals("Tester", names[1]);
	}
	
	@Test
	void testGetNameSpecial() {
		String input = ("Te$t" + System.lineSeparator() + "T3st" + System.lineSeparator() + "Te  st"
	+ System.lineSeparator() + "Test" + System.lineSeparator() + "Tester");
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		String[] names = game.getNamesCMD();
		assertEquals("Test", names[0]);
		assertEquals("Tester", names[1]);
		String expected = "Username must not contain digits or special characters."; 
		assertTrue(testOut.toString().contains(expected));
	}
	
	@Test
	void testGetNameShort() {
		String input = "T" + System.lineSeparator() + "Test" + System.lineSeparator() + "Tester";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		String[] names = game.getNamesCMD();
		assertEquals("Test", names[0]);
		assertEquals("Tester", names[1]);
		String expected = "Length of username must be between 3 and 15 characters."; 
		assertTrue(testOut.toString().contains(expected));
	}
	
	@Test
	void testGetNameLowerbound() {
		String input = "Tes" + System.lineSeparator() + "Tester";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		String[] names = game.getNamesCMD();
		assertEquals("Tes", names[0]);
		assertEquals("Tester", names[1]);
		String expected = "Length of username must be between 3 and 15 characters."; 
		assertFalse(testOut.toString().contains(expected));
	}
	
	@Test
	void testGetNameLong() {
		//Also testing allowing 1 space, if this were broken the error would go to special character first
		String input = "Testing the tester" + System.lineSeparator() + "Test" + System.lineSeparator() + "Tester";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		String[] names = game.getNamesCMD();
		assertEquals("Test", names[0]);
		assertEquals("Tester", names[1]);
		String expected = "Length of username must be between 3 and 15 characters."; 
		assertTrue(testOut.toString().contains(expected));
	}
	
	@Test
	void testGetNameUpperbound() {
		String input = "JackMcorkindale" + System.lineSeparator() + "Tester";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		String[] names = game.getNamesCMD();
		assertEquals("JackMcorkindale", names[0]);
		assertEquals("Tester", names[1]);
		String expected = "Length of username must be between 3 and 15 characters."; 
		assertFalse(testOut.toString().contains(expected));
	}
	
	@Test
	void testMainMenuQuit() {
		String input = "2" + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		game.mainMenu();
		String expected = "Thanks for playing. Goodbye.";
		assertTrue(testOut.toString().contains(expected));
	}
	
	@Test
	void testGenerateIslands() {
		game.generateIslands();
		assertEquals(game.getIslands().size(), 5);
	}

	@Test
	void testGenerateRoutes() {
		ArrayList<Island> islands = new ArrayList<Island>();
		Island test1 = new Island("Test1", 0, 0, 0);
		Island test2 = new Island("Test2", 0, 0, 0);
		Island test3 = new Island("Test3", 0, 0, 0);
		Island test4 = new Island("Test4", 0, 0, 0);
		Island test5 = new Island("Test5", 0, 0, 0);
		islands.add(test1);
		islands.add(test2);
		islands.add(test3);
		islands.add(test4);
		islands.add(test5);
		
		game.generateAllRoutes(islands);
		
		ArrayList<Route> island1routes = test1.getRoutes();
		for (Route route: island1routes) {
			assertTrue(islands.contains(route.getDestination()));
		}
		assertEquals(4, island1routes.size());
	}

		
	@Test
	void testGetInt() {
		for (int i = 0; i < 100; i++) {
			String input = Integer.toString(i) + System.lineSeparator();
			InputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			Game.setTestInput();
			assertEquals(i, Game.getInt());
		}
	}
	
	@Test
	void testSessionSetup() {
		game.gameSetup();
		game.sessionSetup("Tester", "Test ship", 25, "3");
		assertEquals("Tester",game.getPlayer().getUserName());
	}
	
	@Test
	void testChargeRepairFull() {
		game.gameSetup();
		game.sessionSetup("Tester", "Test ship", 25, "3");
		int gold = game.getPlayer().getGold();
		assertTrue(game.chargeRepair());
		assertEquals(gold, game.getPlayer().getGold());
	}
	
	@Test
	void testChargeRepairDamaged() {
		String input = System.lineSeparator() + 1 + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		game.gameSetup();
		game.sessionSetup("Tester", "Test ship", 25, "3");
		game.getPlayer().damage(20);
		int gold = game.getPlayer().getGold();
		assertTrue(game.chargeRepair());
		assertEquals(game.getPlayer().getMaxHealth(), game.getPlayer().getHealth());
		assertEquals(gold - 4, game.getPlayer().getGold());
	}
	
	@Test
	void testChargeRepairNoItems() {
		String input = 1 + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		game.gameSetup();
		game.sessionSetup("Tester", "Test ship", 25, "3");
		game.getPlayer().damage(20);
		int gold = game.getPlayer().getGold();
		game.getPlayer().modifyGold(-gold);
		try {
			game.chargeRepair();
			fail("Didn't end the game");
		} catch (EndGameException e){
		}
	}
	
	@Test
	void testExecuteSail() {
		String expected = "You travelled for 4 days and have arrived at Island: Golgolles";
		String input = System.lineSeparator()
			+ System.lineSeparator()
			+ System.lineSeparator()
			+ System.lineSeparator()
			+ System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		game.gameSetup();
		game.sessionSetup("Tester", "Test ship", 25, "3");
		Route route = game.getPlayer().getLocation().getRoutes().get(0);
		route.getEvent().setChance(new ArrayList<Integer>(Arrays.asList(1)));
		
		game.executeSail(route);
		assertFalse(game.getPlayer().getLocation() == game.getIslands().get(0));
		assertTrue(testOut.toString().contains(expected));
	}
	
	@Test
	void testEndGame() {
		String expected = "Total gold earned: 255" + System.lineSeparator()
			+ "Days survived: 10/20" + System.lineSeparator()
			+ "Total score: 255";
		
		game.createPlayer("Test", "Test ship", "1");
		
		game.setCurrentDay(10);
		game.setDayLimit(20);
		
		game.getPlayer().addItem(Item.getItems().get(0));
		
		game.endGame();
		
		assertTrue(testOut.toString().contains(expected));
	}
	
	@Test
	void testCreatePlayer() {
		String input = "Tester" + System.lineSeparator()
			+ "Test ship" + System.lineSeparator()
			+ 1 + System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		String[] names = game.getNamesCMD();
		Player player = game.createPlayer(names[0], names[1], game.getShipCMD());
		
		
		assertEquals(250, player.getGold());
		assertEquals(10, player.getNumCrew());
		
		assertEquals(player, game.getPlayer());
	}
	
	@Test
	void testGameEndingTravel() {
		String input = System.lineSeparator()
			+ System.lineSeparator()
			+ System.lineSeparator()
			+ System.lineSeparator()
			+ System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		
		game.gameSetup();
		game.sessionSetup("Tester", "Test ship", 25, "3");
		Route route = game.getPlayer().getLocation().getRoutes().get(0);
		route.getEvent().setChance(new ArrayList<Integer>(Arrays.asList(1)));
		
		game.setCurrentDay(19);
		game.setDayLimit(20);
		try {
			game.executeSail(route);
			fail("Game didn't end");
		}catch (EndGameException e) {
			assertFalse(game.getPlayer().getLocation() == game.getIslands().get(0));
		}
	}
	
	@Test
	void testSelectRoute() {
		String expected1 = "You travelled for 4 days and have arrived at Island: Golgolles";
		String expected2 = "Are you sure you want to travel to Island: Golgolles";
		String expected3 = "100% chance for nothing to happen";
		String expected4 = "You must pay your crew $40 for this route.";
		String input = 1 + System.lineSeparator()
				+ 1 + System.lineSeparator()
				+ 1 + System.lineSeparator()
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ System.lineSeparator();
			InputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			Game.setTestInput();
			
			game.gameSetup();
			game.sessionSetup("Tester", "Test ship", 25, "3");
			Route route = game.getPlayer().getLocation().getRoutes().get(0);
			route.getEvent().setChance(new ArrayList<Integer>(Arrays.asList(1)));
			
			game.selectRoute();
			
			assertEquals(210, game.getPlayer().getGold());
			assertFalse(game.getPlayer().getLocation() == game.getIslands().get(0));
			assertTrue(testOut.toString().contains(expected1));
			assertTrue(testOut.toString().contains(expected2));
			assertTrue(testOut.toString().contains(expected3));
			assertTrue(testOut.toString().contains(expected4));
	}

/*
	@Test
	void testGetGameLength() {
		fail("Not yet implemented");
	}

	@Test
	void testPlay() {
		fail("Not yet implemented");
	}

	@Test
	void testShopInteraction() {
		fail("Not yet implemented");
	}

	@Test
	void testBuyStock() {
		fail("Not yet implemented");
	}

	@Test
	void testViewItem() {
		fail("Not yet implemented");
	}

	@Test
	void testPurchaseItem() {
		fail("Not yet implemented");
	}

	@Test
	void testSellItem() {
		fail("Not yet implemented");
	}

	@Test
	void testGetPrice() {
		fail("Not yet implemented");
	}

	@Test
	void testSellStock() {
		fail("Not yet implemented");
	}

	@Test
	void testGetInt() {
		fail("Not yet implemented");
	}

	@Test
	void testSelectRoute() {
		fail("Not yet implemented");
	}

	@Test
	void testViewInventory() {
		fail("Not yet implemented");
	}

	@Test
	void testGenerateStore() {
		fail("Not yet implemented");
	}

	@Test
	void testEndGame() {
		fail("Not yet implemented");
	}

	@Test
	void testGetTotalWorth() {
		fail("Not yet implemented");
	}

	@Test
	void testPrintResults() {
		fail("Not yet implemented");
	}
*/
}
