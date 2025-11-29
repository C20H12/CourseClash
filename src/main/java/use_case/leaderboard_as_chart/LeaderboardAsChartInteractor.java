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
    public void execute(LeaderboardAsChartInputData inputData) throws DataAccessException {
        Map<LeaderboardType, ArrayList<User>> daoData = dao.getTopUsers(inputData.getLeaderboardEntryCount());
        Map<LeaderboardType, List<User>> chartData = new HashMap<>();

        for (LeaderboardType type : LeaderboardType.values()) {
            List<User> users = daoData.getOrDefault(type, new ArrayList<>());
            chartData.put(type, new ArrayList<>(users));
        }
        LeaderboardAsChartOutputData outputData = new LeaderboardAsChartOutputData(chartData);
        presenter.present(outputData);
    }

}
