package deckGame;

import java.util.ArrayList;
import java.util.Collections;

import enums.Rarity;

public class Card extends Item {
	private String action;
	private int target;
	private int result;
	private int requirement;
	private double multiplier;

	public Card(String tempName, String tempDescription, int tempSize, int tempBasePrice, Rarity tempRarity) {
		// TODO Auto-generated constructor stub
		super(tempName, tempDescription, tempSize, tempBasePrice, tempRarity);
	}
	
	public void makeTransform(int target, int result) {
		this.action = "transform";
		this.target = target;
		this.result = result;
	}
	
	public void makeMultiTransform(int target, int result, int requirement) {
		this.action = "transform";
		this.target = target;
		this.result = result;
		this.requirement = requirement;
	}
	
	public void makeDiceAdder(int target, int result) {
		this.action = "moreDice";
		this.target = target;
		this.result = result;
	}
	
	public void makeDamageAdder(int target) {
		this.action = "add";
		this.target = target;
	}
	
	public void makeDamageMultiplier(double multiplier) {
		this.action = "add";
		this.multiplier = multiplier;
	}
	
	public ArrayList<Integer> doSpecial(ArrayList<Integer> dice) {
		int sum = 0;
		for (int die: dice) {
			sum += die;
		}
		int transformNumber = 0;
		switch(action) {
		case "moreDice":
			for (int die: dice) {
				if (die == target) {
					transformNumber += 1;
			}
				for (int i = 0; i < transformNumber; i++) {
					dice.add(result);
				}
			}
			break;
		case "transform":
			Collections.replaceAll(dice, target, result);
			break;
		case "add":
			dice.add(result * dice.size() + 100);
			break;
		case "multiply":
			for (int die: dice) {
				if (die == target) {
					dice.add((int) (sum * (multiplier - 1) + 6));
				}
			}
			break;
		}
		return dice;
	}

}