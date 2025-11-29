package interface_adapter.leaderboard_as_chart;

import use_case.DataAccessException;
import use_case.leaderboard_as_chart.LeaderboardAsChartInputBoundary;
import use_case.leaderboard_as_chart.LeaderboardAsChartInputData;

public class LeaderboardAsChartController {

    private final LeaderboardAsChartInputBoundary interactor;

    public LeaderboardAsChartController(LeaderboardAsChartInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void requestChart(int leaderboardEntryCount) throws DataAccessException {
        LeaderboardAsChartInputData inputData = new LeaderboardAsChartInputData("chart", leaderboardEntryCount);
        interactor.execute();
    }
}
