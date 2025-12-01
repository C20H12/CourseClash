package interface_adapter.MultiPlayer;

import entity.User;
import entity.DeckManagement.StudyDeck;
import use_case.MultiPlayer.MultiPlayerInputBoundary;

/**
 * Controller for the Multiplayer mode.
 * Receives input from the view/UI and delegates actions to the interactor.
 */
public class MultiPlayerController {
    private final MultiPlayerInputBoundary interactor;

    /**
     * Constructs the MultiPlayerController.
     * @param interactor The input boundary to handle business logic.
     */
    public MultiPlayerController(MultiPlayerInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Triggers the advancement to the next card or state in the game.
     */
    public void advance() {
        interactor.advance();
    }

    /**
     * Triggers the end of the current game session.
     */
    public void endGame() {
        interactor.endGame();
    }

    /**
     * Requests the interactor to retrieve and display all available decks.
     */
    public void showAllDecks() {
        interactor.showAllDecks();
    }

    /**
     * Starts a new multiplayer game with the given parameters.
     * @param selectedDeck The deck selected for gameplay.
     * @param host The user acting as the host.
     * @param guest The user acting as the guest.
     */
    public void startGame(StudyDeck selectedDeck, User host, User guest) {
        interactor.startGame(selectedDeck, host, guest);
    }

    /**
     * Submits a player's answer for the current card.
     * @param option The answer string selected by the user.
     * @param host True if the input comes from the host, false if from the guest.
     */
    public void chooseAnswer(String option, boolean host) {
        interactor.chooseAnswer(option, host);
    }

    /**
     * Updates the score of the other player for synchronization.
     * @param scoreHost The new score to set.
     * @param scoreGuest The new score to set
     */
    public void updateScore(int scoreHost, int scoreGuest) {
        interactor.updateScore(scoreHost, scoreGuest);
    }
}