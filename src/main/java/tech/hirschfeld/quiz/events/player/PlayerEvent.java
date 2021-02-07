package tech.hirschfeld.quiz.events.player;

import tech.hirschfeld.quiz.events.Event;
import tech.hirschfeld.quiz.game.Game;
import tech.hirschfeld.quiz.player.Player;

public class PlayerEvent extends Event {
    
    private Player player;

    public PlayerEvent(Game currentGame, Player p) {
        super(currentGame);
        this.player = p;
    }

    public Player getPlayer() {
        return player;
    }

}
