package tech.hirschfeld.quiz.events.board;

import tech.hirschfeld.quiz.endpoints.WebsocketClient;
import tech.hirschfeld.quiz.events.Event;
import tech.hirschfeld.quiz.game.Game;

public class BoardEvent extends Event {

    private WebsocketClient board;

    public BoardEvent(Game currentGame, WebsocketClient board) {
        super(currentGame);
        this.board = board;
    }
    
    public WebsocketClient getBoard() {
        return board;
    } 

}
