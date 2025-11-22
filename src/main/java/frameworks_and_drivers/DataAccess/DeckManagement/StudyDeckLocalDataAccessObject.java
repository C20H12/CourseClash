// Orchestrates high-level local storage operations for StudyDecks
// Uses SysFileHandler
// Interacts with the use case layer components like DeckManager, DeckBookmarker, etc.
// Archie
package frameworks_and_drivers.DataAccess.DeckManagement;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import use_case.studyDeck.StudyDeckDataAccessInterface;

import java.util.List;
import java.util.ArrayList;

public class StudyDeckLocalDataAccessObject implements StudyDeckDataAccessInterface {

    private final StudyDeckJSONFileHandler fileHandler;

    // Initializing SysFileHandler instance via constructor.
    public StudyDeckLocalDataAccessObject() {
        this.fileHandler = new StudyDeckJSONFileHandler();
    }

    // Save a StudyDeck object to local storage using SysFileHandler.
    // @param deck The StudyDeck object to save.
    public void saveDeck(StudyDeck deck) {
        fileHandler.saveDeck(deck);
    }

    // Get all StudyDeck objects from local storage.
    // @return A List of all StudyDeck objects stored locally.
    public List<StudyDeck> getAllDecks() {
        return fileHandler.loadAllDecks();
    }

    // Get a specific StudyDeck object from local storage by name.
    // @param deckName The name of the deck to retrieve
    // @return The requested StudyDeck object, or null if not found.
    public StudyDeck getDeck(String deckName) {
        return fileHandler.loadDeck(deckName);
    }

    // Delete a StudyDeck object from local storage.
    // @param deckName The name of the deck to delete.
    public void deleteDeck(String deckName) {
        fileHandler.deleteDeck(deckName);
    }

    // Update an existing StudyDeck object in local storage.
    // @param deck The updated StudyDeck object to save.
    public void updateDeck(StudyDeck deck) {
        // Since the deck name might have changed, need to delete the old one and save the new one
        // First, check if a deck with the same name already exists and delete it
        if (fileHandler.deckExists(deck.getTitle())) {
            fileHandler.deleteDeck(deck.getTitle());
        }
        // Then save the updated deck
        fileHandler.saveDeck(deck);
    }

    // Add a new StudyCard to an existing StudyDeck in local storage.
    // @param deckName The name of the deck to modify.
    // @param card The new StudyCard to add.
    public void addCardToDeck(String deckName, StudyCard card) {
        // Load the existing deck
        StudyDeck deck = fileHandler.loadDeck(deckName);
        if (deck != null) {
            // Create a new deck with the added card (since deck is immutable)
            ArrayList<StudyCard> newCards = new ArrayList<>(deck.getDeck());
            newCards.add(card);
            StudyDeck newDeck = new StudyDeck(deck.getTitle(), deck.getDescription(), newCards, deck.getId());
            // Save the updated deck back to storage
            fileHandler.saveDeck(newDeck);
        }
        // If the deck doesn't exist, this method does nothing
    }

    // Remove a StudyCard from an existing StudyDeck in local storage.
    // @param deckName The name of the deck to modify.
    // @param cardIndex The index of the card to remove.
    public void removeCardFromDeck(String deckName, int cardIndex) {
        // Load the existing deck
        StudyDeck deck = fileHandler.loadDeck(deckName);
        if (deck != null && cardIndex >= 0 && cardIndex < deck.getDeck().size()) {
            // Create a new deck without the card at the specified index
            ArrayList<StudyCard> newCards = new ArrayList<>(deck.getDeck());
            newCards.remove(cardIndex);
            StudyDeck newDeck = new StudyDeck(deck.getTitle(), deck.getDescription(), newCards, deck.getId());
            // Save the updated deck back to storage
            fileHandler.saveDeck(newDeck);
        }
        // If the deck doesn't exist or index is invalid, this method does nothing
    }

    // Replace an existing StudyCard in a StudyDeck in local storage.
    // @param deckName The name of the deck to modify.
    // @param cardIndex The index of the card to replace.
    // @param newCard The new StudyCard to put in its place.
    public void updateCardInDeck(String deckName, int cardIndex, StudyCard newCard) {
        // Load the existing deck
        StudyDeck deck = fileHandler.loadDeck(deckName);
        if (deck != null && cardIndex >= 0 && cardIndex < deck.getDeck().size()) {
            // Create a new deck with the updated card
            ArrayList<StudyCard> newCards = new ArrayList<>(deck.getDeck());
            newCards.set(cardIndex, newCard);
            StudyDeck newDeck = new StudyDeck(deck.getTitle(), deck.getDescription(), newCards, deck.getId());
            // Save the updated deck back to storage
            fileHandler.saveDeck(newDeck);
        }
        // If the deck doesn't exist or index is invalid, this method does nothing
    }

    // Rename an existing StudyDeck in local storage.
    // @param oldDeckName The current name of the deck.
    // @param newDeckName The new name for the deck.
    public void renameDeck(String oldDeckName, String newDeckName) {
        // Load the existing deck
        StudyDeck deck = fileHandler.loadDeck(oldDeckName);
        if (deck != null) {
            // Create a new deck with the updated title
            StudyDeck newDeck = new StudyDeck(newDeckName, deck.getDescription(),
                    new ArrayList<>(deck.getDeck()), deck.getId());
            // Delete the old deck file
            fileHandler.deleteDeck(oldDeckName);
            // Save the deck with the new name
            fileHandler.saveDeck(newDeck);
        }
        // If the deck doesn't exist, this method does nothing
    }
}