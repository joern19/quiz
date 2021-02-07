package tech.hirschfeld.quiz.endpoints.boardEndpoint;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import tech.hirschfeld.quiz.endpoints.WebsocketClient;
import tech.hirschfeld.quiz.events.board.BoardEvent;
import tech.hirschfeld.quiz.events.board.BoardJoinEvent;
import tech.hirschfeld.quiz.events.board.BoardLeaveEvent;
import tech.hirschfeld.quiz.events.board.BoardLeaveEvent.LEAVE_CAUSE;
import tech.hirschfeld.quiz.game.Game;

/**
 * Cannot name endpoint board for some reason. Using monitor instead.
 */
@ApplicationScoped
@ServerEndpoint("/monitor")
public class BoardEndpoint {

  @Inject
  BoardManager boardManager;

  @Inject
  Game game;

  @Inject
  @Any
  Event<BoardEvent> boardEvent;

  @OnOpen
  public void onOpen(Session session) {
    boardEvent.fire(new BoardJoinEvent(game, new WebsocketClient(session)));
  }

  @OnClose
  public void onClose(Session session) {
    boardManager.getBoardBySessionOptional(session.getId()).ifPresent((board) -> {
      boardEvent.fire(new BoardLeaveEvent(game, board, LEAVE_CAUSE.DISCONNECT));
    });
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    Logger.getLogger(BoardEndpoint.class.getName()).warning("onError threw: " + throwable.getMessage());

    boardManager.getBoardBySessionOptional(session.getId()).ifPresent((board) -> {
      boardEvent.fire(new BoardLeaveEvent(game, board, LEAVE_CAUSE.ERROR));
    });
  }

}
