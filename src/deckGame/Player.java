package deckGame;

public class Player extends Ship {

	public Player(String name, int health, int speed, int capacity, int power) {
		super(name, health, speed, capacity, power);
		// TODO Auto-generated constructor stub
	}
	
	public void getDestroyed() {
		System.out.println("The " + getName() + " has been destroyed. Game Over.");
		super.getDestroyed();
	}

}
