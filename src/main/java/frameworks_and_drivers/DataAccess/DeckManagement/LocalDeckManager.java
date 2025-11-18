// Orchestrates high-level local storage operations for StudyDecks.
// Uses SysFileHandler
// Implements DeckDataAccessInterface
// Interfaces with the use case layer components like DeckManager, DeckBookmarker, etc.
// Archie
package frameworks_and_drivers.DataAccess.DeckManagement;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import frameworks_and_drivers.DataAccess.DeckManagement.JSON.SysFileHandler;

import java.util.List;

public class LocalDeckManager {

    private final SysFileHandler fileHandler;

    // Init LocalDeckManager via constructor with SysFileHandler instance
    public LocalDeckManager() {
        this.fileHandler = new SysFileHandler();
    }

    // TODO Save a StudyDeck object to local storage using SysFileHandler.
    // @param deck The StudyDeck object to save.
    public void saveDeck(StudyDeck deck) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Get all StudyDeck objects from local storage.
    // @return A List of all StudyDeck objects stored locally.
    public List<StudyDeck> getAllDecks() {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Get a specific StudyDeck object from local storage by name.
    // @param deckName The name of the deck to retrieve
    // @return The requested StudyDeck object, or null if not found.
    public StudyDeck getDeck(String deckName) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Delete a StudyDeck object from local storage.
    // @param deckName The name of the deck to delete.
    public void deleteDeck(String deckName) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Update an existing StudyDeck object in local storage.
    // @param deck The updated StudyDeck object to save.
    public void updateDeck(StudyDeck deck) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Add a new StudyCard to an existing StudyDeck in local storage.
    // @param deckName The name of the deck to modify.
    // @param card The new StudyCard to add.
    public void addCardToDeck(String deckName, StudyCard card) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Remove a StudyCard from an existing StudyDeck in local storage.
    // @param deckName The name of the deck to modify.
    // @param cardIndex The index of the card to remove.
    public void removeCardFromDeck(String deckName, int cardIndex) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Replace an existing StudyCard in a StudyDeck in local storage.
    // @param deckName The name of the deck to modify.
    // @param cardIndex The index of the card to replace.
    // @param newCard The new StudyCard to put in its place.
    public void updateCardInDeck(String deckName, int cardIndex, StudyCard newCard) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Rename an existing StudyDeck in local storage.
    // @param oldDeckName The current name of the deck.
    // @param newDeckName The new name for the deck.
    public void renameDeck(String oldDeckName, String newDeckName) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }
}
