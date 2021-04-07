package deckGame;

import java.util.ArrayList;


public class Ship {
	private String status = "repaired";
	private String name;
	private int maxHealth;
	private int health;
	private int speed;
	private int capacity;
	//private enum strength;
	private ArrayList<Crewmate> crew;
	//private enum weakness;
	private ArrayList<Cargo> inventory;
	private int power;
	
	public Ship(String name, int health, int speed, int capacity, int power) {
		this.name = name;
		this.maxHealth = health;
		this.health = health;
		this.speed = speed;
		this.capacity = capacity;
		crew = new ArrayList<Crewmate>();
		inventory = new ArrayList<Cargo>();
		this.power = power;
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
	
	public void attack(Enemy enemy) {
		enemy.damage(power);
	}
	
	public static void main(String[] args) {
		Player firstShip = new Player("Jolly Rogers", 200, 45, 5, 253);
		Enemy enemy= new Enemy("Black Pearl", 250, 80, 3);
		firstShip.attack(enemy);
		firstShip.damage(205);
	}
}
