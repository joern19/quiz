package tech.hirschfeld.quiz.events.game;

import tech.hirschfeld.quiz.events.Event;
import tech.hirschfeld.quiz.game.Game;

public class GameEvent extends Event {

    public GameEvent(Game currentGame) {
        super(currentGame);
    }
    
}
