package tech.hirschfeld.quiz.events.game;

import tech.hirschfeld.quiz.game.Game;

public class EveryoneAnswerdEvent extends GameEvent {

    public EveryoneAnswerdEvent(Game currentGame) {
        super(currentGame);
    }
    
}
