package enums;

public enum Rarity {
	COMMON(1), UNCOMMON(2), RARE(3), LEGENDARY(4);
	
	private int chanceModifier;

    Rarity(int chanceModifier) {
        this.chanceModifier = chanceModifier;
    }
    
    public int getChanceModifier() {
    	return chanceModifier;
    }
    
}
