package tech.hirschfeld.quiz.player;

import javax.websocket.Session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import tech.hirschfeld.quiz.endpoints.Message;
import tech.hirschfeld.quiz.endpoints.WebsocketClient;

public class Player extends WebsocketClient {

  @JsonProperty("score")
  private int score = 0;

  @JsonProperty("rightAnswers")
  private int rightAnswers = 0;

  @JsonProperty("wrongAnswers")
  private int wrongAnswers = 0;

  @JsonProperty("name")
  private String name;


  @JsonIgnore
  private boolean answered = false; // did this Player answerd the Question?

  @JsonIgnore
  private boolean pendingAnswer = false; // is the answer wrong or right? Must be ignored if answerd = false.

  /**
   * A custom name will be generated
   * 
   * @param session
   */
  public Player(Session session, String name) {
    super(session);
    this.name = name;
  }

  public int getScore() {
    return score;
  }

  public String getName() {
    return name;
  }

  /**
   * Notifys the player that he won. Sets the new score.
   * 
   * @param scoreToAdd
   */
  public void correct(int scoreToAdd) {
    sendJson(new Message("question_result", true));
    rightAnswers += 1;
    score += scoreToAdd;
    answered = false;
  }

  /**
   * Notifys the player that he lost. Sets the new score.
   * 
   * @param scoreToAdd
   */
  public void incorrect(int scoreToAdd) {
    sendJson(new Message("question_result", false));
    wrongAnswers += 1;
    score += scoreToAdd;
    answered = false;
  }

  public boolean hasAnswerd() {
    return this.answered;
  }

  /**
   * Return false if there is no current Question.
   * 
   * @return true if the select answer was correct.
   */
  public boolean isPendingAnswerCorrect() {
    if (this.answered) {
      return pendingAnswer;
    }
    return false;
  }

  /**
   * Notice functions: correct and incorrect
   * 
   * @param wasItCorrect if the answer was correct
   */
  public void answerd(boolean wasItCorrect) {
    this.answered = true;
    this.pendingAnswer = wasItCorrect;
  }

}
