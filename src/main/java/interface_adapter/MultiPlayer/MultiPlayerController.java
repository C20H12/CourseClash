package interface_adapter.MultiPlayer;

import entity.User;
import entity.DeckManagement.StudyDeck;
import use_case.MultiPlayer.MultiPlayerInputBoundary;

public class MultiPlayerController {
  final MultiPlayerInputBoundary interactor;

  public MultiPlayerController(MultiPlayerInputBoundary interactor) {
    this.interactor = interactor;
  }

  public void advance() {
    interactor.advance();
  }
  public void endGame() {
    interactor.endGame();
  }
  public void showAllDecks() {
    interactor.showAllDecks();
  }

  public void startGame(StudyDeck selectedDeck, User host, User guest) {
    interactor.startGame(selectedDeck, host, guest);
  }

  public void chooseAnswer(String option, boolean host) {
    interactor.chooseAnswer(option, host);
  }

  public void updateOtherPlayerScore(int score, boolean host) {
    interactor.updateOtherPlayerScore(score, host);
  }
}
