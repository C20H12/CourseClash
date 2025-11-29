package interface_adapter.leaderboard;

import use_case.DataAccessException;
import use_case.leaderboard.LeaderboardInputBoundary;
import use_case.leaderboard.LeaderboardInputData;

public class LeaderboardController {
    private final LeaderboardInputBoundary leaderboardInputBoundary;

    public LeaderboardController(LeaderboardInputBoundary leaderboardInputBoundary) {
        this.leaderboardInputBoundary = leaderboardInputBoundary;
    }

    public void loadLeaderboard(int leaderboardEntryCount) throws DataAccessException {
        LeaderboardInputData inputData = new LeaderboardInputData(leaderboardEntryCount);
        leaderboardInputBoundary.execute(inputData);
    }
}
