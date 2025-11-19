package use_case.leaderboard;

import entity.User;

import java.util.ArrayList;
import java.util.Map;

public interface LeaderboardUserDataAccessInterface {
    Map<LeaderboardType, ArrayList<User>> getTopUsers(int topN);
    Map<LeaderboardType, Integer> getUserRank(User user);
}
