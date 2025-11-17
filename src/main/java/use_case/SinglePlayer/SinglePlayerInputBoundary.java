// huzaifa - start game, end game, submit answer
package use_case.SinglePlayer;

public interface SinglePlayerInputBoundary {
    void startGame(SinglePlayerInputData inputData);
    void submitAnswer(String answer);
    void endGame();
}

