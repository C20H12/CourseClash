// huzaifa - start game, end game, submit answer
package use_case.SinglePlayer;

import use_case.DataAccessException;

public interface SinglePlayerInputBoundary {
    void startGame(SinglePlayerInputData inputData) throws DataAccessException;
    void submitAnswer(String answer) throws DataAccessException;
    void endGame() throws DataAccessException;
}

