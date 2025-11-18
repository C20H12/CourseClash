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

    public LeaderboardViewModel() {
        super("leaderboard");
    }

    public void setData(LeaderboardType type, ArrayList<User> users, int myRank) {
        topUsersByType.put(type, users);
        myRankByType.put(type, myRank);
        currentType = type;
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
}
