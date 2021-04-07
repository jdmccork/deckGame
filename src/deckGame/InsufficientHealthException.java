package deckGame;

public class InsufficientHealthException extends IllegalStateException {
	
	public InsufficientHealthException() {}
    
    public InsufficientHealthException(String message) {
        super(message);
        System.out.println("test");
    }
}
