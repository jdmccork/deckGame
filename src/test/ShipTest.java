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
		ship = new Ship("Jolly Rogers", 200, 45, 253);
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
	//TODO move to player
	
//	@Test
//	void inventoryTest() {
//		ship.addItem((Cargo) Item.getItems().get(0));
//		assertEquals(ship.getInventory().get(0), Item.getItems().get(0));
//	}
	
//	@Test
//	void smallInventoryTest() {
//		ship = new Ship("Jolly Rogers", 200, 45, 0, 253);
//		ship.addItem((Cargo) Item.getItems().get(0));
//		assertEquals(ship.getInventory().size(), 0);
//	}
	
	
//	@Test
//	void maxHealthItemTest(){
//		Cargo item = new Cargo("Max health test", "Test max health stat", 1, 1, Rarity.COMMON, Stats.MAXHEALTH, 50);
//		ship.addItem(item);
//		assertEquals(ship.getMaxHealth(), 250);
//	}

}
