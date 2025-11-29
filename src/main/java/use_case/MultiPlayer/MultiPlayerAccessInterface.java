//Mahir

package use_case.MultiPlayer;

import java.util.List;

import entity.DeckManagement.StudyDeck;
import interface_adapter.user_session.UserSession;

public interface MultiPlayerAccessInterface {

    /**
     * Retrieves a list of all available study decks from the data source.
     * @return A list containing all loaded StudyDeck objects.
     */
    List<StudyDeck> getAllDecks();

    /**
     * Retrieves the current active user session.
     * @return The UserSession object containing current user details.
     */
    UserSession getSession();
}