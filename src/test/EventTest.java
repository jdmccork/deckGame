package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import deckGame.Event;
import deckGame.Island;
import deckGame.Item;
import deckGame.Player;

class EventTest {
	private Event event;
	private ArrayList<Island> islands;
	private Player player;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Item.generateItems();
	}

	@BeforeEach
	void setUp() throws Exception {
		event = new Event();
		Island island1 = new Island("island1", 0, 0);
		Island island2 = new Island("Island2", 5, 5);
		player = new Player("Test", "Tester", 1, 1, 1, 1, 1, 1, island1);
		islands.add(island1);
		islands.add(island2);
	}

	@Test
	void testSelectEvent() {
		islands.get(0).generateRoutes(islands);
		event.selectEvent(islands.get(0).getRoutes().get(0), player);
		fail("Not yet implemented");
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
	void testFlee() {
		fail("Not yet implemented");
	}

	@Test
	void testStorm() {
		fail("Not yet implemented");
	}

	@Test
	void testReward() {
		fail("Not yet implemented");
	}

}
