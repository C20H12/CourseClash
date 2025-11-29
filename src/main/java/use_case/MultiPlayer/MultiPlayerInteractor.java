package use_case.MultiPlayer;

import entity.MultiPlayerGame;
import entity.User;
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;

/**
 * Implements the business logic for the multiplayer game mode.
 * Coordinates interactions between the game entity, data access, and presenter.
 */
public class MultiPlayerInteractor implements MultiPlayerInputBoundary {

    private final MultiPlayerOutputBoundary presenter;
    private MultiPlayerAccessInterface dao;
    private MultiPlayerGame game;

    /**
     * Constructs the MultiPlayerInteractor with necessary dependencies.
     * @param presenter The output boundary to present data to the view.
     * @param dao The data access object for retrieving decks.
     */
    public MultiPlayerInteractor(MultiPlayerOutputBoundary presenter, MultiPlayerAccessInterface dao) {
        this.presenter = presenter;
        this.dao = dao;
    }

    /**
     * Advances the game to the next card or concludes the game if the deck is finished.
     */
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

    /**
     * Manually ends the current game session.
     */
    @Override
    public void endGame() {
        presentEndState("Host ended the match.");
    }

    /**
     * Retrieves all available study decks and prepares them for display.
     */
    @Override
    public void showAllDecks() {
        presenter.presentAllDecks(dao.getAllDecks());
    }

    /**
     * Helper method to package and present the final game state.
     * @param message The reason for the game ending or final status.
     */
    private void presentEndState(String message) {
        MultiPlayerOutputData data = new MultiPlayerOutputData();
        data.setMessage(message);
        data.setRoundResult(message);
        data.setGameOver(true);
        data.setScoreA(game.getHostScore());
        data.setScoreB(game.getGuestScore());
        presenter.presentEndGame(data);
    }

    /**
     * Starts a new multiplayer game session with the specified deck and players.
     * @param selectedDeck The deck chosen for the game.
     * @param host The user acting as the host.
     * @param guest The user acting as the guest.
     */
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

    /**
     * Processes a selected answer from a player and updates the game state.
     * @param option The answer string selected by the player.
     * @param host True if the answer is from the host, false if from the guest.
     */
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

    /**
     * Updates the score of the opposing player (typically used for synchronization).
     * @param score The new score to set.
     * @param host True if updating the guest's score (as host), false otherwise.
     */
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
