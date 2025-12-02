package use_case.leaderboard_as_chart;

public class LeaderboardAsChartInputData {
    private final int leaderboardEntryCount;

    public LeaderboardAsChartInputData(int leaderboardEntryCount) {
        this.leaderboardEntryCount = leaderboardEntryCount;
    }
    
    public int getLeaderboardEntryCount() {
        return leaderboardEntryCount;
    }
}
