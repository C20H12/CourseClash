package use_case.MultiPlayer;

import java.util.List;
import java.util.stream.Collectors;

import entity.MultiPlayerGame;
import entity.User;
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;


public class MultiPlayerInteractor implements MultiPlayerInputBoundary {

    private final MultiPlayerOutputBoundary presenter;
    private MultiPlayerAccessInterface dao;
    private MultiPlayerGame game;

    public MultiPlayerInteractor(MultiPlayerOutputBoundary presenter, MultiPlayerAccessInterface dao) {
      this.presenter = presenter;
      this.dao = dao;
    }

    @Override
    public void advance() {
      game.advanceCard();
      if (game.getCardIndex() >= game.getDeck().getDeck().size()) {
        presentEndState("Deck complete");
        return;
      }

      StudyCard card = game.getCurrentCard();
      MultiPlayerOutputData data = new MultiPlayerOutputData();
      data.setCurrentCard(card);
      data.setMessage(String.format("Card %d of %d", game.getCardIndex() + 1, game.getDeck().getCardCount()));
      data.setRoundResult(" ");
      data.setScoreA(game.getHostScore());
      data.setScoreB(game.getGuestScore());
      presenter.presentQuestion(data);
    }

    @Override
    public void endGame() {
      presentEndState("Host ended the match.");
    }

    @Override
    public void showAllDecks() {
      presenter.presentAllDecks(dao.getAllDecks());
    }

    private void presentEndState(String message) {
      MultiPlayerOutputData data = new MultiPlayerOutputData();
      data.setMessage(message);
      data.setRoundResult(message);
      data.setGameOver(true);
      data.setScoreA(game.getHostScore());
      data.setScoreB(game.getGuestScore());
      presenter.presentEndGame(data);
    }

    @Override
    public void startGame(StudyDeck selectedDeck, User host, User guest) {
      this.game = new MultiPlayerGame(host, guest, selectedDeck);
      StudyCard firstCard = game.getCurrentCard();
      MultiPlayerOutputData data = new MultiPlayerOutputData();
      data.setPlayerA(game.getHost().getUserName());
      data.setPlayerB(game.getGuest().getUserName());
      data.setCurrentCard(firstCard);
      data.setMessage(String.format("Card %d of %d", game.getCardIndex() + 1, game.getDeck().getCardCount()));
      data.setRoundResult(" ");
      presenter.presentQuestion(data);
    }

    @Override
    public void chooseAnswer(String option, boolean host) {
      if (game == null || game.isGameOver()) {
        return;
      }

      StudyCard currentCard = game.getCurrentCard();
      String correctAnswer;
      if (currentCard.getSolutionId() < 0 || currentCard.getSolutionId() >= currentCard.getAnswers().size()) {
        correctAnswer = "";
      } else {
        correctAnswer = currentCard.getAnswers().get(currentCard.getSolutionId());
      }

      MultiPlayerOutputData data = new MultiPlayerOutputData();
      data.setPlayerA(game.getHost().getUserName());
      data.setPlayerB(game.getGuest().getUserName());
      data.setMessage(String.format("Card %d of %d", game.getCardIndex() + 1, game.getDeck().getCardCount()));

      if (option.equals(correctAnswer)) {
        if (host) {
          game.incrementScoreFor(game.getHost().getUserName());
        } else {
          game.incrementScoreFor(game.getGuest().getUserName());
        }
        data.setRoundResult("Correct!");
      } else {
        data.setRoundResult("Incorrect! The correct answer was: " + correctAnswer);
      }

      data.setScoreA(game.getHostScore());
      data.setScoreB(game.getGuestScore());

      presenter.presentUpdateScore(data);
    }

    @Override
    public void updateOtherPlayerScore(int score, boolean host) {
      if (game == null || game.isGameOver()) {
        return;
      }
      if (host) {
        game.setGuestScore(score);
      } else {
        game.setHostScore(score);
      }
    }
  
}
