package deckGame;

import java.util.ArrayList;
import java.util.Collections;

import enums.ItemType;
import enums.Rarity;

public class Card extends Item {
	/**
	 * What the card is able to do provided it meets the criteria
	 */
	private String action;
	
	/**
	 * What integer will the action is looking for for it to be successful
	 */
	private int target;
	
	/**
	 * What the function will output if the criteria are meet
	 */
	private int result;
	
	/**
	 * The number of target integers required to meet the criteria
	 */
	private int requirement;
	
	/**
	 * A separate number for multiplier's otherwise the minimum amount it could be multiplied
	 * by would be 2
	 */
	private double multiplier;
	
	/**
	 * The number that the card should be placed in a list in regards to other cards
	 */
	private int priority;


	/**
	 * Creates a new instance of Card based on givin parameters.
	 * @param tempName
	 * @param tempDescription
	 * @param tempSize
	 * @param tempBasePrice
	 * @param tempRarity
	 */
	public Card(String tempName, String tempDescription, int tempSize, int tempBasePrice, Rarity tempRarity) {
		super(tempName, tempDescription, tempSize, tempBasePrice, tempRarity);
	}
	
	/**
	 * Returns the type of item this is
	 */
	public ItemType getType() {
		return ItemType.CARD;
	}
	
	/**
	 * Sets the action, target, and result of the card to transform allowing doSpecial to operate correctly
	 * Turns any dice of target into result
	 * It has a priority of 3 as dice like those that are added from other cards can be affected by this one
	 * while cards that transform more than 1 are more important that a card that transforms all.
	 * @param target
	 * @param result
	 */
	public void makeTransform(int target, int result) {
		this.action = "transform";
		this.target = target;
		this.result = result;
		priority = 3;
	}
	
	/**
	 * Sets the action, target, result, and requirement of the card to transform allowing doSpecial to operate correctly
	 * Turns the requirement number of dice that roll target into the result
	 * It has a priority of 2 as dice that are rerolled or added from other cards can be affected by this one
	 * @param target
	 * @param result
	 * @param requirement
	 */
	public void makeMultiTransform(int target, int result, int requirement) {
		this.action = "multi-transform";
		this.target = target;
		this.result = result;
		this.requirement = requirement;
		priority = 2;
	}
	
	/**
	 * Sets the action, target, result, and requirement of the card to transform allowing doSpecial to operate correctly
	 * Adds result more dice if the requirement number of dice roll the target
	 * It has a priority of 0 as it has the most basic of operation that all other operations can be used on
	 * @param target
	 * @param result
	 * @param requirement
	 */
	public void makeDiceAdder(int target, int result, int requirement) {
		this.action = "moreDice";
		this.target = target;
		this.result = result;
		this.requirement = requirement;
		priority = 0;
	}
	
	/**
	 * Sets the action, target, and result of the card to transform allowing doSpecial to operate correctly
	 * Rerolls any dice that rolls target
	 * It has a priority of 1 as dice that are added from other cards can be affected by it
	 * @param target
	 */
	public void makeReroll(int target) {
		this.action = "reroll";
		this.target = target;
		priority = 1;
	}
	
	/**
	 * Sets the action, target, and result of the card to transform allowing doSpecial to operate correctly
	 * Adds a flat amount of damage to all dice that roll the target
	 * It has a priority of 4 as dice that are added from other cards can be affected by this one
	 * @param target
	 * @param result
	 */
	public void makeDamageAdder(int target, int result) {
		this.action = "add";
		this.result = result;
		this.target = target;
		priority = 4;
	}
	
	/**
	 * Sets the action, target, and result of the card to transform allowing doSpecial to operate correctly
	 * Multiplies the total damage by multiplier if requirement targets are rolled
	 * It has a priority of 5 as it affects the total amount of damage done
	 * @param target
	 * @param multiplier
	 */
	public void makeDamageMultiplier(int target, double multiplier, int requirement) {
		this.action = "multiply";
		this.target = target;
		this.multiplier = multiplier;
		priority = 5;
	}
	
	/**
	 * Runs the logic for the action that the card has been given on the dice
	 * @param dice
	 * @return Modified dice values
	 */
	public ArrayList<Integer> doSpecial(ArrayList<Integer> dice) {
		String output = this.getName();
		int sum = 0;
		for (int die: dice) {
			sum += die;
		}
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
				output += " caused " +  target + " dice get added to the roll";
			}
			break;
		case "transform":
			Collections.replaceAll(dice, target, result);
			output += " caused all dice that rolled a " + target + " to be replaced with " + result + "'s";
			break;
		case "add":
			for (int die: dice) {
				if (die == target) {
					count++;
				}
			}
			dice.add(result * count);
			output += " caused " + result * count + " damage to be added to the roll";
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
			output += " multiplied the value of your roll and added a " + (int) (sum * (multiplier - 1));
			break;
		case "multi-transform":
			for (int die: dice) {
				if (die == target) {
					count++;
				}
			}
			if (count >= requirement) {
				for (int i = 0; i < requirement; i++) {
					dice.remove(Integer.valueOf(target));
					dice.add(result);
				}
				output = " caused" +  requirement + " dice that rolled a " 
				+ target + " to become " + result + "'s";
			}
			break;
		case "reroll":
			while (dice.contains(target)) {
				dice.remove(Integer.valueOf(target));
				count++;
			}
			output += " rerolled " + count + " dice that rolled a " + target + " into :";
			for (int i = 0; i < count; i++) {
				dice.add((int) (Math.random() * 6) + 1);
				output += " " + dice.get(dice.size() - 1);
			}
			break;
		default:
			output = "Card " + this.getName() + " is not functioning correctly.";
			break;
		}
		output += ".";
		System.out.println(output);
		return dice;
	}
	
	/**
	 * Returns the priority of the cards. Allows them to be sorted into the correct order
	 * @return the priority of the card
	 */
	public int getPriority() {
		return priority;
	}

}