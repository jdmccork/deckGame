package deckGame;
/*
 * We can extend this further and have cargo categories such as animals, foods etc, this would allow us to 
have the animals eat the food to become worth more
*/
public class Cargo extends Item{
	private Stats modifyStat;
	private int modifyAmount;
	
	Cargo(String name, String description, int size, int basePrice, String rarity, Stats modifyStat, int modifyAmount) {
		super(name, description, size, basePrice, rarity);
		this.modifyStat = modifyStat;
		this.modifyAmount = modifyAmount;
	}
	
	Cargo(String name, String description, int size, int basePrice, String rarity) {
		super(name, description, size, basePrice, rarity);
		this.modifyStat = Stats.NONE;
	}
	
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
	
	public Stats getModifyStat(){
		return modifyStat;
	}
	
	public int getModifyAmount(){
		return modifyAmount;
	}
}
