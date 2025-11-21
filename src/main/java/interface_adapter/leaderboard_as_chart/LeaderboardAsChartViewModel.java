package interface_adapter.leaderboard_as_chart;

import interface_adapter.ViewModel;

public class LeaderboardAsChartViewModel extends ViewModel<LeaderboardAsChartState> {

    public LeaderboardAsChartViewModel() {
        super("leaderboard_as_chart");
        this.setState(new LeaderboardAsChartState());
    }

    @Override
    public void firePropertyChange() {
        super.firePropertyChange();
    }
}
