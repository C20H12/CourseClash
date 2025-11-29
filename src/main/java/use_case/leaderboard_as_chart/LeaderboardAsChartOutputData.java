package use_case.leaderboard_as_chart;

import java.util.List;
import java.util.Map;

import entity.User;
import use_case.leaderboard.LeaderboardType;

public class LeaderboardAsChartOutputData {
    private final Map<LeaderboardType, List<User>> topUsers;

    public LeaderboardAsChartOutputData(Map<LeaderboardType, List<User>> topUsers) {
        this.topUsers = topUsers;
    }

    public Map<LeaderboardType, List<User>> getTopUsers() {
        return topUsers;
    }
}
