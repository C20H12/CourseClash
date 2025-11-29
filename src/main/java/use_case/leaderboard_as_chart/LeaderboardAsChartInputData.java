package use_case.leaderboard_as_chart;

public class LeaderboardAsChartInputData {
    private final String requestType;
    private final int leaderboardEntryCount;
    public LeaderboardAsChartInputData(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }
    
    public int getLeaderboardEntryCount() {
        return leaderboardEntryCount;
    }
}
