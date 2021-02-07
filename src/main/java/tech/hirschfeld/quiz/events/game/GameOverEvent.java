package tech.hirschfeld.quiz.events.game;

import tech.hirschfeld.quiz.game.Game;

public class GameOverEvent extends GameEvent {

    public GameOverEvent(Game currentGame) {
        super(currentGame);
    }
    
}
