package tech.hirschfeld.quiz.game;

import com.fasterxml.jackson.annotation.JsonProperty;

import tech.hirschfeld.quiz.endpoints.Message;
import tech.hirschfeld.quiz.player.Player;

public class Scoreboard {

  @JsonProperty("question")
  public String question;

  @JsonProperty("player")
  public Player[] playerStats;

  public Scoreboard(String question, Player... stats) {
    this.question = question;
    this.playerStats = stats;
  }

  public Message wrapInMessage() {
    return new Message("scoreboard", this);
  }

}
