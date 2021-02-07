package tech.hirschfeld.quiz.player;

import java.util.LinkedList;
import java.util.Optional;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import tech.hirschfeld.quiz.endpoints.Message;
import tech.hirschfeld.quiz.events.game.EveryoneAnswerdEvent;
import tech.hirschfeld.quiz.events.game.GameEvent;
import tech.hirschfeld.quiz.events.player.PlayerAnswerEvent;
import tech.hirschfeld.quiz.events.player.PlayerEvent;
import tech.hirschfeld.quiz.events.player.PlayerJoinEvent;
import tech.hirschfeld.quiz.events.player.PlayerLeaveEvent;
import tech.hirschfeld.quiz.events.player.PlayerMessageEvent;
import tech.hirschfeld.quiz.events.player.PlayerPreJoinEvent;
import tech.hirschfeld.quiz.events.player.PlayerLeaveEvent.LEAVE_CAUSE;
import tech.hirschfeld.quiz.game.Scoreboard;
import tech.hirschfeld.quiz.utils.NameGenerator;

/**
 * This class is not supposed to handle the Game logic.
 * Instead it is built to Manage a list of Player.
 * 
 */
@ApplicationScoped
public class PlayerManager {

  private LinkedList<Player> allPlayer = new LinkedList<>();

  @Inject
  @Any
  Event<PlayerEvent> playerEvent;

  @Inject
  @Any
  Event<GameEvent> gameEvent;

  @Inject
  NameGenerator nameGenerator;

  public PlayerManager() {
  }

  public void onPlayerPreJoin(@Observes PlayerPreJoinEvent event) { //if you want to implement, that the user can choose his name, you could create a list with pending logins.
    Player newP = new Player(event.getSessionOfPlayer(), nameGenerator.getUniqueName());
    allPlayer.add(newP); //does not matter if this is done here or, in the onPlayerJoinEvent
    playerEvent.fire(new PlayerJoinEvent(event.getGame(), newP));
  }

  public void onPlayerLeave(@Observes PlayerLeaveEvent event) {
    allPlayer.removeFirstOccurrence(event.getPlayer());

    if (event.getLeaveCause() == LEAVE_CAUSE.ERROR) {
      broadcast(new Message("alert", event.getPlayer().getName() + " left due to an error."));
    } else if (event.getLeaveCause() == LEAVE_CAUSE.DISCONNECT) {
      broadcast(new Message("alert", event.getPlayer().getName() + " left"));
    }
  }

  public void onPlayerMessage(@Observes PlayerMessageEvent event) {
    switch (event.getMessage().getType()) {
      case "answer":
        //TODO: warning validation need for below.
        playerEvent.fire(new PlayerAnswerEvent(event.getGame(), event.getPlayer(), event.getGame().getCurrentQuestion(), null));
        break;
      default:
        Logger.getLogger(PlayerManager.class.getName()).warning("Message type \"" + event.getMessage().getType() + "\" not found.");
    }
  }

  public void onPlayerAnswer(@Observes PlayerAnswerEvent event) {
    event.getPlayer().answerd(event.getGame().getCurrentQuestion().isCorrect(event.getAnswer()));
    if (didEveryoneAnswered()) {
      gameEvent.fire(new EveryoneAnswerdEvent(event.getGame()));
    }
  }

  public void broadcast(Object message) {
    allPlayer.forEach(p -> p.sendJson(message));
  }

  /**
   * Help function for onPlayerAnswer
   * 
   * @return true if all Players answered.
   */
  private boolean didEveryoneAnswered() {
    return !allPlayer.stream().filter(p -> !p.hasAnswerd()).findFirst().isPresent();
  }

  /**
   * @throws NullPointerException if Player could not be found.
   * @param sessionId The id of the Session
   * @return the Player, if found.
   */
  public Player getPlayerBySession(String sessionId) {
    return allPlayer.stream().filter(p -> p.getSession().getId() == sessionId).findFirst().orElseThrow();
  }

  /**
   * 
   * @param sessionId The id of the Session
   * @return the Player inside an Optional.
   */
  public Optional<Player> getPlayerBySessionOptional(String sessionId) {
    return allPlayer.stream().filter(p -> p.getSession().getId() == sessionId).findFirst();
  }

  public Player[] getWinner() {
    LinkedList<Player> winner = new LinkedList<>();
    int score = Integer.MIN_VALUE;
    for (Player p : allPlayer) {
      if (p.getScore() > score) {
        winner.clear();
        winner.add(p);
      } else if (p.getScore() == score) {
        winner.add(p);
      }
    }
    return winner.toArray(new Player[winner.size()]);
  }

  public Scoreboard generateScoreboard(String question) {
    return new Scoreboard(question, allPlayer.toArray(Player[]::new)); //thank you steffen
  }

}
