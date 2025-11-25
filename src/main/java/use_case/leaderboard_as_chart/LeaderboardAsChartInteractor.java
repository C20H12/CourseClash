package use_case.leaderboard_as_chart;

import entity.User;
import frameworks_and_drivers.DataAccess.LeaderboardUserDataAccessObject;
import use_case.DataAccessException;
import use_case.leaderboard.LeaderboardType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardAsChartInteractor implements LeaderboardAsChartInputBoundary {

    private final LeaderboardUserDataAccessObject dao;
    private final LeaderboardAsChartOutputBoundary presenter;

    public LeaderboardAsChartInteractor(LeaderboardUserDataAccessObject dao, LeaderboardAsChartOutputBoundary presenter) {
        this.dao = dao;
        this.presenter = presenter;
    }

    @Override
    public void execute() throws DataAccessException {
        // Get top users for all types
        Map<LeaderboardType, List<User>> chartData = new HashMap<>();
        for (LeaderboardType type : LeaderboardType.values()) {
            chartData.put(type, new ArrayList<>(dao.getTopUsers(50).get(type)));
        }
        LeaderboardAsChartOutputData outputData = new LeaderboardAsChartOutputData(chartData);
        presenter.present(outputData);
    }
}
