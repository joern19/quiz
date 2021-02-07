package tech.hirschfeld.quiz.events.player;

import javax.websocket.Session;

import tech.hirschfeld.quiz.events.Event;
import tech.hirschfeld.quiz.game.Game;

public class PlayerPreJoinEvent extends Event {

    private Session session;

    public PlayerPreJoinEvent(Game currentGame, Session session) {
        super(currentGame);
        this.session = session;
    }

    public Session getSessionOfPlayer() {
        return session;
    }
    
}
