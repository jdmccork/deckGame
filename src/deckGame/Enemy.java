package deckGame;

public class Enemy extends Ship{
	
	public Enemy(String name, int health, int speed, int power) {
		super(name, health, speed, 1, power);
	}
	
	public void getDestroyed() {
		System.out.println("The enemy has been sunk");
		super.getDestroyed();
	}
}
