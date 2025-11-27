// Huzaifa - converts OutputData to view model - Presenter File
// generated sample code from GPT to be worked on
package interface_adapter.SinglePlayer;

import java.util.List;
import java.util.Map;

import entity.DeckManagement.StudyDeck;
import use_case.SinglePlayer.SinglePlayerOutputBoundary;
import use_case.SinglePlayer.SinglePlayerOutputData;

/**
 * Presenter for the Single Player Game use case.
 * Converts OutputData from the interactor into state updates in the ViewModel.
 */
public class SinglePlayerPresenter implements SinglePlayerOutputBoundary {

    private final SinglePlayerViewModel viewModel;

    public SinglePlayerPresenter(SinglePlayerViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentQuestion(SinglePlayerOutputData data) {
        int current = data.getCurrentIndex(); // 1-based
        int total = data.getTotal();
        int questionsLeft = total - current + 1;

        SinglePlayerState state = viewModel.getState();
        state.setQuestionText(data.getQuestionText());
        state.setOptions(data.getOptions());
        state.setCurrentIndex(questionsLeft);
        state.setTotal(data.getTotal());
        state.setScore(data.getScore());
        state.setAccuracy(data.getAccuracy());
        state.setAvgResponseTime(data.getAvgResponseTime());
        state.setGameOver(data.isGameOver());
        state.setMessage(data.getMessage());
        state.setError(null);

        viewModel.setState(state);
        viewModel.firePropertyChange("question");
    }
    @Override
    public void presentResults(SinglePlayerOutputData data) {
        SinglePlayerState state = viewModel.getState();
        state.setScore(data.getScore());
        state.setAccuracy(data.getAccuracy());
        state.setAvgResponseTime(data.getAvgResponseTime());
        state.setMessage(data.getMessage());
        state.setGameOver(true);
        viewModel.firePropertyChange("end");
    }
    @Override
    public void presentError(String message) {
        SinglePlayerState state = viewModel.getState();
        state.setError(message);
        state.setMessage(message);
        viewModel.firePropertyChange("error");
    }

    @Override
    public void presentAllDecks(List<StudyDeck> names) {
        SinglePlayerState state = viewModel.getState();
        state.setDecksList(names);
        viewModel.firePropertyChange("init");
    }
}
