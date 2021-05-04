package enums;

public enum Rarity {
	COMMON(1), UNCOMMON(4), RARE(8), LEGENDARY(16);
	
	private int chanceModifier;

    Rarity(int chanceModifier) {
        this.chanceModifier = chanceModifier;
    }
    
    public int getChanceModifier() {
    	return chanceModifier;
    }
    
}
