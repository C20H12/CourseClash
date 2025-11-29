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

	public MultiPlayerPresenter(MultiPlayerViewModel viewModel) {
		this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
	}

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

	@Override
	public void presentEndGame(MultiPlayerOutputData data) {
		MultiPlayerGameState state = ensureState();
		applyCommon(state, data);
		state.setGameOver(data.isGameOver());
		state.setCurrentCard(null);
		state.setCountdownSeconds(0);
		state.setRoundResult(defaultString(data.getRoundResult(), ""));
		viewModel.setState(state);
		viewModel.firePropertyChange("end");
  }

  @Override
  public void presentSubmitAnswer(MultiPlayerOutputData data) {
    MultiPlayerGameState state = ensureState();
    applyCommon(state, data);
    viewModel.setState(state);
    viewModel.firePropertyChange("submitAnswer");
  }
  
  @Override
  public void presentUpdateScore(MultiPlayerOutputData data) {
    MultiPlayerGameState state = ensureState();
    applyCommon(state, data);
    viewModel.setState(state);
    viewModel.firePropertyChange("updateScore");
  }

	@Override
	public void presentAllDecks(List<StudyDeck> decks) {
		MultiPlayerGameState state = ensureState();
    state.setAvailableDecks(decks);
    viewModel.setState(state);
    viewModel.firePropertyChange("init");
  }

	private MultiPlayerGameState ensureState() {
		MultiPlayerGameState current = viewModel.getState();
		if (current == null) {
			current = new MultiPlayerGameState();
			viewModel.setState(current);
		}
		return current;
	}

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

	private String defaultString(String candidate, String fallback) {
		return candidate == null || candidate.isBlank() ? fallback : candidate;
	}
}
