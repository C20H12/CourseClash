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

        Map<LeaderboardType, ArrayList<User>> leaderboard = state.getLeaderboard();
        Map<LeaderboardType, Integer> myRank = state.getMyRank();
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
