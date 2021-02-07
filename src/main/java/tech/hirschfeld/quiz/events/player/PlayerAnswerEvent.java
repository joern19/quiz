package tech.hirschfeld.quiz.events.player;

import tech.hirschfeld.quiz.game.Game;
import tech.hirschfeld.quiz.game.Question;
import tech.hirschfeld.quiz.player.Player;

public class PlayerAnswerEvent extends PlayerEvent {

    private Question question;
    private String answer;

    public PlayerAnswerEvent(Game currentGame, Player p, Question question, String answer) {
        super(currentGame, p);
        this.question = question;
        this.answer = answer;
    }
    
    public boolean wasAnswerCorrect() {
        return question.isCorrect(answer);
    }

    public String getAnswer() {
        return answer;
    }

    public Question getQuestion() {
        return question;
    }

}
