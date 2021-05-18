package deckGame;

import java.util.ArrayList;

//import enums.ItemType;
import enums.Rarity;
import enums.Stats;
import enums.Statuses;
import enums.Damages;

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
	 * The damage types the ship has a resistance to
	 */
	private ArrayList<Damages> resistance = new ArrayList<Damages>();
	
	/**
	 * The damage type the ship has a weakness too
	 */
	private Damages weakness;
	
	/**
	 * A list of the crew currently hired
	 */
	private ArrayList<Crewmate> crew;
	
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
		crew = new ArrayList<Crewmate>();
		this.strength = strength;
		weakness = Damages.FIRE;
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
	public void damage(int damage) {
		if (health >= damage) {
            health -= damage;
            status = Statuses.DAMAGED;
    	} else {
    		getDestroyed();
    	}
		System.out.println("The " + shipName + " took " + damage + " damage."
				+ "\nIt currently has " + health + " health remaining.");
    }
	
	public void damage(ArrayList<Integer> dice) {
		int damage = 0;
		for (int die:dice) {
			damage += die;
		}
		damage(damage);
	}
	
	public int getHealth() {
		return health;
	}
	
	/**
	 * Takes damage and alters the status of this ship, accounting for damage type.
	 * @param damage the amount of damage this ship takes.
	 * @param type the type of damage being dealt
	 */
	public void damage(int damage, Damages type) {
		if (type == this.weakness) {
			damage *= 1.5;
		}
		if(this.resistance.contains(type)) {
			damage /= 1.5;
		}
		if (health >= damage) {
            health -= damage;
            if (health < maxHealth) {
            	status = Statuses.DAMAGED;
            }
    	} else {
    		getDestroyed();
    	}
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
	public void getDestroyed() {
		status = Statuses.DESTROYED;
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
	 * Adds a type of damage that this ship can resist
	 * @param damage the type of damage to add
	 */
	public void addResistance(Damages damage) {
		if (!resistance.contains(damage)) {
			resistance.add(damage);
		}
	}
	
	/**
	 * Removes a type of damage that this ship can resist
	 * @param damage the type of damage to remove
	 */
	public void removeResistance(Damages damage) {
		resistance.remove(damage);
	}
	
	/**
	 * Gets the stats of the ship in string form
	 * @return the string detailing all the stats of this ship
	 */
	public String getStats() {
		String output = "The " + shipName + " has the following stats:\n";
		output += "Health: " + health + "/" + maxHealth + "\n";
		output += "Speed: " + speed + "\n";
		output += "Strength: " + strength + "\n";
		if (this instanceof Player) {
			output += "Capacity: " + ((Player) this).getCapacity() + "\n";
		}else {
			output += "Capacity: 4\n"; //TODO literally anything with this
		}
		String resistances = "";
		for (Damages damage: resistance) {
			resistances += damage + ", ";
		}
		output += "Resistance: " + resistances.substring(0, resistances.length() - 2) + "\n";
		return output;
	}
	
	public int getStrength() {
		return strength;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public void modifySpeed(int amount) {
		speed += amount;
	}
}
