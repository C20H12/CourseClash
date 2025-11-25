package use_case.leaderboard;

import entity.User;

/**
 * The Input Data for the Leaderboard.
 */

public class LeaderboardInputData {
    private int leaderboardEntryCount;

    public LeaderboardInputData(int leaderboardEntryCount) {
        this.leaderboardEntryCount = leaderboardEntryCount;
    }

    public int getLeaderboardEntryCount() {
        return leaderboardEntryCount;
    }
}
