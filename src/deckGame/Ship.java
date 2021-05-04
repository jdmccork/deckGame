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
	 * The number of item space that can be used on the ship
	 */
	private int capacity;
	
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
	 * A list of cargo that is currently being transported
	 */
	private ArrayList<Cargo> inventory;
	
	/**
	 * The number of dice that are available to roll
	 */
	private int strength;
	
	/**
	 * Creates a new instance of ship with pre-generated stats.
	 * <p>
	 * This exists purely as a developer tool for testing.
	 */
	public Ship() {
		shipName= "Tester";
		health = 100;
		speed = 5;
		capacity = 4;
		strength = 3;
		weakness = Damages.FIRE;
		crew.add(new Crewmate());
	}
	
	/**
	 * Creates a new instance of ship based on given parameters.
	 * @param name the name of this ship
	 * @param health the amount of health this ship has
	 * @param speed the speed at which this ship can travel
	 * @param capacity the amount of cargo space this ship has
	 * @param strength the base damage this ship will do.
	 */
	public Ship(String name, int health, int speed, int capacity, int strength) {
		this.shipName = name;
		this.setMaxHealth(health);
		this.health = health;
		this.speed = speed;
		this.capacity = capacity;
		crew = new ArrayList<Crewmate>();
		inventory = new ArrayList<Cargo>();
		this.strength = strength;
		weakness = Damages.FIRE;
	}
	
	/**
	 * Gets the speed of this ship.
	 * @return speed the speed of this ship
	 */
	public int getSpeed() {
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
	
	/**
	 * Changes the given stat of the ship by the given amount.
	 * @param stat the stat to be changed
	 * @param amount the amount to change the stat by
	 */
	public void alterStat(Stats stat, int amount) {
		//TODO add checks to ensure values are positive
		switch (stat) {
			case MAXHEALTH:
				setMaxHealth(getMaxHealth() + amount);
				health += amount;
				break;
			case SPEED:
				speed += amount;
				break;
			case CAPACITY:
				capacity += amount;
				break;
			default:
				break;
		}
	}
	
	/**
	 * Damages the given enemy by the attack strength of this ship.
	 * @param enemy the enemy to attack
	 */
	public void attack(Enemy enemy) {
		enemy.damage(strength);
	}
	
	/**
	 * Prints out the number of items in this ship's inventory, 
	 * then prints each item in turn.
	 */
	public void printInventory() {
		if(inventory.size() == 1) {
			System.out.println("There is currently " + inventory.size() + " item on the ship:");
		} else {
			System.out.println("There are currently " + inventory.size() + " items on the ship:");
		}
		int i = 1;
		for (Cargo cargo: inventory) {
			System.out.println(i++ + ": " + cargo.getName());
		}
	}
	
	/**
	 * Adds the given cargo to this ship's inventory and checks
	 * if the capacity has not been exceeded.
	 * @param cargo the cargo to add
	 */
	public boolean addItem(Cargo cargo) {
		int space = 0;
		for (Cargo item: inventory) {
			space += item.getSize();
		}
		if (space <= capacity) {
			inventory.add(cargo);
			alterStat(cargo.getModifyStat(), cargo.getModifyAmount());
			return true;
		}
		return false;
	}
	
	/**
	 * Removes the first instance of the given kind of cargo
	 * from this ship's inventory.
	 * @param cargo the cargo to remove
	 */
	public void removeCargo(Cargo cargo) {
		if (inventory.contains(cargo)) {
			inventory.remove(cargo);
			alterStat(cargo.getModifyStat(), -cargo.getModifyAmount());
		}
	}
	
	/**
	 * Gets the capacity of this ship
	 * @return the capacity of the ship
	 */
	public int getCapacity() {
		return capacity;
	}
	
	/**
	 * Gets the inventory of the ship
	 * @return the ArrayList of the items in the inventory
	 */
	public ArrayList<Cargo> getInventory() {
		return inventory;
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
		output += "Weakness: " + weakness + "\n";
		output += "Capacity: " + capacity + "\n";
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

	/**
	 * For testing purposes
	 * @param args
	 */
	public static void main(String[] args) {
		Player firstShip = new Player("Jack", "Jolly Rogers", 200, 45, 5, 253, 25, new Island("test", 0, 0));
		Cargo bread = new Cargo("Bread", "It's bread", 1, 1, Rarity.COMMON);
		firstShip.addItem(bread);
		firstShip.printInventory();
		firstShip.removeCargo(bread);
		firstShip.printInventory();
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
}
