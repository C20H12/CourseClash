// Primary deck management orchestrator in the Use Case layer.
// Uses CardCreator and CardGenerator
// Archie
package use_case.DeckManagement;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import frameworks_and_drivers.DataAccess.DeckManagement.LocalDeckManager;
import java.util.ArrayList;

public class DeckManager {
    private final LocalDeckManager localDeckManager;
    private StudyDeck currentDeck;

    // Constructor that initializes the DeckManager with a LocalDeckManager instance.
    // @param localDeckManager The LocalDeckManager instance to coordinate with for storage operations.
    public DeckManager(LocalDeckManager localDeckManager) {
        this.localDeckManager = localDeckManager;
        this.currentDeck = null;
    }

    // TODO Create a new empty deck with the specified title and description.
    // @param title The title for the new deck.
    // @param description The description for the new deck.
    public void createNewDeck(String title, String description) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Add a new StudyCard to the current deck using CardCreator.
    // @param question The question text for the new card.
    // @param answers The list of answer options for the new card.
    // @param correctAnswerIndex The index of the correct answer in the answers list.
    public void addCardToCurrentDeck(String question, ArrayList<String> answers, int correctAnswerIndex) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Remove a StudyCard from the current deck by index.
    // @param cardIndex The index of the card to remove.
    public void removeCardFromCurrentDeck(int cardIndex) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Update an existing StudyCard in the current deck by index.
    // @param cardIndex The index of the card to update.
    // @param question The new question text for the card.
    // @param answers The new list of answer options for the card.
    // @param correctAnswerIndex The new index of the correct answer in the answers list.
    public void updateCardInCurrentDeck(int cardIndex, String question, ArrayList<String> answers, int correctAnswerIndex) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Save the current deck to local storage via LocalDeckManager.
    public void saveCurrentDeck() {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Load an existing deck from local storage by name.
    // @param deckName The name of the deck to load.
    public void loadDeck(String deckName) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Delete an existing deck from local storage by name.
    // @param deckName The name of the deck to delete.
    public void deleteDeck(String deckName) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Rename the current deck and update it in local storage.
    // @param newDeckName The new name for the current deck.
    public void renameCurrentDeck(String newDeckName) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Switch to working with an existing deck by name.
    // @param deckName The name of the deck to switch to.
    public void switchToDeck(String deckName) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Get the current deck being managed.
    // @return The current StudyDeck object.
    public StudyDeck getCurrentDeck() {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Get a list of all deck names from local storage.
    // @return A list of deck names available in local storage.
    public ArrayList<String> getAllDeckNames() {
        throw new UnsupportedOperationException("Method not yet implemented");
    }
}