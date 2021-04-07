package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import deckGame.Enemy;
import deckGame.Ship;

class ShipTest {

	@Test
	void test() {
		Ship firstShip = new Ship("Jolly Rogers", 200, 45, 5, 253);
		Enemy enemy= new Enemy("Black Pearl", 250, 80, 3);
		firstShip.attack(enemy);
		firstShip.damage(205);
		assertEquals("destroyed", enemy.getStatus());
	}

}
