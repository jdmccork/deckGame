package deckGame;

import java.util.ArrayList;

public class Ship {
	private String status = "repaired";
	private String name;
	private int maxHealth;
	private int health;
	private int speed;
	private int capacity;
	//private enum resistance;
	private ArrayList<Crewmate> crew;
	//private enum weakness;
	private ArrayList<Cargo> inventory;
	private int strength;
	
	public Ship(String name, int health, int speed, int capacity, int strength) {
		this.name = name;
		this.maxHealth = health;
		this.health = health;
		this.speed = speed;
		this.capacity = capacity;
		crew = new ArrayList<Crewmate>();
		inventory = new ArrayList<Cargo>();
		this.strength = strength;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void damage(int damage) {
		if (health >= damage) {
            health -= damage;
            status = "damaged";
    	} else {
    		getDestroyed();
    	}
    }
	
	public String getStatus() {
		return status;
	}
	
	public String getName() {
		return name;
	}
	
	public void getDestroyed() {
		status = "destroyed";
	}
	
	public void repair() {
		health = maxHealth;
	}
	
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
	
	public void attack(Enemy enemy) {
		enemy.damage(strength);
	}
	
	public void printInventory() {
		if(inventory.size() == 1) {
			System.out.println("There is " + inventory.size() + " item on the ship");
		} else {
			System.out.println("There is " + inventory.size() + " items on the ship");
		}
		
		for (Cargo cargo: inventory) {
			System.out.println(cargo);
		}
	}
	
	public void addCargo(Cargo cargo) {
		if (inventory.size() <= capacity) {
			inventory.add(cargo);
			alterStat(cargo.getModifyStat(), cargo.getModifyAmount());
		}
	}
	
	public void removeCargo(Cargo cargo) {
		if (inventory.contains(cargo)) {
			inventory.remove(cargo);
			alterStat(cargo.getModifyStat(), -cargo.getModifyAmount());
		}
	}
	
	public void sail(Route route) {
		
	}
	
	public static void main(String[] args) {
		Player firstShip = new Player("Jolly Rogers", 200, 45, 5, 253);
		Cargo bread = new Cargo("Bread", "It's bread", 1, 1, "Common");
		firstShip.addCargo(bread);
		firstShip.printInventory();
		firstShip.removeCargo(bread);
		firstShip.printInventory();
	}
}
