package interface_adapter.leaderboard_as_chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.User;
import use_case.leaderboard.LeaderboardType;

public class LeaderboardAsChartState {
    private Map<LeaderboardType, List<User>> scores;

    public LeaderboardAsChartState() {
        this.scores = new HashMap<>();
    }

    public Map<LeaderboardType, List<User>> getScores() {
        return scores;
    }

    public void setScores(Map<LeaderboardType, List<User>> scores) {
        this.scores = scores;
    }
}
