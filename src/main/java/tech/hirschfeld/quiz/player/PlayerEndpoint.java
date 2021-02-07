package tech.hirschfeld.quiz.player;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tech.hirschfeld.quiz.endpoints.Message;
import tech.hirschfeld.quiz.events.player.PlayerErrorEvent;
import tech.hirschfeld.quiz.events.player.PlayerErrorEvent.ERROR_TYPE;
import tech.hirschfeld.quiz.events.player.PlayerEvent;
import tech.hirschfeld.quiz.events.player.PlayerLeaveEvent;
import tech.hirschfeld.quiz.events.player.PlayerLeaveEvent.LEAVE_CAUSE;
import tech.hirschfeld.quiz.events.player.PlayerMessageEvent;
import tech.hirschfeld.quiz.events.player.PlayerPreJoinEvent;
import tech.hirschfeld.quiz.game.Game;

@ApplicationScoped
@ServerEndpoint("/game")
public class PlayerEndpoint {

  @Inject
  PlayerManager playerManager;

  @Inject
  Game game;

  @Inject
  @Any
  Event<PlayerEvent> playerEvent;

  @Inject
  @Any
  Event<tech.hirschfeld.quiz.events.Event> event;

  @OnOpen
  public void onOpen(Session session) {
    event.fire(new PlayerPreJoinEvent(game, session));
  }

  @OnClose
  public void onClose(Session session) {
    playerManager.getPlayerBySessionOptional(session.getId()).ifPresent((p) -> {
      playerEvent.fire(new PlayerLeaveEvent(game, p, LEAVE_CAUSE.DISCONNECT));
    });
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    Logger.getLogger(PlayerEndpoint.class.getName()).warning("onError threw: " + throwable.getMessage());

    playerManager.getPlayerBySessionOptional(session.getId()).ifPresent((p) -> {
      playerEvent.fire(new PlayerErrorEvent(game, p, ERROR_TYPE.WEBSOCKET_ERROR, throwable.getMessage()));
      playerEvent.fire(new PlayerLeaveEvent(game, p, LEAVE_CAUSE.ERROR));
    });
  }

  @OnMessage
  public void onMessage(Session session, String raw) {
    playerManager.getPlayerBySessionOptional(session.getId()).ifPresent((p) -> {
      try {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(raw, Message.class);

        playerEvent.fire(new PlayerMessageEvent(game, p, message));                
      } catch (JsonProcessingException ex) {
        Logger.getLogger(PlayerEndpoint.class.getName()).warning("message could not be parsed: \"" + raw + "\". The error Message: " + ex.getMessage());
        playerEvent.fire(new PlayerErrorEvent(game, p, ERROR_TYPE.INVALID_MESSAGE, ex.getMessage()));
      }
    }); 
  }

}
