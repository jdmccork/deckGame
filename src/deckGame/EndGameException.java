package deckGame;

public class EndGameException extends IllegalStateException {
	/**
	 * Creates EndGameException as an extension of IllegalStateException with no message
	 */
	public EndGameException() {}
    
	/**
	 * Creates EndGameException as an extension of IllegalStateException with a message
	 */
    public EndGameException(String message) {
        super(message);
        System.out.println("test");
    }
}