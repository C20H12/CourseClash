package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;

public class LeaderboardOutputData {
    private ArrayList<User> topUsers;
    private User currentUser;
    private int currentUserRank;
    private LeaderboardType leaderboardType;
    public LeaderboardOutputData(ArrayList<User> topUsers, User currentUser, int currentUserRank, LeaderboardType leaderboardType) {
        this.topUsers = topUsers;
        this.currentUser = currentUser;
        this.currentUserRank = currentUserRank;
        this.leaderboardType = leaderboardType;
    }
    public ArrayList<User> getTopUsers() {
        return topUsers;
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public int getCurrentUserRank() {
        return currentUserRank;
    }
    public  LeaderboardType getLeaderboardType() {
        return leaderboardType;
    }
}
