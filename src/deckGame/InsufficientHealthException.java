package deckGame;

public class InsufficientHealthException extends IllegalStateException {
	
	public InsufficientHealthException() {}
    
    public InsufficientHealthException(String message) {
    	System.out.println("test");
        super(message);
    }
}
