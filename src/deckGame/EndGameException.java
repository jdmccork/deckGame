package deckGame;

public class EndGameException extends IllegalStateException {
	
	public EndGameException() {}
    
    public EndGameException(String message) {
        super(message);
        System.out.println("test");
    }
}