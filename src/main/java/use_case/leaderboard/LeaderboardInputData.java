package use_case.leaderboard;

import entity.User;

/**
 * The Input Data for the Leaderboard.
 */

public class LeaderboardInputData {
    private User user = null;
    private LeaderboardType leaderboardType = null;

    public LeaderboardInputData(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public LeaderboardType getLeaderboardType() {
        return this.leaderboardType;
    }
}
