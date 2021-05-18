package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import deckGame.Item;

class GenerateItemTest {
	private int currentItemCount = 23;
	
	@Test
	void testGenerateItems() {
		Item.generateItems();
	}
	
	@Test
	void correctNumItems() {
		Item.generateItems();
		assertEquals(currentItemCount, Item.getItems().size());
	}
}