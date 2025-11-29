package interface_adapter.leaderboard;

import interface_adapter.ViewManagerModel;
import use_case.DataAccessException;
import use_case.leaderboard.LeaderboardOutputBoundary;
import use_case.leaderboard.LeaderboardOutputData;

public class LeaderboardPresenter implements LeaderboardOutputBoundary {

    private final LeaderboardViewModel leaderboardViewModel;
    private final ViewManagerModel viewManagerModel;

    public LeaderboardPresenter(LeaderboardViewModel leaderboardViewModel, ViewManagerModel viewManagerModel) {
        this.leaderboardViewModel = leaderboardViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void presentLeaderboard(LeaderboardOutputData outputData) throws DataAccessException {
        leaderboardViewModel.setLeaderboard(outputData.getLeaderboard());
        viewManagerModel.firePropertyChange();
        leaderboardViewModel.firePropertyChange();
    }
}
