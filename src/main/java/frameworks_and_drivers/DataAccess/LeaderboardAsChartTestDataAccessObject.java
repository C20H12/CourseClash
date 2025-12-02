package frameworks_and_drivers.DataAccess;

import entity.User;
import use_case.DataAccessException;
import use_case.leaderboard.LeaderboardType;
import use_case.leaderboard.LeaderboardUserDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Mock DAO for testing LeaderboardAsChartInteractor.
 */
public class LeaderboardAsChartTestDataAccessObject implements LeaderboardUserDataAccessInterface {

    private final Map<LeaderboardType, ArrayList<User>> fakeData;
    private final boolean throwException;

    // Normal constructor
    public LeaderboardAsChartTestDataAccessObject(Map<LeaderboardType, ArrayList<User>> fakeData) {
        this.fakeData = fakeData;
        this.throwException = false;
    }

    // Constructor that forces an exception
    public LeaderboardAsChartTestDataAccessObject(boolean throwException) {
        this.fakeData = new HashMap<>();
        this.throwException = throwException;
    }

    @Override
    public int getTotalUserCount() throws DataAccessException {
        if (throwException) throw new DataAccessException("DAO failure (total count).");
        return fakeData.values().stream().mapToInt(ArrayList::size).sum();
    }

    @Override
    public Map<LeaderboardType, ArrayList<User>> getTopUsers(int topN) throws DataAccessException {
        if (throwException) throw new DataAccessException("DAO failure (top users).");

        Map<LeaderboardType, ArrayList<User>> trimmed = new HashMap<>();

        for (LeaderboardType type : LeaderboardType.values()) {
            ArrayList<User> users = fakeData.getOrDefault(type, new ArrayList<>());

            ArrayList<User> limited = new ArrayList<>(users.subList(
                    0, Math.min(topN, users.size())
            ));

            trimmed.put(type, limited);
        }

        return trimmed;
    }
}
