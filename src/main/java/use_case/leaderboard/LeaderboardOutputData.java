package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;
import java.util.Map;

public class LeaderboardOutputData {
    private Map<LeaderboardType, ArrayList<User>> leaderboard;
//    private User currentUser;
//    private Map<LeaderboardType, Integer> currentUserRank;

    public LeaderboardOutputData(Map<LeaderboardType, ArrayList<User>> leaderboard) {
        this.leaderboard = leaderboard;
    }
    public Map<LeaderboardType, ArrayList<User>> getLeaderboard() {
        return leaderboard;
    }
//    public User getCurrentUser() {
//        return currentUser;
//    }
//    public Map<LeaderboardType, Integer> getCurrentUserRank() {
//        return currentUserRank;
//    }
}
