package deckGame;

public class Cargo extends Item{
	private String modifyStat;
	private int modifyAmount;
	
	Cargo(String name, String description, int size, int basePrice, String rarity, String modifyStat, int modifyAmount) {
		super(name, description, size, basePrice, rarity);
		//ModifyStat can be null if the item does not change a stat.
		this.modifyStat = modifyStat;
		this.modifyAmount = modifyAmount;
	}
	
	public String ToString() {
		String output = super.ToString();
		if (modifyStat != null) {
			output += "\nThis modifies your "+modifyStat+" stat by "+modifyAmount+".";
		} else {
			output += "\nThis does not affect any of your stats.";
		}
		return output;
	}
}
