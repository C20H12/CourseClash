package interface_adapter.leaderboard;

import entity.User;
import use_case.leaderboard.LeaderboardInputBoundary;
import use_case.leaderboard.LeaderboardInputData;
import use_case.leaderboard.LeaderboardType;

public class LeaderboardController {
    private final LeaderboardInputBoundary leaderboardInputBoundary;
    public LeaderboardController(LeaderboardInputBoundary leaderboardInputBoundary) {
        this.leaderboardInputBoundary = leaderboardInputBoundary;
    }

    public void loadLeaderboard(User user) {
        LeaderboardInputData inputData = new LeaderboardInputData(user);
        leaderboardInputBoundary.execute(inputData);
    }
}
