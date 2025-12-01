package use_case.MultiPlayer;

import entity.User;
import entity.DeckManagement.StudyDeck;

public interface MultiPlayerInputBoundary {

    /**
     * Proceeds to the next card in the deck or ends the game if the deck is complete.
     */
    void advance();

    /**
     * Terminates the current multiplayer session manually.
     */
    void endGame();

    /**
     * Retrieves and displays the list of available study decks.
     */
    void showAllDecks();

    /**
     * Initializes a new multiplayer game session with the specified settings.
     * @param selectedDeck The deck chosen for the game.
     * @param host The user acting as the host.
     * @param guest The user acting as the guest.
     */
    void startGame(StudyDeck selectedDeck, User host, User guest);

    /**
     * Processes the user's answer selection for the current card.
     * @param option The answer string selected by the player.
     * @param host True if the answer is from the host, false if from the guest.
     */
    void chooseAnswer(String option, boolean host);

    /**
     * Updates the score for the opposing player to maintain synchronization.
     * @param score The score to update.
     * @param host True if the update is for the guest (context dependent), false otherwise.
     */
    void updateScore(int scoreHost, int scoreGuest);
}
