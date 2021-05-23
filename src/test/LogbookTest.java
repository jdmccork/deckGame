package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import deckGame.Cargo;
import deckGame.Entry;
import deckGame.Logbook;
import deckGame.Item;

class LogbookTest {
	Logbook logbook;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Item.generateItems();
	}

	@BeforeEach
	void setUp() throws Exception {
		logbook = new Logbook();
	}

	@Test
	void testViewEntries() {
		Entry entry1 = new Entry(1);
		entry1.makeTransaction(Item.getItems().get(0), "sold");
		entry1.addCost(-20);
		Entry entry2 = new Entry(5);
		entry2.makeTransaction(Item.getItems().get(1), "bought");
		entry2.addCost(5);
		logbook.addEntry(entry1);
		logbook.addEntry(entry2);
		logbook.viewEntries();
	}

}
