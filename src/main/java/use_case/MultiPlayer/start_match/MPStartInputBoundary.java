package use_case.MultiPlayer.start_match;

import use_case.DataAccessException;

public interface MPStartInputBoundary {
    void execute(MPStartInputData inputData) throws DataAccessException;
}
