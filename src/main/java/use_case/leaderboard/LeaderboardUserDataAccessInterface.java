package use_case.leaderboard;

import java.util.ArrayList;
import java.util.Map;

import entity.User;
import use_case.DataAccessException;

public interface LeaderboardUserDataAccessInterface {

    /**
     * Get the total number of users in the database.
     *
     * @return total user count
     * @throws DataAccessException Exception when data access fails
     */
    int getTotalUserCount() throws DataAccessException;

    /**
     * Get the top ranking user information. Note: user password will be null.
     * If user number < topn, all user will be returned in order and the arraylist has length < topn
     * @param topN specify the nth top user
     * @return a map with key as type of ranking and an arraylist of user in descending order (highest ranked is first)
     * @throws DataAccessException Exception when data access fails
     */
    Map<LeaderboardType, ArrayList<User>> getTopUsers(int topN) throws DataAccessException;
}
