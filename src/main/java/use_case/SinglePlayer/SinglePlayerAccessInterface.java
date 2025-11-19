package use_case.SinglePlayer;

import entity.DeckManagement.StudyDeck;
import use_case.DataAccessException;

/**
 * Data access interface for Single Player mode.
 * Used by the SinglePlayerInteractor to load decks
 * and store game results.
 */
public interface SinglePlayerAccessInterface {

    /**
     * Returns true if a deck with this title exists.
     */
    boolean existsDeck(String deckTitle);

    /**
     * Loads the deck with the given title.
     * @return StudyDeck if found
     * @throws DataAccessException if the deck cannot be read
     */
    StudyDeck loadDeck(String deckTitle) throws DataAccessException;

    /**
     * Saves the user's single-player results.
     * @throws DataAccessException if saving fails
     */
    void saveSinglePlayerResult(
            String username,
            String deckTitle,
            int score,
            double accuracy,
            double avgResponseTime
    ) throws DataAccessException;
}
