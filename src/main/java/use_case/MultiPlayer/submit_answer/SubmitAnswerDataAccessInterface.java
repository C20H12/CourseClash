//Mahir
package use_case.MultiPlayer.submit_answer;

import entity.MultiPlayerGame;

public interface SubmitAnswerDataAccessInterface {
    void save(MultiPlayerGame game);

    MultiPlayerGame getMultiPlayerGame(String username);
}
