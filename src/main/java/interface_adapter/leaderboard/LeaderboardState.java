package interface_adapter.leaderboard;

import entity.User;
import use_case.leaderboard.LeaderboardType;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardState {
    private LeaderboardType leaderboardType;
    private ArrayList<User> topUsers;
    private User currentUser;
    private Integer myRank;

    public LeaderboardState() {
        this.leaderboardType = LeaderboardType.LEVEL;
        this.topUsers = new ArrayList<>();
        this.myRank = null;
    }
    public LeaderboardType getLeaderboardType() {
        return leaderboardType;
    }
    public List<User> getTopUsers() {
        return topUsers;
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public Integer getMyRank() {
        return myRank;
    }

    public void setLeaderboardType(LeaderboardType leaderboardType) {
        this.leaderboardType = leaderboardType;
    }
    public void setTopUsers(ArrayList<User> topUsers) {
        this.topUsers = topUsers;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public void setMyRank(Integer myRank) {
        this.myRank = myRank;
    }
}