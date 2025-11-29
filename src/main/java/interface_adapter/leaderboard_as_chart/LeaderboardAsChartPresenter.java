package interface_adapter.leaderboard_as_chart;

import use_case.leaderboard_as_chart.LeaderboardAsChartOutputBoundary;
import use_case.leaderboard_as_chart.LeaderboardAsChartOutputData;

public class LeaderboardAsChartPresenter implements LeaderboardAsChartOutputBoundary {

    private final LeaderboardAsChartViewModel viewModel;

    public LeaderboardAsChartPresenter(LeaderboardAsChartViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(LeaderboardAsChartOutputData outputData) {
        LeaderboardAsChartState state = viewModel.getState();
        state.setScores(outputData.getTopUsers());
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }
}
