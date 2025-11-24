package interface_adapter.MultiPlayer;

import java.util.List;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;


public class MultiPlayerGameState {

  private String playerA = "Host";
  private String playerB = "Guest";
  private int scoreA;
  private int scoreB;
  private String message = "Waiting to start";
  private StudyCard currentCard;
  private boolean gameOver;
  private int countdownSeconds;
  private String roundResult = "";

  private List<StudyDeck> availableDecks;

  public List<StudyDeck> getAvailableDecks() {
    return availableDecks;
  }

  public void setAvailableDecks(List<StudyDeck> availableDecks) {
    this.availableDecks = availableDecks;
  }

  public String getPlayerA() {
    return playerA;
  }

  public void setPlayerA(String playerA) {
    this.playerA = playerA;
  }

  public String getPlayerB() {
    return playerB;
  }

  public void setPlayerB(String playerB) {
    this.playerB = playerB;
  }

  public int getScoreA() {
    return scoreA;
  }

  public void setScoreA(int scoreA) {
    this.scoreA = scoreA;
  }

  public int getScoreB() {
    return scoreB;
  }

  public void setScoreB(int scoreB) {
    this.scoreB = scoreB;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public StudyCard getCurrentCard() {
    return currentCard;
  }

  public void setCurrentCard(StudyCard currentCard) {
    this.currentCard = currentCard;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
  }

  public int getCountdownSeconds() {
    return countdownSeconds;
  }

  public void setCountdownSeconds(int countdownSeconds) {
    this.countdownSeconds = Math.max(0, countdownSeconds);
  }

  public String getRoundResult() {
    return roundResult;
  }

  public void setRoundResult(String roundResult) {
    this.roundResult = roundResult == null ? "" : roundResult;
  }

  public String toString() {
    return "MultiPlayerGameState{" +
        "playerA='" + playerA + '\n' +
        ", playerB='" + playerB + '\n' +
        ", scoreA=" + scoreA +
        ", scoreB=" + scoreB +
        ", message='" + message + '\n' +
        ", currentCard=" + currentCard +
        ", gameOver=" + gameOver +
        ", countdownSeconds=" + countdownSeconds +
        ", roundResult='" + roundResult + '\n' +
        ", availableDecks=" + availableDecks +
        '}';
  }
}
