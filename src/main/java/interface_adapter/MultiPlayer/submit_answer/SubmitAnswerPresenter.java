package interface_adapter.MultiPlayer.submit_answer;

import interface_adapter.MultiPlayer.MultiPlayerState;
import interface_adapter.MultiPlayer.MultiPlayerViewModel;
import use_case.MultiPlayer.submit_answer.SubmitAnswerOutputBoundary;
import use_case.MultiPlayer.submit_answer.SubmitAnswerOutputData;

public class SubmitAnswerPresenter implements SubmitAnswerOutputBoundary {
    private final MultiPlayerViewModel multiPlayerViewModel;

    public SubmitAnswerPresenter(MultiPlayerViewModel multiPlayerViewModel) {
        this.multiPlayerViewModel = multiPlayerViewModel;
    }

    @Override
    public void prepareSuccessView(SubmitAnswerOutputData response) {
        MultiPlayerState state = multiPlayerViewModel.getState();

        // 1. Always update scores regardless of game state
        state.setScoreA(response.getPlayer1Score());
        state.setScoreB(response.getPlayer2Score());

        // 2. Check for Game Over
        if (response.isGameOver()) {
            state.setMessage("GAME OVER! Final Score - P1: " + response.getPlayer1Score() + " | P2: " + response.getPlayer2Score());
            state.setCurrentCard(null); // Clear the card display
        } else {
            // 3. Game is still running: Update Turn and Card
            state.setCurrentTurnUser(response.getCurrentTurnUser());
            state.setCurrentCard(response.getNextCard());

            // 4. Provide Feedback on the last answer
            if (response.isCorrect()) {
                state.setMessage("Correct!");
            } else {
                state.setMessage("Wrong! The answer was: " + response.getCorrectAnswer());
            }
        }

        // 5. Update ViewModel and notify View
        multiPlayerViewModel.setState(state);
        multiPlayerViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        MultiPlayerState state = multiPlayerViewModel.getState();
        state.setMessage("Error: " + error);
        multiPlayerViewModel.firePropertyChange();
    }
}