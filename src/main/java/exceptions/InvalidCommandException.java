/**
 * This file is part of the Backgammon game project developed by the Dice Bros - Group 5 team.
 *
 * Team Information:
 * Team Name: Dice Bros - Group 5
 * Student Names:
 *   - Harshal Desai
 *   - Alparslan Balci
 *   - Manish Tawade
 * GitHub IDs:
 *   - harshaldesai01
 *   - Apistomeister
 *   - Manish9881
 */

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
