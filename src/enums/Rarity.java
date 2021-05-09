package enums;

public enum Rarity {
	COMMON(1), UNCOMMON(10), RARE(16), LEGENDARY(20);
	
	private int chanceModifier;

    Rarity(int chanceModifier) {
        this.chanceModifier = chanceModifier;
    }
    
    public int getChanceModifier() {
    	return chanceModifier;
    }
    
}
