// Handles manual card creation logic in the Use Case layer.
// Archie
package use_case.DeckManagement;

import entity.DeckManagement.StudyCard;
import java.util.ArrayList;

// TODO Implements the Builder pattern from Clean Architecture.
public class CardCreator {

    // Init CardCreator via constructor
    public CardCreator() {
        // No init needed.
    }

    // TODO Create a new StudyCard with the specified question, answers, and correct answer index.
    // @param question The question text for the new card.
    // @param answers The list of answer options for the new card.
    // @param correctAnswerIndex The index of the correct answer in the answers list.
    // @return A new StudyCard object constructed with the provided parameters.
    public StudyCard createCard(String question, ArrayList<String> answers, int correctAnswerIndex) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Validate that the provided card parameters are valid.
    // @param question The question text to validate.
    // @param answers The list of answer options to validate.
    // @param correctAnswerIndex The index of the correct answer to validate.
    // @return True if all parameters are valid, false otherwise.
    public boolean validateCardParameters(String question, ArrayList<String> answers, int correctAnswerIndex) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Create a StudyCard with default values for initialization purposes.
    // @return A StudyCard object with placeholder values.
    public StudyCard createDefaultCard() {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Update an existing StudyCard with new question, answers, and correct answer index.
    // @param existingCard The StudyCard object to update.
    // @param question The new question text for the card.
    // @param answers The new list of answer options for the card.
    // @param correctAnswerIndex The new index of the correct answer in the answers list.
    public void updateCard(StudyCard existingCard, String question, ArrayList<String> answers, int correctAnswerIndex) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Clone an existing StudyCard to create a new one with the same content.
    // @param originalCard The StudyCard to clone.
    // @return A new StudyCard object with the same content as the original.
    public StudyCard cloneCard(StudyCard originalCard) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }
}

