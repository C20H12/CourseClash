package use_case.leaderboard_as_chart;

import use_case.DataAccessException;

public interface LeaderboardAsChartInputBoundary {
    void execute() throws DataAccessException;
}
