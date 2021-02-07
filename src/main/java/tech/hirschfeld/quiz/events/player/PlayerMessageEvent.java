package tech.hirschfeld.quiz.events.player;

import tech.hirschfeld.quiz.endpoints.Message;
import tech.hirschfeld.quiz.game.Game;
import tech.hirschfeld.quiz.player.Player;

public class PlayerMessageEvent extends PlayerEvent {

    private Message message;

    public PlayerMessageEvent(Game currentGame, Player p, Message message) {
        super(currentGame, p);
        this.message = message;
    }
    
    public Message getMessage() {
        return message;
    }

}
