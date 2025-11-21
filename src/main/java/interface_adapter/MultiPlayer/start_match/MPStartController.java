//Mahir
package interface_adapter.MultiPlayer.start_match;

import use_case.DataAccessException;
import use_case.MultiPlayer.start_match.MPStartInputBoundary;
import use_case.MultiPlayer.start_match.MPStartInputData;

public class MPStartController {
    final MPStartInputBoundary interactor;

    public MPStartController(MPStartInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String player1, String player2, String deckName) throws DataAccessException {
        MPStartInputData inputData = new MPStartInputData(player1, player2, deckName);

        interactor.execute(inputData);
    }
}