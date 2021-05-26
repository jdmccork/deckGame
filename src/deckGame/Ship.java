package deckGame;

import java.util.ArrayList;

import enums.Statuses;

public class Ship {
	/**
	 * The state that the ship is in, these can be repaired, damaged, or destroyed
	 */
	private Statuses status = Statuses.REPAIRED;
	
	/**
	 * The name of the ship
	 */
	private String shipName;
	
	/**
	 * The amount of health the ship will have when repaired
	 */
	private int maxHealth;
	
	/**
	 * The ship's current health
	 */
	private int health;
	
	/**
	 * The distance that the ship can travel in 1 day
	 */
	private int speed;
	
	/**
	 * The number of dice that are available to roll
	 */
	private int strength;
	
	/**
	 * Creates a new instance of ship based on given parameters.
	 * @param name the name of this ship
	 * @param health the amount of health this ship has
	 * @param speed the speed at which this ship can travel
	 * @param capacity the amount of cargo space this ship has
	 * @param strength the base damage this ship will do.
	 */
	public Ship(String name, int health, int speed, int strength) {
		this.shipName = name;
		this.setMaxHealth(health);
		this.health = health;
		this.speed = speed;
		this.strength = strength;
	}
	
	/**
	 * Gets the speed of this ship.
	 * @return speed the speed of this ship
	 */
	public int getSpeed() {
		if (speed <= 0) {
			return 1;
		}
		return speed;
	}
	
	/**
	 * Takes damage and alters the status of this ship.
	 * @param damage the amount of damage this ship takes.
	 */
	public String damage(int damage) {
		String output;
		System.out.println("The " + shipName + " took " + damage + " damage.");
		if (health > damage) {
            health -= damage;
            status = Statuses.DAMAGED;
            System.out.println("It currently has " + health + " health remaining.");
            output =  "The " + shipName + " took " + damage + " damage."
			+ "\nIt currently has " + health + " health remaining.";
    	} else {
    		output = getDestroyed();
    	}
		Game.pause();
		return output;
	}
	
	public String damage(ArrayList<Integer> dice) {
		int damage = 0;
		for (int die:dice) {
			damage += die;
		}
		return damage(damage);
	}
	
	public int getHealth() {
		return health;
	}
	
	/**
	 * Gets the status of this ship.
	 * @return the status of the ship: destroyed, damaged or repaired
	 */
	public Statuses getStatus() {
		return status;
	}
	
	/**
	 * Gets the name of this ship
	 * @return this ship's name
	 */
	public String getShipName() {
		return shipName;
	}
	
	/**
	 * Sets the status of the ship to destroyed
	 */
	public String getDestroyed() {
		status = Statuses.DESTROYED;
		health = 0;
		if (!(this instanceof Player)) {
			System.out.println("The enemy has been sunk");
			return "The enemy has been sunk";
		}
		return "";
	}
	
	/**
	 * Fixes this ship by increasing to maximum health and 
	 * setting the status to repaired.
	 */
	public void repair() {
		health = getMaxHealth();
		status = Statuses.REPAIRED;
	}
	
	public void repair(int amount) {
		health += amount;
		if (health >= maxHealth) {
			health = maxHealth;
			status = Statuses.REPAIRED;
		}
	}
	
	/**
	 * Gets the stats of the ship in string form
	 * @return the string detailing all the stats of this ship
	 */
	public String toString() {
		String output = "The " + shipName + " has the following stats:";
		output += "\nHealth: " + health + "/" + maxHealth;
		output += "\nSpeed: " + speed;
		output += "\nStrength: " + strength;
		if (this instanceof Player) {
			output += "\nCapacity: " + ((Player) this).getCapacity();
		}
		return output;
	}
	
	/**
	 * 
	 * @return The strength of the ship
	 */
	public int getStrength() {
		return strength;
	}

	/**
	 * 
	 * @return The maximum possible health of the ship.
	 */
	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * 
	 * @param maxHealth
	 */
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	/**
	 * changes the ships speed by the amount. This can be negative and is accounted for elsewhere.
	 * @param amount
	 */
	public void modifySpeed(int amount) {
		speed += amount;
	}
}
