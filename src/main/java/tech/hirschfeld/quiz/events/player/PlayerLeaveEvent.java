package tech.hirschfeld.quiz.events.player;

import tech.hirschfeld.quiz.game.Game;
import tech.hirschfeld.quiz.player.Player;

/**
 * Called when a Player disconnects. Ignoring if the Player disconnects due to
 * an error or not. (you can check LeaveCause with getLeaveCause())
 */
public class PlayerLeaveEvent extends PlayerEvent {

    public static enum LEAVE_CAUSE {
        ERROR,
        DISCONNECT,
    }

    private LEAVE_CAUSE leaveCause;

    public PlayerLeaveEvent(Game currentGame, Player p, LEAVE_CAUSE leaveCause) {
        super(currentGame, p);
        this.leaveCause = leaveCause;
    }
    
    public LEAVE_CAUSE getLeaveCause() {
        return leaveCause;
    }

}
