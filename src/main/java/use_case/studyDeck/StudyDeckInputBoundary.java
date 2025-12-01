package use_case.studyDeck;

import java.util.ArrayList;

import entity.DeckManagement.StudyDeck;

public interface StudyDeckInputBoundary {
    // Execute a specific study deck action with provided input data
    void execute(StudyDeckInputData inputData, StudyDeckAction action);

    // Create a new empty study deck with title and description
    void createNewDeck(String title, String description);

    // Add a new multiple-choice card to the currently loaded deck
    void addCardToCurrentDeck(String question, ArrayList<String> answers, int correctAnswerIndex);

    // Remove a card at the specified index from the current deck
    void removeCardFromCurrentDeck(int cardIndex);

    // Update an existing card's content at the specified index
    void updateCardInCurrentDeck(int cardIndex, String question, ArrayList<String> answers, int correctAnswerIndex);

    // Persist the current deck to local storage
    void saveCurrentDeck();

    // Load a previously saved deck by its name
    void loadDeck(String deckName);

    // Clear the currently loaded deck from memory
    void unloadDeck();

    // Get the currently loaded study deck object
    StudyDeck getCurrentDeck();
}
