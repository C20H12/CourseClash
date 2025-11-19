package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;

public class LeaderboardOutputData {
    private ArrayList<User> topUsers;
    private User currentUser;
    private int currentUserRank;

    public LeaderboardOutputData(ArrayList<User> topUsers, User currentUser, int currentUserRank) {
        this.topUsers = topUsers;
        this.currentUser = currentUser;
        this.currentUserRank = currentUserRank;
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
}
