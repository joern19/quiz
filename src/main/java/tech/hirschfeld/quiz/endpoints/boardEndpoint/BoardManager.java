package tech.hirschfeld.quiz.endpoints.boardEndpoint;

import java.util.LinkedList;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.logging.Logger;

import tech.hirschfeld.quiz.endpoints.WebsocketClient;
import tech.hirschfeld.quiz.events.board.BoardJoinEvent;
import tech.hirschfeld.quiz.events.board.BoardLeaveEvent;
import tech.hirschfeld.quiz.events.board.BoardLeaveEvent.LEAVE_CAUSE;

@ApplicationScoped
public class BoardManager {

  private static final Logger LOGGER = Logger.getLogger(BoardManager.class);

  private LinkedList<WebsocketClient> allBoards = new LinkedList<>();

  public void onBoardJoin(@Observes BoardJoinEvent event) {
    allBoards.add(event.getBoard());
  }

  public void onBoardLeave(@Observes BoardLeaveEvent event) {
    allBoards.remove(event.getBoard());
    if (event.getLeaveCause() == LEAVE_CAUSE.ERROR) {
      LOGGER.debug("A board left due to an error");
    }
  }

  /**
   * @throws NullPointerException if Board could not be found.
   * @param sessionId The id of the Session
   * @return the Board, if found.
   */
  public WebsocketClient getBoardBySession(String sessionId) {
    return allBoards.stream().filter(board -> board.getSession().getId() == sessionId).findFirst().orElseThrow();
  }

  /**
   * 
   * @param sessionId The id of the Session
   * @return the Board inside an Optional.
   */
  public Optional<WebsocketClient> getBoardBySessionOptional(String sessionId) {
    return allBoards.stream().filter(board -> board.getSession().getId() == sessionId).findFirst();
  }

  public void broadcast(Object message) {
    allBoards.forEach(p -> p.sendJson(message));
  }

}
