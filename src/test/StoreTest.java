package test;

import static org.junit.jupiter.api.Assertions.*;

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
	
	static ArrayList<Island> islands = new ArrayList<Island>();
	private Player player;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Game.generateItems();
		
		islands.add(new Island("Home", 0, 0));
		islands.add(new Island("Golgolles", -10, 5));
		islands.add(new Island("Cansburg", 5, 5));
		islands.add(new Island("Tisjour", -5, -5));
		islands.add(new Island("Brighdown", 5, -5));
	}

	@BeforeEach
	void setUp() throws Exception {
		player = new Player("Tester", "The void", 100, 2, 4, 3, 25, islands.get(0));
	}

	@Test
	void storeCreationTest() {
		Store testStore = new Store("test store", player);
		Item item = testStore.getStock().get(0);
		assertTrue(Game.getItems().contains(item));
	}
}
