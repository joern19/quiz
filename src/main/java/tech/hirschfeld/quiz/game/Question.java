package tech.hirschfeld.quiz.game;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class Question {

  private String question;

  private String a1;
  private String a2;
  private String a3;
  private String correct;

  private Integer scoreOnLose;
  private Integer scoreOnWin;

  //TODO: vscode did not show that question was unused!

  /**
   * 
   * @param answer1 a wrong answer
   * @param answer2 a wrong answer
   * @param answer3 a wrong answer
   * @param correct the correct answer
   */
  public Question(String question, String answer1, String answer2, String answer3, String correct, Integer scoreOnLose, Integer scoreOnWin) {
    a1 = answer1;
    a2 = answer2;
    a3 = answer3;
    this.correct = correct;
    this.scoreOnLose = scoreOnLose;
    this.scoreOnWin = scoreOnWin;
    this.question = question;
  }

  public String[] getAnswersInRandomOrder() {
    List<String> answers = Arrays.asList(new String[] {a1, a2, a3, correct});
    Collections.shuffle(answers);
    return answers.toArray(new String[answers.size()]);
  }

  public String getQuestion() {
    return question;
  }

  public boolean isCorrect(String answer) {
    return correct == answer;
  }

  public Integer getScoreOnWin() {
    return scoreOnWin;
  }

  public Integer getScoreOnLose() {
    return scoreOnLose;
  }
}
