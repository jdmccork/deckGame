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
	
	private Damages weapon;
	
	Crewmate(){
		weapon = Damages.BASIC;
		health = 20;
		name = "A crewmember";
	}
	
	Crewmate(String name, int health, Damages damage) {
		this.name = name;
		this.health = health;
		this.weapon = damage;
	}
	
	public String takeDamage(int damage) {
		health -= damage;
		if(health > 0) {
			return name + " took " + damage + "damage and now has " + health + "health.";
		} else {
			health = 0;
			return name + " suffered a fatal blow of " + damage + "damage.";
		}
	}
}
