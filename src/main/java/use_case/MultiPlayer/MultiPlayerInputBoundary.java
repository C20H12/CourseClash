package use_case.MultiPlayer;

import entity.User;
import entity.DeckManagement.StudyDeck;

public interface MultiPlayerInputBoundary {
  void advance();
  void endGame();
  void showAllDecks();
  void startGame(StudyDeck selectedDeck, User host, User guest);
  void chooseAnswer(String option, boolean host);
  void updateOtherPlayerScore(int score, boolean host);
}
