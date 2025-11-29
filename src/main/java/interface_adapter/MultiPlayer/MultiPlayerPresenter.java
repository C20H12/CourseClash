package interface_adapter.MultiPlayer;

import java.util.List;
import java.util.Objects;

import entity.DeckManagement.StudyDeck;
import use_case.MultiPlayer.MultiPlayerOutputBoundary;
import use_case.MultiPlayer.MultiPlayerOutputData;

/**
 * Presenter responsible for moving interactor output into the observable view model state.
 */
public class MultiPlayerPresenter implements MultiPlayerOutputBoundary {

    private final MultiPlayerViewModel viewModel;

    /**
     * Constructs the MultiPlayerPresenter with the target view model.
     * @param viewModel The view model to update with game state changes.
     */
    public MultiPlayerPresenter(MultiPlayerViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    /**
     * Updates the view model to display a new question/card to the players.
     * @param data The output data containing the current card and game status.
     */
    @Override
    public void presentQuestion(MultiPlayerOutputData data) {
        MultiPlayerGameState state = ensureState();
        applyCommon(state, data);
        state.setGameOver(false);
        state.setCurrentCard(data.getCurrentCard());
        state.setRoundResult("");
        viewModel.setState(state);
        viewModel.firePropertyChange("question");
    }

    /**
     * Updates the view model to display the game over state and final results.
     * @param data The output data containing final scores and game over messages.
     */
    @Override
    public void presentEndGame(MultiPlayerOutputData data) {
        MultiPlayerGameState state = ensureState();
        applyCommon(state, data);
        state.setGameOver(true);
        state.setCurrentCard(null);
        state.setCountdownSeconds(0);
        state.setRoundResult(defaultString(data.getRoundResult(), ""));
        viewModel.setState(state);
        viewModel.firePropertyChange("end");
    }

    /**
     * Updates the view model with new scores after a round or action.
     * @param data The output data containing the updated scores.
     */
    @Override
    public void presentUpdateScore(MultiPlayerOutputData data) {
        MultiPlayerGameState state = ensureState();
        applyCommon(state, data);
        viewModel.setState(state);
        viewModel.firePropertyChange("update");
    }

    /**
     * Updates the view model with the list of available decks for selection.
     * @param decks The list of StudyDeck objects to display.
     */
    @Override
    public void presentAllDecks(List<StudyDeck> decks) {
        MultiPlayerGameState state = ensureState();
        state.setAvailableDecks(decks);
        viewModel.setState(state);
        viewModel.firePropertyChange("init");
    }

    /**
     * Helper method to retrieve the current state or create a new one if null.
     * @return The current or newly created MultiPlayerGameState.
     */
    private MultiPlayerGameState ensureState() {
        MultiPlayerGameState current = viewModel.getState();
        if (current == null) {
            current = new MultiPlayerGameState();
            viewModel.setState(current);
        }
        return current;
    }

    /**
     * Helper method to map common fields (players, scores, messages) from output data to state.
     * @param state The state object to update.
     * @param data The source output data.
     */
    private void applyCommon(MultiPlayerGameState state, MultiPlayerOutputData data) {
        if (data == null) {
            return;
        }
        if (data.getPlayerA() != null) {
            state.setPlayerA(data.getPlayerA());
        }
        if (data.getPlayerB() != null) {
            state.setPlayerB(data.getPlayerB());
        }
        state.setScoreA(data.getScoreA());
        state.setScoreB(data.getScoreB());
        state.setMessage(defaultString(data.getMessage(), state.getMessage()));
        state.setRoundResult(defaultString(data.getRoundResult(), state.getRoundResult()));
    }

    /**
     * Utility method to return a fallback string if the candidate is null or blank.
     * @param candidate The string to check.
     * @param fallback The string to return if the candidate is invalid.
     * @return The candidate string if valid, otherwise the fallback.
     */
    private String defaultString(String candidate, String fallback) {
        return candidate == null || candidate.isBlank() ? fallback : candidate;
    }
}