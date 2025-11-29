package use_case.leaderboard;

public enum LeaderboardType {
    LEVEL(2),
    EXPERIENCE_POINTS(3),
    QUESTIONS_ANSWERED(4),
    QUESTIONS_CORRECT(5);

    private final int colNumber;

    LeaderboardType(int colNumber) {
        this.colNumber = colNumber;
    }

    public int getColNumber() {
        return colNumber;
    }

}