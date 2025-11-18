// Huzaifa – called by the view; builds InputData, calls interactor
package interface_adapter.SinglePlayer;

import entity.DeckManagement.StudyDeck;
import use_case.SinglePlayer.SinglePlayerInputBoundary;
import use_case.SinglePlayer.SinglePlayerInputData;
import entity.User;
/**
 * Controller for the Single Player Game use case.
 * Connects the UI (view) to the interactor.
 */
public class SinglePlayerController {
    private final SinglePlayerInputBoundary interactor;

    public SinglePlayerController(SinglePlayerInputBoundary interactor) {
        this.interactor = interactor;
    }
    /**
     * Starts a new single player game with the user’s chosen configuration.
     */
    public void startGame(User user, StudyDeck studyDeck,
                          int timerPerQuestion, boolean shuffle, int numQuestions) {

        SinglePlayerInputData inputData = new SinglePlayerInputData(
                user,
                studyDeck,
                timerPerQuestion,
                shuffle,
                numQuestions
        );

        interactor.startGame(inputData);
    }
    /**
     * Submits an answer to the interactor.
     */
    public void submitAnswer(String userAnswer) {
        interactor.submitAnswer(userAnswer);
    }
    /**
     * Ends the current game prematurely.
     */
    public void endGame() {
        interactor.endGame();
    }
}
