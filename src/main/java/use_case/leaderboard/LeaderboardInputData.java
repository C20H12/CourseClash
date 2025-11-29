package use_case.leaderboard;

/**
 * The Input Data for the Leaderboard.
 */

public class LeaderboardInputData {
    private final int leaderboardEntryCount;

    public LeaderboardInputData(int leaderboardEntryCount) {
        this.leaderboardEntryCount = leaderboardEntryCount;
    }

    public int getLeaderboardEntryCount() {
        return leaderboardEntryCount;
    }
}
