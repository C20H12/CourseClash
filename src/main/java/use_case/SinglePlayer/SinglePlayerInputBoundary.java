// huzaifa - start game, end game, submit answer

package use_case.SinglePlayer;

import use_case.DataAccessException;

public interface SinglePlayerInputBoundary {
    /**
     * Starts a new single-player game session using the provided input data.
     * Responsible for initializing the deck, questions, and tracking state.
     *
     * @param inputData the data required to start the game (deck name, user info, etc.)
     * @throws DataAccessException if the deck or related data cannot be retrieved
     */
    void startGame(SinglePlayerInputData inputData) throws DataAccessException;

    /**
     * Submits the player's answer for the current question.
     * Triggers scoring, progress tracking, and prepares the next question.
     *
     * @param answer the user's selected or typed answer
     * @throws DataAccessException if answer validation or state persistence fails
     */
    void submitAnswer(String answer) throws DataAccessException;

    /**
     * Ends the current game session and finalizes results.
     * Used to compute final score, performance metrics, and store game history.
     *
     * @throws DataAccessException if final game results cannot be saved or processed
     */
    void endGame() throws DataAccessException;

    /**
     * Retrieves the names of all available decks for single-player mode.
     * Used to populate selection menus or display deck options to the user.
     *
     * @throws DataAccessException if deck metadata cannot be accessed or read
     */
    void showAllDeckNames() throws DataAccessException;
}
