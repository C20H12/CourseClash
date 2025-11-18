package interface_adapter.leaderboard;

import interface_adapter.ViewManagerModel;
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
    public void presentLeaderboard(LeaderboardOutputData outputData) {
        LeaderboardState state = new LeaderboardState();
        state.setLeaderboardType(outputData.getLeaderboardType());
        state.setTopUsers(outputData.getTopUsers());
        state.setCurrentUser(outputData.getCurrentUser());
        state.setMyRank(outputData.getCurrentUserRank());
        viewManagerModel.setState(leaderboardViewModel.getViewName());
        leaderboardViewModel.setState(state);
        viewManagerModel.firePropertyChange();
        leaderboardViewModel.firePropertyChange();
    }
}
