package interface_adapter.leaderboard;

import entity.User;
import use_case.leaderboard.LeaderboardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeaderboardState {
    private Map<LeaderboardType, ArrayList<User>> leaderboard;
//    private User currentUser;
//    private Map<LeaderboardType, Integer> myRank;

    public LeaderboardState() {
        this.leaderboard = null;
//        this.myRank = null;
//        this.currentUser = null;
    }

    public Map<LeaderboardType, ArrayList<User>> getLeaderboard() {
        return leaderboard;
    }
//    public User getCurrentUser() {
//        return currentUser;
//    }
//    public Map<LeaderboardType, Integer> getMyRank() {
//        return myRank;
//    }

    public void setLeaderboard(Map<LeaderboardType, ArrayList<User>> topUsers) {
        this.leaderboard = topUsers;
    }
//    public void setCurrentUser(User currentUser) {
//        this.currentUser = currentUser;
//    }
//    public void setMyRank(Map<LeaderboardType, Integer> myRank) {
//        this.myRank = myRank;
//    }
}