package tech.hirschfeld.quiz.events.board;

import tech.hirschfeld.quiz.endpoints.WebsocketClient;
import tech.hirschfeld.quiz.game.Game;

public class BoardLeaveEvent extends BoardEvent {

    public static enum LEAVE_CAUSE { //TODO: combine Leave cause of BoardLeaveEvent and PlayerLeaveEvent
        ERROR,
        DISCONNECT,
    }

    private LEAVE_CAUSE leaveCause;

    public BoardLeaveEvent(Game currentGame, WebsocketClient board, LEAVE_CAUSE leaveCause) {
        super(currentGame, board);
        this.leaveCause = leaveCause;
    }
    
    public LEAVE_CAUSE getLeaveCause() {
        return leaveCause;
    }

}
