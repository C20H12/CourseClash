package use_case.MultiPlayer;

import java.util.List;

import entity.DeckManagement.StudyDeck;

public interface MultiPlayerOutputBoundary {

    /**
     * Prepares the view to display a question to the players.
     * @param data The output data containing the current card and question information.
     */
    void presentQuestion(MultiPlayerOutputData data);

    /**
     * Prepares the view to display the final game results and winner.
     * @param data The output data containing final scores and game over status.
     */
    void presentEndGame(MultiPlayerOutputData data);

    /**
     * Prepares the view to display the list of available study decks for selection.
     * @param decks The list of StudyDeck objects available to play.
     */
    void presentAllDecks(List<StudyDeck> decks);

    /**
     * Prepares the view to update the displayed scores and round results after a turn.
     * @param data The output data containing the updated scores and result message.
     */
    void presentUpdateScore(MultiPlayerOutputData data);
    
    /**
     * Prepares the view to update the displayed scores and round results after a turn.
     * @param data The output data containing the updated scores and result message.
     */
    void presentSubmitAnswer(MultiPlayerOutputData data);
}
