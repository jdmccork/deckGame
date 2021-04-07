package deckGame;

public class Island {
	private int locationX;
	private int locationY;
	
	public static double getDistance(Island source, Island destination) {
		int x = source.locationX - destination.locationX;
		int y = source.locationY - destination.locationY;
		return Math.sqrt(x^2+y^2);
	}
}
