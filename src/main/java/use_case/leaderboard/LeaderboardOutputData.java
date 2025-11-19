package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;
import java.util.Map;

public class LeaderboardOutputData {
    private Map<LeaderboardType, ArrayList<User>> topUsers;
    private User currentUser;
    private Map<LeaderboardType, Integer> currentUserRank;

    public LeaderboardOutputData(Map<LeaderboardType, ArrayList<User>> topUsers, User currentUser,
                                 Map<LeaderboardType, Integer> currentUserRank) {
        this.topUsers = topUsers;
        this.currentUser = currentUser;
        this.currentUserRank = currentUserRank;
    }
    public Map<LeaderboardType, ArrayList<User>> getTopUsers() {
        return topUsers;
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public Map<LeaderboardType, Integer> getCurrentUserRank() {
        return currentUserRank;
    }
}
