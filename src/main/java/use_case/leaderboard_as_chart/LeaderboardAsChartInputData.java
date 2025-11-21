package use_case.leaderboard_as_chart;

public class LeaderboardAsChartInputData {
    private final String requestType;

    public LeaderboardAsChartInputData(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }
}
