package deckGame;

import java.util.ArrayList;

import enums.Stats;
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
	 * The ships current health
	 */
	private int health;
	
	/**
	 * The distance that the ship can travel in 1 day
	 */
	private int speed;
	
	/**
	 * The number of items that can be put on the ship
	 */
	private int capacity;
	
	/**
	 * The damage types the ship has a resistance too
	 */
	//private ArrayList<Resistance> resistance;
	
	/**
	 * The damage types the ship has a weakness too
	 */
	//private enum weakness;
	
	/**
	 * A list of the crew currently hired
	 */
	private ArrayList<Crewmate> crew;
	
	/**
	 * A list of cargo that is currently being transported
	 */
	private ArrayList<Cargo> inventory;
	
	/**
	 * The base damage your ship will do
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
		this.maxHealth = health;
		this.health = health;
		this.speed = speed;
		this.capacity = capacity;
		crew = new ArrayList<Crewmate>();
		inventory = new ArrayList<Cargo>();
		this.strength = strength;
	}
	
	/**
	 * Gets the speed of this ship.
	 * @return the speed of this ship
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
		health = maxHealth;
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
				maxHealth += amount;
				health += amount;
				break;
			case SPEED:
				speed += amount;
				break;
			case CAPACITY:
				capacity += amount;
				break;
			case NONE:
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
			System.out.println("There is currently " + inventory.size() + " items on the ship:");
		}
		
		for (Cargo cargo: inventory) {
			System.out.println(cargo);
		}
	}
	
	/**
	 * Adds the given cargo to this ship's inventory and checks
	 * if the capacity has not been exceeded.
	 * @param cargo the cargo to remove
	 */
	public boolean addCargo(Cargo cargo) {
		if (inventory.size() <= capacity) {
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
	
	public int getCapacity() {
		return capacity;
	}
	
	public ArrayList<Cargo> getInventory(){
		return inventory;
	}

	public static void main(String[] args) {
		Player firstShip = new Player("Jack", "Jolly Rogers", 200, 45, 5, 253, 25);
		Cargo bread = new Cargo("Bread", "It's bread", 1, 1, Rarity.COMMON);
		firstShip.addCargo(bread);
		firstShip.printInventory();
		firstShip.removeCargo(bread);
		firstShip.printInventory();
	}
}
