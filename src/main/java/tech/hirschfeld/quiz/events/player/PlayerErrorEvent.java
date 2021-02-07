package tech.hirschfeld.quiz.events.player;

import tech.hirschfeld.quiz.game.Game;
import tech.hirschfeld.quiz.player.Player;

public class PlayerErrorEvent extends PlayerEvent {

    public static enum ERROR_TYPE {
        INVALID_MESSAGE,
        WEBSOCKET_ERROR,
    }

    private ERROR_TYPE errorType;
    private String errorMessage;

    public PlayerErrorEvent(Game currentGame, Player p, ERROR_TYPE errorType, String errorMessage) {
        super(currentGame, p);
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public ERROR_TYPE getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
