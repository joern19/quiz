package tech.hirschfeld.quiz.events;

import tech.hirschfeld.quiz.game.Game;

public abstract class Event {

    private boolean canceled;
    private Game currentGame;

    public Event(Game currentGame) {
        this.currentGame = currentGame;
    }

    public Game getGame() {
        return currentGame;
    }

    public void setCanceled(boolean isCanceled) {
        this.canceled = isCanceled;
    }

    public boolean isCanceled() {
        return canceled;
    }

}
