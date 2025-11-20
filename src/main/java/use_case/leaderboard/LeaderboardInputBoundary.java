package use_case.leaderboard;

import use_case.DataAccessException;

public interface LeaderboardInputBoundary {
    void execute(LeaderboardInputData inputData) throws DataAccessException;
}
