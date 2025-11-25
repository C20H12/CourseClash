package use_case.leaderboard;

import use_case.DataAccessException;

public interface LeaderboardOutputBoundary {
    void presentLeaderboard(LeaderboardOutputData outputData) throws DataAccessException;
}
