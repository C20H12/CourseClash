// huzaifa - present question, present results, error
package use_case.SinglePlayer;

import java.util.List;

import entity.DeckManagement.StudyDeck;

public interface SinglePlayerOutputBoundary {
    void presentQuestion(SinglePlayerOutputData outputData);
    void presentResults(SinglePlayerOutputData outputData);
    void presentError(String message);
    void presentAllDecks(List<StudyDeck> names);
}

