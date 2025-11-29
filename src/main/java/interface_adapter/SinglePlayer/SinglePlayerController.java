// Huzaifa – called by the view; builds InputData, calls interactor - Controller

package interface_adapter.SinglePlayer;

import entity.User;
import use_case.DataAccessException;
import use_case.SinglePlayer.SinglePlayerInputBoundary;
import use_case.SinglePlayer.SinglePlayerInputData;

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
     * Handles a request from the view to start a new single-player game.
     * Constructs a {@link SinglePlayerInputData} object using the user's
     * chosen settings and forwards it to the interactor to initialize the
     * game session.
     *
     * @param user the user starting the game
     * @param deckTitle the title of the selected study deck
     * @param timerPerQuestion the time limit (in seconds) for each question
     * @param shuffle whether the question order should be randomized
     * @param numQuestions the number of questions to include in the session
     * @throws DataAccessException if the interactor fails to load the deck
     *                             or initialize the game
     */
    public void startGame(User user, String deckTitle,
                          int timerPerQuestion, boolean shuffle, int numQuestions) throws DataAccessException {

        SinglePlayerInputData inputData = new SinglePlayerInputData(
                user,
                deckTitle,
                timerPerQuestion,
                shuffle,
                numQuestions
        );

        interactor.startGame(inputData);
    }
    /**
     * Submits the user's selected answer to the interactor for validation and
     * scoring. Called by the view when the user confirms their choice for the
     * current question.
     *
     * @param userAnswer the answer selected by the user
     * @throws DataAccessException if answer processing or data retrieval fails
     */

    public void submitAnswer(String userAnswer) throws DataAccessException {
        interactor.submitAnswer(userAnswer);
    }
    /**
     * Signals the interactor to end the current single-player game. Used when
     * the user exits or the session must be terminated early.
     *
     * @throws DataAccessException if shutting down the session involves data
     *                             retrieval or saving that fails
     */

    public void endGame() throws DataAccessException {
        interactor.endGame();
    }

    public void showAllDecks() {
        try {
            interactor.showAllDeckNames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
