package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import deckGame.Cargo;
import deckGame.Game;
import deckGame.Island;
import deckGame.Item;
import deckGame.Player;
import deckGame.Ship;
import enums.Rarity;
import enums.Stats;
import enums.Statuses;

class ShipTest {
	
	static ArrayList<Island> islands = new ArrayList<Island>();
	private Ship ship;
	
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
	public void init() {
		ship = new Ship("Jolly Rogers", 200, 45, 5);
	}

	@Test
	void damageTest() {
		String input = System.lineSeparator();
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Game.setTestInput();
		
		assertEquals(200, ship.getHealth());
		ship.damage(15);
		assertEquals(185, ship.getHealth());
		assertEquals(ship.getStatus(), Statuses.DAMAGED);
	}
	
	@Test
	void testPrint() {
		//This will also test the creation of ship
		String expected = "The Jolly Rogers has the following stats:\n"
				+ "Health: 200/200\n"
				+ "Speed: 45\n"
				+ "Strength: 5";
		assertEquals(expected, ship.toString());
	}
}
