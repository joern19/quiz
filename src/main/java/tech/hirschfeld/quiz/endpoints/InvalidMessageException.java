package tech.hirschfeld.quiz.endpoints;

/**
 * Thrown, if an send over Websockets is invalid.
 */
public class InvalidMessageException extends Exception {

  private static final long serialVersionUID = 123;

  public InvalidMessageException() {
    super();
  }

  public InvalidMessageException(String message) {
    super(message);
  }

}
