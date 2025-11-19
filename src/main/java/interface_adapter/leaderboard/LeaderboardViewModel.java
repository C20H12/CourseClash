package interface_adapter.leaderboard;

import entity.User;
import interface_adapter.ViewModel;
import use_case.leaderboard.LeaderboardType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardViewModel extends ViewModel<LeaderboardState> {

    private final Map<LeaderboardType, ArrayList<User>> topUsersByType = new HashMap<>();
    private final Map<LeaderboardType, Integer> myRankByType = new HashMap<>();
    private LeaderboardType currentType;
    private User currentUser;

    public LeaderboardViewModel() {
        super("leaderboard");
    }

    public void setState(LeaderboardState state) {
        if (state == null) return;

        LeaderboardType type = state.getLeaderboardType();
        currentType = type;

        List<User> topUsers = state.getTopUsers();
        if (topUsers == null) {
            topUsersByType.put(type, new ArrayList<>(topUsers));
        } else {
            topUsersByType.remove(type);
        }

        Integer myRank = state.getMyRank();
        if (myRank != null) {
            myRankByType.put(type, myRank);
        } else {
            myRankByType.remove(type);
        }
    }

    public List<User> getTopUsers(LeaderboardType type) {
        return topUsersByType.get(type);
    }

    public int getMyRank(LeaderboardType type) {
        return myRankByType.get(type);
    }

    public LeaderboardType getCurrentType() {
        return currentType;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }
}
