//Mahir
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

        state.setScoreA(response.getPlayer1Score());
        state.setScoreB(response.getPlayer2Score());

        if (response.isGameOver()) {
            state.setIsGameOver(true);

            // --- Determine Winner Logic ---
            String winnerMessage;
            int score1 = response.getPlayer1Score();
            int score2 = response.getPlayer2Score();
            String playerA = state.getPlayerA();
            String playerB = state.getPlayerB();

            if (score1 > score2) {
                winnerMessage = "🏆 " + playerA + " WINS!";
            } else if (score2 > score1) {
                winnerMessage = "🏆 " + playerB + " WINS!";
            } else {
                winnerMessage = "🤝 IT'S A TIE!";
            }

            state.setMessage("GAME OVER! " + winnerMessage + " Final Scores: " + score1 + " - " + score2);
            state.setCurrentCard(null);

        } else {
            state.setCurrentTurnUser(response.getCurrentTurnUser());
            state.setCurrentCard(response.getNextCard());

            if (response.isCorrect()) {
                state.setMessage("Correct!");
            } else {
                state.setMessage("Wrong! Answer was: " + response.getCorrectAnswer());
            }
        }

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