package use_case.studyDeck;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;

import java.util.List;

public interface StudyDeckDataAccessInterface {
    // Save a StudyDeck object to local storage using SysFileHandler.
    // @param deck The StudyDeck object to save.
    void saveDeck(StudyDeck deck);

    // Get all StudyDeck objects from local storage.
    // @return A List of all StudyDeck objects stored locally.
    List<StudyDeck> getAllDecks();

    // Get a specific StudyDeck object from local storage by name.
    // @param deckName The name of the deck to retrieve
    // @return The requested StudyDeck object, or null if not found.
    StudyDeck getDeck(String deckName);

    // Delete a StudyDeck object from local storage.
    // @param deckName The name of the deck to delete.
    void deleteDeck(String deckName);

    // Update an existing StudyDeck object in local storage.
    // @param deck The updated StudyDeck object to save.
    void updateDeck(StudyDeck deck);

    // Add a new StudyCard to an existing StudyDeck in local storage.
    // @param deckName The name of the deck to modify.
    // @param card The new StudyCard to add.
    void addCardToDeck(String deckName, StudyCard card);

    // Remove a StudyCard from an existing StudyDeck in local storage.
    // @param deckName The name of the deck to modify.
    // @param cardIndex The index of the card to remove.
    void removeCardFromDeck(String deckName, int cardIndex);

    // Replace an existing StudyCard in a StudyDeck in local storage.
    // @param deckName The name of the deck to modify.
    // @param cardIndex The index of the card to replace.
    // @param newCard The new StudyCard to put in its place.
    void updateCardInDeck(String deckName, int cardIndex, StudyCard newCard);

    // Rename an existing StudyDeck in local storage.
    // @param oldDeckName The current name of the deck.
    // @param newDeckName The new name for the deck.
    void renameDeck(String oldDeckName, String newDeckName);
}
