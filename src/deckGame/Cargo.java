package deckGame;

import enums.ItemType;
import enums.Rarity;
import enums.Stats;
import enums.Damages;

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
	 * The type of damage that is used when modifying weakness or resistance stats
	 */
	private Damages modifyType;
	
	/**
	 * Creates a new instance of cargo which modifies a stat otehr than resistance or damage.
	 * @param name the name of the cargo
	 * @param description a description of the cargo
	 * @param size the amount of space the cargo requires
	 * @param basePrice the base value of the cargo
	 * @param rarity the rarity of the cargo
	 * @param modifyStat the stat that the cargo will modify
	 * @param modifyAmount the amount the cargo will modify the given stat by
	 */
	public Cargo(String name, String description, int size, int basePrice, Rarity rarity, Stats modifyStat, int modifyAmount) {
		super(name, description, size, basePrice, rarity);
		this.modifyStat = modifyStat;
		this.modifyAmount = modifyAmount;
	}
	
	/**
	 * Creates a new instance of cargo which modifies the resistance or damage stat.
	 * @param name the name of the cargo
	 * @param description a description of the cargo
	 * @param size the amount of space the cargo requires
	 * @param basePrice the base value of the cargo
	 * @param rarity the rarity of the cargo
	 * @param modifyStat the stat that the cargo will modify
	 * @param type the type of damage that will be used
	 */
	Cargo(String name, String description, int size, int basePrice, Rarity rarity, Stats modifyStat, Damages type) {
		super(name, description, size, basePrice, rarity, ItemType.CARGO);
		this.modifyStat = modifyStat;
		this.modifyType = type;
		this.modifyAmount = 0;
	}
	
	/**
	 * Creates a new instance of cargo which does not modify a stat.
	 * @param name the name of the cargo
	 * @param description a description of the cargo
	 * @param size the amount of space the cargo requires
	 * @param basePrice the base value of the cargo
	 * @param rarity the rarity of the cargo
	 */
	public Cargo(String name, String description, int size, int basePrice, Rarity rarity) {
		super(name, description, size, basePrice, rarity);
		this.modifyStat = Stats.NONE;
		this.modifyAmount = 0;
	}
	
	/**
	 * Gets a description of this cargo as a string.
	 * @return a string combination of this cargo's modifying ability and its item description
	 */
	public String toString() {
		String output = super.toString();
		//should we reveal this as a separate thing or include it in the description allowing for hidden aspects to items
		if (modifyStat != Stats.NONE) {
			if (modifyAmount != 0) {
				output += "\nThis modifies your " + modifyStat + " stat by " + modifyAmount + ".";
			} else if(modifyStat == Stats.WEAKNESS){
				output += "\nThis makes " + modifyType + " your weakness.";
			} else {
				output += "\nThis adds " + modifyType + "to your resistance types.";
			}
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
