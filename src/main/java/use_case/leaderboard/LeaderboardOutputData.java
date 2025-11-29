package use_case.leaderboard;

import java.util.ArrayList;
import java.util.Map;

import entity.User;

public class LeaderboardOutputData {
    private final Map<LeaderboardType, ArrayList<User>> leaderboard;

    public LeaderboardOutputData(Map<LeaderboardType, ArrayList<User>> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public Map<LeaderboardType, ArrayList<User>> getLeaderboard() {
        return leaderboard;
    }
}
