package deckGame;

import enums.Damages;

public class Crewmate {
	/**
	 * The name of this crewmate.
	 */
	private String name;
	
	/**
	 * The amount of health this crewmate has.
	 */
	private int health;
	
	/**
	 * The damage type this crewmate does
	 */
	private Damages weapon;
	
	/**
	 * Creates a default instance of crewmate
	 */
	Crewmate(){
		weapon = Damages.BASIC;
		health = 20;
		name = "A crewmember";
	}
	
	/**
	 * Creates a non-default instance of crewmate
	 * @param name this crewmate's name
	 * @param health this crewmate's health
	 * @param damage the damage type this crewmate does
	 */
	Crewmate(String name, int health, Damages damage) {
		this.name = name;
		this.health = health;
		this.weapon = damage;
	}
	
	/**
	 * Damages this crewmate and returns a description of what happens to them.
	 * @param damage the amount of damage dealt
	 * @return a description of whether the crewmate survived or was killed
	 */
	public String takeDamage(int damage) {
		health -= damage;
		if(health > 0) {
			return name + " took " + damage + "damage and now has " + health + "health.";
		} else {
			health = 0;
			return name + " suffered a fatal blow of " + damage + "damage.";
		}
	}
	
	/**
	 * Gets the damage type that this crewmate inflicts
	 * @return the damage type associated with this crewmate
	 */
	public Damages getWeapon() {
		return weapon;
	}
}
