package tech.hirschfeld.quiz.events.board;

import tech.hirschfeld.quiz.endpoints.WebsocketClient;
import tech.hirschfeld.quiz.game.Game;

public class BoardJoinEvent extends BoardEvent {

    public BoardJoinEvent(Game currentGame, WebsocketClient board) {
        super(currentGame, board);
        
    }
    
}
