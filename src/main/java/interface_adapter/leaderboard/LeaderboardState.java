package interface_adapter.leaderboard;

import java.util.Map;
import java.util.ArrayList;

import entity.User;
import use_case.leaderboard.LeaderboardType;

public class LeaderboardState {
    private Map<LeaderboardType, ArrayList<User>> leaderboard;

    public LeaderboardState() {
        this.leaderboard = null;
    }

    public Map<LeaderboardType, ArrayList<User>> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Map<LeaderboardType, ArrayList<User>> topUsers) {
        this.leaderboard = topUsers;
    }
}