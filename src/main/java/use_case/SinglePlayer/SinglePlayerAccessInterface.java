package use_case.SinglePlayer;

import java.util.List;

import entity.DeckManagement.StudyDeck;
import use_case.DataAccessException;

/**
 * Data access interface for Single Player mode.
 * Used by the SinglePlayerInteractor to load decks
 * and store game results.
 */
public interface SinglePlayerAccessInterface {

    /**
     * Loads and returns the deck associated with the given title.
     *
     * @param deckTitle the name of the deck to load
     * @return the corresponding StudyDeck if found
     * @throws DataAccessException if the deck cannot be accessed or read
     */
    StudyDeck loadDeck(String deckTitle) throws DataAccessException;

    /**
     * Saves the results of a completed single-player game session.
     * Records score, timing information, and deck usage under the user profile.
     *
     * @param username the user whose results are being stored
     * @param deckTitle the deck used in the game session
     * @param score the final score achieved
     * @param avgResponseTime the player's average answer response time
     * @param accuracy the player's score accuracy
     * @throws DataAccessException if storing the results fails
     */
    void saveSinglePlayerResult(
            String username,
            String deckTitle,
            int score,
            double accuracy,
            double avgResponseTime
    ) throws DataAccessException;

    /**
     * gets a list of all the decks, for initalization
     * @return none
     */
    List<StudyDeck> getAllDecks();
}
