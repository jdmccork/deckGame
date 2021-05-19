package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import deckGame.Card;
import enums.Rarity;

class CardTest {
	private Card card;
	private ArrayList<Integer> dice;
	private ArrayList<Integer> expected;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		card = new Card("Test card", "A card used for testing.", 1, 10, Rarity.COMMON);
		dice = new ArrayList<>();
		expected = new ArrayList<>();
	}

	@Test
	void testCard() {
		assertEquals(card.getName(), "Test card");
		assertEquals(card.getRarity(), Rarity.COMMON);
	}

	@Test
	void testMakeTransform() {
		card.makeTransform(1, 6);
		
		dice.add(1);
		expected.add(6);
		
		card.doSpecial(dice);
		assertEquals(expected, dice);
	}

	@Test
	void testMakeMultiTransform() {
		card.makeMultiTransform(1, 6, 2);
		
		dice.add(1);
		dice.add(3);
		dice.add(1);
		expected.add(3);
		expected.add(6);
		expected.add(6);
		
		card.doSpecial(dice);
		assertEquals(expected, dice);
	}

	@Test
	void testMakeDiceAdder() {
		card.makeDiceAdder(1, 1, 2);
		
		dice.add(1);
		dice.add(3);
		dice.add(1);
		
		card.doSpecial(dice);
		System.out.println(dice);
		assertTrue(dice.size() == 4);
	}

	@Test
	void testMakeDamageAdder() {
		card.makeDamageAdder(2, 3);
		
		dice.add(1);
		dice.add(2);
		dice.add(1);
		
		expected.add(1);
		expected.add(2);
		expected.add(1);
		expected.add(3);
		
		card.doSpecial(dice);
		assertEquals(expected, dice);
		
		dice.add(2);
		
		expected.add(2);
		expected.add(6);
		
		card.doSpecial(dice);
		assertEquals(expected, dice);
		
	}

	@Test
	void testMakeDamageMultiplier() {
		card.makeDamageMultiplier(5, 1.2);
		
		dice.add(1);
		dice.add(5);
		
		expected.add(1);
		expected.add(5);
		expected.add(1);
		
		card.doSpecial(dice);
		assertEquals(expected, dice);
		
		int sum = 0;
		for (int die: dice) {
			sum += die;
		}
		
		assertEquals(7, sum);
	}
	
	@Test
	void testMakeReroll() {
		card.makeReroll(1);
		
		dice.add(1);
		
		expected.add(2);
		expected.add(3);
		expected.add(4);
		expected.add(5);
		expected.add(6);
		
		int count = 0;
		
		while (!expected.contains(dice.get(0))) {
			card.doSpecial(dice);
			count++;
			if (count == 10) {
				fail("Extremly unlucky or broken");
			}
		}
	}

}
