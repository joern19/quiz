package tech.hirschfeld.quiz.endpoints.controllerEndpoint;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import tech.hirschfeld.quiz.endpoints.Message;
import tech.hirschfeld.quiz.endpoints.WebsocketClient;
import tech.hirschfeld.quiz.game.Game;
import tech.hirschfeld.quiz.game.Question;
import tech.hirschfeld.quiz.game.Scoreboard;

@ApplicationScoped //todo: game scoped
@ServerEndpoint("/controller")
public class ControllerEndpoint { //todo: additional Manager?
    
  @Inject
  private Game game;

  private Optional<WebsocketClient> controller = Optional.empty();

  public boolean isControllerPresent() {
    return controller.isPresent();
  }

  /**
   * Dont worry, I will check if controller is present.
   * @param scoreboard
   */
  public void sendScoreboard(Scoreboard scoreboard) {
    if (controller.isEmpty()) {
      return;
    }
    //todo: send scoreboard.
  }

  public void sendQuestionDone() {
    controller.get().sendJson(new Message("gameUpdate", "question_done"));
  }

  @OnOpen
  public void onOpen(Session session) { //todo: send current Question and stuff.
    if (controller.isPresent()) {
      try {
        session.close();
      } catch (IOException e) {
        //do nothing.
      }
    } else {
      controller = Optional.of(new WebsocketClient(session));
    }
  }

  @OnClose
  public void onClose(Session session) {
    if (!controller.get().getSession().getId().equals(session.getId())) { //safty first!
      return; //todo: make above shorter maybe?
    }
    controller = Optional.empty();
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    Logger.getLogger(ControllerEndpoint.class.getName()).warning("onError threw: " + throwable.getMessage());
    if (!controller.get().getSession().getId().equals(session.getId())) { //safty first!
      return;
    }
    try {
      session.close();
    } catch (IOException e) {
      //do nothing.
    } finally {
      controller = Optional.empty();
    }
  }

  @OnMessage
  public void onMessage(Session session, String message) { //todo: auslagern?
    //following is test Code.
    Question[] questions = new Question[] {
      new Question("Correct: b", "a", "d", "c", "b", -100, 100),
      new Question("Correct: a", "d", "b", "c", "a", -100, 100),
      new Question("Correct: d", "a", "b", "c", "d", -100, 100)
    };
    game.loadGame(questions);
    //process, for example next Question and previus question. Note: the controller wants to know how many questions are left.
  }

}
