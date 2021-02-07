package tech.hirschfeld.quiz.events.game;

import tech.hirschfeld.quiz.game.Game;

public class NewQuestionOpendEvent extends GameEvent {

    public NewQuestionOpendEvent(Game currentGame) {
        super(currentGame);
    }
    
}
