package deckGame;

import enums.Stats;

/*
 * We can extend this further and have cargo categories such as animals, foods etc, this would allow us to 
have the animals eat the food to become worth more
*/
public class Cargo extends Item{
	/**
	 *The stat which the cargo modifies
	*/
	private Stats modifyStat;
	
	/**
	 * The amount by which the cargo modifies a given stat
	 */
	private int modifyAmount;
	
	/**
	 * Creates a new instance of cargo which modifies a stat.
	 * @param name the name of the cargo
	 * @param description a description of the cargo
	 * @param size the amount of space the cargo requires
	 * @param basePrice the base value of the cargo
	 * @param rarity the rarity of the cargo
	 * @param modifyStat the stat that the cargo will modify
	 * @param modifyAmount the amount the cargo will modify the given stat by
	 */
	Cargo(String name, String description, int size, int basePrice, Rarity rarity, Stats modifyStat, int modifyAmount) {
		super(name, description, size, basePrice, rarity);
		this.modifyStat = modifyStat;
		this.modifyAmount = modifyAmount;
	}
	
	/**
	 * Creates a new instance of cargo which does not modify a stat.
	 * @param name the name of the cargo
	 * @param description a description of the cargo
	 * @param size the amount of space the cargo requires
	 * @param basePrice the base value of the cargo
	 * @param rarity the rarity of the cargo
	 */
	Cargo(String name, String description, int size, int basePrice, Rarity rarity) {
		super(name, description, size, basePrice, rarity);
		this.modifyStat = Stats.NONE;
	}
	
	/**
	 * Gets a description of this cargo as a string.
	 * @return a string combination of this cargo's modifying ability
	 */
	public String toString() {
		String output = super.toString();
		//should we reveal this as a separate thing or include it in the description allowing for hidden aspects to items
		if (modifyStat != Stats.NONE) {
			output += "\nThis modifies your " + modifyStat + " stat by " + modifyAmount + ".";
		} else {
			output += "\nThis does not affect any of your stats.";
		}
		return output;
	}
	
	/**
	 * Gets the stat which this cargo modifies
	 * @return this cargo's stat
	 */
	public Stats getModifyStat(){
		return modifyStat;
	}

	/**
	 * Gets the amount by which this cargo modifies a stat
	 * @return this cargo's modifying amount
	 */
	public int getModifyAmount(){
		return modifyAmount;
	}
	
}
