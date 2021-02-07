package tech.hirschfeld.quiz.endpoints;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.Session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * WebsocketClient
 */
public class WebsocketClient {

  @JsonIgnore
  protected Session session;

  public WebsocketClient(Session session) {
    this.session = session;
  }

  public void sendJson(Object message) {
    try {
      String m = new ObjectMapper().writeValueAsString(message);
      session.getAsyncRemote().sendText(m);
    } catch (JsonProcessingException ex) {
      Logger.getGlobal().log(Level.WARNING, "Error Parsing JSON: " + ex.getMessage());
    }
  }

  public void sendString(String message) {
    session.getAsyncRemote().sendText(message);
  }

  public Session getSession() {
    return session;
  }

}