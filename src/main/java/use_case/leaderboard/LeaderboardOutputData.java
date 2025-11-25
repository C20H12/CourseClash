package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;
import java.util.Map;

public class LeaderboardOutputData {
    private final Map<LeaderboardType, ArrayList<User>> leaderboard;

    public LeaderboardOutputData(Map<LeaderboardType, ArrayList<User>> leaderboard) {
        this.leaderboard = leaderboard;
    }
    public Map<LeaderboardType, ArrayList<User>> getLeaderboard() {
        return leaderboard;
    }
}
