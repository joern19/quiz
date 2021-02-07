package tech.hirschfeld.quiz.events.player;

import tech.hirschfeld.quiz.game.Game;
import tech.hirschfeld.quiz.player.Player;

public class PlayerJoinEvent extends PlayerEvent {

    public PlayerJoinEvent(Game currentGame, Player p) {
        super(currentGame, p);
    }

}
