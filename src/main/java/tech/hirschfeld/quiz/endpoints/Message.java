package tech.hirschfeld.quiz.endpoints;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message { // TODO: create enum with message types. Already using: alert, question, question_result
                        // And using to revieve: answer,            maybe a Message for parsing input?

  @JsonProperty("type")
  @NotNull
  private String type;

  @JsonProperty("message")
  @NotNull
  private Object message;

  /**
   * 
   * @param type    like question_result or final_winner.
   * @param message like true or Lukas and Kevin.
   */
  public Message(String type, Object message) {
    this.type = type;
    this.message = message;
  }

  public String getType() {
    return type;
  }

  public Object getMessage() {
    return message;
  }

}
