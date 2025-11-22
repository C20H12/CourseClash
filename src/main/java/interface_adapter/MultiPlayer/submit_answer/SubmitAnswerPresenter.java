//Mahir
package interface_adapter.MultiPlayer.submit_answer;

import interface_adapter.MultiPlayer.MultiPlayerState;
import interface_adapter.MultiPlayer.MultiPlayerViewModel;
import interface_adapter.ViewManagerModel;
import use_case.MultiPlayer.submit_answer.SubmitAnswerOutputBoundary;
import use_case.MultiPlayer.submit_answer.SubmitAnswerOutputData;

public class SubmitAnswerPresenter implements SubmitAnswerOutputBoundary {
    private final MultiPlayerViewModel multiPlayerViewModel;
    private final ViewManagerModel viewManagerModel;

    public SubmitAnswerPresenter(MultiPlayerViewModel multiPlayerViewModel, ViewManagerModel viewManagerModel) {
        this.multiPlayerViewModel = multiPlayerViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(SubmitAnswerOutputData response) {
        System.out.println("PRESENTER: Updating View State...");
        MultiPlayerState state = multiPlayerViewModel.getState();

        state.setPlayerA(response.getPlayerA());
        state.setPlayerB(response.getPlayerB());

        state.setScoreA(response.getPlayer1Score());
        state.setScoreB(response.getPlayer2Score());

        if (response.isGameOver()) {
            state.setIsGameOver(true);

            String winnerMessage;
            int score1 = response.getPlayer1Score();
            int score2 = response.getPlayer2Score();
            String p1Name = state.getPlayerA();
            String p2Name = state.getPlayerB();

            if (score1 > score2) winnerMessage = "🏆 " + p1Name + " WINS!";
            else if (score2 > score1) winnerMessage = "🏆 " + p2Name + " WINS!";
            else winnerMessage = "🤝 IT'S A TIE!";

            state.setMessage("GAME OVER! " + winnerMessage);
            state.setCurrentCard(null);

        } else {
            state.setIsGameOver(false);
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

        String currentView = viewManagerModel.getState();
        String gameViewName = multiPlayerViewModel.getViewName();

        if (!gameViewName.equals(currentView)) {
            System.out.println("PRESENTER: Auto-switching to " + gameViewName);
            viewManagerModel.setState(gameViewName);
            viewManagerModel.firePropertyChange("view");
        }
    }

    @Override
    public void prepareFailView(String error) {
        System.out.println("PRESENTER Error: " + error);
        MultiPlayerState state = multiPlayerViewModel.getState();
        state.setMessage("Error: " + error);
        multiPlayerViewModel.firePropertyChange();
    }
}