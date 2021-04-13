package deckGame;

public class Enemy extends Ship{
	/**
	 * Creates a new enemy.
	 * @param name the name of this enemy ship
	 * @param health the amount of health this enemy ship has
	 * @param speed the speed at which this enemy ship can travel
	 * @param power the amount of damage this ship can do
	 */
	public Enemy(String name, int health, int speed, int power) {
		super(name, health, speed, 1, power);
	}
	
	/**
	 * Announces that this enemy has been sunk.
	 * <p>
	 * Sets its status to destroyed.
	 */
	public void getDestroyed() {
		System.out.println("The enemy has been sunk");
		super.getDestroyed();
	}
}
