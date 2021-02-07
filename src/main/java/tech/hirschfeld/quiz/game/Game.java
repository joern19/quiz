package tech.hirschfeld.quiz.game;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jboss.logmanager.Level;

import tech.hirschfeld.quiz.endpoints.Message;
import tech.hirschfeld.quiz.endpoints.boardEndpoint.BoardManager;
import tech.hirschfeld.quiz.endpoints.controllerEndpoint.ControllerEndpoint;
import tech.hirschfeld.quiz.events.game.EveryoneAnswerdEvent;
import tech.hirschfeld.quiz.player.PlayerManager;

@ApplicationScoped // TODO: game scoped.
public class Game {

  @Inject
  PlayerManager playerManager;

  @Inject
  BoardManager boardManager;

  @Inject
  ControllerEndpoint controllerManager;

  private List<Question> questions;
  private ListIterator<Question> currentQuestion;

  public Game() {
  }

  public Question getCurrentQuestion() {
    return questions.get(currentQuestion.nextIndex()); // todo: improve
  }

  public void loadGame(Question... questions) {
    this.questions = Arrays.asList(questions);
    currentQuestion = this.questions.listIterator();
    nextQuestion();
  }

  /**
   * 
   * @return false if there are no more questions.
   */
  public boolean nextQuestion() {
    if (!currentQuestion.hasNext()) {
      return false;
    }
    currentQuestion.next();
    broadcastCurrentQuestion();
    return true;
  }

  public boolean previousQuestion() {
    if (!currentQuestion.hasPrevious()) {
      return false;
    }
    currentQuestion.previous();
    broadcastCurrentQuestion();
    return true;
  }

  public void revealWinner() {
    broadcast(new Message("winner", playerManager.getWinner()));
  }

  public void onEveryoneAnswerd(@Observes EveryoneAnswerdEvent event) {
    boardManager.broadcast(playerManager.generateScoreboard(getCurrentQuestion().getQuestion()));
    if (controllerManager.isControllerPresent()) {
      controllerManager.sendQuestionDone();
    } else {
      // the game is able to run automaticly...
      // todo: learn about multithreading in quarkus.
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            Thread.sleep(5000);
            if (controllerManager.isControllerPresent()) {
              return; // controller is back!
            }
            if (!nextQuestion()) {
              Thread.sleep(10000); // give 10 more secounds to reveal winner!
              if (controllerManager.isControllerPresent()) {
                return; // controller is back!
              }
              revealWinner();
            }
          } catch (InterruptedException ex) {
          }
        }
      }).start();
    }
  }

  private void broadcast(Message message) {
    playerManager.broadcast(message);
    boardManager.broadcast(message);
  }

  private void broadcastCurrentQuestion() {
    boardManager.broadcast(playerManager.generateScoreboard(getCurrentQuestion().getQuestion()));
    Message message = new Message("question", getCurrentQuestion());
    try {
      Logger.getLogger(Game.class.getName()).log(Level.DEBUG, "Broadcasting: " + new ObjectMapper().writeValueAsString(message));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } // turn into debug or remove.
    broadcast(message);
  }

}
