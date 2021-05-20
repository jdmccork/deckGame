package deckGame;

import java.util.ArrayList;
import java.util.Collections;

import enums.ItemType;
import enums.Rarity;

public class Card extends Item {
	private String action;
	private int target;
	private int result;
	private int requirement;
	private double multiplier;
	private int priority;

	public Card(String tempName, String tempDescription, int tempSize, int tempBasePrice, Rarity tempRarity) {
		// TODO Auto-generated constructor stub
		super(tempName, tempDescription, tempSize, tempBasePrice, tempRarity);
	}
	
	public ItemType getType() {
		return ItemType.CARD;
	}
	
	public void makeTransform(int target, int result) {
		this.action = "transform";
		this.target = target;
		this.result = result;
		priority = 3;
	}
	
	public void makeMultiTransform(int target, int result, int requirement) {
		this.action = "multi-transform";
		this.target = target;
		this.result = result;
		this.requirement = requirement;
		priority = 2;
	}
	
	public void makeDiceAdder(int target, int result, int requirement) {
		this.action = "moreDice";
		this.target = target;
		this.result = result;
		this.requirement = requirement;
		priority = 0;
	}
	
	public void makeReroll(int target) {
		this.action = "reroll";
		this.target = target;
		priority = 1;
	}
	
	public void makeDamageAdder(int target, int result) {
		this.action = "add";
		this.result = result;
		this.target = target;
		priority = 4;
	}
	
	public void makeDamageMultiplier(int target, double multiplier) {
		this.action = "multiply";
		this.target = target;
		this.multiplier = multiplier;
		priority = 5;
	}
	
	public ArrayList<Integer> doSpecial(ArrayList<Integer> dice) {
		int sum = 0;
		for (int die: dice) {
			sum += die;
		}
		int transformNumber = 0;
		int count = 0;
		switch(action) {
		case "moreDice":
			for (int die: dice) {
				if (die == target) {
					count += 1;
				}
			}
			if (count >= requirement) {
				for (int i = 0; i < result; i++) {
					dice.add((int) (Math.random() * 6) + 1);
				}
				System.out.println(target + " dice of have been added to the roll.");
			}
			break;
		case "transform":
			Collections.replaceAll(dice, target, result);
			System.out.println("All dice that rolled a " + target + " have been replaced with " + result + "'s.");
			break;
		case "add":
			for (int die: dice) {
				if (die == target) {
					count++;
				}
			}
			dice.add(result * count);
			System.out.println(result * count + " damage has been added to the roll.");
			break;
		case "multiply":
			boolean procced = false;
			for (int die: dice) {
				if (die == target) {
					procced = true;
				}
			}
			if (procced) {
				dice.add((int) (sum * (multiplier - 1)));
			}
			break;
		case "multi-transform":
			for (int die: dice) {
				if (die == target) {
					count++;
				}
			}
			if (count >= requirement) {
				for (int i = 0; i < count; i++) {
					dice.remove(Integer.valueOf(target));
					dice.add(result);
				}
				System.out.println(requirement + " dice that rolled a " + target + " have become " + result + "'s.");
			}
			break;
		case "reroll":
			while (dice.contains(target)) {
				dice.remove(Integer.valueOf(target));
				count++;
			}
			for (int i = 0; i < count; i++) {
				dice.add((int) (Math.random() * 6) + 1);
			}
			break;
		default:
			System.out.println("This card is not functioning correctly");
			break;
		}
		return dice;
	}

}