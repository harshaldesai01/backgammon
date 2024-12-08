package exceptions;

/**
 * Exception thrown when an invalid command is encountered in the game.
 */
public class InvalidCommandException extends RuntimeException {

  /**
   * Constructs an InvalidCommandException with the specified detail message.
   *
   * @param message the detail message.
   */
  public InvalidCommandException(String message) {
    super(message);
  }
}
