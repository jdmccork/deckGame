package deckGame;

import java.util.ArrayList;


public class Ship {
	private String name;
	private int maxHealth;
	private int health;
	private int speed;
	private int capacity;
	//private enum strength;
	private ArrayList<Crewmate> crew;
	//private enum weakness;
	private ArrayList<Cargo> inventory;
	
	public Ship(String name, int health, int speed, int capacity) {
		this.name = name;
		this.maxHealth = health;
		this.health = health;
		this.speed = speed;
		this.capacity = capacity;
		crew = new ArrayList<Crewmate>();
		inventory = new ArrayList<Cargo>();
	}
	
	public void takeDamage(int damage) {
		if (health >= damage) {
            health -= 20;
    	} else {
    		throw new InsufficientHealthException("Not enough health");
    	}
    }
	
	
	public static void main(String[] args) {
		Ship firstShip = new Ship("Jolly Rogers", 200, 45, 5);
		System.out.println(firstShip.name);
		firstShip.takeDamage(205);
	}
}
